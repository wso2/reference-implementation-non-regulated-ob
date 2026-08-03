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
import org.wso2.non.regulated.ob.extensions.model.ValidateConsentAccessRequestBody;
import org.wso2.non.regulated.ob.extensions.utils.CommonUtil;
import org.wso2.non.regulated.ob.extensions.validator.AccountSubmissionValidator;
import org.wso2.non.regulated.ob.extensions.validator.PaymentSubmissionValidator;

import javax.ws.rs.core.Response;

/**
 * This class handles the UK specific consent access validation.
 */
public class ValidateConsentAccessApiImpl {

    // Field names used only in this class.

    private static final String SERVER_ERROR_MSG = "server_error";

    private static final Integer SERVER_ERROR = 500;

    // Validation error messages used only in this class.

    private static final String UNEXPECTED_ERROR = "UK.OBIE.UnexpectedError";

    private static final String INVALID_CONSENT_TYPE = "Invalid Consent Type found in the request";

    private static final Log log = LogFactory.getLog(ValidateConsentAccessApiImpl.class);

    /**
     * Handles the validation of consent access requests based on the provided request body.
     *
     * @param consentAccessRequestBody  the request body containing necessary parameters for validating consent access
     * @return Response indicating the result of the consent access validation
     */
    public static Response handleConsentAccessValidation(ValidateConsentAccessRequestBody consentAccessRequestBody) {

        // Read the request body
        String requestId = consentAccessRequestBody.getRequestId();
        Object consentResource = consentAccessRequestBody.getData().getConsentResource();
        Object dataPayload = consentAccessRequestBody.getData().getDataRequestPayload();
        String authDetailsType = consentAccessRequestBody.getData().getConsentResource().getType();

        try {
            switch (authDetailsType) {
                case CommonConstants.ACCOUNT_INFORMATION:
                    JSONObject accountValidationResponse = AccountSubmissionValidator
                            .validateAccountSubmission(dataPayload, consentResource, requestId);
                    return Response.status(Response.Status.OK).entity(accountValidationResponse.toString()).build();
                case CommonConstants.DOMESTIC_PAYMENT:
                case CommonConstants.DOMESTIC_SCHEDULED_PAYMENT:
                case CommonConstants.DOMESTIC_STANDING_ORDER:
                case CommonConstants.INTERNATIONAL_PAYMENT:
                    JSONObject paymentValidationResponse = PaymentSubmissionValidator
                            .validatePaymentSubmission(dataPayload, consentResource, requestId);
                    return Response.status(Response.Status.OK).entity(paymentValidationResponse.toString()).build();
                default:
                    log.error(INVALID_CONSENT_TYPE);
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil
                            .getErrorResponse(SERVER_ERROR, UNEXPECTED_ERROR,
                                    INVALID_CONSENT_TYPE).toString()).build();
            }
        } catch (JsonProcessingException e) {
            return CommonUtil.buildErrorResponse(log,
                    "Error while validating the consent access request", e, Response.Status.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_MSG);
        }
    }

}
