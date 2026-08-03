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

/**
 * Error Constant Class.
 */
public class ErrorConstants {

    //Error Response Structure constants
    public static final String CODE = "Code";
    public static final String ID = "Id";
    public static final String MESSAGE = "Message";
    public static final String ERRORS = "Errors";
    public static final String ERROR_CODE = "ErrorCode";
    public static final String PATH = "Path";
    public static final String URL = "Url";
    public static final String ERROR_URL = "ErrorURL";
    public static final String ERROR = "error";
    public static final String ERROR_DESCRIPTION = "error_description";

    //HTTP Error Codes
    public static final String HTTP_BAD_REQUEST = "400";
    public static final String HTTP_UNAUTHORIZED = "401";
    public static final String HTTP_FORBIDDEN = "403";
    public static final String HTTP_NOT_FOUND = "404";
    public static final String HTTP_NOT_ALLOWED = "405";
    public static final String HTTP_NOT_ACCEPTABLE = "406";
    public static final String HTTP_TOO_MANY_REQUESTS = "429";
    public static final String HTTP_SERVER_ERROR = "500";
    public static final String HTTP_CONFLICT = "409";
    public static final String HTTP_UNSUPPORTED_MEDIA_TYPE = "415";

    // High level textual error code, to help categorize the errors.
    public static final String BAD_REQUEST_CODE = "400 BadRequest";
    public static final String UNAUTHORIZED_CODE = "401 Unauthorized";
    public static final String FORBIDDEN_CODE = "403 Forbidden";
    public static final String NOT_FOUND_CODE = "404 Not Found";
    public static final String NOT_ALLOWED_CODE = "405 Method Not Allowed";
    public static final String NOT_ACCEPTABLE_CODE = "406 Not Acceptable";
    public static final String TOO_MANY_REQUESTS_CODE = "429 Too Many Requests";
    public static final String SERVER_ERROR_CODE = "500 Internal Server Error";
    public static final String CONFLICT_CODE = "409 Conflict";
    public static final String UNSUPPORTED_MEDIA_TYPE_CODE = "415 Unsupported Media Type";

    //Low level textual error code
    public static final String FIELD_EXPECTED = "UK.OBIE.Field.Expected";
    public static final String FIELD_INVALID = "UK.OBIE.Field.Invalid";
    public static final String FIELD_INVALID_DATE = "UK.OBIE.Field.InvalidDate";
    public static final String FIELD_MISSING = "UK.OBIE.Field.Missing";
    public static final String FIELD_UNEXPECTED = "UK.OBIE.Field.Unexpected";
    public static final String HEADER_INVALID = "UK.OBIE.Header.Invalid";
    public static final String HEADER_INVALID_COP = "UK.OBIE.Header";
    public static final String HEADER_MISSING = "UK.OBIE.Header.Missing";
    public static final String JWS_HEADER_MISSING_COP = "UK.OBIE.Signature";
    public static final String RESOURCE_CONSENT_MISMATCH = "UK.OBIE.Resource.ConsentMismatch";
    public static final String RESOURCE_INVALID_CONSENT_STATUS = "UK.OBIE.Resource.InvalidConsentStatus";
    public static final String RESOURCE_INVALID_FORMAT = "UK.OBIE.Resource.InvalidFormat";
    public static final String RESOURCE_NOT_FOUND = "UK.OBIE.Resource.NotFound";
    public static final String RULES_CUTOFF = "UK.OBIE.Rules.AfterCutOffDateTime";
    public static final String RULES_CONTROL_PARAMETERS = "UK.OBIE.Rules.FailsControlParameters";
    public static final String RULES_DUPLICATE_REFERENCE = "UK.OBIE.Rules.DuplicateReference";
    public static final String RULES_RESOURCE_ALREADY_EXISTS = "UK.OBIE.Rules.ResourceAlreadyExists";
    public static final String SIGNATURE_INVALID = "UK.OBIE.Signature.Invalid";
    public static final String SIGNATURE_INVALID_CLAIM = "UK.OBIE.Signature.InvalidClaim";
    public static final String SIGNATURE_MISSING_CLAIM = "UK.OBIE.Signature.MissingClaim";
    public static final String SIGNATURE_MALFORMED = "UK.OBIE.Signature.Malformed";
    public static final String SIGNATURE_MISSING = "UK.OBIE.Signature.Missing";
    public static final String SIGNATURE_UNEXPECTED = "UK.OBIE.Signature.Unexpected";
    public static final String UNSUPPORTED_ACCOUNT_IDENTIFIER = "UK.OBIE.Unsupported.AccountIdentifier";
    public static final String UNSUPPORTED_ACCOUNT_SECONDARY_IDENTIFIER =
            "UK.OBIE.Unsupported.AccountSecondaryIdentifier";
    public static final String UNSUPPORTED_CURRENCY = "UK.OBIE.Unsupported.Currency";
    public static final String UNSUPPORTED_INSTRUCTED_AMOUNT = "UK.OBIE.Unsupported.InstructedAmount";
    public static final String UNSUPPORTED_FREQUENCY = "UK.OBIE.Unsupported.Frequency";
    public static final String UNSUPPORTED_EXECUTION_DATE = "UK.OBIE.Unsupported.RequestedExecutionDate";
    public static final String UNSUPPORTED_LOCAL_INSTRUMENTS = "UK.OBIE.Unsupported.LocalInstrument";
    public static final String UNSUPPORTED_SCHEME = "UK.OBIE.Unsupported.Scheme";
    public static final String UNEXPECTED_ERROR = "UK.OBIE.UnexpectedError";
    public static final String UNSUPPORTED_PSU_AUTHENTICATION_METHOD = "UK.OBIE.Unsupported.PSUAuthenticationMethods";
    public static final String UNSUPPORTED_VRP_TYPE = "UK.OBIE.Unsupported.VRPType";
    public static final String UNSUPPORTED_EVENT_TYPE = "UK.OBIE.Unsupported.EventType";
    public static final String REAUTHENTICATE = "UK.OBIE.Reauthenticate";

    // Low level textual error code for UK API v4
    public static final String U001 = "U001";
    public static final String U002 = "U002";
    public static final String U003 = "U003";
    public static final String U004 = "U004";
    public static final String U005 = "U005";
    public static final String U006 = "U006";
    public static final String U007 = "U007";
    public static final String U008 = "U008";
    public static final String U009 = "U009";
    public static final String U010 = "U010";
    public static final String U011 = "U011";
    public static final String U012 = "U012";
    public static final String U013 = "U013";
    public static final String U014 = "U014";
    public static final String U015 = "U015";
    public static final String U016 = "U016";
    public static final String U017 = "U017";
    public static final String U018 = "U018";
    public static final String U019 = "U019";
    public static final String U020 = "U020";
    public static final String U021 = "U021";
    public static final String U022 = "U022";
    public static final String U023 = "U023";
    public static final String U024 = "U024";
    public static final String U025 = "U025";
    public static final String U026 = "U026";
    public static final String U027 = "U027";
    public static final String U028 = "U028";
    public static final String U029 = "U029";
    public static final String U000 = "U000";

    //Path of the fields in request
    public static final String PATH_FINANCIAL_ID = "Header.X-fapi-financial-id";
    public static final String PATH_X_FAPI_CUSTOMER_LAST_LOGGED_TIME = "Header.X-fapi-customer-last-logged-time";
    public static final String PATH_CLIENT_ID = "Header.Client-id";
    public static final String PATH_X_WSO2_CLIENT_ID = "Header.x-wso2-client-id";
    public static final String PATH_CONSENT_ID = "Data.Initiation.Consent-id";
    public static final String PATH_REQUEST_ID = "Header.Consent-id";
    public static final String PATH_USER_ID = "Header.User-id";
    public static final String PATH_ACCOUNT_ID = "Header.Account-ids";
    public static final String PATH_IDEM_KEY = "Header.x-Idempotency-Key";
    public static final String PATH_REQUEST_TIME = "Payload.Time";
    public static final String PATH_REQUEST_BODY = "Payload.Body";
    public static final String PATH_PERMISSIONS = "Data.Permissions";
    public static final String PATH_DATE = "Payload.Date";
    public static final String PATH_EXPIRATION_DATE = "Data.Expiration-Date";
    public static final String PATH_TRANSACTION_DATE = "Data.TransactionFromDateTime";
    public static final String PATH_TRANSACTION_TO_DATE = "Data.TransactionToDateTime";
    public static final String PATH_LIMIT = "Query.Limit";
    public static final String PATH_OFFSET = "Query.Offset";
    public static final String PATH_QUERY_CONSENT_ID = "Query.ConsentId";
    public static final String PATH_STATUS = "Payload.Status";
    public static final String PATH_AUTH_STATUS = "Payload.Authorization.Status";
    public static final String PATH_CONSENT_DATA = "Consent.Data";
    public static final String PATH_ACCOUNT_DATA = "Account.Consent-Data";
    public static final String PATH_INSTRUCTED_AMOUNT = "Data.Initiation.InstructedAmount";
    public static final String PATH_INSTRUCTED_AMOUNT_AMOUNT = "Data.Initiation.InstructedAmount.Amount";
    public static final String PATH_INSTRUCTED_AMOUNT_CURRENCY = "Data.Initiation.InstructedAmount.Currency";
    public static final String PATH_FREQUENCY = "Data.Initiation.Frequency";
    public static final String PATH_FREQUENCY_API_V4 = "Data.Initiation.MandateRelatedInformation.Frequency";
    public static final String PATH_EXECUTION_DATE = "Data.Initiation.RequestedExecutionDate";
    public static final String PATH_VALID_TO_DATE = "Data.ControlParameters.ValidToDateTime";
    public static final String PATH_FUNDS_DATA = "Funds.Confirmation.Consent-Data";
    public static final String PATH_COLLECTION_METHOD = "Payload.CollectionMethod";
    public static final String PATH_PAYLOAD_USER_ID = "Payload.UserId";
    public static final String PATH_PAYLOAD_ACCOUNT_ID = "Payload.AccountId";
    public static final String PATH_PAYLOAD_PAYMENT_ID = "Payload.PaymentRequestId";
    public static final String PATH_DATA = "Data";
    public static final String PATH_URL = "Data.Url";
    public static final String PATH_VERSION = "Data.Version";
    public static final String PATH_REQUEST_PAYLOAD = "Request.Payload";
    public static final String PATH_REQUEST_HEADER = "Request.Header";
    public static final String REQUEST_HEADER = "Request.Header";
    public static final String HEADER_SIGNATURE = "Header.Signature";
    public static final String PATH_INITIATION = "Data.Initiation";
    public static final String PATH_INSTRUCTION = "Data.Instruction";
    public static final String PATH_INSTRUCTION_IDENTIFICATION = "Data.Initiation.InstructionIdentification";
    public static final String PATH_ENDTOEND_IDENTIFICATION = "Data.Initiation.EndToEndIdentification";
    public static final String PATH_CURRENCY_OF_TRANSFER = "Data.Initiation.CurrencyOfTransfer";
    public static final String PATH_DESTINATION_COUNTRY_CODE = "Data.Initiation.DestinationCountryCode";
    public static final String PATH_FIRST_PAYMENT_DATE_TIME = "Data.Initiation.FirstPaymentDateTime";
    public static final String PATH_FIRST_PAYMENT_DATE_TIME_API_V4 =
            "Data.Initiation.MandateRelatedInformation.FirstPaymentDateTime";
    public static final String PATH_COMPLETION_DATE_TIME = "Data.Authorisation.CompletionDateTime";
    public static final String PATH_DEBTOR_ACCOUNT = "Data.Initiation.DebtorAccount";
    public static final String PATH_DEBTOR_ACCOUNT_NAME = "Data.Initiation.DebtorAccount.Name";
    public static final String PATH_DEBTOR_ACCOUNT_IDENTIFICATION = "Data.Initiation.DebtorAccount.Identification";
    public static final String PATH_DEBTOR_ACCOUNT_SCHEME = "Data.Initiation.DebtorAccount.SchemeName";
    public static final String PATH_DEBTOR_ACCOUNT_SECOND_IDENTIFICATION =
            "Data.Initiation.DebtorAccount.SecondaryIdentification";
    public static final String PATH_DEBTOR_ACCOUNT_PROXY_IDENTIFICATION = "Data.Initiation.DebtorAccount.Proxy" +
            ".Identification";
    public static final String PATH_DEBTOR_ACCOUNT_PROXY_CODE = "Data.Initiation.DebtorAccount.Proxy.Code";
    public static final String PATH_DEBTOR_ACCOUNT_PROXY_TYPE = "Data.Initiation.DebtorAccount.Proxy.Type";
    public static final String PATH_DEBTOR_ACCOUNT_PROXY = "Data.Initiation.DebtorAccount.Proxy";
    public static final String PATH_CREDIT_ACCOUNT = "Data.Initiation.CreditorAccount";
    public static final String PATH_INSTRUCTION_CREDIT_ACCOUNT = "Data.Instruction.CreditorAccount";
    public static final String PATH_CREDIT_ACCOUNT_NAME = "Data.Initiation.CreditorAccount.Name";
    public static final String PATH_CREDIT_ACCOUNT_SCHEME = "Data.Initiation.CreditorAccount.SchemeName";
    public static final String PATH_CREDIT_ACCOUNT_PROXY_IDENTIFICATION = "Data.Initiation.CreditorAccount.Proxy" +
            ".Identification";
    public static final String PATH_CREDIT_ACCOUNT_PROXY_CODE = "Data.Initiation.CreditorAccount.Proxy.Code";
    public static final String PATH_CREDIT_ACCOUNT_PROXY_TYPE = "Data.Initiation.CreditorAccount.Proxy.Type";
    public static final String PATH_CREDIT_ACCOUNT_IDENTIFICATION = "Data.Initiation.CreditorAccount.Identification";
    public static final String PATH_CREDIT_ACCOUNT_SEC_IDENTIFICATION = "Data.Initiation.CreditorAccount" +
            ".SecondaryIdentification";
    public static final String PATH_CREDIT_ACCOUNT_PROXY = "Data.Initiation.CreditorAccount.Proxy";
    public static final String PATH_RISK = "Data.Risk";
    public static final String PATH_RISK_PAYMENT_CONTEXT = "Data.Risk.PaymentContextCode";
    public static final String PATH_RISK_MERCHANT_CATEGORY = "Data.Risk.MerchantCategoryCode";
    public static final String PATH_RISK_MERCHANT_CUSTOMER = "Data.Risk.MerchantCustomerIdentification";
    public static final String PATH_RISK_ADDRESS = "Data.Risk.Address";
    public static final String PATH_RISK_COUNTRY = "Data.Risk.Address.Country";
    public static final String PATH_RISK_ADDRESS_STREET_NAME = "Data.Risk.Address.StreetName";
    public static final String PATH_RISK_ADDRESS_BUILDING_NO = "Data.Risk.Address.BuildingNumber";
    public static final String PATH_RISK_ADDRESS_TOWN_NAME = "Data.Risk.Address.TownName";
    public static final String PATH_RISK_ADDRESS_POST_CODE = "Data.Risk.Address.PostCode";
    public static final String PATH_RISK_ADDRESS_COUNTRY_SUB_DIVISION = "Data.Risk.Address.CountrySubDivision";
    public static final String PATH_RISK_ADDRESS_LINE = "Data.Risk.Address.AddressLines";
    public static final String PATH_CREDIT_AGENT = "Data.Initiation.CreditorAgent";
    public static final String PATH_CREDIT_AGENT_NAME = "Data.Initiation.CreditorAgent.SchemeName";
    public static final String PATH_CREDIT_AGENT_IDENTIFICATION = "Data.Initiation.CreditorAgent.Identification";
    public static final String PATH_DEBTOR_AGENT = "Data.Initiation.DebtorAgent";
    public static final String PATH_DEBTOR_AGENT_NAME = "Data.Initiation.DebtorAgent.SchemeName";
    public static final String PATH_DEBTOR_AGENT_IDENTIFICATION = "Data.Initiation.DebtorAgent.Identification";
    public static final String PATH_REMITTANCE_INFO = "Data.Initiation.RemittanceInformation";
    public static final String PATH_REMITTANCE_INFO_REFERENCE = "Data.Initiation.RemittanceInformation.Reference";
    public static final String PATH_REMITTANCE_INFO_UNSTRUCTURED = "Data.Initiation.RemittanceInformation.Unstructured";
    public static final String PATH_LOCAL_INSTRUMENT = "Data.Initiation.LocalInstrument";
    public static final String PATH_REFERENCE = "Data.Initiation.Reference";
    public static final String PATH_FINAL_PAYMENT_DATE_TIME = "Data.Initiation.FinalPaymentDateTime";
    public static final String PATH_FINAL_PAYMENT_DATE_TIME_API_V4 =
            "Data.Initiation.MandateRelatedInformation.FinalPaymentDateTime";
    public static final String PATH_RECURRING_PAYMENT_DATE_TIME = "Data.Initiation.RecurringPaymentDateTime";
    public static final String PATH_RECURRING_PAYMENT_DATE_TIME_API_V4 =
            "Data.Initiation.MandateRelatedInformation.RecurringPaymentDateTime";
    public static final String PATH_CURRENCY = "Data.Initiation.CurrencyOfTransfer";
    public static final String PATH_INSTRUCTION_PRIORITY = "Data.Initiation.InstructionPriority";
    public static final String PATH_EXTENDED_PURPOSE = "Data.Initiation.ExtendedPurpose";
    public static final String PATH_PURPOSE = "Data.Initiation.Purpose";
    public static final String PATH_FIRST_PAYMENT_AMOUNT = "Data.Initiation.FirstPaymentAmount";
    public static final String PATH_FIRST_PAYMENT_AMOUNT_AMOUNT = "Data.Initiation.FirstPaymentAmount.Amount";
    public static final String PATH_FIRST_PAYMENT_CURRENCY = "Data.Initiation.FirstPaymentAmount.Currency";
    public static final String PATH_FINAL_PAYMENT_AMOUNT = "Data.Initiation.FinalPaymentAmount";
    public static final String PATH_FINAL_PAYMENT_AMOUNT_AMOUNT = "Data.Initiation.FinalPaymentAmount.Amount";
    public static final String PATH_FINAL_PAYMENT_CURRENCY = "Data.Initiation.FinalPaymentAmount.Currency";
    public static final String PATH_RECURRING_PAYMENT_AMOUNT = "Data.Initiation.RecurringPaymentAmount";
    public static final String PATH_RECURRING_PAYMENT_AMOUNT_AMOUNT = "Data.Initiation.RecurringPaymentAmount.Amount";
    public static final String PATH_RECURRING_PAYMENT_CURRENCY = "Data.Initiation.RecurringPaymentAmount.Currency";
    public static final String PATH_EXCHANGE_RATE_INFO = "Data.Initiation.ExchangeRateInformation";
    public static final String PATH_EXCHANGE_RATE_UNIT_CURRENCY =
            "Data.Initiation.ExchangeRateInformation.UnitCurrency";
    public static final String PATH_EXCHANGE_RATE = "Data.Initiation.ExchangeRateInformation.ExchangeRate";
    public static final String PATH_EXCHANGE_RATE_TYPE = "Data.Initiation.ExchangeRateInformation.RateType";
    public static final String PATH_EXCHANGE_RATE_IDENTIFICATION =
            "Data.Initiation.ExchangeRateInformation.ContractIdentification";
    public static final String PATH_CREDITOR = "Data.Initiation.Creditor";
    public static final String PATH_CREDITOR_NAME = "Data.Initiation.Creditor.Name";
    public static final String PATH_CREDITOR_ADRESS = "Data.Initiation.Creditor.DeliveryAddress";
    public static final String PATH_HEADER_CONTENT_TYPE = "Header.ContentType";
    public static final String PATH_RESPONSE = "Response.body";
    public static final String PATH_PARAM_CONSENTID = "Consent-id";
    public static final String PATH_FILE_TYPE = "Data.Initiation.FileType";
    public static final String PATH_FILE_HASH = "Data.Initiation.FileHash";
    public static final String PATH_FILE_REFERENCE = "Data.Initiation.FileReference";
    public static final String PATH_FILE_DEBTOR = "Data.Initiation.DebtorAccount";
    public static final String PATH_NO_OF_TXS = "Data.Initiation.NumberOfTransactions";
    public static final String PATH_CONTROL_SUM = "Data.Initiation.ControlSum";
    public static final String PATH_ACCESS_TOKEN = "Header.AccessToken";
    public static final String PATH_EVENT_TYPES = "Data.EventTypes";
    public static final String PATH_CALLBACK_URL = "Data.CallbackUrl";
    public static final String PATH_SUBSCRIPTION_ID = "Data.EventSubscriptionId";
    public static final String COF_PATH_DEBTOR_ACCOUNT_NAME = "Data.DebtorAccount.Name";
    public static final String COF_PATH_DEBTOR_ACCOUNT_IDENTIFICATION = "Data.DebtorAccount.Identification";
    public static final String COF_PATH_DEBTOR_ACCOUNT_SCHEME = "Data.DebtorAccount.SchemeName";
    public static final String COF_PATH_DEBTOR_ACCOUNT_SECOND_IDENTIFICATION =
            "Data.DebtorAccount.SecondaryIdentification";
    public static final String PATH_QUERY_PARAM = "Url.QueryParameters";
    public static final String PATH_CONSENT_AUTH = "Consent.Authorisation";
    public static final String PATH_REQUEST = "Request/Resource";
    public static final String PATH_CREATION_DATE_TIME = "CreationDateTime";
    public static final String PATH_PAYMENT_TYPE = "PaymentType";
    public static final String PATH_FILE = "Data.Initiation.File";
    public static final String PATH_CUTOFF_DATE = "Data.CutOffDateTime";
    public static final String PATH_PSU_AUTHENTICATION_METHOD = "Data.ControlParameters.PSUAuthenticationMethods";
    public static final String PATH_VRP_TYPE = "Data.ControlParameters.VRPType";
    public static final String PATH_MAXIMUM_INDIVIDUAL_AMOUNT = "Data.ControlParameters.MaximumIndividualAmount.Amount";
    public static final String PATH_PERIODIC_LIMITS_AMOUNT = "Data.ControlParameters.PeriodicLimits.Amount";
    public static final String PATH_PERIODIC_LIMITS_CURRENCY = "Data.ControlParameters.PeriodicLimits.Currency";
    public static final String PATH_MAXIMUM_INDIVIDUAL_CURRENCY = "Data.ControlParameters.MaximumIndividualAmount." +
            "Currency";
    public static final String PATH_PERIOD_ALIGNMENT = "Data.ControlParameters.PeriodicLimits.PeriodAlignment";
    public static final String PATH_PERIOD_TYPE = "Data.ControlParameters.PeriodicLimits.PeriodType";
    public static final String PATH_MANDATE_RELATED_INFORMATION = "Data.Initiation.MandateRelatedInformation";
    public static final String PATH_MANDATE_IDENTIFICATION =
            "Data.Initiation.MandateRelatedInformation.MandateIdentification";
    public static final String PATH_CLASSIFICATION = "Data.Initiation.MandateRelatedInformation.Classification";
    public static final String PATH_CATEGORY_PURPOSE_CODE =
            "Data.Initiation.MandateRelatedInformation.CategoryPurposeCode";
    public static final String PATH_REASON = "Data.Initiation.MandateRelatedInformation.Reason";

    public static final String PATH_ULTIMATE_CREDITOR = "Data.Initiation.UltimateCreditor";
    public static final String PATH_ULTIMATE_CREDITOR_SCHEME_NAME = "Data.Initiation.UltimateCreditor.SchemeName";
    public static final String PATH_ULTIMATE_CREDITOR_IDENTIFICATION =
            "Data.Initiation.UltimateCreditor.Identification";
    public static final String PATH_ULTIMATE_CREDITOR_NAME = "Data.Initiation.UltimateCreditor.Name";
    public static final String PATH_ULTIMATE_CREDITOR_LEI = "Data.Initiation.UltimateCreditor.LEI";
    public static final String PATH_ULTIMATE_CREDITOR_POSTAL_ADDRESS = "Data.Initiation.UltimateCreditor.PostalAddress";
    public static final String PATH_ULTIMATE_DEBTOR = "Data.Initiation.UltimateDebtor";
    public static final String PATH_ULTIMATE_DEBTOR_SCHEME_NAME = "Data.Initiation.UltimateDebtor.SchemeName";
    public static final String PATH_ULTIMATE_DEBTOR_IDENTIFICATION =
            "Data.Initiation.UltimateDebtor.Identification";
    public static final String PATH_ULTIMATE_DEBTOR_NAME = "Data.Initiation.UltimateDebtor.Name";
    public static final String PATH_ULTIMATE_DEBTOR_LEI = "Data.Initiation.UltimateDebtor.LEI";
    public static final String PATH_ULTIMATE_DEBTOR_POSTAL_ADDRESS = "Data.Initiation.UltimateDebtor.PostalAddress";
    public static final String PATH_REGULATORY_REPORTING = "Data.Initiation.RegulatoryReporting";

    //DCR Error Messages
    public static final String DCR_INVALID_METADATA = "invalid_client_metadata";
    public static final String DCR_TOKEN_VALIDATION_ERROR = "Error occurred while validating the token received" +
            " against the client id provided ";

    //Error Messages
    public static final String PUT_NOT_SUPPORTED = "Method PUT is not supported";
    public static final String PATCH_NOT_SUPPORTED = "Method PATCH is not supported";
    public static final String PATH_INVALID = "Request path invalid";
    public static final String PATH_NULL = "Request path cannot be null";
    public static final String PAYLOAD_INVALID = "Consent validation failed due to invalid initiation payload";
    public static final String NOT_JSON_OBJECT_ERROR = "Receipt is not a JSON object";
    public static final String STATE_INVALID_ERROR = "Consent not in authorizable state";
    public static final String FILE_STATUS_INVALID_ERROR = "Consent not in awaiting upload state";
    public static final String CONSENT_RETRIEVAL_ERROR = "Exception occurred while getting consent data";
    public static final String CONSENT_ID_NOT_FOUND = "Consent ID not available in consent data";
    public static final String AUTH_RESOURCE_NOT_FOUND = "Auth resource not available in consent data";
    public static final String ACCOUNT_ID_NOT_FOUND_ERROR = "Account IDs not available in persist request";
    public static final String ACCOUNT_ID_FORMAT_ERROR = "Account IDs format error in persist request";
    public static final String CONSENT_PERSIST_ERROR = "Exception occurred while persisting consent";
    public static final String PERMISSION_MISMATCH_ERROR = "Permission mismatch. Consent does not contain necessary " +
            "permissions. :" + ErrorConstants.PATH_PERMISSIONS;
    public static final String INVALID_URI_ERROR = "Path requested is invalid. :" + ErrorConstants.PATH_URL;
    public static final String CONSENT_EXPIRED_ERROR = "Provided consent is expired. :"
            + ErrorConstants.PATH_EXPIRATION_DATE;
    public static final String ACCOUNT_CONSENT_STATE_INVALID = "Account validation failed due to invalid consent" +
            " state. :" + ErrorConstants.PATH_STATUS;
    public static final String PERMISSION_VALIDATION_ERROR = "Exception occurred while validating permissions.";
    public static final String EXP_DATE_PARSE_ERROR = "Error occurred while parsing the expiration date. ";
    public static final String TRANSACTION_TO_FROM_INVALID_ERROR = "The Transaction From/To values are invalid.";
    public static final String DATE_FORMAT_ERROR = "invalid against requested date format(s) [yyyy-MM-dd'T'HH:mm:ssZ," +
            " yyyy-MM-dd'T'HH:mm:ss.[0-9]{1,12}Z]";
    public static final String EXPIRED_DATE_ERROR = "The ExpirationDateTime value has to be a future date.";
    public static final String PAYLOAD_FORMAT_ERROR = "Request Payload is not in correct JSON format";
    public static final String MSG_MISSING_PERMISSIONS = "Missing permissions in the payload.";
    public static final String INVALID_REQ_PAYLOAD = "Invalid request payload";
    public static final String MSG_INVALID_PERMISSIONS = "The payload contains invalid permissions.";
    public static final String MSG_RESTRICTED_PERMISSIONS = "The payload contains restricted permissions.";
    public static final String MSG_NOT_STRING_PERMISSIONS = "Permission array contains non string values";
    public static final String MSG_WRONG_PERMISSIONS = "The payload contains wrong set of permissions.";
    public static final String MSG_EXPIRE_DATE_NOT_FOUND = "Expiration Date Time required but not found";
    public static final String MSG_INVALID_DATE_FORMAT = "The payload contains non ISO 8601 datetime formats.";
    public static final String INITIATION_HANDLE_ERROR = "Error occurred while handling the initiation" +
            " request";
    public static final String INITIATION_RETRIEVAL_ERROR = "Error occurred while handling the initiation" +
            " retrieval request";
    public static final String INITIATION_DELETE_ERROR = "Error occurred while handling the initiation" +
            " delete request";
    public static final String ACC_INITIATION_HANDLE_ERROR = "Error occurred while handling the account initiation" +
            " request";
    public static final String ACC_INITIATION_RETRIEVAL_ERROR = "Error occurred while handling the account initiation" +
            " retrieval request";
    public static final String ACC_INITIATION_DELETE_ERROR = "Error occurred while handling the account initiation" +
            " delete request";
    public static final String ACC_CONSENT_RETRIEVAL_ERROR = "Error occurred while retrieving the account initiation" +
            " request details";
    public static final String ACC_CONSENT_PERSIST_ERROR = "Error occurred while persisting the account initiation " +
            "request details";
    public static final String INVALID_CONSENT_ID = "Invalid Consent Id found in the request";
    public static final String INVALID_STATUS = "Invalid Status value";
    public static final String CLIENT_ID_MISMATCH = "Client Id does not match with the consent Id";
    public static final String INVALID_DATE_FORMAT_NO_TIME_ZONE =
            "Not a valid ISO 8601 date-time query string must not include the timezone";
    public static final String DATE_TIME_QUERY_PARAM_VALIDATION_ERROR = "Datetime Query Parameter Validation Error";
    public static final String WRONG_DATE_FORMAT_QUERY = "Wrongly formatted date. Use valid ISO 8601 date/date-time " +
            "format. :" + ErrorConstants.PATH_QUERY_PARAM;
    public static final String INVALID_QUERY_PARAMS = "Transaction time validation failed. Invalid query parameters " +
            "found in the request. :" + ErrorConstants.PATH_QUERY_PARAM;
    public static final String INVALID_REQUEST_URL = "Invalid request URL";
    public static final String DECODE_FAILED = "Failed to decode the string value with the charset used. :" +
            ErrorConstants.PATH_QUERY_PARAM;
    public static final String DATE_MISMATCH = "Request forbidden due to dates in the query param does not match" +
            " with the dates in the initiation.";
    public static final String PROCESSING_ERROR = "Error while processing the request";
    public static final String MISSING_REQ_PARAM = "R";
    public static final String INVALID_CLIENT_ID = "Invalid mandatory parameter x-wso2-client-id.";
    public static final String MSG_MISSING_CLIENT_ID = "Missing mandatory parameter x-wso2-client-id.";
    public static final String MSG_MISSING_DEBTOR_ACC = "Mandatory parameter DebtorAccount is missing in the payload.";
    public static final String MSG_INVALID_DEBTOR_ACC = "Mandatory parameter DebtorAccount object is invalid.";
    public static final String MISSING_DEBTOR_ACC_SCHEME_NAME = "Mandatory parameter Debtor Account Scheme Name does " +
            "not exists";
    public static final String MISSING_DEBTOR_ACC_IDENTIFICATION = "Mandatory parameter Debtor Account Identification" +
            " does not exists";
    public static final String EMPTY_DEBTOR_ACC_SCHEME_NAME = "Mandatory parameter Debtor Account Scheme Name cannot " +
            "be empty";
    public static final String EMPTY_DEBTOR_ACC_IDENTIFICATION = "Mandatory parameter Debtor Account Identification" +
            " cannot be empty";
    public static final String INVALID_DEBTOR_ACC_SCHEME_NAME = "Debtor Account Scheme Name does not match with the" +
            " Scheme Names defined in the specification";
    public static final String INVALID_DEBTOR_ACC_IDENTIFICATION = "Debtor Account Identification should not exceed" +
            " the max length of 256 characters defined in the specification";
    public static final String INVALID_DEBTOR_ACC_NAME = "Debtor Account Name should not exceed the max length of 70" +
            " character defined in the specification";
    public static final String INVALID_DEBTOR_ACC_SEC_IDENTIFICATION = "Debtor Account Secondary Identification" +
            " should not exceed the max length of 34 characters defined in the specification";
    public static final String COF_INITIATION_HANDLE_ERROR = "Error occurred while handling the Confirmation of Funds" +
            " initiation request";
    public static final String COF_INITIATION_RETRIEVAL_ERROR = "Error occurred while handling the Confirmation of " +
            "Funds initiation retrieval request";
    public static final String COF_INITIATION_DELETE_ERROR = "Error occurred while handling the Confirmation of Funds" +
            " initiation delete request";
    public static final String COF_CONSENT_RETRIEVAL_ERROR = "Error occurred while retrieving the Confirmation " +
            "of Funds initiation request details";
    public static final String NO_CONSENT_FOUND_ERROR = "No valid consent found for given consent ID";
    public static final String NO_CONSENT_FOR_CLIENT_ERROR = "No valid consent found for given information";
    public static final String TOKEN_REVOKE_ERROR = "Token revocation unsuccessful. :" +
            ErrorConstants.PATH_CUTOFF_DATE;
    public static final String AUTH_TOKEN_REVOKE_ERROR = "Cutoff date time elapsed. Error while revoking the consent.";
    public static final String CONSENT_EXPIRED = "Provided consent is expired";
    public static final String COF_CONSENT_STATE_INVALID = "Confirmation of Funds validation failed due to invalid" +
            " consent state.:" + ErrorConstants.PATH_STATUS;
    public static final String ACC_RETRIEVAL_ERROR = "Error occurred while handling the Account submission validation";
    public static final String COF_RETRIEVAL_ERROR = "Error occurred while handling the Confirmation of Funds " +
            "submission validation";
    public static final String PAYMENT_INITIATION_HANDLE_ERROR = "Error occurred while handling the payment " +
            "initiation request";
    public static final String VRP_INITIATION_HANDLE_ERROR = "Error occurred while handling the variable recurring " +
            "payment initiation request";
    public static final String PAYMENT_AUTHORIZE_HANDLE_ERROR = "Error occurred while handling the payment " +
            "authorization request";
    public static final String PAYMENT_SUBMISSION_HANDLE_ERROR = "Error occurred while handling the payment " +
            "submission request";
    public static final String PAYMENT_INITIATION_RETRIEVAL_ERROR = "Error occurred while handling the payment " +
            "initiation retrieval request";
    public static final String PAYMENT_INITIATION_DELETE_ERROR = "Error occurred while handling the payment " +
            "initiation delete request";
    public static final String PAYMENT_CONSENT_RETRIEVAL_ERROR = "Error occurred while retrieving the payment " +
            "initiation request details";
    public static final String FILE_PAYMENT_UPLOAD_ERROR = "Error occurred while handling the file payment " +
            "upload request";
    public static final String FILE_RETRIEVAL_ERROR = "Error occurred while retrieving the file payment " +
            "request file details";
    public static final String MSG_ELAPSED_CUT_OFF_DATE_TIME = "{payment-order} consent / resource received after " +
            "CutOffDateTime.";
    public static final String PAYMENT_DELETE_NOT_SUPPORTED = "Method DELETE is not supported for payment requests";
    public static final String INVALID_PAYMENT_TYPE = "Invalid or not acceptable payment type found";
    public static final String MAX_FIRST_INSTRUCTED_AMOUNT_ERROR = "First Payment Amount in the request exceeds the " +
            "maximum allowed amount by the bank ";
    public static final String MAX_RECURRING_INSTRUCTED_AMOUNT = "Recurring Amount specified exceed the Maximum" +
            " Instructed Amount of the bank";
    public static final String MAX_FINAL_INSTRUCTED_AMOUNT = "Final Amount specified exceed the Maximum Instructed " +
            "Amount of the bank";
    public static final String MAX_INSTRUCTED_AMOUNT = "Instructed Amount specified exceed the Maximum Instructed " +
            "Amount of the bank";
    public static final String INVALID_INSTRUCTED_AMOUNT = "Instructed Amount specified should be grater than zero";
    public static final String MAX_EXECUTION_DATE = "Requested Execution Date specified cannot exceed the maximum " +
            "number of days supported by the ASPSP";
    public static final String INVALID_VALID_TO_DATE = "Valid to Date specified in the request is invalid ";
    public static final String INVALID_EXECUTION_DATE = "Requested Execution Date specified is a past date";
    public static final String MISSING_EXECUTION_DATE = "Mandatory Parameter RequestedExecutionDateTime is missing";
    public static final String INVALID_COMPLETION_DATE_FORMAT = "Completion Date is not a valid ISO 8601 date-time";
    public static final String ELAPSED_COMPLETION_DATE_TIME = "Completion Payment Date Time value cannot be set in" +
            " the past";
    public static final String MISSING_FIRST_PAYMENT_DATE = "Mandatory Parameter FirstPaymentDateTime is missing";
    public static final String INVALID_FIRST_PAYMENT_DATE_FORMAT = "FirstPaymentDateTime is not a valid ISO 8601" +
            " date-time";
    public static final String ELAPSED_FIRST_PAYMENT_DATE_TIME = "First Payment Date Time value cannot be set in " +
            "the past";
    public static final String INVALID_FINAL_PAYMENT_DATE_FORMAT = "FinalPaymentDateTime is not a valid ISO 8601" +
            " date-time";
    public static final String ELAPSED_FINAL_PAYMENT_DATE_TIME = "Final Payment Date Time value cannot be set in " +
            "the past";
    public static final String INVALID_RECURRING_PAYMENT_DATE_FORMAT = "RecurringPaymentDateTime is not a valid ISO" +
            " 8601 date-time";
    public static final String ELAPSED_RECURRING_PAYMENT_DATE_TIME = "Recurring Payment Date Time value cannot be set" +
            " in the past";
    public static final String FINAL_PAYMENT_BEFORE_FIRSTPAYEMNT =
            "Final Payment Date Time value cannot be before the first payment date time";
    public static final String RECURRINGPAYMENT_BEFORE_FIRSTPAYMENT =
            "Recurring Payment Date Time value cannot be before the first payment date time";
    public static final String FINAL_PAYMENT_BEFORE_RECURRINGPAYMENT =
            "Final Payment Date Time value cannot be before the recurring payment date time";
    public static final String MISSING_FIRST_PAYMENT_AMOUNT_OBJECT = "Mandatory Parameter FirstPaymentAmount is " +
            "missing";
    public static final String INVALID_FIRST_PAYMENT_AMOUNT_OBJECT = "Mandatory Parameter FirstPaymentAmount is " +
            "invalid";
    public static final String INVALID_RECURRING_PAYMENT_AMOUNT_OBJECT = "Mandatory Parameter FirstPaymentAmount is " +
            "invalid";
    public static final String INVALID_FINAL_PAYMENT_AMOUNT_OBJECT = "Mandatory Parameter FirstPaymentAmount is " +
            "invalid";
    public static final String MISSING_FIRST_PAYMENT_AMOUNT = "Mandatory Parameter Amount is missing in " +
            "FirstPaymentAmount";
    public static final String MISSING_FINAL_PAYMENT_AMOUNT = "Mandatory Parameter Amount is missing in " +
            "FinalPaymentAmount";
    public static final String MISSING_RECURRING_PAYMENT_AMOUNT = "Mandatory Parameter Amount is missing in " +
            "RecurringPaymentAmount";
    public static final String INVALID_RECURRING_PAYMENT = "Recurring Payment Date Time or recurring payment amount" +
            " should be different from the first payment date time and first payment amount";
    public static final String INVALID_STANDING_ORDER = "Close ended standing order should have only one of these: " +
            "NumberOfPayments or FinalPaymentDateTime";
    public static final String INVALID_BICIF_IDENTIFICATION = "Invalid identifier for BICFI scheme in creditor agent";
    public static final String MISSING_VALUES_EXCHANGE_RATE = "Mandatory values in ExchangeRateInformation object is" +
            " missing";
    public static final String INVALID_EXCHANGE_RATE_INFO_AGREED_RATE = "Exchange rate and ContractIdentification " +
            "must be specified for agreed rate type";
    public static final String INVALID_EXCHANGE_RATE_INFO = "ExchangeRateInformation,must not specify ExchangeRate" +
            " and/or ContractIdentification when requesting an Actual or Indicative RateType";
    public static final String INVALID_FILE = "Invalid file content found in the request";

    public static final String MSG_MISSING_CREDITOR_ACC = "Mandatory parameter CreditorAccount is missing in the" +
            " payload.";
    public static final String MSG_INVALID_CREDITOR_ACC = "Mandatory parameter CreditorAccount object is invalid.";
    public static final String MISSING_CREDITOR_ACC_SCHEME_NAME = "Mandatory parameter Creditor Account Scheme Name" +
            " does not exists";
    public static final String MISSING_CREDITOR_ACC_IDENTIFICATION = "Mandatory parameter Creditor Account " +
            "Identification does not exists";
    public static final String INVALID_CREDITOR_ACC_SCHEME_NAME = "Creditor Account Scheme Name does not match with" +
            " the Scheme Names defined in the specification";
    public static final String INVALID_CREDITOR_ACC_IDENTIFICATION = "Creditor Account Identification should not " +
            "exceed the max length of 256 characters defined in the specification";
    public static final String INVALID_CREDITOR_ACC_NAME = "Creditor Account Name should not exceed the max length" +
            " of 350 character defined in the specification";
    public static final String INVALID_CREDITOR_ACC_SEC_IDENTIFICATION = "Creditor Account Secondary Identification" +
            " should not exceed the max length of 34 characters defined in the specification";
    public static final String INVALID_IDENTIFICATION = "Identification validation for SortCodeNumber Scheme failed.";
    public static final String CREDITOR_AGENT_UNEXPECTED = "Creditor agent should not be given if scheme name is " +
            "sort code";
    public static final String INVALID_CREDITOR_AGENT = "Must specify either Scheme Name and Identification or Name" +
            " and Postal Address for creditor agent";
    public static final String INVALID_FILE_TYPE = "File Type of the request does not match with any allowed values";
    public static final String RECEIPT_PARSING_ERROR = "Error while parsing the receipt data";

    public static final String INVALID_CREDITOR_AGENT_SCHEME_NAME = "Invalid Creditor Agent scheme name";
    public static final String INVALID_LOCAL_INSTRUMENT = "The given local instrument value is not supported";
    public static final String INVALID_PURPOSE = "The given purpose value is not supported";
    public static final String INVALID_RISK_SECTION = "When PaymentContextCode is EcommerceGoods or " +
            "EcommerceServices, MerchantCategoryCode and MerchantCustomerIdentification should be populated";
    public static final String INVALID_DELIVERY_ADDRESS = "EcommerceGoods context is set, but delivery address is" +
            " not set";
    public static final String AUTH_CUT_OFF_DATE_ELAPSED = "Cut off time has elapsed";
    public static final String INVALID_CONSENT_TYPE = "Invalid Consent Type found in the request";
    public static final String PAYMENT_TYPE_MISMATCH = "Payment Type related to given consent Id does not match " +
            "with the payment type of the request. :" + ErrorConstants.PATH_PAYMENT_TYPE;
    public static final String DATE_PARSE_MSG = "Parsed OffsetDateTime: %s, current OffsetDateTime: %s";
    public static final String REQUEST_OBJ_EXTRACT_ERROR = "Request object cannot be extracted";
    public static final String REQUEST_OBJ_NOT_SIGNED = "request object is not signed JWT";
    public static final String NOT_JSON_PAYLOAD = "Payload is not a JSON object";
    public static final String INTENT_ID_NOT_FOUND = "intent_id not found in request object";
    public static final String REQUEST_OBJ_PARSE_ERROR = "Error while parsing the request object.";
    public static final String INVALID_DEBTOR_ACC_SCHEME_NAME_LENGTH = "Debtor Account Scheme Name length does not " +
            "match with the length defined in the specification";
    public static final String INVALID_CREDITOR_ACC_SCHEME_NAME_LENGTH = "Creditor Account Scheme Name length does" +
            " not match with the length defined in the specification";
    public static final String CONSENT_ID_MISMATCH = "Consent Id not found or not matching";
    public static final String ACCOUNT_CONSENT_NOT_FOUND = "Account validation failed due to consent not found for " +
            "given consent Id.";
    public static final String PAYMENT_CONSENT_NOT_FOUND = "Payment validation failed due to consent not found for " +
            "given consent Id.";
    public static final String COF_CONSENT_NOT_FOUND = "Confirmation of Funds validation failed due to consent not " +
            "found for given consent Id.";
    public static final String IDEMPOTENCY_KEY_NOT_FOUND = "Idempotency related details should be submitted" +
            " in order to proceed.";
    public static final String IDEMPOTENCY_KEY_FRAUDULENT = "Idempotency check failed.";
    public static final String FILE_UPLOAD_HANDLE_ERROR = "Error occurred while handling the file payment upload";
    public static final String FILE_HASH_CALCULATE_ERROR = "Error while calculating the file hash from the" +
            " uploaded file";
    public static final String FILE_HASH_ERROR = "File hash doesn't match with the hash provided in the " +
            "initiation call.";
    public static final String FILE_DEBTOR_ACC_ERROR = "Debtor Account validation failed for the debtor account " +
            "in the file.";
    public static final String FILE_NO_OF_TRANS_ERROR = "No of Transactions doesn't match with the initiation call.";
    public static final String FILE_EXECUTION_DATE_ERROR = "Execution Date provided is invalid.";
    public static final String FILE_TOTAL_SUM_NOT_MATCHING_ERROR = "Total sum of the transactions specified doesn't" +
            " match with the initiation call.";
    public static final String FILE_TOTAL_SUM_ERROR = "Total sum of the transactions specified doesn't match with" +
            " the sum of transaction elements present in the file.";

    //Submission Validation Error Messages containing path value as well. Handled these errors in UKErrorHandler.
    public static final String CUT_OFF_DATE_ELAPSED = "Cut off time has elapsed :" +
            ErrorConstants.PATH_CUTOFF_DATE;
    public static final String MSG_INVALID_CONSENT_ID = "The requested consent-Id does not match with the consent-Id" +
            " bound to token:" +  ErrorConstants.PATH_CONSENT_ID;
    public static final String MSG_INVALID_CLIENT_ID = "The client Id related the consent does not match with the " +
            "client id bound to token:" +  ErrorConstants.PATH_CLIENT_ID;
    public static final String PAYMENT_CONSENT_STATE_INVALID = "Payment validation failed due to invalid consent" +
            " state.:" + ErrorConstants.PATH_STATUS;
    public static final String INVALID_CONTENT_TYPE = "Invalid Content Type found in the request.:"
            + ErrorConstants.PATH_HEADER_CONTENT_TYPE;
    public static final String MISSING_CONTENT_TYPE = "Request Content-Type header does not match any allowed types.:"
            + ErrorConstants.PATH_HEADER_CONTENT_TYPE;
    public static final String INVALID_USER_ID = "Token received does not bound to the authorized user.:"
            + ErrorConstants.PATH_ACCESS_TOKEN;
    public static final String DATA_NOT_FOUND = "Data is not found or empty in the request.:" +
            ErrorConstants.PATH_DATA;
    public static final String INITIATION_NOT_FOUND = "Initiation is not found or empty in the request.:" +
            ErrorConstants.PATH_INITIATION;
    public static final String INSTRUCTION_NOT_FOUND = "Instruction is not found or empty in the request.:" +
            ErrorConstants.PATH_INSTRUCTION;
    public static final String RISK_NOT_FOUND = "RISK is not found or empty in the request.:" +
            ErrorConstants.PATH_RISK;
    public static final String RISK_MISMATCH = "RISK Does Not Match.:" + ErrorConstants.PATH_RISK;
    public static final String FILE_TYPE_NOT_FOUND = "File Type Not found:" + ErrorConstants.PATH_FILE_TYPE;
    public static final String FILE_TYPE_MISMATCH = "File Type Does Not Match:" + ErrorConstants.PATH_FILE_TYPE;
    public static final String FILE_HASH_NOT_FOUND = "File Hash Not found:" + ErrorConstants.PATH_FILE_HASH;
    public static final String FILE_HASH_MISMATCH = "File Hash Does Not Match:" + ErrorConstants.PATH_FILE_HASH;
    public static final String FILE_REFERENCE_MISMATCH = "File Reference Does Not Match:" +
            ErrorConstants.PATH_FILE_REFERENCE;
    public static final String NUM_OF_TRANSACTIONS_MISMATCH = "Number of Transactions Does Not Match:" +
            ErrorConstants.PATH_NO_OF_TXS;
    public static final String CONTROL_SUM_MISMATCH = "Control Sum Does Not Match:" +
            ErrorConstants.PATH_CONTROL_SUM;
    public static final String EXECUTION_DATE_MISMATCH = "Execution Data & Time does Not Match:" +
            ErrorConstants.PATH_EXECUTION_DATE;
    public static final String LOCAL_INSTRUMENT_MISMATCH = "Local Instrument Does Not Match:" +
            ErrorConstants.PATH_LOCAL_INSTRUMENT;
    public static final String REMITTANCE_INFO_MISMATCH = "Remittance Information Does Not Match:" +
            ErrorConstants.PATH_REMITTANCE_INFO;
    public static final String INSTRUCTED_AMOUNT_NOT_FOUND = "Instructed Amount isn't present in the payload:" +
            ErrorConstants.PATH_INSTRUCTED_AMOUNT;
    public static final String INSTRUCTED_AMOUNT_AMOUNT_NOT_FOUND = "Instructed Amount  Amount isn't present in the " +
            "payload:" + ErrorConstants.PATH_INSTRUCTED_AMOUNT;
    public static final String INSTRUCTED_AMOUNT_CURRENCY_NOT_FOUND = "Instructed Amount Currency isn't present in " +
            "the payload:" + ErrorConstants.PATH_INSTRUCTED_AMOUNT;
    public static final String INSTRUCTED_AMOUNT_AMOUNT_MISMATCH = "Instructed Amount Amount does not match the " +
            "initiated amount:" + ErrorConstants.PATH_INSTRUCTED_AMOUNT_AMOUNT;
    public static final String INSTRUCTED_AMOUNT_CURRENCY_MISMATCH = "Instructed Amount currency does not match the " +
            "initiated amount or currency:" + ErrorConstants.PATH_INSTRUCTED_AMOUNT_CURRENCY;
    public static final String FIRST_PAYMENT_MISMATCH = "First Payment Amount or Currency does not match the " +
            "initiated amount or currency";
    public static final String FIRST_PAYMENT_AMOUNT_MISMATCH = "First Payment Amount does not match:" +
            ErrorConstants.PATH_FIRST_PAYMENT_AMOUNT_AMOUNT;
    public static final String CREDITOR_ACC_NOT_FOUND = "Creditor Account isn't present in the request.:" +
            ErrorConstants.PATH_CREDIT_ACCOUNT;
    public static final String CREDITOR_ACC_MISSING = "Creditor Account isn't present in the request.:" +
            ErrorConstants.PATH_INSTRUCTION_CREDIT_ACCOUNT;
    public static final String CREDITOR_ACC_MISMATCH = "Missing Creditor Account in the payload";
    public static final String CREDITOR_ACC_SCHEME_NAME_MISMATCH = "Creditor Accounts Scheme does not match:" +
            ErrorConstants.PATH_CREDIT_ACCOUNT_SCHEME;
    public static final String CREDITOR_ACC_SCHEME_NAME_NOT_FOUND = "Creditor Accounts Scheme isn't present in the" +
            " request or in the consent.:" + ErrorConstants.PATH_CREDIT_ACCOUNT_SCHEME;
    public static final String CREDITOR_ACC_PROXY_IDENTIFICATION_MISMATCH = "Creditor Accounts Proxy Identification " +
            "does not match:" + ErrorConstants.PATH_CREDIT_ACCOUNT_PROXY_IDENTIFICATION;
    public static final String CREDITOR_ACC_PROXY_IDENTIFICATION_NOT_FOUND = "Creditor Accounts Proxy Identification " +
            "isn't present in the request or in the consent.:"
            + ErrorConstants.PATH_CREDIT_ACCOUNT_PROXY_IDENTIFICATION;
    public static final String CREDITOR_ACC_PROXY_CODE_MISMATCH = "Creditor Accounts Proxy Code " +
            "does not match:" + ErrorConstants.PATH_CREDIT_ACCOUNT_PROXY_CODE;
    public static final String CREDITOR_ACC_PROXY_CODE_NOT_FOUND = "Creditor Accounts Proxy Code " +
            "isn't present in the request or in the consent.:"
            + ErrorConstants.PATH_CREDIT_ACCOUNT_PROXY_CODE;
    public static final String CREDITOR_ACC_PROXY_TYPE_MISMATCH = "Creditor Accounts Proxy Type " +
            "does not match:" + ErrorConstants.PATH_CREDIT_ACCOUNT_PROXY_TYPE;
    public static final String CREDITOR_ACC_IDENTIFICATION_MISMATCH = "Creditor Account Identification does not match:"
            + ErrorConstants.PATH_CREDIT_ACCOUNT_IDENTIFICATION;
    public static final String CREDITOR_ACC_IDENTIFICATION_NOT_FOUND = "Creditor Account Identification isn't " +
            "present in the request or in the consent.:" + ErrorConstants.PATH_CREDIT_ACCOUNT_IDENTIFICATION;
    public static final String CREDITOR_ACC_NAME_MISMATCH = "Creditor Account Name does not match:" +
            ErrorConstants.PATH_CREDIT_ACCOUNT_NAME;
    public static final String CREDITOR_ACC_SEC_IDENTIFICATION_MISMATCH = "Creditor Account Secondary Identification" +
            " does not match:" + ErrorConstants.PATH_CREDIT_ACCOUNT_SEC_IDENTIFICATION;
    public static final String CREDITOR_ACC_PROXY_MISMATCH = "Creditor Account Proxy does not match:"
            + ErrorConstants.PATH_CREDIT_ACCOUNT_PROXY;
    public static final String CREDITOR_AGENT_MISMATCH = "Creditor Agent isn't present in the request or in the " +
            "consent:" + ErrorConstants.PATH_CREDIT_AGENT;
    public static final String CREDITOR_AGENT_SCHEME_NAME_MISMATCH = "Creditor Agent Scheme name does not match:" +
            ErrorConstants.PATH_CREDIT_AGENT_NAME;
    public static final String CREDITOR_AGENT_IDENTIFICATION_MISMATCH = "Creditor Agent Identification does not match:"
            + ErrorConstants.PATH_CREDIT_AGENT_IDENTIFICATION;
    public static final String CREDITOR_AGENT_NAME_MISMATCH = "Creditor Agent Name does not match:"
            + ErrorConstants.PATH_CREDIT_AGENT_NAME;
    public static final String INSTRUCTION_IDENTIFICATION_NOT_FOUND = "Instruction Identification isn't present in " +
            "the request:" + ErrorConstants.PATH_INSTRUCTION_IDENTIFICATION;
    public static final String INSTRUCTION_IDENTIFICATION_MISMATCH = "Instruction Identification does not match:"
            + ErrorConstants.PATH_INSTRUCTION_IDENTIFICATION;
    public static final String END_TO_END_IDENTIFICATION_NOT_FOUND = "End to End Identification isn't present in " +
            "the request or in the consent:" + ErrorConstants.PATH_ENDTOEND_IDENTIFICATION;
    public static final String END_TO_END_IDENTIFICATION_MISMATCH = "End to End Identification does not match:"
            + ErrorConstants.PATH_ENDTOEND_IDENTIFICATION;
    public static final String DEBTOR_ACC_MISMATCH = "Debtor Account isn't present in the request " +
            "or in the consent:" + ErrorConstants.PATH_DEBTOR_ACCOUNT;
    public static final String DEBTOR_ACC_SCHEME_NAME_MISMATCH = "Debtor Account Scheme name does not " +
            "match:" + ErrorConstants.PATH_DEBTOR_ACCOUNT_SCHEME;
    public static final String DEBTOR_ACC_SCHEME_NAME_NOT_FOUND = "Debtor Account Scheme name isn't present in the " +
            "request or in the consent:" + ErrorConstants.PATH_DEBTOR_ACCOUNT_SCHEME;
    public static final String DEBTOR_ACC_IDENTIFICATION_MISMATCH = "Debtor Account Identification does " +
            "not match:" + ErrorConstants.PATH_DEBTOR_ACCOUNT_IDENTIFICATION;
    public static final String DEBTOR_ACC_IDENTIFICATION_NOT_FOUND = "Debtor Account Identification isn't present " +
            "in the request or in the consent:" + ErrorConstants.PATH_DEBTOR_ACCOUNT_IDENTIFICATION;
    public static final String DEBTOR_ACC_NAME_MISMATCH = "Debtor Account Name does not match:" +
            ErrorConstants.PATH_DEBTOR_ACCOUNT_NAME;
    public static final String DEBTOR_ACC_SEC_IDENTIFICATION_MISMATCH = "Debtor Account Secondary Identification" +
            " does not match:" + ErrorConstants.PATH_DEBTOR_ACCOUNT_SECOND_IDENTIFICATION;
    public static final String DEBTOR_ACC_PROXY_MISMATCH = "Debtor Account Proxy does not match:"
            + ErrorConstants.PATH_DEBTOR_ACCOUNT_PROXY;
    public static final String DEBTOR_ACC_PROXY_IDENTIFICATION_MISMATCH = "Debtor Accounts Proxy Identification " +
            "does not match:" + ErrorConstants.PATH_DEBTOR_ACCOUNT_PROXY_IDENTIFICATION;
    public static final String DEBTOR_ACC_PROXY_IDENTIFICATION_NOT_FOUND = "Debtor Accounts Proxy Identification " +
            "isn't present in the request or in the consent.:"
            + ErrorConstants.PATH_DEBTOR_ACCOUNT_PROXY_IDENTIFICATION;
    public static final String DEBTOR_ACC_PROXY_CODE_MISMATCH = "Debtor Accounts Proxy Code " +
            "does not match:" + ErrorConstants.PATH_DEBTOR_ACCOUNT_PROXY_CODE;
    public static final String DEBTOR_ACC_PROXY_CODE_NOT_FOUND = "Debtor Accounts Proxy Code " +
            "isn't present in the request or in the consent.:"
            + ErrorConstants.PATH_DEBTOR_ACCOUNT_PROXY_CODE;
    public static final String DEBTOR_ACC_PROXY_TYPE_MISMATCH = "Debtor Accounts Proxy Type " +
            "does not match:" + ErrorConstants.PATH_DEBTOR_ACCOUNT_PROXY_TYPE;
    public static final String DEBTOR_AGENT_MISMATCH = "Debtor Agent isn't present in the request or in the consent:" +
            ErrorConstants.PATH_DEBTOR_AGENT;
    public static final String DEBTOR_AGENT_SCHEME_NAME_MISMATCH = "Debtor Agent scheme name does not match";
    public static final String DEBTOR_AGENT_IDENTIFICATION_MISMATCH = "Debtor Agent Identification does not match";
    public static final String REMMITANCE_INFO_MISMATCH = "Remittance Information isn't present in the " +
            "request or in the consent:" + ErrorConstants.PATH_REMITTANCE_INFO;
    public static final String REMMITANCE_REFERENCE_MISMATCH = "Remittance Information Reference does not match:" +
            ErrorConstants.PATH_REMITTANCE_INFO_REFERENCE;
    public static final String REMMITANCE_UNSTRUCTURED_MISMATCH = "Remittance Information Unstructured does not " +
            "match:" + ErrorConstants.PATH_REMITTANCE_INFO_UNSTRUCTURED;
    public static final String REQUESTED_EXECUTION_DATE_MISMATCH = "Requested Execution Date Time does not match.:" +
            ErrorConstants.PATH_EXECUTION_DATE;
    public static final String REQUESTED_EXECUTION_DATE_NOT_FOUND = "Requested Execution Date Time does not found in" +
            " the request or in the consent.:" + ErrorConstants.PATH_EXECUTION_DATE;
    public static final String FREQUENCY_MISMATCH = "Frequency does not match.:" + ErrorConstants.PATH_FREQUENCY;
    public static final String FREQUENCY_MISMATCH_API_V4 = "Frequency does not match.:" +
            ErrorConstants.PATH_FREQUENCY_API_V4;
    public static final String FREQUENCY_NOT_FOUND = "Frequency does not found in the request or in the consent.:"
            + ErrorConstants.PATH_FREQUENCY;
    public static final String FREQUENCY_NOT_FOUND_API_V4 =
            "Frequency does not found in the request or in the consent.:" + ErrorConstants.PATH_FREQUENCY_API_V4;
    public static final String REFERENCE_MISMATCH = "Reference does not match.:" + ErrorConstants.PATH_REFERENCE;
    public static final String FIRST_PAYMENT_DATE_MISMATCH = "First Payment Date Time does not match.:" +
            ErrorConstants.PATH_FIRST_PAYMENT_DATE_TIME;
    public static final String FIRST_PAYMENT_DATE_MISMATCH_API_V4 = "First Payment Date Time does not match.:" +
            ErrorConstants.PATH_FIRST_PAYMENT_DATE_TIME_API_V4;
    public static final String FIRST_PAYMENT_DATE_NOT_FOUND = "First Payment Date Time does not found in the request" +
            " or in the consent.:" + ErrorConstants.PATH_FIRST_PAYMENT_DATE_TIME;
    public static final String FINAL_PAYMENT_DATE_MISMATCH = "Final Payment Date Time does not match:" +
            ErrorConstants.PATH_FINAL_PAYMENT_DATE_TIME;
    public static final String FINAL_PAYMENT_DATE_MISMATCH_API_V4 = "Final Payment Date Time does not match:" +
            ErrorConstants.PATH_FINAL_PAYMENT_DATE_TIME_API_V4;
    public static final String RECURRING_PAYMENT_DATE_MISMATCH = "Recurring Payment Date Time does not match:" +
            ErrorConstants.PATH_RECURRING_PAYMENT_DATE_TIME;
    public static final String RECURRING_PAYMENT_DATE_MISMATCH_API_V4 = "Recurring Payment Date Time does not match:" +
            ErrorConstants.PATH_RECURRING_PAYMENT_DATE_TIME_API_V4;
    public static final String CURRENCY_TRANSFER_MISMATCH = "Currency Of Transfer does not match.:" +
            ErrorConstants.PATH_CURRENCY_OF_TRANSFER;
    public static final String CURRENCY_TRANSFER_NOT_FOUND = "Currency Of Transfer isn't present in the request or" +
            " in the consent.:" + ErrorConstants.PATH_CURRENCY_OF_TRANSFER;
    public static final String COUNTRY_CODE_MISMATCH = "Destination Country Code does not match:" +
            ErrorConstants.PATH_DESTINATION_COUNTRY_CODE;
    public static final String INSTRUCTION_PRIORITY_MISMATCH = "Instruction Priority does not match:" +
            ErrorConstants.PATH_INSTRUCTION_PRIORITY;
    public static final String PURPOSE_MISMATCH = "Purpose does not match:" + ErrorConstants.PATH_PURPOSE;
    public static final String EXTENDED_PURPOSE_MISMATCH = "Extended Purpose does not match:" +
            ErrorConstants.PATH_EXTENDED_PURPOSE;
    public static final String FIRST_PAYMENT_AMOUNT_NOT_FOUND = "First Payment Amount isn't present in the " +
            "request or in the consent:" + ErrorConstants.PATH_FIRST_PAYMENT_AMOUNT;
    public static final String FIRST_PAYMENT_CURRENCY_MISMATCH = "First Payment Currency does not match:" +
            ErrorConstants.PATH_FIRST_PAYMENT_CURRENCY;
    public static final String FINAL_PAYMENT_AMOUNT_NOT_FOUND = "Final Payment Amount isn't present in the" +
            " request or in the consent:" + ErrorConstants.PATH_FINAL_PAYMENT_AMOUNT;
    public static final String FINAL_PAYMENT_AMOUNT_MISMATCH = "Final Payment Amount does not match:" +
            ErrorConstants.PATH_FINAL_PAYMENT_AMOUNT;
    public static final String FINAL_PAYMENT_CURRENCY_MISMATCH = "Final Payment Currency does not match:" +
            ErrorConstants.PATH_FINAL_PAYMENT_CURRENCY;
    public static final String RECURRING_PAYMENT_AMOUNT_NOT_FOUND = "Recurring Payment Amount isn't present in the" +
            " request or in the consent:" + ErrorConstants.PATH_RECURRING_PAYMENT_AMOUNT;
    public static final String RECURRING_PAYMENT_AMOUNT_MISMATCH = "Recurring Payment Amount does not match:" +
            ErrorConstants.PATH_RECURRING_PAYMENT_AMOUNT_AMOUNT;
    public static final String RECURRING_PAYMENT_CURRENCY_MISMATCH = "Recurring Payment Currency does not match:" +
            ErrorConstants.PATH_RECURRING_PAYMENT_CURRENCY;
    public static final String EXCHANGE_RATE_INFO_MISMATCH = "Exchange Rate Information isn't present in " +
            "the request or in the consent:" + ErrorConstants.PATH_EXCHANGE_RATE_INFO;
    public static final String EXCHNAGE_RATE_UNIT_CURRENCY_MISMATCH = "Exchange Rate Unit Currency does not match:" +
            ErrorConstants.PATH_EXCHANGE_RATE_UNIT_CURRENCY;
    public static final String  EXCHANGE_RATE_NOT_FOUND = "Exchange Rate isn't present in the request or in the" +
            " consent:" + ErrorConstants.PATH_EXCHANGE_RATE;
    public static final String EXCHANGE_RATE_TYPE_MISMATCH = "Exchange Rate Type does not match:" +
            ErrorConstants.PATH_EXCHANGE_RATE_TYPE;
    public static final String EXCHANGE_RATE_MISMATCH = "Exchange Rate does not match:" +
            ErrorConstants.PATH_EXCHANGE_RATE;
    public static final String CONTRACT_IDENTIFICATION_MISMATCH = "Exchange Rate Contract Identification" +
            " does not match:" + ErrorConstants.PATH_EXCHANGE_RATE_IDENTIFICATION;
    public static final String CREDITOR_MISMATCH = "Creditor aren't present in the request or in the consent:" +
            ErrorConstants.PATH_CREDITOR;
    public static final String CREDITOR_NAME_MISMATCH = "Creditor Name is not matching:" +
            ErrorConstants.PATH_CREDITOR_NAME;
    public static final String POSTAL_ADDRESS_MISMATCH = "Postal Address does not match:" +
            ErrorConstants.PATH_CREDITOR_ADRESS;
    public static final String PAYMENT_CONTEXT_CODE_MISMATCH = "Risk Payment Context Code does not match:" +
            ErrorConstants.PATH_RISK_PAYMENT_CONTEXT;
    public static final String MERCHANT_CATEGORY_CODE_MISMATCH = "Merchant Category Code does not match:" +
            ErrorConstants.PATH_RISK_MERCHANT_CATEGORY;
    public static final String MERCHANT_CUSTOMER_IDENTIFICATION_MISMATCH = "Risk Merchant Customer Identification" +
            " does not match:" + ErrorConstants.PATH_RISK_MERCHANT_CUSTOMER;
    public static final String DELIVERY_ADDRESS_MISMATCH = "Risk Delivery Addresses isn't present in the " +
            "request or in the consent:" + ErrorConstants.PATH_RISK_ADDRESS;
    public static final String STREET_NAME_MISMATCH = "Risk Street Name does not match:" +
            ErrorConstants.PATH_RISK_ADDRESS_STREET_NAME;
    public static final String BUILDING_NUMBER_MISMATCH = "Risk Building Number does not match:" +
            ErrorConstants.PATH_RISK_ADDRESS_BUILDING_NO;
    public static final String POST_CODE_MISMATCH = "Risk Post Code does not match:" +
            ErrorConstants.PATH_RISK_ADDRESS_POST_CODE;
    public static final String TOWN_NAME_MISMATCH = "Risk Town Name does not match:" +
            ErrorConstants.PATH_RISK_ADDRESS_TOWN_NAME;
    public static final String COUNTRY_MISMATCH = "Risk Country does not match:" +
            ErrorConstants.PATH_RISK_COUNTRY;
    public static final String COUNTRY_NOT_FOUND = "Risk Country isn't present in the request or consent:" +
            ErrorConstants.PATH_RISK_COUNTRY;
    public static final String ADDRESS_LINE_NOT_FOUND = "Risk Address Lines aren't present in the request or consent:" +
            ErrorConstants.PATH_RISK_ADDRESS_LINE;
    public static final String ADDRESS_LINE_MISMATCH = "Risk Address Line does not match:" +
            ErrorConstants.PATH_RISK_ADDRESS_LINE;
    public static final String COUNTRY_SUB_DIVISION_NOT_FOUND = "Risk Country Sub Division isn't present either in" +
            " the request or consent:" + ErrorConstants.PATH_RISK_ADDRESS_COUNTRY_SUB_DIVISION;
    public static final String COUNTRY_SUB_DIVISION_MISMATCH = "Country Sub Division does not match:" +
            ErrorConstants.PATH_RISK_ADDRESS_COUNTRY_SUB_DIVISION;
    public static final String ACCOUNT_ID_NOT_AVAILABLE_MSG = "Requested Resource with the given ID is Unavailable. :"
            + ErrorConstants.PATH_PAYLOAD_ACCOUNT_ID;
    public static final String MANDATE_RELATED_INFORMATION_MISMATCH = "Mandate Related Information does not match.:" +
            ErrorConstants.PATH_MANDATE_RELATED_INFORMATION;
    public static final String ULTIMATE_CREDITOR_MISMATCH = "UltimateCreditor does not match:" +
            ErrorConstants.PATH_ULTIMATE_CREDITOR;
    public static final String ULTIMATE_DEBTOR_MISMATCH = "UltimateDebtor does not match:" +
            ErrorConstants.PATH_ULTIMATE_DEBTOR;

    public static final String ULTIMATE_CREDITOR_NAME_MISMATCH = "UltimateCreditors Name does not match:" +
            ErrorConstants.PATH_ULTIMATE_CREDITOR_NAME;
    public static final String ULTIMATE_CREDITOR_IDENTIFICATION_MISMATCH = "UltimateCreditors Identification does " +
            "not match:" + ErrorConstants.PATH_ULTIMATE_CREDITOR_IDENTIFICATION;
    public static final String ULTIMATE_CREDITOR_LEI_MISMATCH = "UltimateCreditors LEI does not match:" +
            ErrorConstants.PATH_ULTIMATE_CREDITOR_LEI;
    public static final String ULTIMATE_CREDITOR_SCHEME_NAME_MISMATCH = "UltimateCreditors SchemeName does not match:" +
            ErrorConstants.PATH_ULTIMATE_CREDITOR_SCHEME_NAME;
    public static final String ULTIMATE_CREDITOR_POSTAL_ADDRESS_MISMATCH = "UltimateCreditors PostalAddress does not " +
            "match:" + ErrorConstants.PATH_ULTIMATE_CREDITOR_POSTAL_ADDRESS;

    public static final String ULTIMATE_DEBTOR_NAME_MISMATCH = "UltimateDebtors Name does not match:" +
            ErrorConstants.PATH_ULTIMATE_DEBTOR_NAME;
    public static final String ULTIMATE_DEBTOR_IDENTIFICATION_MISMATCH = "UltimateDebtors Identification does " +
            "not match:" + ErrorConstants.PATH_ULTIMATE_DEBTOR_IDENTIFICATION;
    public static final String ULTIMATE_DEBTOR_LEI_MISMATCH = "UltimateDebtors LEI does not match:" +
            ErrorConstants.PATH_ULTIMATE_DEBTOR_LEI;
    public static final String ULTIMATE_DEBTOR_SCHEME_NAME_MISMATCH = "UltimateDebtors SchemeName does not match:" +
            ErrorConstants.PATH_ULTIMATE_DEBTOR_SCHEME_NAME;
    public static final String ULTIMATE_DEBTOR_POSTAL_ADDRESS_MISMATCH = "UltimateDebtors PostalAddress does not " +
            "match:" + ErrorConstants.PATH_ULTIMATE_DEBTOR_POSTAL_ADDRESS;

    public static final String REGULATORY_REPORTING_MISMATCH = "RegulatoryReporting does not match:"
            + ErrorConstants.PATH_REGULATORY_REPORTING;
    public static final String MANDATE_IDENTIFICATION_MISMATCH = "MandateIdentification does not match.:" +
            ErrorConstants.PATH_MANDATE_IDENTIFICATION;
    public static final String CLASSIFICATION_MISMATCH = "Classification does not match.:" +
            ErrorConstants.PATH_CLASSIFICATION;
    public static final String CATEGORY_PURPOSE_CODE_MISMATCH = "CategoryPurposeCode does not match.:" +
            ErrorConstants.PATH_CATEGORY_PURPOSE_CODE;
    public static final String REASON_MISMATCH = "Reason does not match.:" +
            ErrorConstants.PATH_REASON;

    public static final String INVALID_FILE_ERROR = "Invalid file found in the file payment upload request";
    public static final String IDEMPOTENCY_CHECK_FAILED = "Idempotency check failed.";
    public static final String EXECUTOR_IDEMPOTENCY_KEY_FRAUDULENT = "Idempotency check failed.:" +
            ErrorConstants.PATH_IDEM_KEY;
    public static final String EXECUTOR_IDEMPOTENCY_KEY_ERROR = "Error while handling Idempotency check.:" +
            ErrorConstants.PATH_IDEM_KEY;
    public static final String EXECUTOR_VRP_RESPONSE_ERROR = "Error while handling VRP submission request response.";
    public static final String EXECUTOR_IDEMPOTENCY_KEY_NOT_FOUND = "Mandatory header x-idempotency key not found" +
            " in the request.:" + ErrorConstants.PATH_IDEM_KEY;
    public static final String EXPIRED_COMPLETION_DATE = "Provided Multi Authorisation completion date is expired.";
    public static final String MISSING_PSU_AUTHENTICATION_METHOD = "Mandatory Parameter PSUAuthenticationMethods " +
            "is missing";
    public static final String MISSING_VRP_TYPE = "Mandatory Parameter VRPType is missing";
    public static final String INVALID_MAXIMUM_INDIVIDUAL_AMOUNT = "Invalid value for Amount in " +
            "MaximumIndividualAmount";
    public static final String INVALID_CURRENCY = "Invalid value for Currency in MaximumIndividualAmount";
    public static final String INVALID_PERIOD_ALIGNMENT = "Invalid value for period alignment in PeriodicLimits";
    public static final String INVALID_PERIOD_TYPE = "Invalid value for period type in PeriodicLimits";
    public static final String INVALID_PSU_AUTHENTICATION_METHOD = "The given PSUAuthenticationMethod value " +
            "is not supported";
    public static final String INVALID_VRP_TYPE = "The given VRPType value is not supported";
    public static final String VRP_INITIATION_DELETE_ERROR = "Error occurred while handling the VRP initiation" +
            " delete request";
    public static final String OLDER_REFRESH_TOKEN = "The refresh token is older than the configured limit value";
    public static final String ACCOUNT_RETRIEVAL_UNSUPPORTED =
            "Attempting to retrieve account data via un-supported API version";
    public static final String ACCOUNT_ACCESS_UNSUPPORTED =
            "Attempting to access account consent via un-supported API version";
    public static final String CONSENT_DELETE_UNSUPPORTED =
            "Attempting to delete consent via un-supported API version";
    public static final String PAYMENT_ACCESS_UNSUPPORTED =
            "Attempting to access payment consent via un-supported API version";
    public static final String COF_ACCESS_UNSUPPORTED =
            "Attempting to access funds confirmation consent via un-supported API version";
    public static final String FILE_PAYMENT_ACCESS_UNSUPPORTED =
            "Attempting to access file payment consent via un-supported API version";

}
