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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.json.JSONObject;
import org.wso2.non.regulated.ob.extensions.exceptions.ConsentException;
import org.wso2.non.regulated.ob.extensions.model.ErrorResponse;
import org.wso2.non.regulated.ob.extensions.model.FailedResponse;
import org.wso2.non.regulated.ob.extensions.model.SuccessResponse;

import javax.ws.rs.core.Response;

/**
 * Class for common utility methods.
 */
public class CommonUtil {

    private static final String ERROR_MESSAGE_FIELD = "errorMessage";
    private static final String ERROR_DESCRIPTION_FIELD = "errorDescription";

    /**
     * Builds the standard failed-response body: an error code plus a data payload.
     */
    private static JSONObject buildFailedResponse(Integer errorCode, JSONObject data) {
        FailedResponse failedResponse = new FailedResponse();
        failedResponse.setStatus(FailedResponse.StatusEnum.ERROR);
        failedResponse.setErrorCode(errorCode);
        failedResponse.setData(data);

        return new JSONObject(failedResponse);
    }

    /**
     * Builds an error response with an errorMessage/errorDescription data payload.
     */
    public static JSONObject getErrorResponse(Integer errorCode, String errorMessage, String errorDescription) {
        return buildFailedResponse(errorCode, getErrorDataObject(errorMessage, errorDescription));
    }

    /**
     * Builds the errorMessage/errorDescription data payload embedded in an error response.
     */
    private static JSONObject getErrorDataObject(String errorMessage, String errorDescription) {

        JSONObject data = new JSONObject();
        data.put(ERROR_MESSAGE_FIELD, errorMessage);
        data.put(ERROR_DESCRIPTION_FIELD, errorDescription);

        return data;
    }

    /**
     * Builds the standard success response for a completed operation.
     */
    public static JSONObject getSuccessResponse(String requestId) {

        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setResponseId(requestId);
        successResponse.setStatus(SuccessResponse.StatusEnum.SUCCESS);

        return new JSONObject(successResponse);
    }

    /**
     * Serializes an object to a {@link JSONObject} via Jackson.
     *
     * @throws JsonProcessingException if serialization fails
     */
    public static JSONObject convertObjectToJson(Object object) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(object);
        return new JSONObject(jsonString);
    }

    /**
     * Strips CR/LF from an exception's message so it's safe to write to logs (guards against log injection
     * from attacker-controlled input reflected into error messages), returning an empty string if there is
     * no message at all.
     */
    public static String sanitize(Exception e) {
        return e.getMessage() == null ? "" : e.getMessage().replaceAll("[\r\n]", "");
    }

    /**
     * Builds the standard response for a caught {@link ConsentException}: logs the sanitized message, then
     * returns the exception's own formatted error body with HTTP 200 (the accelerator expects business-level
     * consent errors back as a 200 with an error status in the payload, not an HTTP error status).
     *
     * @param log       the caller's logger, so log output is still attributed to the right class
     * @param context   short description of what was being done, for the log message
     * @param ex        the exception that was caught
     * @param requestId the original request's ID, echoed back in the error body
     */
    public static Response buildConsentExceptionResponse(Log log, String context, ConsentException ex,
                                                          String requestId) {
        log.error(context + ": " + sanitize(ex), ex);
        return Response.status(Response.Status.OK).entity(ex.getFormattedError(requestId)).build();
    }

    /**
     * Builds the standard response for a failure processing request/response JSON: logs the sanitized
     * message, then returns an {@link ErrorResponse} body.
     *
     * @param log              the caller's logger, so log output is still attributed to the right class
     * @param context          short description of what was being done, for the log message
     * @param e                the exception that was caught
     * @param httpStatus       the HTTP status to return
     * @param errorMessageType the error-message label to use in the response body
     */
    public static Response buildErrorResponse(Log log, String context, Exception e,
                                               Response.Status httpStatus, String errorMessageType) {
        log.error(context + ": " + sanitize(e), e);
        ErrorResponse errorResponse = new ErrorResponse().status(ErrorResponse.StatusEnum.ERROR)
                .data(getErrorDataObject(errorMessageType, e.getMessage()));
        return Response.status(httpStatus).entity(new JSONObject(errorResponse).toString()).build();
    }

}
