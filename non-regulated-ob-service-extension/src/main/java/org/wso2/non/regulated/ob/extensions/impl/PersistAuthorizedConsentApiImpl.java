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

package org.wso2.non.regulated.ob.extensions.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.wso2.non.regulated.ob.extensions.configurations.ConfigurableProperties;
import org.wso2.non.regulated.ob.extensions.constants.CommonConstants;
import org.wso2.non.regulated.ob.extensions.exceptions.ConsentException;
import org.wso2.non.regulated.ob.extensions.model.Account;
import org.wso2.non.regulated.ob.extensions.model.Authorization;
import org.wso2.non.regulated.ob.extensions.model.AuthorizedResourcesAuthorizedDataInner;
import org.wso2.non.regulated.ob.extensions.model.DetailedConsentResourceDataWithAmendments;
import org.wso2.non.regulated.ob.extensions.model.PersistAuthorizedConsent;
import org.wso2.non.regulated.ob.extensions.model.PersistAuthorizedConsentRequestBody;
import org.wso2.non.regulated.ob.extensions.model.Resource;
import org.wso2.non.regulated.ob.extensions.model.SuccessResponsePersistAuthorizedConsent;
import org.wso2.non.regulated.ob.extensions.model.SuccessResponsePersistAuthorizedConsentData;
import org.wso2.non.regulated.ob.extensions.model.UserGrantedData;
import org.wso2.non.regulated.ob.extensions.utils.CommonUtil;
import org.wso2.non.regulated.ob.extensions.utils.ConsentAuthorizationUtil;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.Response;

/**
 * Implementation class for persisting a newly-authorized consent.
 */
public class PersistAuthorizedConsentApiImpl {

    // Field names used only in this class.

    private static final String PRIMARY = "primary";

    private static final String ACTIVE_MAPPING_STATUS = "active";

    private static final String NA = "n/a";

    // Validation error messages used only in this class.

    private static final String CONSENT_ID_NOT_FOUND = "Consent ID not available in consent data";

    private static final Log log = LogFactory.getLog(PersistAuthorizedConsentApiImpl.class);

    /**
     * The accelerator's discriminator for a freshly-created authorization resource, as opposed to a later
     * amendment or cancellation event. There is only ever one authorization per consent in this
     * implementation, so this is always the value used.
     */
    private static final String INITIAL_AUTHORIZATION_TYPE = "authorisation";

    /**
     * Handles the persistence of authorized consent data.
     *
     * @param persistAuthorizedConsentRequestBody the request body containing the consent data to be persisted
     * @return Response containing the status of the persistence operation
     */
    public static Response handlePersistAuthorizedConsent(
            PersistAuthorizedConsentRequestBody persistAuthorizedConsentRequestBody) {
        try {
            SuccessResponsePersistAuthorizedConsentData data = persistConsent(persistAuthorizedConsentRequestBody);

            SuccessResponsePersistAuthorizedConsent response = new SuccessResponsePersistAuthorizedConsent();
            response.setResponseId(persistAuthorizedConsentRequestBody.getRequestId());
            response.setStatus(SuccessResponsePersistAuthorizedConsent.StatusEnum.SUCCESS);
            response.setData(data);

            return Response.status(Response.Status.OK)
                    .entity(CommonUtil.convertObjectToJson(response).toString()).build();
        } catch (ConsentException ex) {
            return CommonUtil.buildConsentExceptionResponse(log,
                    "Error while persisting authorized consent", ex,
                    persistAuthorizedConsentRequestBody.getRequestId());
        } catch (JsonProcessingException | ClassCastException | JSONException e) {
            return CommonUtil.buildErrorResponse(log,
                    "Error while processing JSON for persisting authorized consent", e, Response.Status.BAD_REQUEST,
                    CommonConstants.INVALID_REQUEST_MSG);
        }
    }

    /**
     * Builds the consent record for the first (and, in this model, only) authorization of a consent: type
     * and receipt are derived from the {@code authorization_details} carried over from the populate-screen
     * call, and a single {@code Authorization} is built from the accounts the PSU selected.
     *
     * @param requestBody the request body containing the approval decision and the authorization_details
     * @return the consent record (type, receipt, status, single authorization) to persist
     */
    private static SuccessResponsePersistAuthorizedConsentData persistConsent(
            PersistAuthorizedConsentRequestBody requestBody) throws ConsentException, JsonProcessingException {

        PersistAuthorizedConsent requestData = requestBody.getData();

        if (requestData.getConsentId() == null) {
            throw new ConsentException(ConsentException.ErrorCode.BAD_REQUEST, CONSENT_ID_NOT_FOUND);
        }

        UserGrantedData userGrantedData = requestData.getUserGrantedData();
        JSONObject authorizationDetails = ConsentAuthorizationUtil.getAuthorizationDetails(userGrantedData);
        String authDetailsType = authorizationDetails.getString(CommonConstants.AUTHORIZATION_DETAILS_TYPE);

        boolean isApproved = Boolean.TRUE.equals(requestData.getIsApproved());
        String status = isApproved ? CommonConstants.UK_AUTHORIZED_STATUS : CommonConstants.UK_REJECTED_STATUS;

        DetailedConsentResourceDataWithAmendments consentResource = new DetailedConsentResourceDataWithAmendments();
        consentResource.setType(authDetailsType);
        consentResource.setStatus(status);
        consentResource.setReceipt(authorizationDetails.toMap());
        consentResource.setValidityTime(getExpirationDateTime(authorizationDetails));
        consentResource.setFrequency(0);
        consentResource.setRecurringIndicator(false);
        consentResource.setAuthorizations(Collections.singletonList(
                buildAuthorization(authDetailsType, status, userGrantedData)));

        SuccessResponsePersistAuthorizedConsentData response = new SuccessResponsePersistAuthorizedConsentData();
        response.setConsentResource(consentResource);
        return response;
    }

    /**
     * Builds the sole authorization resource from the accounts the PSU selected on the authorize screen.
     */
    private static Authorization buildAuthorization(String authDetailsType, String status,
                                                     UserGrantedData userGrantedData) {

        Authorization authorization = new Authorization();
        authorization.setUserId(userGrantedData.getUserId());
        authorization.setType(INITIAL_AUTHORIZATION_TYPE);
        authorization.setStatus(status);
        authorization.setResources(buildResources(authDetailsType, userGrantedData));
        return authorization;
    }

    /**
     * Builds one {@code Resource} per account the PSU selected, marking payment resources "primary" and
     * account-access resources "n/a" - the same resource-permission convention used elsewhere in this module.
     */
    private static List<Resource> buildResources(String authDetailsType, UserGrantedData userGrantedData) {

        boolean isPaymentType = CommonConstants.DOMESTIC_PAYMENT.equals(authDetailsType) ||
                CommonConstants.DOMESTIC_SCHEDULED_PAYMENT.equals(authDetailsType) ||
                CommonConstants.DOMESTIC_STANDING_ORDER.equals(authDetailsType) ||
                CommonConstants.INTERNATIONAL_PAYMENT.equals(authDetailsType);
        String permission = isPaymentType ? PRIMARY : NA;

        List<Resource> resources = new ArrayList<>();
        for (AuthorizedResourcesAuthorizedDataInner authorizedDataInner :
                userGrantedData.getAuthorizedResources().getAuthorizedData()) {
            for (Account account : authorizedDataInner.getAccounts()) {
                Resource resource = new Resource();
                resource.setAccountId(account.getDisplayName());
                resource.setPermission(permission);
                resource.setStatus(ACTIVE_MAPPING_STATUS);
                resources.add(resource);
            }
        }
        return resources;
    }

    /**
     * Calculates the expiration date/time for the consent based on the provided authorization details.
     *
     * @param authorizationDetails the JSON object containing the authorization details
     * @return the expiration date/time in epoch seconds
     */
    private static long getExpirationDateTime(JSONObject authorizationDetails) {

        long epochSeconds;

        if (authorizationDetails.has(CommonConstants.EXPIRATION_DATE)) {
            String expirationDateTime = authorizationDetails.getString(CommonConstants.EXPIRATION_DATE);

            // Convert to Instant
            Instant instant = OffsetDateTime.parse(expirationDateTime).toInstant();

            // Get epoch value In seconds
            epochSeconds = instant.getEpochSecond();
        } else {

            long days = Long.parseLong(ConfigurableProperties.ACCOUNTS_CONSENT_VALIDITY_PERIOD);

            // Get epoch seconds by adding days to epoch start (1970-01-01T00:00:00Z)
            epochSeconds = Instant.EPOCH.plus(days, ChronoUnit.DAYS).getEpochSecond();
        }

        return epochSeconds;
    }
}
