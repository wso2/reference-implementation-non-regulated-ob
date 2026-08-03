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
 * Constants shared across more than one class in the module.
 */
public class CommonConstants {

    // RAR "type" discriminators and consent/authorization status vocabulary shared across the module.

    public static final String UK_REJECTED_STATUS = "Rejected";

    public static final String UK_AUTHORIZED_STATUS = "Authorised";

    public static final String ERROR = "error";

    public static final String AUTHORIZATION_DETAILS_TYPE = "type";

    public static final String INVALID_REQUEST_MSG = "invalid_request";

    public static final Integer BAD_REQUEST = 400;

    public static final String ACCOUNT_INFORMATION = "account_information";

    public static final String DOMESTIC_PAYMENT = "domestic_payment";

    public static final String DOMESTIC_SCHEDULED_PAYMENT = "domestic_scheduled_payment";

    public static final String DOMESTIC_STANDING_ORDER = "domestic_standing_order";

    public static final String INTERNATIONAL_PAYMENT = "international_payment";

    // Shared OB/RAR JSON field names — read by both the authorize-screen builder
    // (ConsentAuthorizationUtil) and the payload validators.

    public static final String RESPONSE_STATUS_KEY = "status";

    public static final String PERMISSIONS = "Permissions";

    public static final String EXPIRATION_DATE = "ExpirationDateTime";

    public static final String TRANSACTION_FROM_DATE = "TransactionFromDateTime";

    public static final String TRANSACTION_TO_DATE = "TransactionToDateTime";

    public static final String DEBTOR_ACC = "DebtorAccount";

    public static final String CREDITOR_ACC = "CreditorAccount";

    public static final String SCHEME_NAME = "SchemeName";

    public static final String IDENTIFICATION = "Identification";

    public static final String NAME = "Name";

    public static final String SECONDARY_IDENTIFICATION = "SecondaryIdentification";

    public static final String IDENTIFICATION_TITLE = "Identification";

    public static final String NAME_TITLE = "Name";

    public static final String READ_ACCOUNTS_BASIC = "ReadAccountsBasic";

    public static final String READ_ACCOUNTS_DETAIL = "ReadAccountsDetail";

    public static final String READ_TRANSACTIONS_BASIC = "ReadTransactionsBasic";

    public static final String READ_TRANSACTIONS_DETAIL = "ReadTransactionsDetail";

    public static final String READ_TRANSACTIONS_CREDITS = "ReadTransactionsCredits";

    public static final String READ_TRANSACTIONS_DEBITS = "ReadTransactionsDebits";

    public static final String INITIATION = "Initiation";

    public static final String REFERENCE = "Reference";

    public static final String FIRST_PAYMENT_DATE = "FirstPaymentDateTime";

    public static final String FIRST_PAYMENT_AMOUNT = "FirstPaymentAmount";

    public static final String AMOUNT = "Amount";

    public static final String CURRENCY = "Currency";

    public static final String RECURRING_PAYMENT_DATE = "RecurringPaymentDateTime";

    public static final String RECURRING_AMOUNT = "RecurringPaymentAmount";

    public static final String FINAL_PAYMENT_DATE = "FinalPaymentDateTime";

    public static final String FINAL_PAYMENT_AMOUNT = "FinalPaymentAmount";

    public static final String REQUEST_EXECUTION_DATE = "RequestedExecutionDateTime";

    public static final String POSTAL_ADDRESS = "PostalAddress";

    public static final String INSTRUCTED_AMOUNT = "InstructedAmount";

    public static final String INSTRUCTION_IDENTIFICATION = "InstructionIdentification";

    public static final String END_TO_END_IDENTIFICATION = "EndToEndIdentification";

    public static final String CURRENCY_OF_TRANSFER = "CurrencyOfTransfer";

    public static final String MANDATE_RELATED_INFORMATION = "MandateRelatedInformation";

    public static final String ELECTED_RESOURCE = "electedResource";

    public static final String RESOURCE_PARAMS = "resourceParams";

    public static final String RECEIPT = "receipt";

    /**
     * UKApiVersion enumeration.
     */
    public enum UKApiVersion {
        UK_API_V310, UK_API_V400
    }
}
