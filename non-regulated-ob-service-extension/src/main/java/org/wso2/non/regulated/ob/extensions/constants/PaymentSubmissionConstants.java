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

import org.wso2.non.regulated.ob.extensions.validator.payload.PaymentSubmissionPayloadValidator;

/**
 * Constants used exclusively by
 * {@link PaymentSubmissionPayloadValidator}.
 */
public class PaymentSubmissionConstants {

    // Field names.

    public static final String CREDITOR_AGENT = "CreditorAgent";

    public static final String EXCHANGE_RATE_INFO = "ExchangeRateInformation";

    public static final String RATE_TYPE = "RateType";

    public static final String UNIT_CURRENCY = "UnitCurrency";

    public static final String CONTRACT_IDENTIFICATION = "ContractIdentification";

    public static final String EXCHANGE_RATE = "ExchangeRate";

    public static final String LOCAL_INSTRUMENT = "LocalInstrument";

    public static final String PURPOSE = "Purpose";

    public static final String REMITTANCE_INFO = "RemittanceInformation";

    public static final String INSTRUCTION_PRIORITY = "InstructionPriority";

    public static final String EXTENDED_PURPOSE = "ExtendedPurpose";

    public static final String CREDITOR = "Creditor";

    public static final String ULTIMATE_CREDITOR = "UltimateCreditor";

    public static final String ULTIMATE_DEBTOR = "UltimateDebtor";

    public static final String REGULATORY_REPORTING = "RegulatoryReporting";

    // Internal JSON-path fragments used only to build the messages above.

    private static final String PATH_DEBTOR_ACCOUNT = "Data.Initiation.DebtorAccount";

    private static final String PATH_CREDIT_AGENT = "Data.Initiation.CreditorAgent";

    private static final String PATH_FINAL_PAYMENT_DATE_TIME = "Data.Initiation.FinalPaymentDateTime";

    private static final String PATH_RECURRING_PAYMENT_DATE_TIME = "Data.Initiation.RecurringPaymentDateTime";

    private static final String PATH_FIRST_PAYMENT_AMOUNT = "Data.Initiation.FirstPaymentAmount";

    private static final String PATH_FIRST_PAYMENT_AMOUNT_AMOUNT = "Data.Initiation.FirstPaymentAmount.Amount";

    private static final String PATH_RECURRING_PAYMENT_AMOUNT = "Data.Initiation.RecurringPaymentAmount";

    private static final String PATH_RECURRING_PAYMENT_AMOUNT_AMOUNT = "Data.Initiation.RecurringPaymentAmount.Amount";

    private static final String PATH_EXCHANGE_RATE_INFO = "Data.Initiation.ExchangeRateInformation";

    private static final String PATH_LOCAL_INSTRUMENT = "Data.Initiation.LocalInstrument";

    private static final String PATH_REMITTANCE_INFO = "Data.Initiation.RemittanceInformation";

    private static final String PATH_CREDIT_ACCOUNT = "Data.Initiation.CreditorAccount";

    private static final String PATH_CREDIT_AGENT_NAME = "Data.Initiation.CreditorAgent.SchemeName";

    private static final String PATH_CREDIT_AGENT_IDENTIFICATION = "Data.Initiation.CreditorAgent.Identification";

    private static final String PATH_REFERENCE = "Data.Initiation.Reference";

    private static final String PATH_INSTRUCTION_PRIORITY = "Data.Initiation.InstructionPriority";

    private static final String PATH_EXTENDED_PURPOSE = "Data.Initiation.ExtendedPurpose";

    private static final String PATH_PURPOSE = "Data.Initiation.Purpose";

    private static final String PATH_FIRST_PAYMENT_CURRENCY = "Data.Initiation.FirstPaymentAmount.Currency";

    private static final String PATH_FINAL_PAYMENT_AMOUNT = "Data.Initiation.FinalPaymentAmount";

    private static final String PATH_FINAL_PAYMENT_CURRENCY = "Data.Initiation.FinalPaymentAmount.Currency";

    private static final String PATH_RECURRING_PAYMENT_CURRENCY = "Data.Initiation.RecurringPaymentAmount.Currency";

    private static final String PATH_EXCHANGE_RATE_UNIT_CURRENCY =
            "Data.Initiation.ExchangeRateInformation.UnitCurrency";

    private static final String PATH_EXCHANGE_RATE = "Data.Initiation.ExchangeRateInformation.ExchangeRate";

    private static final String PATH_EXCHANGE_RATE_TYPE = "Data.Initiation.ExchangeRateInformation.RateType";

    private static final String PATH_EXCHANGE_RATE_IDENTIFICATION =
            "Data.Initiation.ExchangeRateInformation.ContractIdentification";

    private static final String PATH_CREDITOR = "Data.Initiation.Creditor";

    private static final String PATH_CREDITOR_NAME = "Data.Initiation.Creditor.Name";

    private static final String PATH_CREDITOR_ADRESS = "Data.Initiation.Creditor.DeliveryAddress";

    private static final String PATH_ULTIMATE_CREDITOR = "Data.Initiation.UltimateCreditor";

    private static final String PATH_ULTIMATE_DEBTOR = "Data.Initiation.UltimateDebtor";

    private static final String PATH_REGULATORY_REPORTING = "Data.Initiation.RegulatoryReporting";

    // Validation error messages.

    public static final String LOCAL_INSTRUMENT_MISMATCH = "Local Instrument does not match:" +
            PATH_LOCAL_INSTRUMENT;

    public static final String LOCAL_INSTRUMENT_NOT_FOUND = "Local Instrument isn't present in the " +
            "request or in the consent:" + PATH_LOCAL_INSTRUMENT;

    public static final String REFERENCE_NOT_FOUND = "Reference isn't present in the " +
            "request or in the consent:" + PATH_LOCAL_INSTRUMENT;

    public static final String DEBTOR_ACC_MISMATCH = "Debtor Account isn't present in the request " +
            "or in the consent:" + PATH_DEBTOR_ACCOUNT;

    public static final String CREDITOR_ACC_NOT_FOUND = "Creditor Account isn't present in the request.:" +
            PATH_CREDIT_ACCOUNT;

    public static final String FIRST_PAYMENT_AMOUNT_MISMATCH = "First Payment Amount does not match:" +
            PATH_FIRST_PAYMENT_AMOUNT_AMOUNT;

    public static final String CREDITOR_AGENT_MISMATCH = "Creditor Agent isn't present in the request or in the " +
            "consent:" + PATH_CREDIT_AGENT;

    public static final String CREDITOR_AGENT_SCHEME_NAME_MISMATCH = "Creditor Agent Scheme name does not match:" +
            PATH_CREDIT_AGENT_NAME;

    public static final String CREDITOR_AGENT_IDENTIFICATION_MISMATCH = "Creditor Agent Identification does not match:"
            + PATH_CREDIT_AGENT_IDENTIFICATION;

    public static final String REMMITANCE_INFO_MISMATCH = "Remittance Information isn't present in the " +
            "request or in the consent:" + PATH_REMITTANCE_INFO;

    public static final String REFERENCE_MISMATCH = "Reference does not match.:" + PATH_REFERENCE;

    public static final String FINAL_PAYMENT_DATE_MISMATCH = "Final Payment Date Time does not match:" +
            PATH_FINAL_PAYMENT_DATE_TIME;

    public static final String RECURRING_PAYMENT_DATE_MISMATCH = "Recurring Payment Date Time does not match:" +
            PATH_RECURRING_PAYMENT_DATE_TIME;

    public static final String INSTRUCTION_PRIORITY_MISSING = "Instruction Priority isn't present in the request or" +
            " in the consent:" + PATH_INSTRUCTION_PRIORITY;

    public static final String INSTRUCTION_PRIORITY_MISMATCH = "Instruction Priority does not match:" +
            PATH_INSTRUCTION_PRIORITY;

    public static final String PURPOSE_MISSING = "Purpose isn't present in the request or" +
            " in the consent:" + PATH_PURPOSE;

    public static final String PURPOSE_MISMATCH = "Purpose does not match:" + PATH_PURPOSE;

    public static final String EXTENDED_PURPOSE_MISSING = "Extended Purpose isn't present in the request or" +
            " in the consent:" + PATH_EXTENDED_PURPOSE;

    public static final String EXTENDED_PURPOSE_MISMATCH = "Extended Purpose does not match:" +
            PATH_EXTENDED_PURPOSE;

    public static final String FIRST_PAYMENT_AMOUNT_NOT_FOUND = "First Payment Amount isn't present in the " +
            "request or in the consent:" + PATH_FIRST_PAYMENT_AMOUNT;

    public static final String FIRST_PAYMENT_CURRENCY_MISMATCH = "First Payment Currency does not match:" +
            PATH_FIRST_PAYMENT_CURRENCY;

    public static final String FINAL_PAYMENT_AMOUNT_NOT_FOUND = "Final Payment Amount isn't present in the" +
            " request or in the consent:" + PATH_FINAL_PAYMENT_AMOUNT;

    public static final String FINAL_PAYMENT_AMOUNT_MISMATCH = "Final Payment Amount does not match:" +
            PATH_FINAL_PAYMENT_AMOUNT;

    public static final String FINAL_PAYMENT_CURRENCY_MISMATCH = "Final Payment Currency does not match:" +
            PATH_FINAL_PAYMENT_CURRENCY;

    public static final String RECURRING_PAYMENT_AMOUNT_NOT_FOUND = "Recurring Payment Amount isn't present in the" +
            " request or in the consent:" + PATH_RECURRING_PAYMENT_AMOUNT;

    public static final String RECURRING_PAYMENT_AMOUNT_MISMATCH = "Recurring Payment Amount does not match:" +
            PATH_RECURRING_PAYMENT_AMOUNT_AMOUNT;

    public static final String RECURRING_PAYMENT_CURRENCY_MISMATCH = "Recurring Payment Currency does not match:" +
            PATH_RECURRING_PAYMENT_CURRENCY;

    public static final String EXCHANGE_RATE_INFO_MISMATCH = "Exchange Rate Information isn't present in " +
            "the request or in the consent:" + PATH_EXCHANGE_RATE_INFO;

    public static final String EXCHNAGE_RATE_UNIT_CURRENCY_MISMATCH = "Exchange Rate Unit Currency does not match:" +
            PATH_EXCHANGE_RATE_UNIT_CURRENCY;

    public static final String EXCHANGE_RATE_UNIT_CURRENCY_NOT_FOUND = "Exchange Rate Unit Currency isn't present in " +
            "the request or in the consent:" + PATH_EXCHANGE_RATE_UNIT_CURRENCY;

    public static final String  EXCHANGE_RATE_NOT_FOUND = "Exchange Rate isn't present in the request or in the" +
            " consent:" + PATH_EXCHANGE_RATE;

    public static final String  EXCHANGE_RATE_TYPE_NOT_FOUND = "Exchange Rate Type isn't present in the request or" +
            " in the consent:" + PATH_EXCHANGE_RATE_TYPE;

    public static final String EXCHANGE_RATE_TYPE_MISMATCH = "Exchange Rate Type does not match:" +
            PATH_EXCHANGE_RATE_TYPE;

    public static final String EXCHANGE_RATE_MISMATCH = "Exchange Rate does not match:" +
            PATH_EXCHANGE_RATE;

    public static final String  CONTRACT_IDENTIFICATION_NOT_FOUND = "Exchange Rate Contract Identification isn't " +
            "present in the request or in the consent:" + PATH_EXCHANGE_RATE_IDENTIFICATION;

    public static final String CONTRACT_IDENTIFICATION_MISMATCH = "Exchange Rate Contract Identification" +
            " does not match:" + PATH_EXCHANGE_RATE_IDENTIFICATION;

    public static final String CREDITOR_MISMATCH = "Creditor aren't present in the request or in the consent:" +
            PATH_CREDITOR;

    public static final String CREDITOR_NAME_MISMATCH = "Creditor Name is not matching:" +
            PATH_CREDITOR_NAME;

    public static final String POSTAL_ADDRESS_MISMATCH = "Postal Address does not match:" +
            PATH_CREDITOR_ADRESS;

    public static final String ULTIMATE_CREDITOR_MISMATCH = "UltimateCreditor does not match:" +
            PATH_ULTIMATE_CREDITOR;

    public static final String ULTIMATE_DEBTOR_MISMATCH = "UltimateDebtor does not match:" +
            PATH_ULTIMATE_DEBTOR;

    public static final String REGULATORY_REPORTING_MISMATCH = "RegulatoryReporting does not match:"
            + PATH_REGULATORY_REPORTING;

}
