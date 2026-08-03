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

package org.wso2.non.regulated.ob.demo.backend.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.wso2.non.regulated.ob.demo.backend.configurations.ConfigurableProperties;

import java.io.IOException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Common Util class for UK.
 */
public class UKCommonUtil {

    public static final String API_URL_VERSION_REGEX = "open-banking\\/v(\\d+\\.\\d+)\\/";

    /**
     * Get the mapping UK status based on API version.
     *
     * @param defaultStatus  Default status returned from the accelerator
     * @return Mapping UK status
     */
    public static String getUKStatus(String defaultStatus, CommonConstants.UKApiVersion apiVersion) {

        if (CommonConstants.UKApiVersion.UK_API_V400.equals(apiVersion)) {
            switch (defaultStatus) {
                case CommonConstants.AUTHORIZED_STATUS:
                    return CommonConstants.UK_AUTHORIZED_STATUS;
                case CommonConstants.REVOKED_STATUS:
                    return CommonConstants.UK_REVOKED_STATUS;
                case CommonConstants.REJECTED_STATUS:
                    return CommonConstants.UK_REJECTED_STATUS;
                case CommonConstants.CONSUMED_STATUS:
                    return CommonConstants.UK_CONSUMED_STATUS;
                case CommonConstants.AWAITING_UPLOAD_STATUS:
                    return CommonConstants.UK_AWAITING_UPLOAD_STATUS;
                default:
                    return CommonConstants.UK_AWAITING_AUTH_STATUS;
            }
        } else {
            switch (defaultStatus) {
                case CommonConstants.AUTHORIZED_STATUS:
                    return CommonConstants.UK_AUTHORIZED_STATUS_API_V3;
                case CommonConstants.REVOKED_STATUS:
                    return CommonConstants.UK_REVOKED_STATUS_API_V3;
                case CommonConstants.REJECTED_STATUS:
                    return CommonConstants.UK_REJECTED_STATUS_API_V3;
                case CommonConstants.CONSUMED_STATUS:
                    return CommonConstants.UK_CONSUMED_STATUS_API_V3;
                case CommonConstants.AWAITING_UPLOAD_STATUS:
                    return CommonConstants.UK_AWAITING_UPLOAD_STATUS_API_V3;
                default:
                    return CommonConstants.UK_AWAITING_AUTH_STATUS_API_V3;
            }
        }
    }

    /**
     * Get the mapping UK status.
     *
     * @param defaultStatus  Default status returned from the accelerator
     * @return Mapping UK status
     * @deprecated use {@link #getUKStatus(String, CommonConstants.UKApiVersion)}  instead.
     */
    @Deprecated
    public static String getUKStatus(String defaultStatus) {

        switch (defaultStatus) {
            case CommonConstants.AUTHORIZED_STATUS:
                return CommonConstants.UK_AUTHORIZED_STATUS;
            case CommonConstants.REVOKED_STATUS:
                return CommonConstants.UK_REVOKED_STATUS;
            case CommonConstants.REJECTED_STATUS:
                return CommonConstants.UK_REJECTED_STATUS;
            case CommonConstants.CONSUMED_STATUS:
                return CommonConstants.UK_CONSUMED_STATUS;
            case CommonConstants.AWAITING_UPLOAD_STATUS:
                return CommonConstants.UK_AWAITING_UPLOAD_STATUS;
            default:
                return CommonConstants.UK_AWAITING_AUTH_STATUS;
        }
    }

    /**
     * Method to compare whether JSON payloads are equal.
     *
     * @param jsonString1    JSON payload retrieved from database
     * @param jsonString2    JSON payload received from current request
     * @return
     * @throws IOException
     */
    public static boolean isJSONPayloadSimilar(String jsonString1, String jsonString2) throws IOException {

        JsonNode expectedNode = new ObjectMapper().readTree(jsonString1);
        JsonNode actualNode = new ObjectMapper().readTree(jsonString2);
        return expectedNode.equals(actualNode);
    }

    /**
     * Method to check whether difference between two dates is less than the configured time.
     *
     * @param createdTime    Created Time of the request
     * @return
     */
    public static boolean isRequestReceivedWithinAllowedTime(String createdTime) {

        if (createdTime == null) {
            return true;
        }
        String allowedTimeDuration = ConfigurableProperties.IDEMPOTENCY_ALLOWED_TIME;
        OffsetDateTime createdDate = OffsetDateTime.parse(createdTime);
        OffsetDateTime currDate = OffsetDateTime.now(createdDate.getOffset());

        long diffInHours = Duration.between(createdDate, currDate).toHours();
        return diffInHours <= Long.parseLong(allowedTimeDuration);
    }

    /**
     * Returns UK API version form the consent attributes.
     * @return UK API version
     */
    public static CommonConstants.UKApiVersion getConsentAPIVersionFromAttributes(
            Map<String, String> consentAttributes) {
        CommonConstants.UKApiVersion ukApiVersion = CommonConstants.UKApiVersion.UK_API_V310;
        if (consentAttributes != null && consentAttributes.containsKey(CommonConstants.SPEC_VERSION)) {
            String specVersion = consentAttributes.get(CommonConstants.SPEC_VERSION);
            if (CommonConstants.UK_API_V4_ATTRIBUTE.equalsIgnoreCase(specVersion)) {
                ukApiVersion = CommonConstants.UKApiVersion.UK_API_V400;
            }
        }
        return ukApiVersion;
    }

    /**
     * Returns UK API version form the request path.
     * @return UK API version
     */
    public static CommonConstants.UKApiVersion getConsentAPIVersionFromRequestPath(String requestPath) {
        CommonConstants.UKApiVersion ukApiVersion = CommonConstants.UKApiVersion.UK_API_V310;
        if (requestPath != null) {
            Pattern pattern = Pattern.compile(API_URL_VERSION_REGEX);
            Matcher matcher = pattern.matcher(requestPath);
            if (matcher.find() && CommonConstants.UK_API_V4_PATH.equalsIgnoreCase(matcher.group(1))) {
                ukApiVersion = CommonConstants.UKApiVersion.UK_API_V400;
            }
        }
        return ukApiVersion;
    }
}
