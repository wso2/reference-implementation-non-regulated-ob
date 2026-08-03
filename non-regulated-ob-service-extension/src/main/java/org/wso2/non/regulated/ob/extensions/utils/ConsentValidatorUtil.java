/*
 * Copyright (c) 2026, WSO2 LLC. (https://www.wso2.com). All Rights Reserved.
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.non.regulated.ob.extensions.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.wso2.non.regulated.ob.extensions.configurations.ConfigurableProperties;
import org.wso2.non.regulated.ob.extensions.constants.CommonConstants;
import org.wso2.non.regulated.ob.extensions.constants.ConsentValidatorConstants;
import org.wso2.non.regulated.ob.extensions.constants.ErrorConstants;

import java.net.URI;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Util class for UK Consent Validator.
 */
public class ConsentValidatorUtil {

    private static final Log log = LogFactory.getLog(ConsentValidatorUtil.class);


    /**
     * Util method to validate the Account request URI.
     *
     * @param uri Request URI
     * @return
     */
    public static boolean isAccountURIValid(String uri) {
        List<String> accountPaths = getAccountAPIPathRegexArray();
        boolean isValid = false;

        for (String entry : accountPaths) {
            if (uri.contains(entry)) {
                isValid = true;
                break;
            }
        }

        return isValid;
    }

    /**
     * Method provides API resource paths applicable for UK Account API.
     *
     * @return map of API Resources.
     */
    public static List<String> getAccountAPIPathRegexArray() {

        return Arrays.asList(ConsentValidatorConstants.ACCOUNT_REGEX,
                ConsentValidatorConstants.BALANCES_REGEX,
                ConsentValidatorConstants.TRANSACTIONS_REGEX,
                ConsentValidatorConstants.ACCOUNT_ID_REGEX,
                ConsentValidatorConstants.BALANCES_ID_REGEX,
                ConsentValidatorConstants.TRANSACTIONS_ID_REGEX);
    }

    /**
     * Validate Account Permissions.
     *
     * @param requestedURL The URL of the request
     * @param permissions  The permissions of the request
     * @return Status of the validation
     */
    public static boolean validateAccountPermissions(String requestedURL, JSONArray permissions) {

        Set<String> permissionSet = new HashSet<>();
        for (int i = 0; i < permissions.length(); i++) {
            permissionSet.add(permissions.getString(i));
        }

        if (requestedURL != null) {

            if (requestedURL.matches(ConsentValidatorConstants.ACCOUNT_REGEX) ||
                    requestedURL.matches(ConsentValidatorConstants.ACCOUNT_ID_REGEX)) {

                return permissionSet.contains(CommonConstants.READ_ACCOUNTS_BASIC) ||
                        permissionSet.contains(CommonConstants.READ_ACCOUNTS_DETAIL);
            } else if (requestedURL.matches(ConsentValidatorConstants.BALANCES_REGEX) ||
                    requestedURL.matches(ConsentValidatorConstants.BALANCES_ID_REGEX)) {
                return permissionSet.contains(ConsentValidatorConstants.READ_BALANCES);
            } else if (requestedURL.matches(ConsentValidatorConstants.TRANSACTIONS_REGEX) ||
                    requestedURL.matches(ConsentValidatorConstants.TRANSACTIONS_ID_REGEX)) {
                return (permissionSet.contains(CommonConstants.READ_TRANSACTIONS_BASIC) ||
                        permissionSet.contains(CommonConstants.READ_TRANSACTIONS_DETAIL)) &&
                        (permissionSet.contains(CommonConstants.READ_TRANSACTIONS_CREDITS) ||
                                permissionSet.contains(CommonConstants.READ_TRANSACTIONS_DEBITS));
            }

        }
        if (log.isDebugEnabled()) {
            log.debug(String.format("Validation of permissions failed for resource: %s Permissions: %s",
                    requestedURL.replaceAll("\r\n", ""),
                    permissions.toString().replaceAll("\r\n", "")));
        }
        return false;
    }

    /**
     * Validate whether consent is expired.
     *
     * @param expDateVal Expiration Date Time
     * @return
     */
    public static boolean isConsentExpired(String expDateVal) {

        if (expDateVal != null && !expDateVal.isEmpty()) {
            OffsetDateTime expDate = OffsetDateTime.parse(expDateVal);
            return OffsetDateTime.now().isAfter(expDate);
        } else {
            return false;
        }
    }

    /**
     * Method to extract query parameters from the submission request body.
     *
     * @param jsonSubmissionRequestBody
     * @return
     */
    public static Map<String, String> extractQueryParams(JSONObject jsonSubmissionRequestBody) {

        JSONObject resourceParams = jsonSubmissionRequestBody.getJSONObject(CommonConstants.RESOURCE_PARAMS);

        // Initialize Map to store resourceParams values
        Map<String, String> resourceParamsMap = new HashMap<>();

        // Dynamically add key-value pairs to the map
        Iterator<String> keys = resourceParams.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            String value = resourceParams.getString(key);
            resourceParamsMap.put(key, value);
        }
        return resourceParamsMap;
    }

    /**
     * Check whether the time period of the transaction is valid.
     *
     * @param resourcePath    URI of the request
     * @param jsonRequestBody Initiation object
     * @param queryParams     Query Parameters
     * @return is transaction time period valid
     */
    public static JSONObject checkTransactionTimePeriodValidity(String resourcePath, JSONObject jsonRequestBody,
                                                                Map<String, String> queryParams, String requestId) {

        LocalDateTime fromDateTime = null;
        LocalDateTime toDateTime = null;
        LocalDateTime transactionFromDateTime = null;
        LocalDateTime transactionToDateTime = null;
        boolean isFromDateValid = true;
        boolean isToDateValid = true;

        try {
            if (resourcePath.contains(ConsentValidatorConstants.TRANSACTIONS)) {

                fromDateTime = extractURIDateParameter(queryParams
                        .get(ConsentValidatorConstants.FROM_BOOKING_DATE_TIME));
                toDateTime = extractURIDateParameter(queryParams
                        .get(ConsentValidatorConstants.TO_BOOKING_DATE_TIME));
            }

            String transactionFromDateFromRequest = jsonRequestBody.getString(CommonConstants.TRANSACTION_FROM_DATE);
            String transactionToDateFromRequest = jsonRequestBody.getString(CommonConstants.TRANSACTION_TO_DATE);

            if (transactionFromDateFromRequest != null) {
                transactionFromDateTime = LocalDateTime.parse(transactionFromDateFromRequest,
                        DateTimeFormatter.ISO_DATE_TIME);
            }

            if (transactionToDateFromRequest != null) {
                transactionToDateTime = LocalDateTime.parse(transactionToDateFromRequest,
                        DateTimeFormatter.ISO_DATE_TIME);
            }

        } catch (DateTimeParseException e) {
            log.error(ConsentValidatorConstants.WRONG_DATE_FORMAT_QUERY);
            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ConsentValidatorConstants.FIELD_INVALID_DATE,
                    ConsentValidatorConstants.WRONG_DATE_FORMAT_QUERY);
        }

        if ((transactionFromDateTime != null) || (transactionToDateTime != null)) {
            if (fromDateTime != null) {
                if (transactionFromDateTime != null &&
                        fromDateTime.compareTo(transactionFromDateTime) < 0) {
                    isFromDateValid = false;
                }
                if (transactionToDateTime != null && fromDateTime.compareTo(transactionToDateTime) > 0) {
                    isFromDateValid = false;
                }
            }
            if (toDateTime != null) {
                if (transactionToDateTime != null && toDateTime.compareTo(transactionToDateTime) > 0) {
                    isToDateValid = false;
                }
                if (transactionFromDateTime != null && toDateTime.compareTo(transactionFromDateTime) < 0) {
                    isToDateValid = false;
                }
            }

            if (!(isFromDateValid && isToDateValid)) {
                log.error(ErrorConstants.INVALID_QUERY_PARAMS);
                return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                        ConsentValidatorConstants.FIELD_INVALID_DATE,
                        ErrorConstants.INVALID_QUERY_PARAMS);
            }
        }
        return CommonUtil.getSuccessResponse(requestId);
    }

    /**
     * Extracts date and time from a URL.
     *
     * @param queryParam Query Param string
     * @return
     * @throws DateTimeParseException
     * @throws java.io.UnsupportedEncodingException
     */
    public static LocalDateTime extractURIDateParameter(String queryParam)
            throws DateTimeParseException {

        LocalDateTime dateTime = null;
        if (queryParam != null) {
            URI uri = URI.create(queryParam.replaceAll(":", "%3A"));
            String decodedRequestedDateTime = uri.getPath();
            if (decodedRequestedDateTime != null) {
                if (decodedRequestedDateTime.charAt(4) == '-') {
                    dateTime = LocalDateTime.parse(decodedRequestedDateTime,
                            ConsentValidatorConstants.EXTENDED_ISO_DATE_TIME);
                } else {
                    dateTime = LocalDateTime.parse(decodedRequestedDateTime,
                            ConsentValidatorConstants.BASIC_ISO_DATE_TIME);
                }
            }
        }

        return dateTime;
    }

    /**
     * Validates if the LastAuthorizedDate for an account consent is beyond the configured Date Limit.
     *
     * @param lastAuthorizedDate seconds value of the last authorized date
     **/
    public static boolean isLastAuthorizedDateOutOfLimit(long lastAuthorizedDate) {

        String zoneId = ConfigurableProperties.ZONE_ID;
        LocalDate lastAuthorizeDate = Instant.ofEpochSecond(lastAuthorizedDate).
                atZone(ZoneId.of(zoneId)).toLocalDate();

        return ChronoUnit.DAYS.between(Date.valueOf(lastAuthorizeDate).toLocalDate(), LocalDate.now()) >
                Integer.parseInt(ConfigurableProperties.ACCOUNT_REFRESH_TOKEN_LAST_AUTHORIZED_DATE_LIMIT);
    }

    /**
     * Validates if the query param fromBookingDateTime for an account consent is beyond the configured Date Limit.
     *
     * @params resourceParams requested query parameters
     **/
    public static boolean isFromDateOutOfDateLimit(Map<String, String> resourceParams) {

        if (resourceParams.containsKey(ConsentValidatorConstants.FROM_BOOKING_DATE_TIME)) {

            LocalDateTime fromBookingDate = null;
            URI uri = URI.create(resourceParams.get(ConsentValidatorConstants.FROM_BOOKING_DATE_TIME)
                    .replaceAll(":", "%3A"));
            String decodedRequestedDateTime = uri.getPath();

            if (decodedRequestedDateTime != null) {
                if (decodedRequestedDateTime.charAt(4) == '-') {

                    fromBookingDate = LocalDateTime.parse(decodedRequestedDateTime,
                            ConsentValidatorConstants.EXTENDED_ISO_DATE_TIME);
                } else {

                    fromBookingDate = LocalDateTime.parse(decodedRequestedDateTime,
                            ConsentValidatorConstants.BASIC_ISO_DATE_TIME);
                }
                return ChronoUnit.DAYS.between(fromBookingDate, LocalDateTime.now()) >
                        Integer.parseInt(ConfigurableProperties.ACCOUNT_REFRESH_TOKEN_LAST_AUTHORIZED_DATE_LIMIT);
            }
            return true;
        } else {
            //return false when there is no fromBookingDateTime in the query params.
            return false;
        }
    }

    /**
     * Method to validate whether account id is valid.
     *
     * @param jsonConsentRequestBody
     * @param resourcePath
     * @return
     * @throws Exception
     */
    public static boolean isAccountIdValid(JSONObject jsonConsentRequestBody, String resourcePath) {

        JSONArray authorizations = jsonConsentRequestBody
                .getJSONArray(ConsentValidatorConstants.AUTHORIZATIONS_KEY);
        JSONObject authorisationResource = authorizations.getJSONObject(0);

        JSONArray consentMappingResources = authorisationResource.getJSONArray(ConsentValidatorConstants.RESOURCES);

        String accountID = consentMappingResources.getJSONObject(0).getString(ConsentValidatorConstants.ACCOUNT_ID);


        // If no valid account IDs exist, return false
        if (accountID.isEmpty()) {
            return false;
        }

        // Check if /accounts/{accountID} is present
        Pattern pattern = Pattern.compile("/accounts/\\d+");
        Matcher matcher = pattern.matcher(resourcePath);

        if (!matcher.find()) {
            return true;
        }

        // If resourcePath does not contain "{AccountId}", return true
        if (resourcePath.contains("/accounts/" + accountID)) {
            return true;
        }

        // Check if resourcePath contains any valid account ID
        if (resourcePath.contains(accountID)) {
            return true;
        }
        return false;
    }

    /**
     * Utility method to validate mandatory parameters.
     *
     * @param str1 First String to validate
     * @param str2 Second String to validate
     * @return
     */
    public static boolean compareMandatoryParameter(String str1, String str2) {

        if ((str1 == null) || (str2 == null)) {
            return false;
        } else {
            return str1.equals(str2);
        }
    }

    /**
     * Utility method to validate optional parameters.
     *
     * @param field      Field to validate
     * @param submission submisison object
     * @param consent    initiation object
     * @return
     */
    public static boolean compareOptionalParameter(String field, JSONObject submission, JSONObject consent) {

        boolean isStr1Empty = !submission.has(field) || submission.getString(field) == null ||
                submission.getString(field).isEmpty();
        boolean isStr2Empty = !consent.has(field) || (consent.getString(field) == null ||
                consent.getString(field).isEmpty());

        if (!(isStr1Empty || isStr2Empty)) {
            return submission.getString(field).equals(consent.getString(field));
        } else {
            return (isStr1Empty && isStr2Empty);
        }
    }

    /**
     * Method to validate Remittance Information.
     *
     * @param remittanceInformationSubmission Remittance Information in Submission Request
     * @param remittanceInformationInitiation Remittance Information in Initiation Request
     * @param invokedAPIVersion
     * @return validation result object
     */
    public static JSONObject validateRemittanceInfo(JSONObject remittanceInformationSubmission,
                                                    JSONObject remittanceInformationInitiation,
                                                    CommonConstants.UKApiVersion invokedAPIVersion, String requestId) {

        if ((!remittanceInformationSubmission.has(CommonConstants.REFERENCE)
                && remittanceInformationInitiation.has(CommonConstants.REFERENCE)) ||
                (remittanceInformationSubmission.has(CommonConstants.REFERENCE)
                        && !remittanceInformationInitiation.has(CommonConstants.REFERENCE))) {

            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                    ConsentValidatorConstants.REMMITANCE_REFERENCE_NOT_FOUND);

        } else if (remittanceInformationSubmission.has(CommonConstants.REFERENCE)
                && remittanceInformationInitiation.has(CommonConstants.REFERENCE)) {
            if (!ConsentValidatorUtil.compareOptionalParameter(CommonConstants.REFERENCE,
                    remittanceInformationSubmission, remittanceInformationInitiation)) {

                return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                        ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                        ConsentValidatorConstants.REMMITANCE_REFERENCE_MISMATCH);
            }
        }

        if (CommonConstants.UKApiVersion.UK_API_V400.equals(invokedAPIVersion)) {

            if ((!remittanceInformationSubmission.has(ConsentValidatorConstants.UNSTRUCTURED)
                    && remittanceInformationInitiation.has(ConsentValidatorConstants.UNSTRUCTURED)) ||
                    (remittanceInformationSubmission.has(ConsentValidatorConstants.UNSTRUCTURED)
                            && !remittanceInformationInitiation.has(ConsentValidatorConstants.UNSTRUCTURED))) {

                return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                        ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                        ConsentValidatorConstants.REMMITANCE_UNSTRUCTURED_NOT_FOUND);

            } else if (remittanceInformationSubmission.has(ConsentValidatorConstants.UNSTRUCTURED)
                    && remittanceInformationInitiation.has(ConsentValidatorConstants.UNSTRUCTURED)) {

                JSONArray remittanceInformationUnstructuredSub = (JSONArray) remittanceInformationSubmission
                        .get(ConsentValidatorConstants.UNSTRUCTURED);
                JSONArray remittanceInformationUnstructuredInit = (JSONArray) remittanceInformationInitiation
                        .get(ConsentValidatorConstants.UNSTRUCTURED);

                if (!remittanceInformationUnstructuredSub.equals(remittanceInformationUnstructuredInit)) {

                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                            ConsentValidatorConstants.REMMITANCE_UNSTRUCTURED_MISMATCH);
                }
            }
        } else {

            if ((!remittanceInformationSubmission.has(ConsentValidatorConstants.UNSTRUCTURED)
                    && remittanceInformationInitiation.has(ConsentValidatorConstants.UNSTRUCTURED)) ||
                    (remittanceInformationSubmission.has(ConsentValidatorConstants.UNSTRUCTURED)
                            && !remittanceInformationInitiation.has(ConsentValidatorConstants.UNSTRUCTURED))) {

                return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                        ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                        ConsentValidatorConstants.REMMITANCE_UNSTRUCTURED_NOT_FOUND);

            } else if (remittanceInformationSubmission.has(ConsentValidatorConstants.UNSTRUCTURED)
                    && remittanceInformationInitiation.has(ConsentValidatorConstants.UNSTRUCTURED)) {
                if (!ConsentValidatorUtil.compareOptionalParameter(ConsentValidatorConstants.UNSTRUCTURED,
                        remittanceInformationSubmission, remittanceInformationInitiation)) {

                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                            ConsentValidatorConstants.REMMITANCE_UNSTRUCTURED_MISMATCH);
                }
            }
        }

        return CommonUtil.getSuccessResponse(requestId);
    }

    public static JSONObject validateInstructionIdentification(String requestPath, JSONObject submissionInitiation,
                                                               JSONObject consentInitiation, String requestId) {

        if (!requestPath.contains("standing-orders")) {

            if (submissionInitiation.has(CommonConstants.INSTRUCTION_IDENTIFICATION)) {

                if (StringUtils.isEmpty(submissionInitiation.getString(CommonConstants.INSTRUCTION_IDENTIFICATION))
                        || !ConsentValidatorUtil.compareMandatoryParameter(
                        submissionInitiation.getString(CommonConstants.INSTRUCTION_IDENTIFICATION),
                        consentInitiation.getString(CommonConstants.INSTRUCTION_IDENTIFICATION))) {

                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                            ConsentValidatorConstants.INSTRUCTION_IDENTIFICATION_MISMATCH);
                }
            } else {
                return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                        ErrorConstants.FIELD_MISSING,
                        ConsentValidatorConstants.INSTRUCTION_IDENTIFICATION_NOT_FOUND);
            }

            return CommonUtil.getSuccessResponse(requestId);
        }
        return CommonUtil.getSuccessResponse(requestId);
    }

    /**
     * Method to validate EndToEndIdentification.
     *
     * @param requestPath
     * @param submissionInitiation
     * @param consentInitiation
     * @param requestId
     * @return
     */
    public static JSONObject validateEndToEndIdentification(String requestPath, JSONObject submissionInitiation,
                                                            JSONObject consentInitiation, String requestId) {

        if (requestPath.contains("domestic-payments") || requestPath.contains("international-payments")) {

            if (submissionInitiation.has(CommonConstants.END_TO_END_IDENTIFICATION)) {

                if (StringUtils.isEmpty(submissionInitiation.getString(CommonConstants.END_TO_END_IDENTIFICATION))
                        || !ConsentValidatorUtil.compareMandatoryParameter(
                        submissionInitiation.getString(CommonConstants.END_TO_END_IDENTIFICATION),
                        consentInitiation.getString(CommonConstants.END_TO_END_IDENTIFICATION))) {

                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                            ConsentValidatorConstants.END_TO_END_IDENTIFICATION_MISMATCH);
                }
            } else {
                return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                        ErrorConstants.FIELD_MISSING,
                        ConsentValidatorConstants.END_TO_END_IDENTIFICATION_NOT_FOUND);
            }
        } else if (requestPath.contains("scheduled-payments")) {
            if (submissionInitiation.has(CommonConstants.END_TO_END_IDENTIFICATION) &&
                    consentInitiation.has(CommonConstants.END_TO_END_IDENTIFICATION) &&
                    !ConsentValidatorUtil.compareOptionalParameter(CommonConstants.END_TO_END_IDENTIFICATION,
                            submissionInitiation, consentInitiation)) {

                return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                        ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                        ConsentValidatorConstants.END_TO_END_IDENTIFICATION_MISMATCH);

            } else if ((submissionInitiation.has(CommonConstants.END_TO_END_IDENTIFICATION) &&
                    !consentInitiation.has(CommonConstants.END_TO_END_IDENTIFICATION)) ||
                    (!submissionInitiation.has(CommonConstants.END_TO_END_IDENTIFICATION) &&
                            consentInitiation.has(CommonConstants.END_TO_END_IDENTIFICATION))) {

                return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                        ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                        ConsentValidatorConstants.END_TO_END_IDENTIFICATION_NOT_FOUND);
            }

            return CommonUtil.getSuccessResponse(requestId);
        }
        return CommonUtil.getSuccessResponse(requestId);
    }


    /**
     * Method to validate InstructedAmount.
     *
     * @param requestPath
     * @param submissionInitiation
     * @param consentInitiation
     * @param requestId
     * @return
     */
    public static JSONObject validateInstructedAmount(String requestPath, JSONObject submissionInitiation,
                                                      JSONObject consentInitiation, String requestId) {

        if (!requestPath.contains("domestic-standing-orders")) {

            if (submissionInitiation.has(CommonConstants.INSTRUCTED_AMOUNT)) {

                JSONObject subInstrAmount = (JSONObject) submissionInitiation.get(CommonConstants.INSTRUCTED_AMOUNT);
                JSONObject initInstrAmount = (JSONObject) consentInitiation.get(CommonConstants.INSTRUCTED_AMOUNT);

                if (subInstrAmount.has(CommonConstants.AMOUNT)) {

                    if (StringUtils.isEmpty(subInstrAmount.getString(CommonConstants.AMOUNT)) ||
                            !ConsentValidatorUtil.compareMandatoryParameter(
                                    subInstrAmount.getString(CommonConstants.AMOUNT),
                                    initInstrAmount.getString(CommonConstants.AMOUNT))) {

                        return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                                ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                                ConsentValidatorConstants.INSTRUCTED_AMOUNT_AMOUNT_MISMATCH);
                    }
                } else {
                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.FIELD_MISSING,
                            ConsentValidatorConstants.INSTRUCTED_AMOUNT_AMOUNT_NOT_FOUND);
                }

                if (subInstrAmount.has(CommonConstants.CURRENCY)) {
                    if (StringUtils.isEmpty(subInstrAmount.getString(CommonConstants.CURRENCY)) ||
                            !ConsentValidatorUtil.compareMandatoryParameter(
                                    subInstrAmount.getString(CommonConstants.CURRENCY),
                                    initInstrAmount.getString(CommonConstants.CURRENCY))) {

                        return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                                ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                                ConsentValidatorConstants.INSTRUCTED_AMOUNT_CURRENCY_MISMATCH);
                    }
                } else {
                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.FIELD_MISSING,
                            ConsentValidatorConstants.INSTRUCTED_AMOUNT_CURRENCY_NOT_FOUND);
                }
            } else {
                return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                        ErrorConstants.FIELD_MISSING,
                        ConsentValidatorConstants.INSTRUCTED_AMOUNT_NOT_FOUND);
            }

            return CommonUtil.getSuccessResponse(requestId);
        }
        return CommonUtil.getSuccessResponse(requestId);
    }

    /**
     * Method to validate RequestedExecutionDate.
     *
     * @param requestPath
     * @param submissionInitiation
     * @param consentInitiation
     * @param requestId
     * @return
     */
    public static JSONObject validateRequestedExecutionDate(String requestPath, JSONObject submissionInitiation,
                                                            JSONObject consentInitiation, String requestId) {

        if (requestPath.contains("scheduled-payments")) {
            if (submissionInitiation.has(CommonConstants.REQUEST_EXECUTION_DATE)) {

                if (StringUtils.isEmpty(submissionInitiation.getString(CommonConstants.REQUEST_EXECUTION_DATE))
                        || !ConsentValidatorUtil.compareMandatoryParameter(
                        submissionInitiation.getString(CommonConstants.REQUEST_EXECUTION_DATE),
                        consentInitiation.getString(CommonConstants.REQUEST_EXECUTION_DATE))) {

                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                            ConsentValidatorConstants.REQUESTED_EXECUTION_DATE_MISMATCH);
                }
            } else {
                return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                        ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                        ConsentValidatorConstants.REQUESTED_EXECUTION_DATE_NOT_FOUND);
            }

            return CommonUtil.getSuccessResponse(requestId);
        }
        return CommonUtil.getSuccessResponse(requestId);
    }

    /**
     * Method to validate Frequency.
     *
     * @param requestPath
     * @param submissionInitiation
     * @param consentInitiation
     * @param invokedAPIVersion
     * @param requestId
     * @return
     */
    public static JSONObject validateFrequency(String requestPath, JSONObject submissionInitiation,
                                               JSONObject consentInitiation,
                                               CommonConstants.UKApiVersion invokedAPIVersion, String requestId) {

        if (requestPath.contains("standing-orders")) {

            if (CommonConstants.UKApiVersion.UK_API_V400.equals(invokedAPIVersion)) {

                if (submissionInitiation.has(CommonConstants.MANDATE_RELATED_INFORMATION)) {

                    JSONObject submissionMandateRelatedInfo = (JSONObject) submissionInitiation.get(
                            CommonConstants.MANDATE_RELATED_INFORMATION);
                    if (submissionMandateRelatedInfo != null &&
                            submissionMandateRelatedInfo.has(ConsentValidatorConstants.FREQUENCY)) {

                        JSONObject initiationMandateRelatedInfo = (JSONObject) consentInitiation.get(
                                CommonConstants.MANDATE_RELATED_INFORMATION);
                        if (initiationMandateRelatedInfo == null || (initiationMandateRelatedInfo != null &&
                                !initiationMandateRelatedInfo.has(ConsentValidatorConstants.FREQUENCY))) {

                            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                                    ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                                    ErrorConstants.MANDATE_RELATED_INFORMATION_MISMATCH);
                        }
                        JSONObject initiationFrequency = (JSONObject) initiationMandateRelatedInfo.get(
                                ConsentValidatorConstants.FREQUENCY);
                        JSONObject submissionFrequency = (JSONObject) submissionMandateRelatedInfo.get(
                                ConsentValidatorConstants.FREQUENCY);

                        if (StringUtils.isEmpty(
                                submissionFrequency.getString(ConsentValidatorConstants.TYPE)) ||
                                !ConsentValidatorUtil.compareMandatoryParameter(
                                        submissionFrequency.getString(ConsentValidatorConstants.TYPE),
                                        initiationFrequency.getString(ConsentValidatorConstants.TYPE))) {

                            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                                    ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                                    ConsentValidatorConstants.FREQUENCY_MISMATCH_API_V4);
                        }
                        if (!ConsentValidatorUtil.compareOptionalParameter(ConsentValidatorConstants.COUNT_PER_PERIOD,
                                submissionFrequency, initiationFrequency)) {

                            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                                    ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                                    ConsentValidatorConstants.FREQUENCY_MISMATCH_API_V4);
                        }
                        if (!ConsentValidatorUtil.compareOptionalParameter(ConsentValidatorConstants.POINT_IN_TIME,
                                submissionFrequency, initiationFrequency)) {

                            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                                    ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                                    ConsentValidatorConstants.FREQUENCY_MISMATCH_API_V4);
                        }
                    } else {
                        return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                                ErrorConstants.FIELD_MISSING,
                                ConsentValidatorConstants.FREQUENCY_NOT_FOUND_API_V4);
                    }
                }
            } else {
                if (submissionInitiation.has(ConsentValidatorConstants.FREQUENCY)) {
                    if (StringUtils.isEmpty(submissionInitiation.getString(ConsentValidatorConstants.FREQUENCY)) ||
                            !ConsentValidatorUtil.compareMandatoryParameter(
                                    submissionInitiation.getString(ConsentValidatorConstants.FREQUENCY),
                                    consentInitiation.getString(ConsentValidatorConstants.FREQUENCY))) {
                        return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                                ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                                ConsentValidatorConstants.FREQUENCY_MISMATCH);
                    }
                } else {
                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.FIELD_MISSING,
                            ConsentValidatorConstants.FREQUENCY_NOT_FOUND);
                }
            }

            return CommonUtil.getSuccessResponse(requestId);
        }
        return CommonUtil.getSuccessResponse(requestId);
    }

    /**
     * Method to validate FirstPaymentDate.
     *
     * @param requestPath
     * @param submissionInitiation
     * @param consentInitiation
     * @param invokedAPIVersion
     * @param requestId
     * @return
     */
    public static JSONObject validateFirstPaymentDate(String requestPath, JSONObject submissionInitiation,
                                                      JSONObject consentInitiation,
                                                      CommonConstants.UKApiVersion invokedAPIVersion,
                                                      String requestId) {

        if (requestPath.contains("standing-orders")) {

            if (CommonConstants.UKApiVersion.UK_API_V400.equals(invokedAPIVersion)) {

                if (submissionInitiation.has(CommonConstants.MANDATE_RELATED_INFORMATION)) {

                    JSONObject submissionMandateRelatedInfo = (JSONObject) submissionInitiation.get(
                            CommonConstants.MANDATE_RELATED_INFORMATION);

                    if (submissionMandateRelatedInfo != null &&
                            submissionMandateRelatedInfo.has(CommonConstants.FIRST_PAYMENT_DATE)) {

                        JSONObject initiationMandateRelatedInfo = (JSONObject) consentInitiation.get(
                                CommonConstants.MANDATE_RELATED_INFORMATION);

                        if (!ConsentValidatorUtil.compareOptionalParameter(CommonConstants.FIRST_PAYMENT_DATE,
                                submissionMandateRelatedInfo, initiationMandateRelatedInfo)) {

                            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                                    ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                                    ConsentValidatorConstants.FIRST_PAYMENT_DATE_MISMATCH_API_V4);
                        }
                    }
                }
            } else {
                if (submissionInitiation.has(CommonConstants.FIRST_PAYMENT_DATE)) {

                    if (StringUtils.isEmpty(submissionInitiation.getString(CommonConstants.FIRST_PAYMENT_DATE)) ||
                            !ConsentValidatorUtil.compareMandatoryParameter(
                                    submissionInitiation.getString(CommonConstants.FIRST_PAYMENT_DATE),
                                    consentInitiation.getString(CommonConstants.FIRST_PAYMENT_DATE))) {

                        return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                                ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                                ConsentValidatorConstants.FIRST_PAYMENT_DATE_MISMATCH);
                    }
                } else {
                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.FIELD_MISSING,
                            ConsentValidatorConstants.FIRST_PAYMENT_DATE_NOT_FOUND);
                }
            }

            return CommonUtil.getSuccessResponse(requestId);
        }
        return CommonUtil.getSuccessResponse(requestId);
    }

    /**
     * Method to validate FinalPaymentDate.
     *
     * @param requestPath
     * @param submissionInitiation
     * @param consentInitiation
     * @param requestId
     * @return
     */
    public static JSONObject validateCurrencyOfTransfer(String requestPath, JSONObject submissionInitiation,
                                                        JSONObject consentInitiation, String requestId) {

        if (requestPath.contains("international")) {
            if (submissionInitiation.has(CommonConstants.CURRENCY_OF_TRANSFER)) {
                if (StringUtils.isEmpty(submissionInitiation.getString(CommonConstants.CURRENCY_OF_TRANSFER)) ||
                        !ConsentValidatorUtil.compareMandatoryParameter(
                                submissionInitiation.getString(CommonConstants.CURRENCY_OF_TRANSFER),
                                consentInitiation.getString(CommonConstants.CURRENCY_OF_TRANSFER))) {

                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                            ConsentValidatorConstants.CURRENCY_TRANSFER_MISMATCH);
                }
            } else {
                return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                        ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                        ConsentValidatorConstants.CURRENCY_TRANSFER_NOT_FOUND);
            }

            return CommonUtil.getSuccessResponse(requestId);
        }
        return CommonUtil.getSuccessResponse(requestId);
    }

    /**
     * Method to validate ChargeBearer.
     *
     * @param requestPath
     * @param submissionInitiation
     * @param consentInitiation
     * @param requestId
     * @return
     */
    public static JSONObject validateChargeBearer(String requestPath, JSONObject submissionInitiation,
                                                  JSONObject consentInitiation, String requestId) {

        if (requestPath.contains("international")) {
            if ((submissionInitiation.has(ConsentValidatorConstants.CHARGE_BEARER) &&
                    !consentInitiation.has(ConsentValidatorConstants.CHARGE_BEARER)) ||
                    (!submissionInitiation.has(ConsentValidatorConstants.CHARGE_BEARER) &&
                            consentInitiation.has(ConsentValidatorConstants.CHARGE_BEARER))) {

                return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                        ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                        ConsentValidatorConstants.CHARGE_BEARER_NOT_FOUND);

            } else if (submissionInitiation.has(ConsentValidatorConstants.CHARGE_BEARER) &&
                    consentInitiation.has(ConsentValidatorConstants.CHARGE_BEARER) &&
                    !ConsentValidatorUtil.compareOptionalParameter(ConsentValidatorConstants.CHARGE_BEARER,
                            submissionInitiation, consentInitiation)) {

                return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                        ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                        ConsentValidatorConstants.CHARGE_BEARER_MISMATCH);
            }

            return CommonUtil.getSuccessResponse(requestId);
        }
        return CommonUtil.getSuccessResponse(requestId);
    }

    /**
     * Method to validate ChargeBearer.
     *
     * @param requestPath
     * @param submissionInitiation
     * @param consentInitiation
     * @param requestId
     * @return
     */
    public static JSONObject validateDestinationCountryCode(String requestPath, JSONObject submissionInitiation,
                                                            JSONObject consentInitiation, String requestId) {

        if (requestPath.contains("international")) {
            if ((submissionInitiation.has(ConsentValidatorConstants.DESTINATION_COUNTRY_CODE) &&
                    !consentInitiation.has(ConsentValidatorConstants.DESTINATION_COUNTRY_CODE)) ||
                    (!submissionInitiation.has(ConsentValidatorConstants.DESTINATION_COUNTRY_CODE) &&
                            consentInitiation.has(ConsentValidatorConstants.DESTINATION_COUNTRY_CODE))) {

                return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                        ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                        ConsentValidatorConstants.DESTINATION_COUNTRY_CODE_NOT_FOUND);

            } else if (submissionInitiation.has(ConsentValidatorConstants.DESTINATION_COUNTRY_CODE) &&
                    consentInitiation.has(ConsentValidatorConstants.DESTINATION_COUNTRY_CODE) &&
                    !ConsentValidatorUtil.compareOptionalParameter(ConsentValidatorConstants.DESTINATION_COUNTRY_CODE,
                            submissionInitiation, consentInitiation)) {

                return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                        ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                        ConsentValidatorConstants.DESTINATION_COUNTRY_CODE_MISMATCH);
            }

            return CommonUtil.getSuccessResponse(requestId);
        }
        return CommonUtil.getSuccessResponse(requestId);
    }

    /**
     * Method to validate payment submission risk payload.
     *
     * @param submissionRisk Risk from submission request
     * @param initiationRisk Risk from submission request
     * @return Validation Result
     */
    public static JSONObject validateRisk(JSONObject submissionRisk, JSONObject initiationRisk,
                                          String requestId) {

        if (submissionRisk != null && initiationRisk != null) {

            if (!ConsentValidatorUtil.compareOptionalParameter(ConsentValidatorConstants.CONTEXT_CODE,
                    submissionRisk, initiationRisk)) {

                return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                        ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                        ConsentValidatorConstants.PAYMENT_CONTEXT_CODE_MISMATCH);
            }

            if (!ConsentValidatorUtil.compareOptionalParameter(ConsentValidatorConstants.MERCHANT_CATEGORY_CODE,
                    submissionRisk, initiationRisk)) {

                return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                        ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                        ConsentValidatorConstants.MERCHANT_CATEGORY_CODE_MISMATCH);
            }

            if (!ConsentValidatorUtil.compareOptionalParameter(ConsentValidatorConstants.MERCHANT_IDENTIFICATION,
                    submissionRisk, initiationRisk)) {

                return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                        ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                        ConsentValidatorConstants.MERCHANT_CUSTOMER_IDENTIFICATION_MISMATCH);
            }

            if ((!initiationRisk.has(ConsentValidatorConstants.DELIVERY_ADDRESS) &&
                    submissionRisk.has(ConsentValidatorConstants.DELIVERY_ADDRESS)) ||
                    (initiationRisk.has(ConsentValidatorConstants.DELIVERY_ADDRESS) &&
                            !submissionRisk.has(ConsentValidatorConstants.DELIVERY_ADDRESS))) {

                return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                        ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                        ConsentValidatorConstants.DELIVERY_ADDRESS_MISMATCH);

            } else if (submissionRisk.has(ConsentValidatorConstants.DELIVERY_ADDRESS)) {

                JSONObject subAddress = (JSONObject) submissionRisk.get(ConsentValidatorConstants.DELIVERY_ADDRESS);
                JSONObject initAddress = (JSONObject) initiationRisk.get(ConsentValidatorConstants.DELIVERY_ADDRESS);

                if (!ConsentValidatorUtil
                        .compareOptionalParameter(ConsentValidatorConstants.STREET_NAME, subAddress, initAddress)) {

                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                            ConsentValidatorConstants.STREET_NAME_MISMATCH);
                }

                if (!ConsentValidatorUtil
                        .compareOptionalParameter(ConsentValidatorConstants.BUILDING_NUMBER, subAddress, initAddress)) {

                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                            ConsentValidatorConstants.BUILDING_NUMBER_MISMATCH);
                }

                if (!ConsentValidatorUtil
                        .compareOptionalParameter(ConsentValidatorConstants.POST_CODE, subAddress, initAddress)) {

                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                            ConsentValidatorConstants.POST_CODE_MISMATCH);
                }

                if (!ConsentValidatorUtil
                        .compareOptionalParameter(ConsentValidatorConstants.TOWN_NAME, subAddress, initAddress)) {

                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                            ConsentValidatorConstants.TOWN_NAME_MISMATCH);
                }

                if (subAddress.has(ConsentValidatorConstants.COUNTRY)) {
                    if (StringUtils.isEmpty(subAddress.getString(ConsentValidatorConstants.COUNTRY)) ||
                            !ConsentValidatorUtil.compareMandatoryParameter(
                                    subAddress.getString(ConsentValidatorConstants.COUNTRY),
                                    initAddress.getString(ConsentValidatorConstants.COUNTRY))) {

                        return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                                ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                                ConsentValidatorConstants.COUNTRY_MISMATCH);
                    }
                } else {
                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.FIELD_MISSING,
                            ConsentValidatorConstants.COUNTRY_MISMATCH);
                }

                if ((!initAddress.has(ConsentValidatorConstants.ADDRESS_LINE) &&
                        subAddress.has(ConsentValidatorConstants.ADDRESS_LINE)) ||
                        (initAddress.has(ConsentValidatorConstants.ADDRESS_LINE) &&
                                !subAddress.has(ConsentValidatorConstants.ADDRESS_LINE))) {

                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                            ConsentValidatorConstants.ADDRESS_LINE_NOT_FOUND);

                } else if (subAddress.has(ConsentValidatorConstants.ADDRESS_LINE)) {

                    JSONArray subAddressLine = subAddress.getJSONArray(ConsentValidatorConstants.ADDRESS_LINE);
                    JSONArray initiationAddressLine = initAddress.getJSONArray(ConsentValidatorConstants.ADDRESS_LINE);

                    if (!new HashSet<>(subAddressLine.toList()).containsAll(initiationAddressLine.toList())) {

                        return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                                ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                                ConsentValidatorConstants.ADDRESS_LINE_MISMATCH);
                    }
                }

                if ((!initAddress.has(ConsentValidatorConstants.COUNTRY_SUB_DIVISION) &&
                        subAddress.has(ConsentValidatorConstants.COUNTRY_SUB_DIVISION)) ||
                        (initAddress.has(ConsentValidatorConstants.COUNTRY_SUB_DIVISION) &&
                                !subAddress.has(ConsentValidatorConstants.COUNTRY_SUB_DIVISION))) {

                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                            ConsentValidatorConstants.COUNTRY_SUB_DIVISION_NOT_FOUND);

                } else if (subAddress.has(ConsentValidatorConstants.COUNTRY_SUB_DIVISION) &&
                        (!ConsentValidatorUtil.compareOptionalParameter(ConsentValidatorConstants.COUNTRY_SUB_DIVISION,
                                subAddress, initAddress))) {

                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                            ConsentValidatorConstants.COUNTRY_SUB_DIVISION_MISMATCH);
                }
            }

            return CommonUtil.getSuccessResponse(requestId);
        }
        return CommonUtil.getSuccessResponse(requestId);
    }

    /**
     * Method to validate payment submission MandateRelatedInformation payload.
     *
     * @param subMandateRelatedInformation  MandateRelatedInformation from submission request
     * @param initMandateRelatedInformation MandateRelatedInformation from initiation request
     * @return Validation Result
     */
    public static JSONObject validateMandateRelatedInformation(JSONObject subMandateRelatedInformation,
                                                               JSONObject initMandateRelatedInformation,
                                                               String requestId) {

        if (!ConsentValidatorUtil.compareOptionalParameter(ConsentValidatorConstants.MANDATE_IDENTIFICATION,
                subMandateRelatedInformation, initMandateRelatedInformation)) {

            return CommonUtil
                    .getErrorResponse(CommonConstants.BAD_REQUEST, ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                            ConsentValidatorConstants.MANDATE_IDENTIFICATION_MISMATCH);
        }
        if (!ConsentValidatorUtil.compareOptionalParameter(ConsentValidatorConstants.CLASSIFICATION,
                subMandateRelatedInformation, initMandateRelatedInformation)) {

            return CommonUtil
                    .getErrorResponse(CommonConstants.BAD_REQUEST, ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                            ConsentValidatorConstants.CLASSIFICATION_MISMATCH);
        }
        if (!ConsentValidatorUtil.compareOptionalParameter(ConsentValidatorConstants.CATEGORY_PURPOSE_CODE,
                subMandateRelatedInformation, initMandateRelatedInformation)) {

            return CommonUtil
                    .getErrorResponse(CommonConstants.BAD_REQUEST, ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                            ConsentValidatorConstants.CATEGORY_PURPOSE_CODE_MISMATCH);
        }
        if (!ConsentValidatorUtil.compareOptionalParameter(ConsentValidatorConstants.REASON,
                subMandateRelatedInformation, initMandateRelatedInformation)) {

            return CommonUtil
                    .getErrorResponse(CommonConstants.BAD_REQUEST, ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                            ConsentValidatorConstants.REASON_MISMATCH);
        }
        if (!ConsentValidatorUtil.compareOptionalParameter(CommonConstants.FIRST_PAYMENT_DATE,
                subMandateRelatedInformation, initMandateRelatedInformation)) {

            return CommonUtil
                    .getErrorResponse(CommonConstants.BAD_REQUEST, ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                            ConsentValidatorConstants.FIRST_PAYMENT_DATE_MISMATCH_API_V4);
        }
        if (!ConsentValidatorUtil.compareOptionalParameter(CommonConstants.FINAL_PAYMENT_DATE,
                subMandateRelatedInformation, initMandateRelatedInformation)) {

            return CommonUtil
                    .getErrorResponse(CommonConstants.BAD_REQUEST, ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                            ErrorConstants.FINAL_PAYMENT_DATE_MISMATCH_API_V4);
        }
        if (!ConsentValidatorUtil.compareOptionalParameter(CommonConstants.RECURRING_PAYMENT_DATE,
                subMandateRelatedInformation, initMandateRelatedInformation)) {

            return CommonUtil
                    .getErrorResponse(CommonConstants.BAD_REQUEST, ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                            ErrorConstants.RECURRING_PAYMENT_DATE_MISMATCH_API_V4);
        }
        if (!subMandateRelatedInformation.has(ConsentValidatorConstants.FREQUENCY) ||
                !initMandateRelatedInformation.has(ConsentValidatorConstants.FREQUENCY)) {

            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                    ConsentValidatorConstants.FREQUENCY_NOT_FOUND_API_V4);
        }

        return CommonUtil.getSuccessResponse(requestId);
    }

    /**
     * Validate whether fields in creditor account from initiation and submission are same.
     *
     * @param subCreditorAccount  Creditor Account from submission request
     * @param initCreditorAccount Creditor Account from initiation request
     * @return Validation Result
     */
    public static JSONObject validateCreditorAcc(JSONObject subCreditorAccount, JSONObject initCreditorAccount,
                                                 String requestId) {

        JSONObject validationResponse = null;

        if (subCreditorAccount.has(CommonConstants.SCHEME_NAME)) {
            if (StringUtils.isEmpty(subCreditorAccount.getString(CommonConstants.SCHEME_NAME)) ||
                    !ConsentValidatorUtil.compareMandatoryParameter(
                            subCreditorAccount.getString(CommonConstants.SCHEME_NAME),
                            initCreditorAccount.getString(CommonConstants.SCHEME_NAME))) {

                return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                        ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                        ConsentValidatorConstants.CREDITOR_ACC_SCHEME_NAME_MISMATCH);
            }
        } else {
            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.FIELD_MISSING,
                    ConsentValidatorConstants.CREDITOR_ACC_SCHEME_NAME_NOT_FOUND);
        }

        if (subCreditorAccount.has(CommonConstants.IDENTIFICATION)) {
            if (StringUtils.isEmpty(subCreditorAccount.getString(CommonConstants.IDENTIFICATION)) ||
                    !ConsentValidatorUtil.compareMandatoryParameter(
                            subCreditorAccount.getString(CommonConstants.IDENTIFICATION),
                            initCreditorAccount.getString(CommonConstants.IDENTIFICATION))) {

                return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                        ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                        ConsentValidatorConstants.CREDITOR_ACC_IDENTIFICATION_MISMATCH);
            }
        } else {
            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.FIELD_MISSING,
                    ConsentValidatorConstants.CREDITOR_ACC_IDENTIFICATION_NOT_FOUND);
        }

        if (!ConsentValidatorUtil.compareOptionalParameter(CommonConstants.NAME,
                subCreditorAccount, initCreditorAccount)) {

            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                    ConsentValidatorConstants.CREDITOR_ACC_NAME_MISMATCH);
        }

        if (!ConsentValidatorUtil.compareOptionalParameter(CommonConstants.SECONDARY_IDENTIFICATION,
                subCreditorAccount, initCreditorAccount)) {

            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                    ConsentValidatorConstants.CREDITOR_ACC_SEC_IDENTIFICATION_MISMATCH);
        }

        if ((!subCreditorAccount.has(ConsentValidatorConstants.PROXY) &&
                initCreditorAccount.has(ConsentValidatorConstants.PROXY)) ||
                (subCreditorAccount.has(ConsentValidatorConstants.PROXY) &&
                        !initCreditorAccount.has(ConsentValidatorConstants.PROXY))) {

            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                    ConsentValidatorConstants.CREDITOR_ACC_PROXY_MISMATCH);

        } else if (subCreditorAccount.has(ConsentValidatorConstants.PROXY) &&
                initCreditorAccount.has(ConsentValidatorConstants.PROXY)) {

            JSONObject subCreditorAccountProxy = (JSONObject) subCreditorAccount.get(ConsentValidatorConstants.PROXY);
            JSONObject initCreditorAccountProxy =
                    (JSONObject) initCreditorAccount.get(ConsentValidatorConstants.PROXY);

            validationResponse = ConsentValidatorUtil.validateCreditorAccProxy(
                    subCreditorAccountProxy, initCreditorAccountProxy, requestId);
            if (validationResponse != null && validationResponse.toString().contains(CommonConstants.ERROR)) {
                return validationResponse;
            }
        }

        return CommonUtil.getSuccessResponse(requestId);
    }

    /**
     * Validate whether fields in debtor account from initiation and submission are same.
     *
     * @param subDebtorAccount  Debtor Account from submission request
     * @param initDebtorAccount Debtor Account from initiation request
     * @return Validation Result
     */
    public static JSONObject validateDebtorAcc(JSONObject subDebtorAccount, JSONObject initDebtorAccount,
                                               String requestId) {

        JSONObject validationResponse = null;

        if (subDebtorAccount.has(CommonConstants.SCHEME_NAME)) {
            if (StringUtils.isEmpty(subDebtorAccount.getString(CommonConstants.SCHEME_NAME)) ||
                    !ConsentValidatorUtil.compareMandatoryParameter(
                            subDebtorAccount.getString(CommonConstants.SCHEME_NAME),
                            initDebtorAccount.getString(CommonConstants.SCHEME_NAME))) {

                return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                        ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                        ConsentValidatorConstants.DEBTOR_ACC_SCHEME_NAME_MISMATCH);
            }
        } else {
            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.FIELD_MISSING,
                    ConsentValidatorConstants.DEBTOR_ACC_SCHEME_NAME_NOT_FOUND);
        }

        if (subDebtorAccount.has(CommonConstants.IDENTIFICATION)) {
            if (StringUtils.isEmpty(subDebtorAccount.getString(CommonConstants.IDENTIFICATION)) ||
                    !ConsentValidatorUtil.compareMandatoryParameter(
                            subDebtorAccount.getString(CommonConstants.IDENTIFICATION),
                            initDebtorAccount.getString(CommonConstants.IDENTIFICATION))) {

                return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                        ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                        ConsentValidatorConstants.DEBTOR_ACC_IDENTIFICATION_MISMATCH);
            }
        } else {
            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.FIELD_MISSING,
                    ConsentValidatorConstants.DEBTOR_ACC_IDENTIFICATION_NOT_FOUND);
        }

        if (!ConsentValidatorUtil.compareOptionalParameter(CommonConstants.NAME,
                subDebtorAccount, initDebtorAccount)) {

            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                    ConsentValidatorConstants.DEBTOR_ACC_NAME_MISMATCH);
        }

        if (!ConsentValidatorUtil.compareOptionalParameter(CommonConstants.SECONDARY_IDENTIFICATION,
                subDebtorAccount, initDebtorAccount)) {

            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                    ConsentValidatorConstants.DEBTOR_ACC_SEC_IDENTIFICATION_MISMATCH);
        }

        if ((!subDebtorAccount.has(ConsentValidatorConstants.PROXY) &&
                initDebtorAccount.has(ConsentValidatorConstants.PROXY)) ||
                (subDebtorAccount.has(ConsentValidatorConstants.PROXY) &&
                        !initDebtorAccount.has(ConsentValidatorConstants.PROXY))) {

            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                    ConsentValidatorConstants.DEBTOR_ACC_PROXY_MISMATCH);
        } else if (subDebtorAccount.has(ConsentValidatorConstants.PROXY) &&
                initDebtorAccount.has(ConsentValidatorConstants.PROXY)) {

            JSONObject subDebtorAccountProxy = (JSONObject) subDebtorAccount.get(ConsentValidatorConstants.PROXY);
            JSONObject initDebtorAccountProxy =
                    (JSONObject) initDebtorAccount.get(ConsentValidatorConstants.PROXY);

            validationResponse = ConsentValidatorUtil.validateDebtorAccProxy(subDebtorAccountProxy,
                    initDebtorAccountProxy, requestId);

            if (validationResponse != null && validationResponse.toString().contains(CommonConstants.ERROR)) {
                return validationResponse;
            }
        }

        return CommonUtil.getSuccessResponse(requestId);
    }

    /**
     * Validate whether fields in creditor account proxy from initiation and submission are same.
     *
     * @param subCreditorAccountProxy  Creditor Account Proxy from submission request
     * @param initCreditorAccountProxy Creditor Account Proxy from initiation request
     * @return Validation Result
     */
    public static JSONObject validateCreditorAccProxy(JSONObject subCreditorAccountProxy,
                                                      JSONObject initCreditorAccountProxy, String requestId) {

        if (subCreditorAccountProxy.has(CommonConstants.IDENTIFICATION_TITLE)) {
            if (StringUtils.isEmpty(subCreditorAccountProxy.getString(
                    CommonConstants.IDENTIFICATION_TITLE)) ||
                    !ConsentValidatorUtil.compareMandatoryParameter(
                            subCreditorAccountProxy.getString(CommonConstants.IDENTIFICATION_TITLE),
                            initCreditorAccountProxy.getString(CommonConstants.IDENTIFICATION_TITLE))) {

                return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                        ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                        ConsentValidatorConstants.CREDITOR_ACC_PROXY_IDENTIFICATION_MISMATCH);
            }
        } else {
            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.FIELD_MISSING,
                    ConsentValidatorConstants.CREDITOR_ACC_PROXY_IDENTIFICATION_NOT_FOUND);
        }

        // validate code
        if (subCreditorAccountProxy.has(ConsentValidatorConstants.CODE)) {
            if (StringUtils.isEmpty(subCreditorAccountProxy.getString(
                    ConsentValidatorConstants.CODE)) ||
                    !ConsentValidatorUtil.compareMandatoryParameter(
                            subCreditorAccountProxy.getString(ConsentValidatorConstants.CODE),
                            initCreditorAccountProxy.getString(ConsentValidatorConstants.CODE))) {

                return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                        ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                        ConsentValidatorConstants.CREDITOR_ACC_PROXY_CODE_MISMATCH);
            }
        } else {
            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.FIELD_MISSING,
                    ConsentValidatorConstants.CREDITOR_ACC_PROXY_CODE_NOT_FOUND);
        }

        // validate optional Type
        if (!ConsentValidatorUtil.compareOptionalParameter(ConsentValidatorConstants.TYPE,
                subCreditorAccountProxy, initCreditorAccountProxy)) {

            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                    ConsentValidatorConstants.CREDITOR_ACC_PROXY_TYPE_MISMATCH);
        }

        return CommonUtil.getSuccessResponse(requestId);
    }

    /**
     * Method to validate payment submission UltimateCreditor payload.
     *
     * @param subUltimateCreditor  UltimateCreditor from submission request
     * @param initUltimateCreditor UltimateCreditor Proxy from initiation request
     * @return Validation Result
     */
    public static JSONObject validateUltimateCreditor(JSONObject subUltimateCreditor, JSONObject initUltimateCreditor,
                                                      String requestId) {

        if (!ConsentValidatorUtil.compareOptionalParameter(CommonConstants.NAME_TITLE,
                subUltimateCreditor, initUltimateCreditor)) {

            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                    ConsentValidatorConstants.ULTIMATE_CREDITOR_NAME_MISMATCH);
        }
        if (!ConsentValidatorUtil.compareOptionalParameter(CommonConstants.IDENTIFICATION_TITLE,
                subUltimateCreditor, initUltimateCreditor)) {

            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                    ConsentValidatorConstants.ULTIMATE_CREDITOR_IDENTIFICATION_MISMATCH);
        }
        if (!ConsentValidatorUtil.compareOptionalParameter(ConsentValidatorConstants.LEI,
                subUltimateCreditor, initUltimateCreditor)) {

            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                    ConsentValidatorConstants.ULTIMATE_CREDITOR_LEI_MISMATCH);
        }
        if (!ConsentValidatorUtil.compareOptionalParameter(CommonConstants.SCHEME_NAME,
                subUltimateCreditor, initUltimateCreditor)) {

            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                    ConsentValidatorConstants.ULTIMATE_CREDITOR_SCHEME_NAME_MISMATCH);
        }

        if (!ConsentValidatorUtil.isValidAddress((JSONObject) subUltimateCreditor
                        .get(CommonConstants.POSTAL_ADDRESS),
                (JSONObject) initUltimateCreditor.get(CommonConstants.POSTAL_ADDRESS))) {

            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                    ConsentValidatorConstants.ULTIMATE_CREDITOR_POSTAL_ADDRESS_MISMATCH);
        }

        return CommonUtil.getSuccessResponse(requestId);
    }

    /**
     * Method to validate whether address is valid.
     *
     * @param submissionAddress Address from submission request
     * @param initiationAddress Address from initiation request
     * @return is valid address
     */
    public static boolean isValidAddress(JSONObject submissionAddress, JSONObject initiationAddress) {

        if ((submissionAddress == null && initiationAddress != null) ||
                (submissionAddress != null && initiationAddress == null)) {
            return false;

        } else if (submissionAddress != null) {

            if (!ConsentValidatorUtil.compareOptionalParameter(ConsentValidatorConstants.ADDRESS_TYPE,
                    submissionAddress, initiationAddress)) {
                return false;
            }

            if (!ConsentValidatorUtil.compareOptionalParameter(ConsentValidatorConstants.DEPARTMENT,
                    submissionAddress, initiationAddress)) {
                return false;
            }

            if (!ConsentValidatorUtil.compareOptionalParameter(ConsentValidatorConstants.SUB_DEPARTMENT,
                    submissionAddress, initiationAddress)) {
                return false;
            }

            if (!ConsentValidatorUtil.compareOptionalParameter(ConsentValidatorConstants.STREET_NAME,
                    submissionAddress, initiationAddress)) {
                return false;
            }

            if (!ConsentValidatorUtil.compareOptionalParameter(ConsentValidatorConstants.BUILDING_NUMBER,
                    submissionAddress, initiationAddress)) {
                return false;
            }

            if (!ConsentValidatorUtil.compareOptionalParameter(ConsentValidatorConstants.BUILDING_NAME,
                    submissionAddress, initiationAddress)) {
                return false;
            }

            if (!ConsentValidatorUtil.compareOptionalParameter(ConsentValidatorConstants.FLOOR,
                    submissionAddress, initiationAddress)) {
                return false;
            }

            if (!ConsentValidatorUtil.compareOptionalParameter(ConsentValidatorConstants.UNIT_NUMBER,
                    submissionAddress, initiationAddress)) {
                return false;
            }

            if (!ConsentValidatorUtil.compareOptionalParameter(ConsentValidatorConstants.ROOM,
                    submissionAddress, initiationAddress)) {
                return false;
            }

            if (!ConsentValidatorUtil.compareOptionalParameter(ConsentValidatorConstants.POST_BOX,
                    submissionAddress, initiationAddress)) {
                return false;
            }

            if (!ConsentValidatorUtil.compareOptionalParameter(ConsentValidatorConstants.TOWN_LOCATION_NAME,
                    submissionAddress, initiationAddress)) {
                return false;
            }

            if (!ConsentValidatorUtil.compareOptionalParameter(ConsentValidatorConstants.DISTRICT_NAME,
                    submissionAddress, initiationAddress)) {
                return false;
            }

            if (!ConsentValidatorUtil.compareOptionalParameter(ConsentValidatorConstants.CARE_OF,
                    submissionAddress, initiationAddress)) {
                return false;
            }

            if (!ConsentValidatorUtil.compareOptionalParameter(ConsentValidatorConstants.POST_CODE,
                    submissionAddress, initiationAddress)) {
                return false;
            }

            if (!ConsentValidatorUtil.compareOptionalParameter(ConsentValidatorConstants.TOWN_NAME,
                    submissionAddress, initiationAddress)) {
                return false;
            }

            if (!ConsentValidatorUtil.compareOptionalParameter(ConsentValidatorConstants.COUNTRY,
                    submissionAddress, initiationAddress)) {
                return false;
            }

            if ((!initiationAddress.has(ConsentValidatorConstants.ADDRESS_LINE) &&
                    submissionAddress.has(ConsentValidatorConstants.ADDRESS_LINE)) ||
                    (initiationAddress.has(ConsentValidatorConstants.ADDRESS_LINE) &&
                            !submissionAddress.has(ConsentValidatorConstants.ADDRESS_LINE))) {
                return false;

            } else {
                JSONArray subAddressLine = submissionAddress.getJSONArray(ConsentValidatorConstants.ADDRESS_LINE);
                JSONArray initiationAddressLine =
                        initiationAddress.getJSONArray(ConsentValidatorConstants.ADDRESS_LINE);

                if (!new HashSet<>(subAddressLine.toList()).containsAll(initiationAddressLine.toList())) {

                    return false;
                }
            }

            if ((!initiationAddress.has(ConsentValidatorConstants.COUNTRY_SUB_DIVISION) &&
                    submissionAddress.has(ConsentValidatorConstants.COUNTRY_SUB_DIVISION)) ||
                    (initiationAddress.has(ConsentValidatorConstants.COUNTRY_SUB_DIVISION) &&
                            !submissionAddress.has(ConsentValidatorConstants.COUNTRY_SUB_DIVISION))) {
                return false;

            } else if (!ConsentValidatorUtil.compareOptionalParameter(ConsentValidatorConstants.COUNTRY_SUB_DIVISION,
                    submissionAddress, initiationAddress)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method to validate payment submission UltimateDebtor payload.
     *
     * @param subUltimateDebtor  UltimateDebtor from submission request
     * @param initUltimateDebtor UltimateDebtor Proxy from initiation request
     * @return Validation Result
     */
    public static JSONObject validateUltimateDebtor(JSONObject subUltimateDebtor, JSONObject initUltimateDebtor,
                                                    String requestId) {

        if (!ConsentValidatorUtil.compareOptionalParameter(CommonConstants.NAME_TITLE,
                subUltimateDebtor, initUltimateDebtor)) {

            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                    ConsentValidatorConstants.ULTIMATE_DEBTOR_NAME_MISMATCH);
        }
        if (!ConsentValidatorUtil.compareOptionalParameter(CommonConstants.IDENTIFICATION_TITLE,
                subUltimateDebtor, initUltimateDebtor)) {

            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                    ConsentValidatorConstants.ULTIMATE_DEBTOR_IDENTIFICATION_MISMATCH);
        }
        if (!ConsentValidatorUtil.compareOptionalParameter(ConsentValidatorConstants.LEI,
                subUltimateDebtor, initUltimateDebtor)) {

            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                    ConsentValidatorConstants.ULTIMATE_DEBTOR_LEI_MISMATCH);
        }
        if (!ConsentValidatorUtil.compareOptionalParameter(CommonConstants.SCHEME_NAME,
                subUltimateDebtor, initUltimateDebtor)) {

            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                    ConsentValidatorConstants.ULTIMATE_DEBTOR_SCHEME_NAME_MISMATCH);
        }

        if (!ConsentValidatorUtil.isValidAddress((JSONObject) subUltimateDebtor
                        .get(CommonConstants.POSTAL_ADDRESS),
                (JSONObject) initUltimateDebtor.get(CommonConstants.POSTAL_ADDRESS))) {

            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                    ConsentValidatorConstants.ULTIMATE_DEBTOR_POSTAL_ADDRESS_MISMATCH);
        }

        return CommonUtil.getSuccessResponse(requestId);
    }

    /**
     * Validate whether fields in debtor account from initiation and submission are same.
     *
     * @param subDebtorAccountProxy  Debtor Account from submission request
     * @param initDebtorAccountProxy Debtor Account from initiation request
     * @return Validation Result
     */
    public static JSONObject validateDebtorAccProxy(JSONObject subDebtorAccountProxy,
                                                    JSONObject initDebtorAccountProxy, String requestId) {

        if (subDebtorAccountProxy.has(CommonConstants.IDENTIFICATION_TITLE)) {

            if (StringUtils.isEmpty(subDebtorAccountProxy.getString(CommonConstants.IDENTIFICATION_TITLE)) ||
                    !ConsentValidatorUtil.compareMandatoryParameter(
                            subDebtorAccountProxy.getString(CommonConstants.IDENTIFICATION_TITLE),
                            initDebtorAccountProxy.getString(CommonConstants.IDENTIFICATION_TITLE))) {

                return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                        ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                        ConsentValidatorConstants.DEBTOR_ACC_PROXY_IDENTIFICATION_MISMATCH);
            }
        } else {
            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.FIELD_MISSING,
                    ConsentValidatorConstants.DEBTOR_ACC_PROXY_IDENTIFICATION_NOT_FOUND);
        }

        // validate code
        if (subDebtorAccountProxy.has(ConsentValidatorConstants.CODE)) {
            if (StringUtils.isEmpty(subDebtorAccountProxy.getString(ConsentValidatorConstants.CODE)) ||
                    !ConsentValidatorUtil.compareMandatoryParameter(
                            subDebtorAccountProxy.getString(ConsentValidatorConstants.CODE),
                            initDebtorAccountProxy.getString(ConsentValidatorConstants.CODE))) {

                return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                        ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                        ConsentValidatorConstants.DEBTOR_ACC_PROXY_CODE_MISMATCH);
            }
        } else {
            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.FIELD_MISSING,
                    ConsentValidatorConstants.DEBTOR_ACC_PROXY_CODE_NOT_FOUND);
        }

        // validate optional Type
        if (!ConsentValidatorUtil.compareOptionalParameter(ConsentValidatorConstants.TYPE,
                subDebtorAccountProxy, initDebtorAccountProxy)) {

            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                    ConsentValidatorConstants.DEBTOR_ACC_PROXY_TYPE_MISMATCH);
        }
        return CommonUtil.getSuccessResponse(requestId);
    }

}
