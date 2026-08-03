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

import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.wso2.non.regulated.ob.extensions.configurations.ConfigurableProperties;
import org.wso2.non.regulated.ob.extensions.constants.CommonConstants;
import org.wso2.non.regulated.ob.extensions.exceptions.ConsentException;
import org.wso2.non.regulated.ob.extensions.model.Account;
import org.wso2.non.regulated.ob.extensions.model.PopulateConsentAuthorizeScreenRequestBody;
import org.wso2.non.regulated.ob.extensions.model.SuccessResponsePopulateConsentAuthorizeScreenDataConsentData;
import org.wso2.non.regulated.ob.extensions.model.SuccessResponsePopulateConsentAuthorizeScreenDataConsentDataPermissionsInner;
import org.wso2.non.regulated.ob.extensions.model.SuccessResponsePopulateConsentAuthorizeScreenDataConsumerDataAccountsInner;
import org.wso2.non.regulated.ob.extensions.model.UserGrantedData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility methods for reading, validating, and building consent data from OAuth authorization_details.
 */
public class ConsentAuthorizationUtil {

    // Field names used only in this class.

    private static final String AUTHORIZATION_DETAILS = "authorization_details";

    private static final String DOMESTIC_PAYMENTS = "Domestic Payments";

    private static final String DOMESTIC_SCHEDULED_PAYMENTS = "Domestic Scheduled Payments";

    private static final String DOMESTIC_STANDING_ORDER_PAYMENTS = "Domestic Standing Orders";

    private static final String INTERNATIONAL_PAYMENTS = "International Payments";

    private static final String INTERNATIONAL_SCHEDULED_PAYMENTS = "International Scheduled Payments";

    private static final String INTERNATIONAL_STANDING_ORDER_PAYMENTS = "International Standing Orders";

    private static final String DATA_SIMPLE = "data";

    private static final String EXPIRATION_DATE_TITLE = "Expiration Date Time";

    private static final String TRANSACTION_FROM_DATE_TITLE = "Transaction From Date Time";

    private static final String TRANSACTION_TO_DATE_TITLE = "Transaction To Date Time";

    private static final String USER_ID_KEY_NAME = "userID";

    private static final String DEBTOR_ACC_TITLE = "Debtor Account";

    private static final String SCHEME_NAME_TITLE = "Scheme Name";

    private static final String SECONDARY_IDENTIFICATION_TITLE = "Secondary Identification";

    private static final String SIMPLE_ACCOUNT_ID = "account_id";

    private static final String DISPLAY_NAME = "display_name";

    private static final String REJECT = "REJECT";

    private static final String PERMISSION = "Permission";

    private static final String READ_REFUND = "ReadRefundAccount";

    private static final String READ_REFUND_TITLE = "Read Refund Account";

    private static final String INSTRUCTION_IDENTIFICATION_TITLE = "Instruction Identification";

    private static final String END_TO_END_IDENTIFICATION_TITLE = "End to End Identification";

    private static final String REQUESTED_EXECUTION_DATE_TIME_TITLE = "Requested Execution Date Time";

    private static final String FIRST_PAYMENT_AMOUNT_TITLE = "First Payment Amount";

    private static final String RECURRING_PAYMENT_AMOUNT_TITLE = "Recurring Payment Amount";

    private static final String FINAL_PAYMENT_AMOUNT_TITLE = "Final Payment Amount";

    private static final String CURRENCY_OF_TRANSFER_TITLE = "Currency of Transfer";

    private static final String INSTRUCTED_AMOUNT_TITLE = "Instructed Amount";

    private static final String AMOUNT_TITLE = "Amount";

    private static final String PERMISSION_TITLE = "Permission";

    private static final String CREDITOR_ACC_TITLE = "Creditor Account";

    private static final String CURRENCY_TITLE = "Currency";

    private static final String PAYMENT_TYPE_TITLE = "Payment Type";

    /**
     * Extracts the authorization_details entry from a populate-consent-authorize-screen request.
     */
    public static JSONObject getAuthorizationDetails(PopulateConsentAuthorizeScreenRequestBody requestBody) {
        return extractAuthorizationDetails(requestBody.getData().getRequestParameters());
    }

    /**
     * Extracts the authorization_details entry from a persist-authorized-consent request.
     */
    public static JSONObject getAuthorizationDetails(UserGrantedData userGrantedData) {
        return extractAuthorizationDetails(userGrantedData.getRequestParameters());
    }

    @SuppressWarnings("unchecked")
    private static JSONObject extractAuthorizationDetails(Object requestParameters) {

        JSONObject requestParams = new JSONObject((Map<String, Object>) requestParameters);
        return requestParams.getJSONArray(AUTHORIZATION_DETAILS).getJSONObject(0);
    }

    /**
     * Validates authorization_details business rules not covered by the RAR JSON schema, dispatching by type.
     *
     * @param authorizationDetails the authorization_details entry to validate
     * @param authDetailsType      the RAR "type" discriminator
     * @throws ConsentException if a business rule is violated
     */
    public static void validateAuthorizationDetails(JSONObject authorizationDetails, String authDetailsType)
            throws ConsentException {

        switch (authDetailsType) {
            case CommonConstants.ACCOUNT_INFORMATION:
                validateAccountAuthorizationDetails(authorizationDetails);
                break;
            case CommonConstants.DOMESTIC_PAYMENT:
            case CommonConstants.DOMESTIC_SCHEDULED_PAYMENT:
            case CommonConstants.DOMESTIC_STANDING_ORDER:
            case CommonConstants.INTERNATIONAL_PAYMENT:
                validatePaymentAuthorizationDetails(authorizationDetails, authDetailsType);
                break;
            default:
                break;
        }
    }

    /**
     * Validates account_information business rules: expiry not in the past, transaction date order, and
     * permissions.
     */
    private static void validateAccountAuthorizationDetails(JSONObject authorizationDetails) throws ConsentException {

        if (authorizationDetails.has(CommonConstants.EXPIRATION_DATE) &&
                !isDateInFuture(authorizationDetails.getString(CommonConstants.EXPIRATION_DATE))) {
            throw new ConsentException(ConsentException.ErrorCode.BAD_REQUEST,
                    "ExpirationDateTime has already elapsed.");
        }

        if (authorizationDetails.has(CommonConstants.TRANSACTION_FROM_DATE) &&
                authorizationDetails.has(CommonConstants.TRANSACTION_TO_DATE) &&
                !isChronologicalOrder(authorizationDetails.getString(CommonConstants.TRANSACTION_FROM_DATE),
                        authorizationDetails.getString(CommonConstants.TRANSACTION_TO_DATE))) {
            throw new ConsentException(ConsentException.ErrorCode.BAD_REQUEST,
                    "TransactionFromDateTime must not be after TransactionToDateTime.");
        }

        if (authorizationDetails.has(CommonConstants.PERMISSIONS)) {
            validatePermissions(authorizationDetails.getJSONArray(CommonConstants.PERMISSIONS));
        }
    }

    /**
     * Whether the given ISO 8601 date-time is still in the future relative to now.
     */
    private static boolean isDateInFuture(String dateTimeValue) {

        try {
            OffsetDateTime dateTime = OffsetDateTime.parse(dateTimeValue);
            return dateTime.isAfter(OffsetDateTime.now(dateTime.getOffset()));
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Whether {@code earlierDateTime} is at or before {@code laterDateTime}.
     */
    private static boolean isChronologicalOrder(String earlierDateTime, String laterDateTime) {

        try {
            return !OffsetDateTime.parse(earlierDateTime).isAfter(OffsetDateTime.parse(laterDateTime));
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Rejects restricted permissions and invalid permission combinations.
     */
    private static void validatePermissions(JSONArray permissions) throws ConsentException {

        List<String> permissionList = new ArrayList<>();
        for (int i = 0; i < permissions.length(); i++) {
            String permission = permissions.getString(i);
            if (ConfigurableProperties.RESTRICTED_PERMISSIONS.contains(permission)) {
                throw new ConsentException(ConsentException.ErrorCode.BAD_REQUEST,
                        "Permission " + permission + " is restricted by this deployment.");
            }
            permissionList.add(permission);
        }

        if (!isPermissionCombinationValid(permissionList)) {
            throw new ConsentException(ConsentException.ErrorCode.BAD_REQUEST,
                    "Requested permission combination is invalid.");
        }
    }

    /**
     * Checks that transaction-detail permissions have their required dependencies.
     */
    private static boolean isPermissionCombinationValid(List<String> permissions) {

        if (!permissions.contains(CommonConstants.READ_ACCOUNTS_BASIC) &&
                !permissions.contains(CommonConstants.READ_ACCOUNTS_DETAIL)) {
            return true;
        }

        boolean hasCreditsOrDebits = permissions.contains(CommonConstants.READ_TRANSACTIONS_CREDITS) ||
                permissions.contains(CommonConstants.READ_TRANSACTIONS_DEBITS);
        boolean hasBasicOrDetail = permissions.contains(CommonConstants.READ_TRANSACTIONS_BASIC) ||
                permissions.contains(CommonConstants.READ_TRANSACTIONS_DETAIL);

        if ((permissions.contains(CommonConstants.READ_TRANSACTIONS_BASIC) ||
                permissions.contains(CommonConstants.READ_TRANSACTIONS_DETAIL)) && !hasCreditsOrDebits) {
            return false;
        }
        return !((permissions.contains(CommonConstants.READ_TRANSACTIONS_CREDITS) ||
                permissions.contains(CommonConstants.READ_TRANSACTIONS_DEBITS)) && !hasBasicOrDetail);
    }

    /**
     * Validates payment business rules: cut-off time, maximum instructed amount, and (for scheduled payments
     * and standing orders) requested dates.
     */
    private static void validatePaymentAuthorizationDetails(JSONObject authorizationDetails, String authDetailsType)
            throws ConsentException {

        JSONObject initiation = authorizationDetails.optJSONObject(CommonConstants.INITIATION);
        if (initiation == null) {
            return;
        }

        if (shouldRejectForCutOff()) {
            throw new ConsentException(ConsentException.ErrorCode.BAD_REQUEST,
                    "Payment submission cut-off time has elapsed for today.", CommonConstants.UK_REJECTED_STATUS);
        }

        JSONObject instructedAmount = initiation.optJSONObject(CommonConstants.INSTRUCTED_AMOUNT);
        if (instructedAmount != null && instructedAmount.has(CommonConstants.AMOUNT) &&
                !isWithinMaxInstructedAmount(instructedAmount.getString(CommonConstants.AMOUNT))) {
            throw new ConsentException(ConsentException.ErrorCode.BAD_REQUEST,
                    "InstructedAmount exceeds the maximum amount permitted by this deployment.");
        }

        if (CommonConstants.DOMESTIC_SCHEDULED_PAYMENT.equals(authDetailsType) &&
                initiation.has(CommonConstants.REQUEST_EXECUTION_DATE)) {
            validateExecutionDate(initiation.getString(CommonConstants.REQUEST_EXECUTION_DATE));
        }

        if (CommonConstants.DOMESTIC_STANDING_ORDER.equals(authDetailsType) &&
                initiation.has(CommonConstants.MANDATE_RELATED_INFORMATION)) {
            validateStandingOrderDates(initiation);
        }
    }

    /**
     * Validates that a scheduled payment's execution date is in the future and within the allowed range.
     */
    private static void validateExecutionDate(String executionDate) throws ConsentException {

        if (!isDateInFuture(executionDate)) {
            throw new ConsentException(ConsentException.ErrorCode.BAD_REQUEST,
                    "RequestedExecutionDateTime has already elapsed.");
        }
        if (!isWithinMaxFutureDate(executionDate)) {
            throw new ConsentException(ConsentException.ErrorCode.BAD_REQUEST,
                    "RequestedExecutionDateTime is further in the future than this deployment permits.");
        }
    }

    /**
     * Validates a standing order's First/Recurring/Final payment dates and amounts.
     */
    private static void validateStandingOrderDates(JSONObject initiation) throws ConsentException {

        JSONObject mandateInfo = initiation.getJSONObject(CommonConstants.MANDATE_RELATED_INFORMATION);
        if (!mandateInfo.has(CommonConstants.FIRST_PAYMENT_DATE)) {
            return;
        }
        String firstPaymentDate = mandateInfo.getString(CommonConstants.FIRST_PAYMENT_DATE);

        if (!isDateInFuture(firstPaymentDate)) {
            throw new ConsentException(ConsentException.ErrorCode.BAD_REQUEST,
                    "FirstPaymentDateTime has already elapsed.");
        }

        String recurringPaymentDate = mandateInfo.optString(CommonConstants.RECURRING_PAYMENT_DATE, null);
        String finalPaymentDate = mandateInfo.optString(CommonConstants.FINAL_PAYMENT_DATE, null);

        if (recurringPaymentDate != null && !isChronologicalOrder(firstPaymentDate, recurringPaymentDate)) {
            throw new ConsentException(ConsentException.ErrorCode.BAD_REQUEST,
                    "FirstPaymentDateTime must not be after RecurringPaymentDateTime.");
        }
        if (finalPaymentDate != null) {
            String precedingDate = recurringPaymentDate != null ? recurringPaymentDate : firstPaymentDate;
            if (!isChronologicalOrder(precedingDate, finalPaymentDate)) {
                throw new ConsentException(ConsentException.ErrorCode.BAD_REQUEST,
                        "FinalPaymentDateTime must not be before " +
                                (recurringPaymentDate != null ? "RecurringPaymentDateTime."
                                        : "FirstPaymentDateTime."));
            }
        }

        JSONObject firstPaymentAmount = initiation.optJSONObject(CommonConstants.FIRST_PAYMENT_AMOUNT);
        JSONObject recurringPaymentAmount = initiation.optJSONObject(CommonConstants.RECURRING_AMOUNT);
        if (recurringPaymentDate != null && firstPaymentAmount != null && recurringPaymentAmount != null &&
                firstPaymentDate.equals(recurringPaymentDate) &&
                firstPaymentAmount.optString(CommonConstants.AMOUNT, "")
                        .equals(recurringPaymentAmount.optString(CommonConstants.AMOUNT, ""))) {
            throw new ConsentException(ConsentException.ErrorCode.BAD_REQUEST,
                    "RecurringPaymentDateTime/Amount must differ from FirstPaymentDateTime/Amount.");
        }
    }

    /**
     * Whether the current time is past the configured daily cut-off and the policy is to reject.
     */
    private static boolean shouldRejectForCutOff() {

        return Boolean.parseBoolean(ConfigurableProperties.CUTOFF_DATE_ENABLED) &&
                REJECT.equals(ConfigurableProperties.CUTOFF_DATE_POLICY) &&
                hasCutOffTimeElapsed();
    }

    private static boolean hasCutOffTimeElapsed() {

        OffsetTime dailyCutOffTime = OffsetTime.parse(ConfigurableProperties.DAILY_CUTOFF);
        OffsetTime currentTime = LocalTime.now().atOffset(dailyCutOffTime.getOffset());
        return currentTime.isAfter(dailyCutOffTime);
    }

    /**
     * Whether the date-time is within the configured maximum number of future days.
     */
    private static boolean isWithinMaxFutureDate(String dateTimeValue) {

        try {
            OffsetDateTime dateTime = OffsetDateTime.parse(dateTimeValue);
            OffsetDateTime maxFutureDate = OffsetDateTime.now(dateTime.getOffset())
                    .plusDays(Long.parseLong(ConfigurableProperties.MAX_FUTURE_PAYMENT_DAYS));
            return !dateTime.isAfter(maxFutureDate);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Whether the instructed amount is within the configured maximum.
     */
    private static boolean isWithinMaxInstructedAmount(String instructedAmount) {

        return Double.parseDouble(instructedAmount) <= Double.parseDouble(ConfigurableProperties.MAX_INSTRUCTED_AMOUNT);
    }

    /**
     * Builds the authorize-screen consent data for an {@code account_information} consent.
     *
     * @param authorizationDetails the authorization_details entry (already validated)
     * @return the consent data to show on the authorize screen
     */
    public static SuccessResponsePopulateConsentAuthorizeScreenDataConsentData getAccountsConsentData(
            JSONObject authorizationDetails) {

        SuccessResponsePopulateConsentAuthorizeScreenDataConsentData consentData =
                new SuccessResponsePopulateConsentAuthorizeScreenDataConsentData();

        if (authorizationDetails != null) {
            Map<String, List<String>> basicConsentData = new HashMap<>();
            putIfPresent(basicConsentData, EXPIRATION_DATE_TITLE, authorizationDetails,
                    CommonConstants.EXPIRATION_DATE);
            putIfPresent(basicConsentData, TRANSACTION_FROM_DATE_TITLE, authorizationDetails,
                    CommonConstants.TRANSACTION_FROM_DATE);
            putIfPresent(basicConsentData, TRANSACTION_TO_DATE_TITLE, authorizationDetails,
                    CommonConstants.TRANSACTION_TO_DATE);
            consentData.setBasicConsentData(basicConsentData);

            SuccessResponsePopulateConsentAuthorizeScreenDataConsentDataPermissionsInner consentDataPermissions =
                    new SuccessResponsePopulateConsentAuthorizeScreenDataConsentDataPermissionsInner();
            if (authorizationDetails.has(CommonConstants.PERMISSIONS)) {
                JSONArray permissionsArray = authorizationDetails.getJSONArray(CommonConstants.PERMISSIONS);
                List<String> permissions = new ArrayList<>();
                for (int i = 0; i < permissionsArray.length(); i++) {
                    permissions.add(permissionsArray.getString(i));
                }
                consentDataPermissions.setDisplayValues(permissions);
                consentDataPermissions.setUid("1");
            }

            consentData.setPermissions(Collections.singletonList(consentDataPermissions));
            consentData.setType(authorizationDetails.getString(CommonConstants.AUTHORIZATION_DETAILS_TYPE));
            consentData.setAllowMultipleAccounts(true);
        }

        return consentData;
    }

    /**
     * Builds the authorize-screen consent data for a payment consent.
     *
     * @param authorizationDetails the authorization_details entry (already validated)
     * @return the consent data to show on the authorize screen
     */
    public static SuccessResponsePopulateConsentAuthorizeScreenDataConsentData getPaymentsConsentData(
            JSONObject authorizationDetails) {

        SuccessResponsePopulateConsentAuthorizeScreenDataConsentData consentData =
                new SuccessResponsePopulateConsentAuthorizeScreenDataConsentData();

        JSONObject initiation = authorizationDetails.optJSONObject(CommonConstants.INITIATION);

        if (initiation != null) {
            if (initiation.has(CommonConstants.INSTRUCTION_IDENTIFICATION) &&
                    initiation.has(CommonConstants.END_TO_END_IDENTIFICATION) &&
                    !initiation.has(CommonConstants.REQUEST_EXECUTION_DATE)) {

                // For Domestic and International Payments
                consentData.setBasicConsentData(populateSinglePaymentData(authorizationDetails));

            } else if (initiation.has(CommonConstants.INSTRUCTION_IDENTIFICATION) &&
                    initiation.has(CommonConstants.REQUEST_EXECUTION_DATE)) {

                // For Domestic and International Scheduled Payments
                consentData.setBasicConsentData(populateScheduledPaymentData(authorizationDetails));

            } else if (initiation.has(CommonConstants.MANDATE_RELATED_INFORMATION)) {

                // For Domestic and International Standing Order Payments
                consentData.setBasicConsentData(populateStandingOrderPaymentData(authorizationDetails));
            }

            // Adding debtor account information
            if (initiation.has(CommonConstants.DEBTOR_ACC)) {
                JSONObject debtorAcc = initiation.getJSONObject(CommonConstants.DEBTOR_ACC);
                consentData.getBasicConsentData().put(DEBTOR_ACC_TITLE,
                        populateAccountDisplayData(debtorAcc));

                Account account = new Account();
                account.setDisplayName(debtorAcc.getString(CommonConstants.IDENTIFICATION));
                consentData.setInitiatedAccountsForConsent(Collections.singletonList(account));
            }

            // Adding creditor account information
            consentData.getBasicConsentData().put(CREDITOR_ACC_TITLE,
                    populateAccountDisplayData(initiation.getJSONObject(CommonConstants.CREDITOR_ACC)));
        }

        consentData.setType(authorizationDetails.getString(CommonConstants.AUTHORIZATION_DETAILS_TYPE));
        consentData.setIsReauthorization(false);
        consentData.setAllowMultipleAccounts(false);

        return consentData;
    }

    /**
     * Formats a debtor or creditor account into "Title : value" display lines.
     */
    private static List<String> populateAccountDisplayData(JSONObject account) {

        List<String> accountData = new ArrayList<>();
        addIfNotEmpty(accountData, SCHEME_NAME_TITLE,
                account.optString(CommonConstants.SCHEME_NAME, ""));
        addIfNotEmpty(accountData, CommonConstants.IDENTIFICATION_TITLE,
                account.optString(CommonConstants.IDENTIFICATION, ""));
        addIfNotEmpty(accountData, CommonConstants.NAME_TITLE, account.optString(CommonConstants.NAME, ""));
        addIfNotEmpty(accountData, SECONDARY_IDENTIFICATION_TITLE,
                account.optString(CommonConstants.SECONDARY_IDENTIFICATION, ""));
        return accountData;
    }

    private static void addIfNotEmpty(List<String> target, String title, String value) {
        if (!value.isEmpty()) {
            target.add(title + " : " + value);
        }
    }

    /**
     * Adds {@code json.get(key)} to {@code basicConsentData} under {@code title}, if present.
     */
    private static void putIfPresent(Map<String, List<String>> basicConsentData, String title, JSONObject json,
                                      String key) {
        if (json.has(key)) {
            basicConsentData.put(title, Collections.singletonList(json.getString(key)));
        }
    }

    /**
     * Adds an amount's value and currency to {@code basicConsentData} under {@code title}, if present.
     */
    private static void putAmountIfPresent(Map<String, List<String>> basicConsentData, String title,
                                            JSONObject parent, String amountKey) {
        JSONObject amount = parent.optJSONObject(amountKey);
        if (amount == null) {
            return;
        }
        List<String> amountList = new ArrayList<>();
        if (amount.has(CommonConstants.AMOUNT)) {
            amountList.add(AMOUNT_TITLE + " : " + amount.optString(CommonConstants.AMOUNT));
        }
        if (amount.has(CommonConstants.CURRENCY)) {
            amountList.add(CURRENCY_TITLE + " : " + amount.optString(CommonConstants.CURRENCY));
        }
        basicConsentData.put(title, amountList);
    }

    /**
     * Sets the payment-type label and, for international payments, the currency-of-transfer.
     *
     * @return whether this is an international payment
     */
    private static boolean putPaymentTypeAndCurrency(Map<String, List<String>> basicConsentData,
                                                      JSONObject initiation, String domesticLabel,
                                                      String internationalLabel) {
        boolean isInternational = initiation.has(CommonConstants.CURRENCY_OF_TRANSFER);
        basicConsentData.put(PAYMENT_TYPE_TITLE,
                Collections.singletonList(isInternational ? internationalLabel : domesticLabel));
        if (isInternational) {
            basicConsentData.put(CURRENCY_OF_TRANSFER_TITLE,
                    Collections.singletonList(initiation.getString(CommonConstants.CURRENCY_OF_TRANSFER)));
        }
        return isInternational;
    }

    /**
     * Populates display data for a single (immediate) domestic or international payment.
     *
     * @param data the authorization_details entry
     */
    private static Map<String, List<String>> populateSinglePaymentData(JSONObject data) {

        Map<String, List<String>> basicConsentData = new HashMap<>();
        if (data == null || !data.has(CommonConstants.INITIATION)) {
            return basicConsentData;
        }
        JSONObject initiation = data.getJSONObject(CommonConstants.INITIATION);

        putPaymentTypeAndCurrency(basicConsentData, initiation, DOMESTIC_PAYMENTS,
                INTERNATIONAL_PAYMENTS);
        putIfPresent(basicConsentData, READ_REFUND_TITLE, data, READ_REFUND);

        // InstructionIdentification and EndToEndIdentification are mandatory for this payment type.
        basicConsentData.put(INSTRUCTION_IDENTIFICATION_TITLE,
                Collections.singletonList(initiation.getString(CommonConstants.INSTRUCTION_IDENTIFICATION)));
        basicConsentData.put(END_TO_END_IDENTIFICATION_TITLE,
                Collections.singletonList(initiation.getString(CommonConstants.END_TO_END_IDENTIFICATION)));

        putAmountIfPresent(basicConsentData, INSTRUCTED_AMOUNT_TITLE, initiation,
                CommonConstants.INSTRUCTED_AMOUNT);
        return basicConsentData;
    }

    /**
     * Populates display data for a domestic or international scheduled payment.
     *
     * @param data the authorization_details entry
     */
    private static Map<String, List<String>> populateScheduledPaymentData(JSONObject data) {

        Map<String, List<String>> basicConsentData = new HashMap<>();
        if (data == null || !data.has(CommonConstants.INITIATION)) {
            return basicConsentData;
        }
        JSONObject initiation = data.getJSONObject(CommonConstants.INITIATION);

        putPaymentTypeAndCurrency(basicConsentData, initiation, DOMESTIC_SCHEDULED_PAYMENTS,
                INTERNATIONAL_SCHEDULED_PAYMENTS);

        // Permission is mandatory for this payment type.
        basicConsentData.put(PERMISSION_TITLE,
                Collections.singletonList(data.getString(PERMISSION)));
        putIfPresent(basicConsentData, READ_REFUND_TITLE, data, READ_REFUND);

        // InstructionIdentification is mandatory; EndToEndIdentification is optional for this payment type.
        basicConsentData.put(INSTRUCTION_IDENTIFICATION_TITLE,
                Collections.singletonList(initiation.getString(CommonConstants.INSTRUCTION_IDENTIFICATION)));
        putIfPresent(basicConsentData, END_TO_END_IDENTIFICATION_TITLE, initiation,
                CommonConstants.END_TO_END_IDENTIFICATION);

        basicConsentData.put(REQUESTED_EXECUTION_DATE_TIME_TITLE,
                Collections.singletonList(initiation.getString(CommonConstants.REQUEST_EXECUTION_DATE)));
        putAmountIfPresent(basicConsentData, INSTRUCTED_AMOUNT_TITLE, initiation,
                CommonConstants.INSTRUCTED_AMOUNT);

        return basicConsentData;
    }

    /**
     * Populates display data for a domestic or international standing order.
     *
     * @param data the authorization_details entry
     */
    private static Map<String, List<String>> populateStandingOrderPaymentData(JSONObject data) {

        Map<String, List<String>> basicConsentData = new HashMap<>();
        if (data == null || !data.has(CommonConstants.INITIATION)) {
            return basicConsentData;
        }
        JSONObject initiation = data.getJSONObject(CommonConstants.INITIATION);

        boolean isInternational = putPaymentTypeAndCurrency(basicConsentData, initiation,
                DOMESTIC_STANDING_ORDER_PAYMENTS,
                INTERNATIONAL_STANDING_ORDER_PAYMENTS);

        if (isInternational) {
            putAmountIfPresent(basicConsentData, INSTRUCTED_AMOUNT_TITLE, initiation,
                    CommonConstants.INSTRUCTED_AMOUNT);
        } else {
            putAmountIfPresent(basicConsentData, FIRST_PAYMENT_AMOUNT_TITLE, initiation,
                    CommonConstants.FIRST_PAYMENT_AMOUNT);
            if (initiation.has(CommonConstants.RECURRING_AMOUNT)) {
                putAmountIfPresent(basicConsentData, RECURRING_PAYMENT_AMOUNT_TITLE, initiation,
                        CommonConstants.RECURRING_AMOUNT);
            }
            if (initiation.has(CommonConstants.FINAL_PAYMENT_AMOUNT)) {
                putAmountIfPresent(basicConsentData, FINAL_PAYMENT_AMOUNT_TITLE, initiation,
                        CommonConstants.FINAL_PAYMENT_AMOUNT);
            }
        }

        // Permission is mandatory for this payment type.
        basicConsentData.put(PERMISSION_TITLE,
                Collections.singletonList(data.getString(PERMISSION)));
        putIfPresent(basicConsentData, READ_REFUND_TITLE, data, READ_REFUND);

        return basicConsentData;
    }

    /**
     * Locates the DebtorAccount object within an authorization_details entry.
     */
    private static JSONObject findDebtorAccount(JSONObject authorizationDetails) {

        if (authorizationDetails.has(CommonConstants.DEBTOR_ACC)) {
            return authorizationDetails.getJSONObject(CommonConstants.DEBTOR_ACC);
        }
        if (authorizationDetails.has(CommonConstants.INITIATION)) {
            JSONObject initiation = authorizationDetails.getJSONObject(CommonConstants.INITIATION);
            if (initiation.has(CommonConstants.DEBTOR_ACC)) {
                return initiation.getJSONObject(CommonConstants.DEBTOR_ACC);
            }
        }
        return null;
    }

    /**
     * @return true if the authorization_details entry names a debtor account, false otherwise.
     */
    public static boolean isDebtorAccExists(JSONObject authorizationDetails) {
        return findDebtorAccount(authorizationDetails) != null;
    }

    /**
     * @param authorizationDetails the authorization_details entry to read the debtor account from
     * @return the debtor account's identification value, or null if there's no debtor account at all.
     */
    public static String getDebtorAccFromAuthorizationDetails(JSONObject authorizationDetails) {

        JSONObject debtorAccount = findDebtorAccount(authorizationDetails);
        return debtorAccount == null ? null : debtorAccount.optString(CommonConstants.IDENTIFICATION, null);
    }

    /**
     * Checks that the debtor account named in the request actually belongs to the end user.
     *
     * @param debtorAccount        the debtor account identification named in the request, or null if none was
     * @param payableAccountsArray the end user's payable accounts, fetched from the banking backend
     * @return true if the debtor account is acceptable, false otherwise
     */
    public static boolean validateDebtorAccount(String debtorAccount, JSONArray payableAccountsArray) {

        if (debtorAccount == null || payableAccountsArray == null) {
            return false;
        }

        boolean skipOwnershipCheck = !Boolean.parseBoolean(ConfigurableProperties.VALIDATE_DEBTOR_ACC);
        if (skipOwnershipCheck && !payableAccountsArray.isEmpty()) {
            return true;
        }

        for (int i = 0; i < payableAccountsArray.length(); i++) {
            JSONObject accountObj = payableAccountsArray.optJSONObject(i);
            if (accountObj != null &&
                    accountObj.optString(SIMPLE_ACCOUNT_ID, "").equals(debtorAccount)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks that the debtor account named in the request belongs to the end user, throwing if not.
     */
    public static void validateDebtorAccountOwnership(JSONObject authorizationDetails, JSONArray payableAccountsArray)
            throws ConsentException {

        String debtorAccFromRequest = getDebtorAccFromAuthorizationDetails(authorizationDetails);
        if (!validateDebtorAccount(debtorAccFromRequest, payableAccountsArray)) {
            throw new ConsentException(ConsentException.ErrorCode.BAD_REQUEST,
                    "Invalid debtor account in consent, consent rejected.", CommonConstants.UK_REJECTED_STATUS);
        }
    }

    /**
     * Builds the display-name list for the end user's accounts, skipping entries that aren't valid JSON objects.
     *
     * @param sharableAccountsArray the accounts array fetched from the banking backend
     * @return one entry per account, unselected by default
     */
    public static List<SuccessResponsePopulateConsentAuthorizeScreenDataConsumerDataAccountsInner> setDisplayNames(
            JSONArray sharableAccountsArray) {

        List<SuccessResponsePopulateConsentAuthorizeScreenDataConsumerDataAccountsInner> consumerDataObject =
                new ArrayList<>();
        if (sharableAccountsArray == null) {
            return consumerDataObject;
        }

        for (int i = 0; i < sharableAccountsArray.length(); i++) {
            JSONObject accountObject = sharableAccountsArray.optJSONObject(i);
            if (accountObject == null) {
                continue;
            }

            SuccessResponsePopulateConsentAuthorizeScreenDataConsumerDataAccountsInner account =
                    new SuccessResponsePopulateConsentAuthorizeScreenDataConsumerDataAccountsInner();
            account.setDisplayName(accountObject.optString(DISPLAY_NAME));
            account.setSelected(false); // Default to not selected
            consumerDataObject.add(account);
        }
        return consumerDataObject;
    }

    /**
     * Whether authDetailsType is one of the four payment RAR types.
     */
    public static boolean isPaymentType(String authDetailsType) {

        return CommonConstants.DOMESTIC_PAYMENT.equals(authDetailsType) ||
                CommonConstants.DOMESTIC_SCHEDULED_PAYMENT.equals(authDetailsType) ||
                CommonConstants.DOMESTIC_STANDING_ORDER.equals(authDetailsType) ||
                CommonConstants.INTERNATIONAL_PAYMENT.equals(authDetailsType);
    }

    /**
     * Maps an authDetailsType to the banking backend endpoint that serves its accounts, or null if this type
     * has no associated accounts endpoint.
     */
    public static String getAccountURL(String authDetailsType) {

        if (CommonConstants.ACCOUNT_INFORMATION.equals(authDetailsType)) {
            return ConfigurableProperties.SHARABLE_ENDPOINT;
        }
        if (isPaymentType(authDetailsType)) {
            return ConfigurableProperties.PAYABLE_ENDPOINT;
        }
        return null;
    }

    /**
     * Fetches the raw JSON account data for an end user from the banking backend over HTTP GET.
     *
     * @param sharableAccountsRetrieveUrl the backend endpoint to call (see {@link #getAccountURL})
     * @param parameters                  query parameters to append to the URL
     * @param headers                     extra request headers to send, if any
     * @return the raw JSON response body, or null if the backend didn't respond with 200 OK
     */
    public static String getAccountsFromEndpoint(String sharableAccountsRetrieveUrl, Map<String, String> parameters,
                                                 Map<String, String> headers) throws IOException, URISyntaxException {

        String retrieveUrl = sharableAccountsRetrieveUrl.endsWith("/")
                ? sharableAccountsRetrieveUrl.substring(0, sharableAccountsRetrieveUrl.length() - 1)
                : sharableAccountsRetrieveUrl;
        if (!parameters.isEmpty()) {
            retrieveUrl = buildRequestURL(retrieveUrl, parameters);
        }

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            URIBuilder uriBuilder = new URIBuilder(retrieveUrl);
            HttpGet request = new HttpGet(uriBuilder.build().toString());
            request.addHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());

            if (headers != null) {
                headers.forEach((key, value) -> {
                    if (key != null && value != null) {
                        request.addHeader(key, value);
                    }
                });
            }

            try (CloseableHttpResponse response = client.execute(request)) {
                if (response.getStatusLine().getStatusCode() != HttpURLConnection.HTTP_OK) {
                    return null;
                }

                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8))) {
                    return reader.lines().collect(Collectors.joining());
                }
            }
        }
    }

    /**
     * Fetches the end user's accounts from the given endpoint, wrapping I/O failures and an empty response as
     * a {@link ConsentException}.
     *
     * @param accountsURL the backend endpoint to call (see {@link #getAccountURL})
     * @param userId      the end user's ID, sent as a query parameter
     * @return the {@code Data.Account} array from the backend response
     */
    public static JSONArray fetchAccounts(String accountsURL, String userId) throws ConsentException {

        Map<String, String> parameters = new HashMap<>();
        parameters.put(USER_ID_KEY_NAME, userId);

        String accountData;
        try {
            accountData = getAccountsFromEndpoint(accountsURL, parameters, new HashMap<>());
        } catch (IOException | URISyntaxException e) {
            throw new ConsentException(ConsentException.ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        if (accountData == null || accountData.isEmpty()) {
            throw new ConsentException(ConsentException.ErrorCode.INTERNAL_SERVER_ERROR,
                    "Exception occurred while getting accounts data");
        }
        return new JSONObject(accountData).getJSONArray(DATA_SIMPLE);
    }

    /**
     * Build the complete URL with query parameters sent in the map.
     *
     * @param baseURL    the base URL
     * @param parameters map of parameters
     * @return the output URL
     */
    private static String buildRequestURL(String baseURL, Map<String, String> parameters) {
        List<NameValuePair> pairs = new ArrayList<>();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null) {
                pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        String query = URLEncodedUtils.format(pairs, StandardCharsets.UTF_8);
        return baseURL.contains("?") ? baseURL + "&" + query : baseURL + "?" + query;
    }
}
