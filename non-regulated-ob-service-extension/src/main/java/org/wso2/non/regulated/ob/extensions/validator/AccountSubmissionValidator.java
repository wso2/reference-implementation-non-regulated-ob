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
import org.json.JSONArray;
import org.json.JSONObject;
import org.wso2.non.regulated.ob.extensions.configurations.ConfigurableProperties;
import org.wso2.non.regulated.ob.extensions.constants.CommonConstants;
import org.wso2.non.regulated.ob.extensions.constants.ErrorConstants;
import org.wso2.non.regulated.ob.extensions.utils.CommonUtil;
import org.wso2.non.regulated.ob.extensions.utils.ConsentValidatorUtil;

import java.util.Map;

/**
 * UK Account Submission Validator.
 */
public class AccountSubmissionValidator {

    // Field names used only in this class.

    private static final String UPDATED_TIME = "updatedTime";

    private static final String RESOURCE_PATH = "ResourcePath";

    // Internal JSON-path fragments used only to build the messages below.

    private static final String PATH_PERMISSIONS = "Data.Permissions";

    private static final String PATH_EXPIRATION_DATE = "Data.ExpirationDate";

    private static final String PATH_URL = "Data.Url";

    private static final String PATH_STATUS = "Payload.Status";

    private static final String PATH_PAYLOAD_ACCOUNT_ID = "Payload.AccountId";

    // Validation error messages used only in this class.

    private static final String RESOURCE_INVALID_FORMAT = "UK.OBIE.Resource.InvalidFormat";

    private static final String PAYLOAD_FORMAT_ERROR = "Request Payload is not in correct JSON format";

    private static final String INVALID_URI_ERROR = "Path requested is invalid. :" + PATH_URL;

    private static final String PERMISSION_MISMATCH_ERROR = "Permission mismatch. Consent does not contain necessary " +
            "permissions. :" + PATH_PERMISSIONS;

    private static final String CONSENT_EXPIRED_ERROR = "Provided consent is expired. :"
            + PATH_EXPIRATION_DATE;

    private static final String OLDER_REFRESH_TOKEN = "The refresh token is older than the configured limit value";

    private static final String ACCOUNT_CONSENT_STATE_INVALID = "Account validation failed due to invalid consent" +
            " state. :" + PATH_STATUS;

    private static final String ACCOUNT_ID_NOT_AVAILABLE_MSG = "Requested Resource with the given ID is Unavailable. :"
            + PATH_PAYLOAD_ACCOUNT_ID;

    private static final Log log = LogFactory.getLog(AccountSubmissionValidator.class);

    /**
     * Validate Account Retrieval Request.
     *
     * @param dataPayload      Account submission payload
     * @param consentPayload   Account Consent Payload
     * @param requestId        Request Id
     * @return Validation Response
     * @throws JsonProcessingException Json Processing Exception
     */
    public static JSONObject validateAccountSubmission(Object dataPayload, Object consentPayload, String requestId)
            throws JsonProcessingException {

        JSONObject jsonDataRequestBody = CommonUtil.convertObjectToJson(dataPayload);
        JSONObject jsonConsentRequestBody = CommonUtil.convertObjectToJson(consentPayload);

        String resourcePath = jsonDataRequestBody.getJSONObject(CommonConstants.RESOURCE_PARAMS)
                .getString(RESOURCE_PATH);
        String electedResource = jsonDataRequestBody.getString(CommonConstants.ELECTED_RESOURCE);

        // Perform URI Validation.
        if (resourcePath == null || !ConsentValidatorUtil.isAccountURIValid(electedResource)) {

            log.error(PAYLOAD_FORMAT_ERROR);
            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    RESOURCE_INVALID_FORMAT, INVALID_URI_ERROR);
        }

        //Retrieve Consent Payload
        JSONObject receiptJson = jsonConsentRequestBody.getJSONObject(CommonConstants.RECEIPT);

        //Check whether required permissions provided.
        JSONArray permissions = (JSONArray) receiptJson.get(CommonConstants.PERMISSIONS);

        if (!ConsentValidatorUtil.validateAccountPermissions(electedResource, permissions)) {

            log.error(PERMISSION_MISMATCH_ERROR);
            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.FIELD_INVALID, PERMISSION_MISMATCH_ERROR);
        }

        //Check whether the consent is expired.
        if (ConsentValidatorUtil.isConsentExpired((String) receiptJson.get(CommonConstants.EXPIRATION_DATE))) {

            log.error(CONSENT_EXPIRED_ERROR);
            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.FIELD_INVALID, CONSENT_EXPIRED_ERROR);
        }

        //Consent Status Validation
        String consentStatus = jsonConsentRequestBody.getString(CommonConstants.RESPONSE_STATUS_KEY);

        if (!CommonConstants.UK_AUTHORIZED_STATUS.equals(consentStatus)) {

            log.error(ACCOUNT_CONSENT_STATE_INVALID);
            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.RESOURCE_INVALID_CONSENT_STATUS, ACCOUNT_CONSENT_STATE_INVALID);
        }

        //Account ID Validation
        String isAccountIdValidationEnabled = ConfigurableProperties.VALIDATE_ACCOUNT_ID;

        if (Boolean.parseBoolean(isAccountIdValidationEnabled) &&
                !ConsentValidatorUtil.isAccountIdValid(jsonConsentRequestBody, resourcePath)) {

            log.error(ACCOUNT_ID_NOT_AVAILABLE_MSG);
            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.FIELD_INVALID, ACCOUNT_ID_NOT_AVAILABLE_MSG);
        }

        // Perform Query Param Validation.
        Map<String, String> resourceParams = ConsentValidatorUtil.extractQueryParams(jsonDataRequestBody);

        JSONObject queryParamValidity = ConsentValidatorUtil.checkTransactionTimePeriodValidity(resourcePath,
                receiptJson, resourceParams, requestId);

        if (queryParamValidity != null && queryParamValidity.toString().contains(CommonConstants.ERROR)) {
            return queryParamValidity;
        }

        return CommonUtil.getSuccessResponse(requestId);
    }
}
