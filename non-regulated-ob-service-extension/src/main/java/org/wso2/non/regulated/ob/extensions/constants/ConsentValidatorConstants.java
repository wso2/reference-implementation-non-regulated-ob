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

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

/**
 * Constants used exclusively by {@link org.wso2.non.regulated.ob.extensions.utils.ConsentValidatorUtil}.
 */
public class ConsentValidatorConstants {

    // Field names, permissions, and endpoint-path patterns.

    public static final String ACCOUNT_ID = "accountId";

    public static final String TRANSACTIONS = "transactions";

    public static final String TO_BOOKING_DATE_TIME = "toBookingDateTime";

    public static final String FROM_BOOKING_DATE_TIME = "fromBookingDateTime";

    public static final String READ_BALANCES = "ReadBalances";

    public static final String ACCOUNT_REGEX = "/accounts";

    public static final String BALANCES_REGEX = "/balances";

    public static final String TRANSACTIONS_REGEX = "/transactions";

    public static final String ACCOUNT_ID_REGEX = "/accounts/[^/?]*";

    public static final String BALANCES_ID_REGEX = "/accounts/[^/?]*/balances";

    public static final String TRANSACTIONS_ID_REGEX = "/accounts/[^/?]*/transactions";

    public static final String FREQUENCY = "Frequency";

    public static final String CONTEXT_CODE = "PaymentContextCode";

    public static final String MERCHANT_CATEGORY_CODE = "MerchantCategoryCode";

    public static final String MERCHANT_IDENTIFICATION = "MerchantCustomerIdentification";

    public static final String DELIVERY_ADDRESS = "DeliveryAddress";

    public static final String CHARGE_BEARER = "ChargeBearer";

    public static final String DESTINATION_COUNTRY_CODE = "DestinationCountryCode";

    public static final String UNSTRUCTURED = "Unstructured";

    public static final String STREET_NAME = "StreetName";

    public static final String BUILDING_NUMBER = "BuildingNumber";

    public static final String POST_CODE = "PostCode";

    public static final String TOWN_NAME = "TownName";

    public static final String COUNTRY = "Country";

    public static final String ADDRESS_LINE = "AddressLine";

    public static final String COUNTRY_SUB_DIVISION = "CountrySubDivision";

    public static final String ADDRESS_TYPE = "AddressType";

    public static final String DEPARTMENT = "Department";

    public static final String SUB_DEPARTMENT = "SubDepartment";

    public static final String MANDATE_IDENTIFICATION = "MandateIdentification";

    public static final String CLASSIFICATION = "Classification";

    public static final String CATEGORY_PURPOSE_CODE = "CategoryPurposeCode";

    public static final String AUTHORIZATIONS_KEY = "authorizations";

    public static final String RESOURCES = "resources";

    public static final String TYPE = "Type";

    public static final String COUNT_PER_PERIOD = "CountPerPeriod";

    public static final String POINT_IN_TIME = "PointInTime";

    public static final String LEI = "LEI";

    public static final String REASON = "Reason";

    public static final String PROXY = "Proxy";

    public static final String CODE = "Code";

    public static final String BUILDING_NAME = "BuildingName";

    public static final String FLOOR = "Floor";

    public static final String UNIT_NUMBER = "UnitNumber";

    public static final String ROOM = "Room";

    public static final String POST_BOX = "PostBox";

    public static final String TOWN_LOCATION_NAME = "TownLocationName";

    public static final String DISTRICT_NAME = "DistrictName";

    public static final String CARE_OF = "CareOf";

    public static final DateTimeFormatter BASIC_ISO_DATE_TIME = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendValue(ChronoField.YEAR, 4)
            .appendValue(ChronoField.MONTH_OF_YEAR, 2)
            .appendValue(ChronoField.DAY_OF_MONTH, 2)
            .appendLiteral('T')
            .appendValue(ChronoField.HOUR_OF_DAY, 2)
            .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
            .optionalStart().appendValue(ChronoField.SECOND_OF_MINUTE, 2).optionalEnd()
            .optionalStart()
            .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true).optionalEnd()
            .optionalStart().appendZoneId().optionalEnd()
            .optionalStart().appendOffset("+HHmm", "+0000").optionalEnd()
            .optionalStart().appendOffset("+HH:mm", "+00:00").optionalEnd()
            .optionalStart().appendOffset("+HH", "+00").optionalEnd()
            .toFormatter();

    public static final DateTimeFormatter EXTENDED_ISO_DATE_TIME = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            .optionalStart().appendZoneId().optionalEnd()
            .optionalStart().appendOffset("+HHmm", "+0000").optionalEnd()
            .optionalStart().appendOffset("+HH:mm", "+00:00").optionalEnd()
            .optionalStart().appendOffset("+HH", "+00").optionalEnd()
            .optionalStart()
            .appendLiteral('[')
            .parseCaseSensitive()
            .appendZoneRegionId()
            .appendLiteral(']')
            .toFormatter();

    // Internal JSON-path fragments used only to build the messages above.

    private static final String PATH_INSTRUCTED_AMOUNT = "Data.Initiation.InstructedAmount";

    private static final String PATH_EXECUTION_DATE = "Data.Initiation.RequestedExecutionDateTime";

    private static final String PATH_FIRST_PAYMENT_DATE_TIME = "Data.Initiation.FirstPaymentDateTime";

    private static final String PATH_RISK_ADDRESS = "Risk.DeliveryAddress";

    private static final String PATH_QUERY_PARAM = "Url.QueryParameters";

    private static final String PATH_DEBTOR_ACCOUNT_SCHEME = "Data.Initiation.DebtorAccount.SchemeName";

    private static final String PATH_DEBTOR_ACCOUNT_SECOND_IDENTIFICATION =
            "Data.Initiation.DebtorAccount.SecondaryIdentification";

    private static final String PATH_DEBTOR_ACCOUNT_IDENTIFICATION = "Data.Initiation.DebtorAccount.Identification";

    private static final String PATH_DEBTOR_ACCOUNT_NAME = "Data.Initiation.DebtorAccount.Name";

    private static final String PATH_REMITTANCE_INFO_REFERENCE = "Data.Initiation.RemittanceInformation.Reference";

    private static final String PATH_REMITTANCE_INFO_UNSTRUCTURED =
            "Data.Initiation.RemittanceInformation.Unstructured";

    private static final String PATH_INSTRUCTION_IDENTIFICATION = "Data.Initiation.InstructionIdentification";

    private static final String PATH_ENDTOEND_IDENTIFICATION = "Data.Initiation.EndToEndIdentification";

    private static final String PATH_INSTRUCTED_AMOUNT_AMOUNT = "Data.Initiation.InstructedAmount.Amount";

    private static final String PATH_INSTRUCTED_AMOUNT_CURRENCY = "Data.Initiation.InstructedAmount.Currency";

    private static final String PATH_FREQUENCY_API_V4 = "Data.Initiation.MandateRelatedInformation.Frequency";

    private static final String PATH_FREQUENCY = "Data.Initiation.Frequency";

    private static final String PATH_FIRST_PAYMENT_DATE_TIME_API_V4 =
            "Data.Initiation.MandateRelatedInformation.FirstPaymentDateTime";

    private static final String PATH_CURRENCY_OF_TRANSFER = "Data.Initiation.CurrencyOfTransfer";

    private static final String PATH_CHARGE_BEARER = "Data.Initiation.ChargeBearer";

    private static final String PATH_RISK_PAYMENT_CONTEXT = "Risk.PaymentContextCode";

    private static final String PATH_RISK_MERCHANT_CATEGORY = "Risk.MerchantCategoryCode";

    private static final String PATH_RISK_MERCHANT_CUSTOMER = "Risk.MerchantCustomerIdentification";

    private static final String PATH_RISK_ADDRESS_STREET_NAME = "Risk.DeliveryAddress.StreetName";

    private static final String PATH_RISK_ADDRESS_BUILDING_NO = "Risk.DeliveryAddress.BuildingNumber";

    private static final String PATH_RISK_ADDRESS_POST_CODE = "Risk.DeliveryAddress.PostCode";

    private static final String PATH_RISK_ADDRESS_TOWN_NAME = "Risk.DeliveryAddress.TownName";

    private static final String PATH_RISK_ADDRESS_COUNTRY_SUB_DIVISION = "Risk.DeliveryAddress.CountrySubDivision";

    private static final String PATH_RISK_ADDRESS_LINE = "Risk.DeliveryAddress.AddressLine";

    private static final String PATH_RISK_COUNTRY = "Risk.DeliveryAddress.Country";

    private static final String PATH_MANDATE_IDENTIFICATION =
            "Data.Initiation.MandateRelatedInformation.MandateIdentification";

    private static final String PATH_CATEGORY_PURPOSE_CODE =
            "Data.Initiation.MandateRelatedInformation.CategoryPurposeCode";

    private static final String PATH_CLASSIFICATION = "Data.Initiation.MandateRelatedInformation.Classification";

    private static final String PATH_REASON = "Data.Initiation.MandateRelatedInformation.Reason";

    private static final String PATH_CREDIT_ACCOUNT_SCHEME = "Data.Initiation.CreditorAccount.SchemeName";

    private static final String PATH_DESTINATION_COUNTRY_CODE = "Data.Initiation.DestinationCountryCode";

    private static final String PATH_DEBTOR_ACCOUNT_PROXY_IDENTIFICATION = "Data.Initiation.DebtorAccount.Proxy" +
            ".Identification";

    private static final String PATH_DEBTOR_ACCOUNT_PROXY_CODE = "Data.Initiation.DebtorAccount.Proxy.Code";

    private static final String PATH_DEBTOR_ACCOUNT_PROXY_TYPE = "Data.Initiation.DebtorAccount.Proxy.Type";

    private static final String PATH_DEBTOR_ACCOUNT_PROXY = "Data.Initiation.DebtorAccount.Proxy";

    private static final String PATH_CREDIT_ACCOUNT_NAME = "Data.Initiation.CreditorAccount.Name";

    private static final String PATH_CREDIT_ACCOUNT_PROXY_IDENTIFICATION = "Data.Initiation.CreditorAccount.Proxy" +
            ".Identification";

    private static final String PATH_CREDIT_ACCOUNT_PROXY_CODE = "Data.Initiation.CreditorAccount.Proxy.Code";

    private static final String PATH_CREDIT_ACCOUNT_PROXY_TYPE = "Data.Initiation.CreditorAccount.Proxy.Type";

    private static final String PATH_CREDIT_ACCOUNT_IDENTIFICATION = "Data.Initiation.CreditorAccount.Identification";

    private static final String PATH_CREDIT_ACCOUNT_SEC_IDENTIFICATION = "Data.Initiation.CreditorAccount" +
            ".SecondaryIdentification";

    private static final String PATH_CREDIT_ACCOUNT_PROXY = "Data.Initiation.CreditorAccount.Proxy";

    private static final String PATH_ULTIMATE_CREDITOR_SCHEME_NAME = "Data.Initiation.UltimateCreditor.SchemeName";

    private static final String PATH_ULTIMATE_CREDITOR_IDENTIFICATION =
            "Data.Initiation.UltimateCreditor.Identification";

    private static final String PATH_ULTIMATE_CREDITOR_NAME = "Data.Initiation.UltimateCreditor.Name";

    private static final String PATH_ULTIMATE_CREDITOR_LEI = "Data.Initiation.UltimateCreditor.LEI";

    private static final String PATH_ULTIMATE_CREDITOR_POSTAL_ADDRESS =
            "Data.Initiation.UltimateCreditor.PostalAddress";

    private static final String PATH_ULTIMATE_DEBTOR_SCHEME_NAME = "Data.Initiation.UltimateDebtor.SchemeName";

    private static final String PATH_ULTIMATE_DEBTOR_IDENTIFICATION =
            "Data.Initiation.UltimateDebtor.Identification";

    private static final String PATH_ULTIMATE_DEBTOR_NAME = "Data.Initiation.UltimateDebtor.Name";

    private static final String PATH_ULTIMATE_DEBTOR_LEI = "Data.Initiation.UltimateDebtor.LEI";

    private static final String PATH_ULTIMATE_DEBTOR_POSTAL_ADDRESS = "Data.Initiation.UltimateDebtor.PostalAddress";

    // Validation error messages.

    public static final String FIELD_INVALID_DATE = "UK.OBIE.Field.InvalidDate";

    public static final String WRONG_DATE_FORMAT_QUERY = "Wrongly formatted date. Use valid ISO 8601 date/date-time " +
            "format. :" + PATH_QUERY_PARAM;

    public static final String DEBTOR_ACC_SCHEME_NAME_MISMATCH = "Debtor Account Scheme name does not " +
            "match:" + PATH_DEBTOR_ACCOUNT_SCHEME;

    public static final String DEBTOR_ACC_SCHEME_NAME_NOT_FOUND = "Debtor Account Scheme name isn't present in the " +
            "request or in the consent:" + PATH_DEBTOR_ACCOUNT_SCHEME;

    public static final String DEBTOR_ACC_IDENTIFICATION_MISMATCH = "Debtor Account Identification does " +
            "not match:" + PATH_DEBTOR_ACCOUNT_IDENTIFICATION;

    public static final String DEBTOR_ACC_IDENTIFICATION_NOT_FOUND = "Debtor Account Identification isn't present " +
            "in the request or in the consent:" + PATH_DEBTOR_ACCOUNT_IDENTIFICATION;

    public static final String DEBTOR_ACC_NAME_MISMATCH = "Debtor Account Name does not match:" +
            PATH_DEBTOR_ACCOUNT_NAME;

    public static final String DEBTOR_ACC_SEC_IDENTIFICATION_MISMATCH = "Debtor Account Secondary Identification" +
            " does not match:" + PATH_DEBTOR_ACCOUNT_SECOND_IDENTIFICATION;

    public static final String REMMITANCE_REFERENCE_NOT_FOUND = "Remittance Information Reference isn't present in " +
            "the request or in the consent:" + PATH_REMITTANCE_INFO_REFERENCE;

    public static final String REMMITANCE_REFERENCE_MISMATCH = "Remittance Information Reference does not match:" +
            PATH_REMITTANCE_INFO_REFERENCE;

    public static final String REMMITANCE_UNSTRUCTURED_NOT_FOUND = "Remittance Information Unstructured isn't present" +
            " in the request or in the consent:" + PATH_REMITTANCE_INFO_UNSTRUCTURED;

    public static final String REMMITANCE_UNSTRUCTURED_MISMATCH = "Remittance Information Unstructured does not " +
            "match:" + PATH_REMITTANCE_INFO_UNSTRUCTURED;

    public static final String REQUESTED_EXECUTION_DATE_MISMATCH = "Requested Execution Date Time does not match.:" +
            PATH_EXECUTION_DATE;

    public static final String INSTRUCTION_IDENTIFICATION_MISMATCH = "Instruction Identification does not match:"
            + PATH_INSTRUCTION_IDENTIFICATION;

    public static final String INSTRUCTION_IDENTIFICATION_NOT_FOUND = "Instruction Identification isn't present in " +
            "the request:" + PATH_INSTRUCTION_IDENTIFICATION;

    public static final String END_TO_END_IDENTIFICATION_NOT_FOUND = "End to End Identification isn't present in " +
            "the request or in the consent:" + PATH_ENDTOEND_IDENTIFICATION;

    public static final String END_TO_END_IDENTIFICATION_MISMATCH = "End to End Identification does not match:"
            + PATH_ENDTOEND_IDENTIFICATION;

    public static final String INSTRUCTED_AMOUNT_CURRENCY_NOT_FOUND = "Instructed Amount Currency isn't present in " +
            "the payload:" + PATH_INSTRUCTED_AMOUNT;

    public static final String INSTRUCTED_AMOUNT_AMOUNT_MISMATCH = "Instructed Amount Amount does not match the " +
            "initiated amount:" + PATH_INSTRUCTED_AMOUNT_AMOUNT;

    public static final String INSTRUCTED_AMOUNT_AMOUNT_NOT_FOUND = "Instructed Amount  Amount isn't present in the " +
            "request:" + PATH_INSTRUCTED_AMOUNT;

    public static final String INSTRUCTED_AMOUNT_CURRENCY_MISMATCH = "Instructed Amount currency does not match the " +
            "initiated amount or currency:" + PATH_INSTRUCTED_AMOUNT_CURRENCY;

    public static final String INSTRUCTED_AMOUNT_NOT_FOUND = "Instructed Amount isn't present in the request:" +
            PATH_INSTRUCTED_AMOUNT;

    public static final String REQUESTED_EXECUTION_DATE_NOT_FOUND = "Requested Execution Date Time isn't present in" +
            " the request or in the consent.:" + PATH_EXECUTION_DATE;

    public static final String FREQUENCY_MISMATCH_API_V4 = "Frequency does not match.:" +
            PATH_FREQUENCY_API_V4;

    public static final String FREQUENCY_NOT_FOUND_API_V4 = "Frequency isn't present in the request or in the consent.:"
            + PATH_FREQUENCY_API_V4;

    public static final String FREQUENCY_MISMATCH = "Frequency does not match.:" + PATH_FREQUENCY;

    public static final String FREQUENCY_NOT_FOUND = "Frequency isn't present in the request or in the consent.:"
            + PATH_FREQUENCY;

    public static final String FIRST_PAYMENT_DATE_MISMATCH = "First Payment Date Time does not match.:" +
            PATH_FIRST_PAYMENT_DATE_TIME;

    public static final String FIRST_PAYMENT_DATE_MISMATCH_API_V4 = "First Payment Date Time does not match.:" +
            PATH_FIRST_PAYMENT_DATE_TIME_API_V4;

    public static final String FIRST_PAYMENT_DATE_NOT_FOUND = "First Payment Date Time does not found in the request" +
            " or in the consent.:" + PATH_FIRST_PAYMENT_DATE_TIME;

    public static final String CURRENCY_TRANSFER_MISMATCH = "Currency Of Transfer does not match.:" +
            PATH_CURRENCY_OF_TRANSFER;

    public static final String CURRENCY_TRANSFER_NOT_FOUND = "Currency Of Transfer isn't present in the request or" +
            " in the consent.:" + PATH_CURRENCY_OF_TRANSFER;

    public static final String CHARGE_BEARER_MISMATCH = "Charge Bearer does not match.:" +
            PATH_CHARGE_BEARER;

    public static final String CHARGE_BEARER_NOT_FOUND = "Charge Bearer isn't present in the request or" +
            " in the consent.:" + PATH_CHARGE_BEARER;

    public static final String DESTINATION_COUNTRY_CODE_MISMATCH = "Charge Bearer does not match.:" +
            PATH_DESTINATION_COUNTRY_CODE;

    public static final String DESTINATION_COUNTRY_CODE_NOT_FOUND = "Charge Bearer isn't present in the request or" +
            " in the consent.:" + PATH_DESTINATION_COUNTRY_CODE;

    public static final String PAYMENT_CONTEXT_CODE_MISMATCH = "Risk Payment Context Code does not match:" +
            PATH_RISK_PAYMENT_CONTEXT;

    public static final String MERCHANT_CATEGORY_CODE_MISMATCH = "Merchant Category Code does not match:" +
            PATH_RISK_MERCHANT_CATEGORY;

    public static final String MERCHANT_CUSTOMER_IDENTIFICATION_MISMATCH = "Risk Merchant Customer Identification" +
            " does not match:" + PATH_RISK_MERCHANT_CUSTOMER;

    public static final String DELIVERY_ADDRESS_MISMATCH = "Risk Delivery Addresses isn't present in the " +
            "request or in the consent:" + PATH_RISK_ADDRESS;

    public static final String STREET_NAME_MISMATCH = "Risk Street Name does not match:" +
            PATH_RISK_ADDRESS_STREET_NAME;

    public static final String BUILDING_NUMBER_MISMATCH = "Risk Building Number does not match:" +
            PATH_RISK_ADDRESS_BUILDING_NO;

    public static final String POST_CODE_MISMATCH = "Risk Post Code does not match:" +
            PATH_RISK_ADDRESS_POST_CODE;

    public static final String TOWN_NAME_MISMATCH = "Risk Town Name does not match:" +
            PATH_RISK_ADDRESS_TOWN_NAME;

    public static final String COUNTRY_MISMATCH = "Risk Country does not match:" +
            PATH_RISK_COUNTRY;

    public static final String ADDRESS_LINE_NOT_FOUND = "Risk Address Lines aren't present in the request or consent:" +
            PATH_RISK_ADDRESS_LINE;

    public static final String ADDRESS_LINE_MISMATCH = "Risk Address Line does not match:" +
            PATH_RISK_ADDRESS_LINE;

    public static final String COUNTRY_SUB_DIVISION_NOT_FOUND = "Risk Country Sub Division isn't present either in" +
            " the request or consent:" + PATH_RISK_ADDRESS_COUNTRY_SUB_DIVISION;

    public static final String COUNTRY_SUB_DIVISION_MISMATCH = "Country Sub Division does not match:" +
            PATH_RISK_ADDRESS_COUNTRY_SUB_DIVISION;

    public static final String MANDATE_IDENTIFICATION_MISMATCH = "MandateIdentification does not match.:" +
            PATH_MANDATE_IDENTIFICATION;

    public static final String CATEGORY_PURPOSE_CODE_MISMATCH = "CategoryPurposeCode does not match.:" +
            PATH_CATEGORY_PURPOSE_CODE;

    public static final String CLASSIFICATION_MISMATCH = "Classification does not match.:" +
            PATH_CLASSIFICATION;

    public static final String REASON_MISMATCH = "Reason does not match.:" +
            PATH_REASON;

    public static final String CREDITOR_ACC_SCHEME_NAME_MISMATCH = "Creditor Accounts Scheme does not match:" +
            PATH_CREDIT_ACCOUNT_SCHEME;

    public static final String CREDITOR_ACC_SCHEME_NAME_NOT_FOUND = "Creditor Accounts Scheme isn't present in the" +
            " request or in the consent.:" + PATH_CREDIT_ACCOUNT_SCHEME;

    public static final String CREDITOR_ACC_PROXY_IDENTIFICATION_MISMATCH = "Creditor Accounts Proxy Identification " +
            "does not match:" + PATH_CREDIT_ACCOUNT_PROXY_IDENTIFICATION;

    public static final String CREDITOR_ACC_PROXY_IDENTIFICATION_NOT_FOUND = "Creditor Accounts Proxy Identification " +
            "isn't present in the request or in the consent.:"
            + PATH_CREDIT_ACCOUNT_PROXY_IDENTIFICATION;

    public static final String CREDITOR_ACC_PROXY_CODE_MISMATCH = "Creditor Accounts Proxy Code " +
            "does not match:" + PATH_CREDIT_ACCOUNT_PROXY_CODE;

    public static final String CREDITOR_ACC_PROXY_CODE_NOT_FOUND = "Creditor Accounts Proxy Code " +
            "isn't present in the request or in the consent.:"
            + PATH_CREDIT_ACCOUNT_PROXY_CODE;

    public static final String CREDITOR_ACC_PROXY_TYPE_MISMATCH = "Creditor Accounts Proxy Type " +
            "does not match:" + PATH_CREDIT_ACCOUNT_PROXY_TYPE;

    public static final String CREDITOR_ACC_IDENTIFICATION_MISMATCH = "Creditor Account Identification does not match:"
            + PATH_CREDIT_ACCOUNT_IDENTIFICATION;

    public static final String CREDITOR_ACC_IDENTIFICATION_NOT_FOUND = "Creditor Account Identification isn't " +
            "present in the request or in the consent.:" + PATH_CREDIT_ACCOUNT_IDENTIFICATION;

    public static final String CREDITOR_ACC_NAME_MISMATCH = "Creditor Account Name does not match:" +
            PATH_CREDIT_ACCOUNT_NAME;

    public static final String CREDITOR_ACC_SEC_IDENTIFICATION_MISMATCH = "Creditor Account Secondary Identification" +
            " does not match:" + PATH_CREDIT_ACCOUNT_SEC_IDENTIFICATION;

    public static final String CREDITOR_ACC_PROXY_MISMATCH = "Creditor Account Proxy does not match:"
            + PATH_CREDIT_ACCOUNT_PROXY;

    public static final String DEBTOR_ACC_PROXY_MISMATCH = "Debtor Account Proxy does not match:"
            + PATH_DEBTOR_ACCOUNT_PROXY;

    public static final String DEBTOR_ACC_PROXY_IDENTIFICATION_MISMATCH = "Debtor Accounts Proxy Identification " +
            "does not match:" + PATH_DEBTOR_ACCOUNT_PROXY_IDENTIFICATION;

    public static final String DEBTOR_ACC_PROXY_IDENTIFICATION_NOT_FOUND = "Debtor Accounts Proxy Identification " +
            "isn't present in the request or in the consent.:"
            + PATH_DEBTOR_ACCOUNT_PROXY_IDENTIFICATION;

    public static final String DEBTOR_ACC_PROXY_CODE_MISMATCH = "Debtor Accounts Proxy Code " +
            "does not match:" + PATH_DEBTOR_ACCOUNT_PROXY_CODE;

    public static final String DEBTOR_ACC_PROXY_CODE_NOT_FOUND = "Debtor Accounts Proxy Code " +
            "isn't present in the request or in the consent.:"
            + PATH_DEBTOR_ACCOUNT_PROXY_CODE;

    public static final String DEBTOR_ACC_PROXY_TYPE_MISMATCH = "Debtor Accounts Proxy Type " +
            "does not match:" + PATH_DEBTOR_ACCOUNT_PROXY_TYPE;

    public static final String ULTIMATE_CREDITOR_NAME_MISMATCH = "UltimateCreditors Name does not match:" +
            PATH_ULTIMATE_CREDITOR_NAME;

    public static final String ULTIMATE_CREDITOR_IDENTIFICATION_MISMATCH = "UltimateCreditors Identification does " +
            "not match:" + PATH_ULTIMATE_CREDITOR_IDENTIFICATION;

    public static final String ULTIMATE_CREDITOR_LEI_MISMATCH = "UltimateCreditors LEI does not match:" +
            PATH_ULTIMATE_CREDITOR_LEI;

    public static final String ULTIMATE_CREDITOR_SCHEME_NAME_MISMATCH = "UltimateCreditors SchemeName does not match:" +
            PATH_ULTIMATE_CREDITOR_SCHEME_NAME;

    public static final String ULTIMATE_CREDITOR_POSTAL_ADDRESS_MISMATCH = "UltimateCreditors PostalAddress does not " +
            "match:" + PATH_ULTIMATE_CREDITOR_POSTAL_ADDRESS;

    public static final String ULTIMATE_DEBTOR_NAME_MISMATCH = "UltimateDebtors Name does not match:" +
            PATH_ULTIMATE_DEBTOR_NAME;

    public static final String ULTIMATE_DEBTOR_IDENTIFICATION_MISMATCH = "UltimateDebtors Identification does " +
            "not match:" + PATH_ULTIMATE_DEBTOR_IDENTIFICATION;

    public static final String ULTIMATE_DEBTOR_LEI_MISMATCH = "UltimateDebtors LEI does not match:" +
            PATH_ULTIMATE_DEBTOR_LEI;

    public static final String ULTIMATE_DEBTOR_SCHEME_NAME_MISMATCH = "UltimateDebtors SchemeName does not match:" +
            PATH_ULTIMATE_DEBTOR_SCHEME_NAME;

    public static final String ULTIMATE_DEBTOR_POSTAL_ADDRESS_MISMATCH = "UltimateDebtors PostalAddress does not " +
            "match:" + PATH_ULTIMATE_DEBTOR_POSTAL_ADDRESS;

}
