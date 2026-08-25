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
import org.json.JSONObject;
import org.wso2.non.regulated.ob.extensions.constants.CommonConstants;
import org.wso2.non.regulated.ob.extensions.constants.ErrorConstants;
import org.wso2.non.regulated.ob.extensions.model.StoredDetailedConsentResourceData;
import org.wso2.non.regulated.ob.extensions.model.ValidateConsentAccessData;
import org.wso2.non.regulated.ob.extensions.model.ValidateConsentAccessRequestBody;
import org.wso2.non.regulated.ob.extensions.utils.AccountConsentValidatorUtil;
import org.wso2.non.regulated.ob.extensions.utils.CommonUtil;
import org.wso2.non.regulated.ob.extensions.utils.PaymentConsentValidatorUtil;
import org.wso2.non.regulated.ob.extensions.utils.ResponseBuilderUtil;

import javax.ws.rs.core.Response;

/**
 * Routes an incoming API call to the validator for the consent type that authorized it.
 */
public class ValidateConsentAccessApiImpl {

    private static final Log log = LogFactory.getLog(ValidateConsentAccessApiImpl.class);

    /**
     * Validates an incoming API call against the consent that authorized it.
     *
     * @param consentAccessRequestBody the accelerator's request, carrying the consent resource and the payload
     *                                 of the call being validated
     * @return the validation result, or an error response if the consent type is not one this module handles
     */
    public static Response handleConsentAccessValidation(ValidateConsentAccessRequestBody consentAccessRequestBody) {

        String requestId = consentAccessRequestBody.getRequestId();
        ValidateConsentAccessData data = consentAccessRequestBody.getData();
        StoredDetailedConsentResourceData consentResource = data.getConsentResource();
        Object dataPayload = data.getDataRequestPayload();
        String authDetailsType = consentResource.getType();

        try {
            if (CommonConstants.ACCOUNT_INFORMATION.equals(authDetailsType)) {
                return Response.status(Response.Status.OK)
                        .entity(AccountConsentValidatorUtil
                                .validateAccountSubmission(dataPayload, consentResource, requestId).toString())
                        .build();
            }
            if (CommonUtil.isPaymentType(authDetailsType)) {
                return Response.status(Response.Status.OK)
                        .entity(PaymentConsentValidatorUtil
                                .validatePaymentSubmission(dataPayload, consentResource, requestId).toString())
                        .build();
            }

            log.error("Invalid Consent Type found in the request");
            JSONObject error = ResponseBuilderUtil.getErrorResponse(ErrorConstants.SERVER_ERROR,
                    ErrorConstants.UNEXPECTED_ERROR, "Invalid Consent Type found in the request");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error.toString()).build();
        } catch (JsonProcessingException e) {
            return ResponseBuilderUtil.buildErrorResponse(log,
                    "Error while validating the consent access request", e, Response.Status.INTERNAL_SERVER_ERROR,
                    "server_error");
        }
    }

}
