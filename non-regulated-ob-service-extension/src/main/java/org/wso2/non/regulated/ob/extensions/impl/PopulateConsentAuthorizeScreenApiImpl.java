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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wso2.non.regulated.ob.extensions.constants.CommonConstants;
import org.wso2.non.regulated.ob.extensions.exceptions.ConsentException;
import org.wso2.non.regulated.ob.extensions.model.PopulateConsentAuthorizeScreenRequestBody;
import org.wso2.non.regulated.ob.extensions.model.SuccessResponsePopulateConsentAuthorizeScreen;
import org.wso2.non.regulated.ob.extensions.model.SuccessResponsePopulateConsentAuthorizeScreenData;
import org.wso2.non.regulated.ob.extensions.model.SuccessResponsePopulateConsentAuthorizeScreenDataConsentData;
import org.wso2.non.regulated.ob.extensions.model.SuccessResponsePopulateConsentAuthorizeScreenDataConsumerData;
import org.wso2.non.regulated.ob.extensions.utils.CommonUtil;
import org.wso2.non.regulated.ob.extensions.utils.ConsentAuthorizationUtil;

import javax.ws.rs.core.Response;

/**
 * Implementation class for handling the population of consent authorize screen data.
 */
public class PopulateConsentAuthorizeScreenApiImpl {

    private static final Log log = LogFactory.getLog(PopulateConsentAuthorizeScreenApiImpl.class);

    /**
     * Handles the population of consent authorize screen data.
     *
     * @param requestBody  the request body containing necessary parameters for consent screen population
     * @return Response containing the consent and account data for the authorize screen
     */
    public static Response handlePopulateConsentAuthorizeScreen(PopulateConsentAuthorizeScreenRequestBody requestBody) {

        try {

            JSONObject authorizationDetails = ConsentAuthorizationUtil.getAuthorizationDetails(requestBody);
            String authDetailsType = authorizationDetails.getString(CommonConstants.AUTHORIZATION_DETAILS_TYPE);

            // Business rules the RAR JSON schema can't express.
            ConsentAuthorizationUtil.validateAuthorizationDetails(authorizationDetails, authDetailsType);

            SuccessResponsePopulateConsentAuthorizeScreenDataConsentData
                    consentData = getConsentData(authorizationDetails, authDetailsType);

            SuccessResponsePopulateConsentAuthorizeScreenDataConsumerData
                    consumerData = getConsumerData(requestBody, authorizationDetails, authDetailsType);

            SuccessResponsePopulateConsentAuthorizeScreenData
                    data = new SuccessResponsePopulateConsentAuthorizeScreenData();
            data.setConsentData(consentData);
            data.setConsumerData(consumerData);

            SuccessResponsePopulateConsentAuthorizeScreen
                    response = new SuccessResponsePopulateConsentAuthorizeScreen();
            response.setResponseId(requestBody.getRequestId());
            response.setStatus(SuccessResponsePopulateConsentAuthorizeScreen.StatusEnum.SUCCESS);
            response.setData(data);

            return Response.status(Response.Status.OK).entity(new JSONObject(response).toString()).build();

        } catch (ConsentException ex) {
            return CommonUtil.buildConsentExceptionResponse(log,
                    "Error while retrieving consent and account data for authorize screen", ex,
                    requestBody.getRequestId());
        } catch (JSONException e) {
            return CommonUtil.buildErrorResponse(log,
                    "Error while processing JSON for consent authorize screen", e, Response.Status.BAD_REQUEST,
                    CommonConstants.INVALID_REQUEST_MSG);
        }
    }

    /**
     * Builds the consent data shown on the authorize screen for the given authorization_details entry.
     *
     * @param authorizationDetails the authorization_details entry (already validated)
     * @param authDetailsType      the RAR "type" discriminator
     */
    private static SuccessResponsePopulateConsentAuthorizeScreenDataConsentData getConsentData(
            JSONObject authorizationDetails, String authDetailsType) {

        switch (authDetailsType) {
            case CommonConstants.ACCOUNT_INFORMATION:
                return ConsentAuthorizationUtil.getAccountsConsentData(authorizationDetails);
            case CommonConstants.DOMESTIC_PAYMENT:
            case CommonConstants.DOMESTIC_SCHEDULED_PAYMENT:
            case CommonConstants.DOMESTIC_STANDING_ORDER:
            case CommonConstants.INTERNATIONAL_PAYMENT:
                return ConsentAuthorizationUtil.getPaymentsConsentData(authorizationDetails);
            default:
                return new SuccessResponsePopulateConsentAuthorizeScreenDataConsentData();
        }
    }

    /**
     * Fetches the end user's accounts from the banking backend and, for payment types, checks that any debtor
     * account named in the request belongs to the end user.
     *
     * @param requestBody          the original request, needed for the user ID
     * @param authorizationDetails the authorization_details entry (already validated)
     * @param authDetailsType      the RAR "type" discriminator
     */
    private static SuccessResponsePopulateConsentAuthorizeScreenDataConsumerData getConsumerData(
            PopulateConsentAuthorizeScreenRequestBody requestBody, JSONObject authorizationDetails,
            String authDetailsType) throws ConsentException {

        SuccessResponsePopulateConsentAuthorizeScreenDataConsumerData consumerData =
                new SuccessResponsePopulateConsentAuthorizeScreenDataConsumerData();

        String accountsURL = ConsentAuthorizationUtil.getAccountURL(authDetailsType);
        if (accountsURL == null) {
            return consumerData;
        }

        JSONArray accountsJSON = ConsentAuthorizationUtil.fetchAccounts(accountsURL, requestBody.getData().getUserId());

        if (ConsentAuthorizationUtil.isPaymentType(authDetailsType) &&
                ConsentAuthorizationUtil.isDebtorAccExists(authorizationDetails)) {
            ConsentAuthorizationUtil.validateDebtorAccountOwnership(authorizationDetails, accountsJSON);
        } else {
            consumerData.setAccounts(ConsentAuthorizationUtil.setDisplayNames(accountsJSON));
        }
        return consumerData;
    }

}
