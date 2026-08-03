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

package org.wso2.non.regulated.ob.extensions.exceptions;

import org.json.JSONObject;
import org.wso2.non.regulated.ob.extensions.model.FailedResponseInConsentAuthorize;
import org.wso2.non.regulated.ob.extensions.model.FailedResponseInConsentAuthorizeData;

/**
 * Exception class to build and return authorisation errors.
 */
public class ConsentException extends Exception {

    /**
     * Enum representing the error codes for consent exceptions.
     */
    public enum ErrorCode {

        BAD_REQUEST(400),
        INTERNAL_SERVER_ERROR(500);

        private final int code;

        ErrorCode(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    private final ErrorCode errorCode;
    private String newConsentStatus;

    public ConsentException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ConsentException(ErrorCode errorCode, String message, String newConsentStatus) {
        super(message);
        this.errorCode = errorCode;
        this.newConsentStatus = newConsentStatus;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getFormattedError(String responseId) {

        FailedResponseInConsentAuthorizeData responseData = new FailedResponseInConsentAuthorizeData();
        responseData.setErrorMessage(this.getMessage());

        if (newConsentStatus != null && !newConsentStatus.isEmpty()) {
            responseData.setNewConsentStatus(newConsentStatus);
        }

        FailedResponseInConsentAuthorize failedResponse = new FailedResponseInConsentAuthorize();
        failedResponse.setResponseId(responseId);
        failedResponse.setStatus(FailedResponseInConsentAuthorize.StatusEnum.ERROR);
        failedResponse.setData(responseData);

        return new JSONObject(failedResponse).toString();
    }
}
