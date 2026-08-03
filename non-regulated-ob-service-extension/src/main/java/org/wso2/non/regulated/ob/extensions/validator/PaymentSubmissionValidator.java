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

package org.wso2.non.regulated.ob.extensions.validator;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.wso2.non.regulated.ob.extensions.constants.CommonConstants;
import org.wso2.non.regulated.ob.extensions.constants.ErrorConstants;
import org.wso2.non.regulated.ob.extensions.model.SuccessResponse;
import org.wso2.non.regulated.ob.extensions.utils.CommonUtil;
import org.wso2.non.regulated.ob.extensions.utils.ConsentValidatorUtil;
import org.wso2.non.regulated.ob.extensions.validator.payload.PaymentSubmissionPayloadValidator;

/**
 * This class validates the payment submission request.
 */
public class PaymentSubmissionValidator {

    // Field names used only in this class.

    private static final String UK_CONSUMED_STATUS = "Consumed";

    private static final String BODY = "body";

    private static final String DATA = "Data";

    private static final String CONSENT_ID = "ConsentId";

    private static final String RISK = "Risk";

    private static final String CONSENT_ID_KEY_NAME = "consentId";

    private static final String CONSENT_RESOURCE_ID = "id";

    // Internal JSON-path fragments used only to build the messages below.

    private static final String PATH_RISK = "Risk";

    private static final String PATH_CONSENT_ID = "Data.Initiation.Consent-id";

    private static final String PATH_STATUS = "Payload.Status";

    private static final String PATH_DATA = "Data";

    private static final String PATH_INITIATION = "Data.Initiation";

    // Validation error messages used only in this class.

    private static final String MSG_INVALID_CONSENT_ID = "The requested consent-Id does not match with the consent-Id" +
            " bound to token:" +  PATH_CONSENT_ID;

    private static final String PAYMENT_CONSENT_STATE_INVALID = "Payment validation failed due to invalid consent" +
            " state.:" + PATH_STATUS;

    private static final String DATA_NOT_FOUND = "Data is not found or empty in the request.:" +
            PATH_DATA;

    private static final String INITIATION_NOT_FOUND = "Initiation is not found or empty in the request.:" +
            PATH_INITIATION;

    private static final String RISK_MISMATCH = "Risk does not match.:" + PATH_RISK;

    private static final String RISK_NOT_FOUND = "Risk is not found or empty in the request.:" +
            PATH_RISK;

    private static final Log log = LogFactory.getLog(PaymentSubmissionValidator.class);

    /**
     * Validate Payment Retrieval Request.
     * @param submissionPayload    Payment Submission Payload
     * @param consentPayload       Payment Consent Payload
     * @param requestId            Request Id
     * @return Validation Response
     * @throws JsonProcessingException Json Processing Exception
     */
    public static JSONObject validatePaymentSubmission(Object submissionPayload, Object consentPayload,
                                                       String requestId) throws JsonProcessingException {

        JSONObject jsonSubmissionRequestBody = CommonUtil.convertObjectToJson(submissionPayload);
        JSONObject jsonConsentRequestBody = CommonUtil.convertObjectToJson(consentPayload);

        String resourcePath = jsonSubmissionRequestBody.getString(CommonConstants.ELECTED_RESOURCE);
        String initiationConsentId = jsonConsentRequestBody.getString(CONSENT_RESOURCE_ID);
        String validationConsentId = jsonSubmissionRequestBody.getString(CONSENT_ID_KEY_NAME);
        CommonConstants.UKApiVersion invokedAPIVersion = CommonConstants.UKApiVersion.UK_API_V310;

        // Check if requested consent ID matches to initiation consent ID.
        if (validationConsentId == null || !validationConsentId.equals(initiationConsentId)) {
            log.error(MSG_INVALID_CONSENT_ID);
            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.RESOURCE_CONSENT_MISMATCH, MSG_INVALID_CONSENT_ID);
        }

        //Consent Status Validation
        String consentStatus = jsonConsentRequestBody.getString(CommonConstants.RESPONSE_STATUS_KEY);
        if (!CommonConstants.UK_AUTHORIZED_STATUS.equals(consentStatus) &&
                !UK_CONSUMED_STATUS.equals(consentStatus)) {
            log.error(PAYMENT_CONSENT_STATE_INVALID);
            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.RESOURCE_INVALID_CONSENT_STATUS, PAYMENT_CONSENT_STATE_INVALID);
        }

        JSONObject submissionJson = (JSONObject) jsonSubmissionRequestBody.get(BODY);
        JSONObject receiptObj = jsonConsentRequestBody.getJSONObject(CommonConstants.RECEIPT);
        JSONObject riskJsonPayload = receiptObj.optJSONObject(RISK);
        JSONObject consentInitiation = receiptObj.getJSONObject(CommonConstants.INITIATION);

        if (!(submissionJson.has(DATA) &&
                submissionJson.get(DATA) instanceof JSONObject)) {
            log.error(DATA_NOT_FOUND);
            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.FIELD_MISSING, DATA_NOT_FOUND);
        }
        JSONObject submissionData = submissionJson.getJSONObject(DATA);

        if (!(submissionData.has(CommonConstants.INITIATION) &&
                submissionData.get(CommonConstants.INITIATION) instanceof JSONObject)) {
            log.error(INITIATION_NOT_FOUND);
            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.FIELD_MISSING, INITIATION_NOT_FOUND);
        }
        JSONObject submissionInitiation = submissionData.getJSONObject(CommonConstants.INITIATION);

        // Check if requested consent ID in the body to initiation consent ID.
        if (!submissionData.has(CONSENT_ID) ||
                submissionData.get(CONSENT_ID) == null ||
                !submissionData.get(CONSENT_ID).equals(initiationConsentId)) {
            log.error(MSG_INVALID_CONSENT_ID);
            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.RESOURCE_CONSENT_MISMATCH, MSG_INVALID_CONSENT_ID);
        }

        PaymentSubmissionPayloadValidator validator = new PaymentSubmissionPayloadValidator();
        JSONObject validationResponse = validator.validateInitiation(resourcePath, submissionInitiation,
                consentInitiation, invokedAPIVersion, requestId);
        if (validationResponse != null && !validationResponse.get(CommonConstants.RESPONSE_STATUS_KEY)
                .equals(SuccessResponse.StatusEnum.SUCCESS)) {
            return validationResponse;
        }

        JSONObject submissionRisk;
        if (submissionJson.has(RISK) &&
                submissionJson.get(RISK) instanceof JSONObject) {
            submissionRisk = (JSONObject) submissionJson.get(RISK);
        } else if (submissionJson.has(RISK) &&
                submissionJson.get(RISK) instanceof String) {
            if (riskJsonPayload == null || !riskJsonPayload.isEmpty()) {
                log.error(RISK_MISMATCH);
                return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                        ErrorConstants.FIELD_MISSING, RISK_MISMATCH);
            }
            submissionRisk = new JSONObject();
        } else {
            log.error(RISK_NOT_FOUND);
            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.FIELD_MISSING, RISK_NOT_FOUND);
        }

        JSONObject riskValidationResponse = ConsentValidatorUtil.validateRisk(submissionRisk, riskJsonPayload,
                requestId);
        if (riskValidationResponse != null && !riskValidationResponse.get(CommonConstants.RESPONSE_STATUS_KEY)
                .equals(SuccessResponse.StatusEnum.SUCCESS)) {
            return riskValidationResponse;
        }

        return CommonUtil.getSuccessResponse(requestId);
    }
}
