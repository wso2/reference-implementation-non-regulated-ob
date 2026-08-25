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

import org.apache.commons.logging.Log;
import org.json.JSONObject;
import org.wso2.non.regulated.ob.extensions.constants.ErrorConstants;
import org.wso2.non.regulated.ob.extensions.exceptions.ConsentException;
import org.wso2.non.regulated.ob.extensions.model.ErrorResponse;
import org.wso2.non.regulated.ob.extensions.model.FailedResponse;
import org.wso2.non.regulated.ob.extensions.model.SuccessResponse;

import javax.ws.rs.core.Response;

/**
 * Builds success and error response bodies.
 */
public class ResponseBuilderUtil {

    /**
     * Builds an error response.
     *
     * @param errorCode        the error code
     * @param errorMessage     the error message
     * @param errorDescription the error description
     * @return the error response
     */
    public static JSONObject getErrorResponse(Integer errorCode, String errorMessage, String errorDescription) {
        return buildFailedResponse(errorCode, getErrorDataObject(errorMessage, errorDescription));
    }

    /**
     * Builds a consent-mismatch error response.
     *
     * @param errorDescription the error description
     * @return the error response
     */
    public static JSONObject mismatchError(String errorDescription) {
        return getErrorResponse(ErrorConstants.BAD_REQUEST, ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                errorDescription);
    }

    /**
     * Builds a missing-field error response.
     *
     * @param errorDescription the error description
     * @return the error response
     */
    public static JSONObject missingFieldError(String errorDescription) {
        return getErrorResponse(ErrorConstants.BAD_REQUEST, ErrorConstants.FIELD_MISSING, errorDescription);
    }

    /**
     * Builds an invalid-field error response.
     *
     * @param errorDescription the error description
     * @return the error response
     */
    public static JSONObject invalidFieldError(String errorDescription) {
        return getErrorResponse(ErrorConstants.BAD_REQUEST, ErrorConstants.FIELD_INVALID, errorDescription);
    }

    /**
     * Builds an invalid-date error response.
     *
     * @param errorDescription the error description
     * @return the error response
     */
    public static JSONObject invalidDateError(String errorDescription) {
        return getErrorResponse(ErrorConstants.BAD_REQUEST, ErrorConstants.FIELD_INVALID_DATE, errorDescription);
    }

    /**
     * Builds a success response.
     *
     * @param requestId the request ID
     * @return the success response
     */
    public static JSONObject getSuccessResponse(String requestId) {

        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setResponseId(requestId);
        successResponse.setStatus(SuccessResponse.StatusEnum.SUCCESS);

        return new JSONObject(successResponse);
    }

    /**
     * Builds a response for a caught consent exception.
     *
     * @param log       the logger
     * @param context   the log context
     * @param ex        the exception
     * @param requestId the request ID
     * @return the response
     */
    public static Response buildConsentExceptionResponse(Log log, String context, ConsentException ex,
                                                          String requestId) {
        log.error(context + ": " + sanitize(ex), ex);
        return Response.status(Response.Status.OK).entity(ex.getFormattedError(requestId)).build();
    }

    /**
     * Builds a response for a JSON processing failure.
     *
     * @param log              the logger
     * @param context          the log context
     * @param e                the exception
     * @param httpStatus       the HTTP status
     * @param errorMessageType the error message label
     * @return the response
     */
    public static Response buildErrorResponse(Log log, String context, Exception e,
                                               Response.Status httpStatus, String errorMessageType) {
        log.error(context + ": " + sanitize(e), e);
        ErrorResponse errorResponse = new ErrorResponse().status(ErrorResponse.StatusEnum.ERROR)
                .data(getErrorDataObject(errorMessageType, e.getMessage()));
        return Response.status(httpStatus).entity(new JSONObject(errorResponse).toString()).build();
    }

    /**
     * Strips line breaks from an exception's message.
     *
     * @param e the exception
     * @return the sanitized message
     */
    private static String sanitize(Exception e) {
        return e.getMessage() == null ? "" : e.getMessage().replaceAll("[\r\n]", "");
    }

    /**
     * Builds a failed-response body.
     *
     * @param errorCode the error code
     * @param data      the data payload
     * @return the failed response
     */
    private static JSONObject buildFailedResponse(Integer errorCode, JSONObject data) {
        FailedResponse failedResponse = new FailedResponse();
        failedResponse.setStatus(FailedResponse.StatusEnum.ERROR);
        failedResponse.setErrorCode(errorCode);
        failedResponse.setData(data);

        return new JSONObject(failedResponse);
    }

    /**
     * Builds the error data payload.
     *
     * @param errorMessage     the error message
     * @param errorDescription the error description
     * @return the data object
     */
    private static JSONObject getErrorDataObject(String errorMessage, String errorDescription) {

        JSONObject data = new JSONObject();
        data.put("errorMessage", errorMessage);
        data.put("errorDescription", errorDescription);

        return data;
    }

}
