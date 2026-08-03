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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Common Constant Class.
 */
public class CommonConstants {

        public static final String OB_CONFIG_FILE = "open-banking-uk.xml";
        public static final String OB_UK_CONFIG_QNAME = "http://wso2.org/projects/carbon/open-banking-uk.xml";
        public static final String DCR_CONFIG_TAG = "DCR";
        public static final String CONSENT_MGT_CONFIG_TAG = "ConsentManagement";
        public static final String METHOD_CONFIG_TAG = "Method";
        public static final String MAX_INSTRUCTED_AMOUNT = "ConsentManagement.PaymentRestrictions" +
                ".MaximumInstructedAmount";
        public static final String MAX_FUTURE_PAYMENT_DAYS = "ConsentManagement.PaymentRestrictions" +
                ".MaximumFuturePaymentDays";
        public static final String CUTOFF_DATE_ENABLED = "ConsentManagement.PaymentRestrictions.CutOffDateTime.Enabled";
        public static final String CUTOFF_DATE_POLICY = "ConsentManagement.PaymentRestrictions.CutOffDateTime" +
                ".CutOffDateTimePolicy";
        public static final String DAILY_CUTOFF = "ConsentManagement.PaymentRestrictions.CutOffDateTime" +
                ".DailyCutOffTime";
        public static final String EXPECTED_EXECUTION_TIME = "ConsentManagement.PaymentRestrictions.CutOffDateTime" +
                ".ExpectedExecutionTime";
        public static final String EXPECTED_SETTLEMENT_TIME = "ConsentManagement.PaymentRestrictions.CutOffDateTime" +
                ".ExpectedSettlementTime";
        public static final String CUSTOM_LOCAL_INSTRUMENT_VALUES = "ConsentManagement.CustomLocalInstrumentValues";
        public static final String REAUTH_ENABLE_ACC_UPDATE = "ConsentManagement.ConsentReAuthentication" +
                ".EnableAccountUpdateByPSU";
        public static final String VALIDATE_DEBTOR_ACC = "ValidateDebtorAccount";
        public static final String IDEMPOTENCY_ALLOWED_TIME = "ConsentManagement.Idempotency" +
                ".AllowedTimeDurationForIdempotency";
        public static final String MULTIPLE_AUTHORIZATION_EXPIRY = "ConsentManagement.MultiAuthorization" +
                ".DaysToExpireRequest";
        public static final String ENABLE_REQUEST_JTI_VALIDATION = "DCR.EnableRequestJTIValidation";
        public static final String ENABLE_SSA_JTI_VALIDATION = "DCR.EnableSSAJTIValidation";
        public static final String JTI_CACHE_ACCESS_EXPIRY = "DCR.JTICache.CacheAccessExpiry";
        public static final String JTI_CACHE_MODIFY_EXPIRY = "DCR.JTICache.CacheModifiedExpiry";
        public static final String ZONE_ID = "ZoneId";

        public static final String REVOKED_STATUS = "revoked";
        public static final String REJECTED_STATUS = "rejected";
        public static final String AUTHORIZED_STATUS = "authorised";
        public static final String RE_AUTHORIZED_STATUS = "re-authorised";
        public static final String CONSUMED_STATUS = "consumed";
        public static final String AWAITING_AUTH_STATUS = "awaitingAuthorisation";
        public static final String AWAITING_UPLOAD_STATUS = "awaitingUpload";
        public static final String UK_REVOKED_STATUS = "CANC";
        public static final String UK_REJECTED_STATUS = "RJCT";
        public static final String UK_AUTHORIZED_STATUS = "AUTH";
        public static final String UK_AWAITING_AUTH_STATUS = "AWAU";
        public static final String UK_CONSUMED_STATUS = "COND";
        public static final String UK_AWAITING_UPLOAD_STATUS = "AWUP";
        public static final String UK_REVOKED_STATUS_API_V3 = "Revoked";
        public static final String UK_REJECTED_STATUS_API_V3 = "Rejected";
        public static final String UK_AUTHORIZED_STATUS_API_V3 = "Authorised";
        public static final String UK_AWAITING_AUTH_STATUS_API_V3 = "AwaitingAuthorisation";
        public static final String UK_CONSUMED_STATUS_API_V3 = "Consumed";
        public static final String UK_AWAITING_UPLOAD_STATUS_API_V3 = "AwaitingUpload";
        /**
         * UK Open Banking API spec versions.
         */
        public enum UKApiVersion {
                UK_API_V310, UK_API_V400
        }
        public static final String SPEC_VERSION = "SPEC_VERSION";
        public static final String UK_API_V3_ATTRIBUTE = "UK300";
        public static final String UK_API_V4_ATTRIBUTE = "UK400";
        public static final String UK_API_V3_PATH = "3.1";
        public static final String UK_API_V4_PATH = "4.0";
        public static final String UK_API_ENDPOINT_HEADER = "x-wso2-endpoint-uri";
        public static final String INVOKED_API_VERSION = "InvokedAPIVersion";

        // Jws Signature Validation & Response  Signing Configuration
        public static final String JWS_SIG_VALIDATION_TAN_LIST = "JwsSignatureConfiguration.OBIE." +
                "TrustedAnchors.SignatureValidation";
        public static final String JWS_RESP_SIGNING_TAN_LIST = "JwsSignatureConfiguration.OBIE." +
                "TrustedAnchors.ResponseSigning";
        public static final String JWS_SIG_VALIDATION_MANDATED_APIS = "JwsSignatureConfiguration.SignatureValidation" +
                ".MandatedAPIs.APIContext";
        public static final String UK_MESSAGE_SIGNING_ORG_ID = "JwsSignatureConfiguration.OBIE.OrganizationId";
        public static final String UK_MESSAGE_SIGNING_REQ_APIS = "JwsSignatureConfiguration.ResponseSigning." +
                "ResponseSignatureRequiredAPIs.APIContext";
        public static final String DOMESTIC_PAYMENT = "domestic-payments";
        public static final String DOMESTIC_STANDING_ORDER_PAYMENT = "domestic-standing-orders";
        public static final String DOMESTIC_SCHEDULED_PAYMENT = "domestic-scheduled-payments";
        public static final String INTERNATIONAL_PAYMENT = "international-payments";
        public static final String INTERNATIONAL_SCHEDULED_PAYMENT = "international-scheduled-payments";
        public static final String INTERNATIONAL_STANDING_ORDER_PAYMENT = "international-standing-orders";
        public static final String FILE_PAYMENT = "file-payments";
        public static final String DOMESTIC_VRP_PAYMENT = "domestic-vrps";
        public static final Map<String, String> UK_API_V4_ERROR_CODE_MAPPING;

        static {
                Map<String, String> errorCodeMapping = new HashMap<>();
                errorCodeMapping.put(ErrorConstants.FIELD_EXPECTED, ErrorConstants.U001);
                errorCodeMapping.put(ErrorConstants.FIELD_INVALID, ErrorConstants.U002);
                errorCodeMapping.put(ErrorConstants.FIELD_INVALID_DATE, ErrorConstants.U003);
                errorCodeMapping.put(ErrorConstants.FIELD_MISSING, ErrorConstants.U004);
                errorCodeMapping.put(ErrorConstants.FIELD_UNEXPECTED, ErrorConstants.U005);
                errorCodeMapping.put(ErrorConstants.HEADER_INVALID, ErrorConstants.U006);
                errorCodeMapping.put(ErrorConstants.HEADER_MISSING, ErrorConstants.U007);
                errorCodeMapping.put(ErrorConstants.RESOURCE_CONSENT_MISMATCH, ErrorConstants.U008);
                errorCodeMapping.put(ErrorConstants.RESOURCE_INVALID_CONSENT_STATUS, ErrorConstants.U009);
                errorCodeMapping.put(ErrorConstants.RESOURCE_INVALID_FORMAT, ErrorConstants.U010);
                errorCodeMapping.put(ErrorConstants.RESOURCE_NOT_FOUND, ErrorConstants.U011);
                errorCodeMapping.put(ErrorConstants.RULES_CUTOFF, ErrorConstants.U012);
                errorCodeMapping.put(ErrorConstants.RULES_DUPLICATE_REFERENCE, ErrorConstants.U013);
                errorCodeMapping.put(ErrorConstants.RULES_CONTROL_PARAMETERS, ErrorConstants.U014);
                errorCodeMapping.put(ErrorConstants.SIGNATURE_INVALID, ErrorConstants.U015);
                errorCodeMapping.put(ErrorConstants.SIGNATURE_INVALID_CLAIM, ErrorConstants.U016);
                errorCodeMapping.put(ErrorConstants.SIGNATURE_MISSING_CLAIM, ErrorConstants.U017);
                errorCodeMapping.put(ErrorConstants.SIGNATURE_MALFORMED, ErrorConstants.U018);
                errorCodeMapping.put(ErrorConstants.SIGNATURE_MISSING, ErrorConstants.U019);
                errorCodeMapping.put(ErrorConstants.SIGNATURE_UNEXPECTED, ErrorConstants.U020);
                errorCodeMapping.put(ErrorConstants.UNSUPPORTED_ACCOUNT_IDENTIFIER, ErrorConstants.U021);
                errorCodeMapping.put(ErrorConstants.UNSUPPORTED_ACCOUNT_SECONDARY_IDENTIFIER, ErrorConstants.U022);
                errorCodeMapping.put(ErrorConstants.UNSUPPORTED_CURRENCY, ErrorConstants.U023);
                errorCodeMapping.put(ErrorConstants.UNSUPPORTED_EVENT_TYPE, ErrorConstants.U024);
                errorCodeMapping.put(ErrorConstants.UNSUPPORTED_FREQUENCY, ErrorConstants.U025);
                errorCodeMapping.put(ErrorConstants.UNSUPPORTED_LOCAL_INSTRUMENTS, ErrorConstants.U026);
                errorCodeMapping.put(ErrorConstants.UNSUPPORTED_SCHEME, ErrorConstants.U027);
                errorCodeMapping.put(ErrorConstants.REAUTHENTICATE, ErrorConstants.U028);
                errorCodeMapping.put(ErrorConstants.RULES_RESOURCE_ALREADY_EXISTS, ErrorConstants.U029);
                errorCodeMapping.put(ErrorConstants.UNEXPECTED_ERROR, ErrorConstants.U000);
                UK_API_V4_ERROR_CODE_MAPPING = Collections.unmodifiableMap(errorCodeMapping);
        }
}
