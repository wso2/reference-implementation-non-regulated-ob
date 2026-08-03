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

package org.wso2.non.regulated.ob.extensions.constants;

/**
 * Error-message constants shared across more than one validator class. Constants used by only a single
 * class live either as private fields on that class, or (for the two large validator-specific clusters) in
 * {@link ConsentValidatorConstants} / {@link PaymentSubmissionConstants}.
 */
public class ErrorConstants {

    // Internal JSON-path fragments used only to build the messages below.

    private static final String PATH_QUERY_PARAM = "Url.QueryParameters";

    private static final String PATH_MANDATE_RELATED_INFORMATION = "Data.Initiation.MandateRelatedInformation";

    private static final String PATH_FINAL_PAYMENT_DATE_TIME_API_V4 =
            "Data.Initiation.MandateRelatedInformation.FinalPaymentDateTime";

    private static final String PATH_RECURRING_PAYMENT_DATE_TIME_API_V4 =
            "Data.Initiation.MandateRelatedInformation.RecurringPaymentDateTime";

    // Error messages shared across more than one validator class.

    public static final String FIELD_INVALID = "UK.OBIE.Field.Invalid";

    public static final String FIELD_MISSING = "UK.OBIE.Field.Missing";

    public static final String RESOURCE_CONSENT_MISMATCH = "UK.OBIE.Resource.ConsentMismatch";

    public static final String RESOURCE_INVALID_CONSENT_STATUS = "UK.OBIE.Resource.InvalidConsentStatus";

    public static final String INVALID_QUERY_PARAMS = "Transaction time validation failed. Invalid query parameters " +
            "found in the request. :" + PATH_QUERY_PARAM;

    public static final String MANDATE_RELATED_INFORMATION_MISMATCH = "Mandate Related Information does not match.:" +
            PATH_MANDATE_RELATED_INFORMATION;

    public static final String FINAL_PAYMENT_DATE_MISMATCH_API_V4 = "Final Payment Date Time does not match:" +
            PATH_FINAL_PAYMENT_DATE_TIME_API_V4;

    public static final String RECURRING_PAYMENT_DATE_MISMATCH_API_V4 = "Recurring Payment Date Time does not match:" +
            PATH_RECURRING_PAYMENT_DATE_TIME_API_V4;

}
