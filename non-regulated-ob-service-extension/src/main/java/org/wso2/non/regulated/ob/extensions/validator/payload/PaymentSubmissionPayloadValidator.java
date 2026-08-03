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

package org.wso2.non.regulated.ob.extensions.validator.payload;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.wso2.non.regulated.ob.extensions.constants.CommonConstants;
import org.wso2.non.regulated.ob.extensions.constants.ErrorConstants;
import org.wso2.non.regulated.ob.extensions.constants.PaymentSubmissionConstants;
import org.wso2.non.regulated.ob.extensions.utils.CommonUtil;
import org.wso2.non.regulated.ob.extensions.utils.ConsentValidatorUtil;

/**
 * Payment Submission Payload Validator.
 */
public class PaymentSubmissionPayloadValidator {

    private static final Log log = LogFactory.getLog(PaymentSubmissionPayloadValidator.class);

    /**
     * Returns a mismatch error if exactly one of the submission/consent initiation objects has the given
     * field (a JSON Schema can't express "present in the submission iff it was present in the consent"), or
     * null if both/neither have it. This one check repeats for ~15 different optional fields below.
     */
    private static JSONObject validateFieldPresenceMatch(JSONObject submissionInitiation,
                                                          JSONObject consentInitiation, String field,
                                                          String mismatchErrorConstant) {
        if (submissionInitiation.has(field) != consentInitiation.has(field)) {
            return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                    ErrorConstants.RESOURCE_CONSENT_MISMATCH, mismatchErrorConstant);
        }
        return null;
    }

    /**
     * Method to validate payment submission initiation payload.
     *
     * @param submissionInitiation        Submission Request
     * @param consentInitiation        Initiation Request
     * @param invokedAPIVersion
     * @return
     */
    public JSONObject validateInitiation(String requestPath, JSONObject submissionInitiation,
                                     JSONObject consentInitiation, CommonConstants.UKApiVersion invokedAPIVersion,
                                     String requestId) {

        JSONObject validationResponse = null;

        if (submissionInitiation != null && consentInitiation != null) {

            //Validate Instruction Identification
            validationResponse = ConsentValidatorUtil
                    .validateInstructionIdentification(requestPath, submissionInitiation, consentInitiation, requestId);

            if (validationResponse != null && validationResponse.toString()
                    .contains(CommonConstants.ERROR)) {
                return validationResponse;
            }

            //Validate End to End Identification
            validationResponse = ConsentValidatorUtil
                    .validateEndToEndIdentification(requestPath, submissionInitiation, consentInitiation, requestId);

            if (validationResponse != null && validationResponse.toString()
                    .contains(CommonConstants.ERROR)) {
                return validationResponse;
            }

            //Validate Instructed Amount
            validationResponse = ConsentValidatorUtil
                    .validateInstructedAmount(requestPath, submissionInitiation, consentInitiation, requestId);

            if (validationResponse != null && validationResponse.toString()
                    .contains(CommonConstants.ERROR)) {
                return validationResponse;
            }

            //Validate Requested Execution Date Time
            validationResponse = ConsentValidatorUtil
                    .validateRequestedExecutionDate(requestPath, submissionInitiation, consentInitiation, requestId);

            if (validationResponse != null && validationResponse.toString()
                    .contains(CommonConstants.ERROR)) {
                return validationResponse;
            }

            //Validate MandateRelatedInformation
            JSONObject mandateRelatedInfoPresenceError = validateFieldPresenceMatch(submissionInitiation,
                    consentInitiation, CommonConstants.MANDATE_RELATED_INFORMATION,
                    ErrorConstants.MANDATE_RELATED_INFORMATION_MISMATCH);
            if (mandateRelatedInfoPresenceError != null) {
                return mandateRelatedInfoPresenceError;
            } else if (submissionInitiation.has(CommonConstants.MANDATE_RELATED_INFORMATION)) {

                JSONObject subMandateRelatedInformation =
                        (JSONObject) submissionInitiation.get(CommonConstants.MANDATE_RELATED_INFORMATION);
                JSONObject initMandateRelatedInformation =
                        (JSONObject) consentInitiation.get(CommonConstants.MANDATE_RELATED_INFORMATION);

                validationResponse = ConsentValidatorUtil.validateMandateRelatedInformation(
                        subMandateRelatedInformation, initMandateRelatedInformation, requestId);

                if (validationResponse != null && validationResponse.toString().contains(CommonConstants.ERROR)) {
                    return validationResponse;
                }
            }

            //Validate Frequency
            validationResponse = ConsentValidatorUtil.validateFrequency(requestPath, submissionInitiation,
                    consentInitiation, invokedAPIVersion, requestId);

            if (validationResponse != null && validationResponse.toString().contains(CommonConstants.ERROR)) {
                return validationResponse;
            }

            //Validate First Payment Date Time
            validationResponse = ConsentValidatorUtil.validateFirstPaymentDate(requestPath, submissionInitiation,
                    consentInitiation, invokedAPIVersion, requestId);

            if (validationResponse != null && validationResponse.toString().contains(CommonConstants.ERROR)) {
                return validationResponse;
            }

            //Validate Currency Of Transfer
            validationResponse = ConsentValidatorUtil.validateCurrencyOfTransfer(requestPath,
                    submissionInitiation, consentInitiation, requestId);

            if (validationResponse != null && validationResponse.toString().contains(CommonConstants.ERROR)) {
                return validationResponse;
            }

            //Validate Charge Bearer
            validationResponse = ConsentValidatorUtil.validateChargeBearer(requestPath,
                    submissionInitiation, consentInitiation, requestId);

            if (validationResponse != null && validationResponse.toString().contains(CommonConstants.ERROR)) {
                return validationResponse;
            }

            //Validate DestinationCountryCode
            validationResponse = ConsentValidatorUtil.validateDestinationCountryCode(requestPath,
                    submissionInitiation, consentInitiation, requestId);

            if (validationResponse != null && validationResponse.toString().contains(CommonConstants.ERROR)) {
                return validationResponse;
            }

            //Validate Creditor Account
            if (submissionInitiation.has(CommonConstants.CREDITOR_ACC) &&
                    consentInitiation.has(CommonConstants.CREDITOR_ACC)) {

                JSONObject subCreditorAccount = (JSONObject) submissionInitiation.get(CommonConstants.CREDITOR_ACC);
                JSONObject initCreditorAccount = (JSONObject) consentInitiation.get(CommonConstants.CREDITOR_ACC);

                validationResponse = ConsentValidatorUtil.validateCreditorAcc(subCreditorAccount,
                        initCreditorAccount, requestId);

                if (validationResponse != null && validationResponse.toString().contains(CommonConstants.ERROR)) {
                    return validationResponse;
                }
            } else {
                return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                        ErrorConstants.FIELD_MISSING,
                        PaymentSubmissionConstants.CREDITOR_ACC_NOT_FOUND);
            }

            //Validate Debtor Account
            JSONObject debtorAccPresenceError = validateFieldPresenceMatch(submissionInitiation, consentInitiation,
                    CommonConstants.DEBTOR_ACC, PaymentSubmissionConstants.DEBTOR_ACC_MISMATCH);
            if (debtorAccPresenceError != null) {
                return debtorAccPresenceError;
            } else if (submissionInitiation.has(CommonConstants.DEBTOR_ACC)) {

                JSONObject subDebtorAccount = (JSONObject) submissionInitiation.get(CommonConstants.DEBTOR_ACC);
                JSONObject initDebtorAccount = (JSONObject) consentInitiation.get(CommonConstants.DEBTOR_ACC);

                validationResponse = ConsentValidatorUtil.validateDebtorAcc(subDebtorAccount, initDebtorAccount,
                        requestId);

                if (validationResponse != null && validationResponse.toString().contains(CommonConstants.ERROR)) {
                    return validationResponse;
                }
            }

            // Validate UltimateCreditor
            JSONObject ultimateCreditorPresenceError = validateFieldPresenceMatch(submissionInitiation,
                    consentInitiation, PaymentSubmissionConstants.ULTIMATE_CREDITOR,
                            PaymentSubmissionConstants.ULTIMATE_CREDITOR_MISMATCH);
            if (ultimateCreditorPresenceError != null) {
                return ultimateCreditorPresenceError;
            } else if (submissionInitiation.has(PaymentSubmissionConstants.ULTIMATE_CREDITOR)) {

                JSONObject subUltimateCreditor =
                        (JSONObject) submissionInitiation.get(PaymentSubmissionConstants.ULTIMATE_CREDITOR);
                JSONObject initUltimateCreditor =
                        (JSONObject) consentInitiation.get(PaymentSubmissionConstants.ULTIMATE_CREDITOR);

                validationResponse = ConsentValidatorUtil.validateUltimateCreditor(subUltimateCreditor,
                        initUltimateCreditor, requestId);

                if (validationResponse != null && validationResponse.toString().contains(CommonConstants.ERROR)) {
                    return validationResponse;
                }
            }

            // Validate UltimateDebtor
            JSONObject ultimateDebtorPresenceError = validateFieldPresenceMatch(submissionInitiation,
                    consentInitiation, PaymentSubmissionConstants.ULTIMATE_DEBTOR,
                            PaymentSubmissionConstants.ULTIMATE_DEBTOR_MISMATCH);
            if (ultimateDebtorPresenceError != null) {
                return ultimateDebtorPresenceError;
            } else if (submissionInitiation.has(PaymentSubmissionConstants.ULTIMATE_DEBTOR)) {

                JSONObject subUltimateDebtor =
                        (JSONObject) submissionInitiation.get(PaymentSubmissionConstants.ULTIMATE_DEBTOR);
                JSONObject initUltimateDebtor =
                        (JSONObject) consentInitiation.get(PaymentSubmissionConstants.ULTIMATE_DEBTOR);

                validationResponse = ConsentValidatorUtil.validateUltimateDebtor(subUltimateDebtor,
                        initUltimateDebtor, requestId);
                if (validationResponse != null && validationResponse.toString().contains(CommonConstants.ERROR)) {
                    return validationResponse;
                }
            }
            //Validate RegulatoryReporting
            JSONObject regulatoryReportingPresenceError = validateFieldPresenceMatch(submissionInitiation,
                    consentInitiation, PaymentSubmissionConstants.REGULATORY_REPORTING,
                    PaymentSubmissionConstants.REGULATORY_REPORTING_MISMATCH);
            if (regulatoryReportingPresenceError != null) {
                return regulatoryReportingPresenceError;
            }
            //Validate First Payment Amount
            JSONObject firstPaymentAmountPresenceError = validateFieldPresenceMatch(submissionInitiation,
                    consentInitiation, CommonConstants.FIRST_PAYMENT_AMOUNT,
                    PaymentSubmissionConstants.FIRST_PAYMENT_AMOUNT_NOT_FOUND);
            if (firstPaymentAmountPresenceError != null) {
                return firstPaymentAmountPresenceError;
            } else if (submissionInitiation.has(CommonConstants.FIRST_PAYMENT_AMOUNT)) {

                JSONObject subFirstPaymentAmount = (JSONObject) submissionInitiation
                        .get(CommonConstants.FIRST_PAYMENT_AMOUNT);
                JSONObject initFirstPaymentAmount = (JSONObject) consentInitiation
                        .get(CommonConstants.FIRST_PAYMENT_AMOUNT);

                if (subFirstPaymentAmount.has(CommonConstants.AMOUNT) &&
                        initFirstPaymentAmount.has(CommonConstants.AMOUNT) &&
                        !ConsentValidatorUtil.compareMandatoryParameter(
                                subFirstPaymentAmount.getString(CommonConstants.AMOUNT),
                                initFirstPaymentAmount.getString(CommonConstants.AMOUNT))) {

                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                            PaymentSubmissionConstants.FIRST_PAYMENT_AMOUNT_MISMATCH);
                }

                if (subFirstPaymentAmount.has(CommonConstants.CURRENCY) &&
                        initFirstPaymentAmount.has(CommonConstants.CURRENCY) &&
                        !ConsentValidatorUtil.compareMandatoryParameter(
                                subFirstPaymentAmount.getString(CommonConstants.CURRENCY),
                                initFirstPaymentAmount.getString(CommonConstants.CURRENCY))) {

                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                            PaymentSubmissionConstants.FIRST_PAYMENT_CURRENCY_MISMATCH);
                }
            }

            //Validate Creditor Agent
            JSONObject creditorAgentPresenceError = validateFieldPresenceMatch(submissionInitiation,
                    consentInitiation, PaymentSubmissionConstants.CREDITOR_AGENT,
                            PaymentSubmissionConstants.CREDITOR_AGENT_MISMATCH);
            if (creditorAgentPresenceError != null) {
                return creditorAgentPresenceError;
            } else if (submissionInitiation.has(PaymentSubmissionConstants.CREDITOR_AGENT)) {
                JSONObject subCreditorAgent = (JSONObject) submissionInitiation.get(
                        PaymentSubmissionConstants.CREDITOR_AGENT);
                JSONObject initCreditorAgent = (JSONObject) consentInitiation.get(
                        PaymentSubmissionConstants.CREDITOR_AGENT);

                if (!ConsentValidatorUtil.compareOptionalParameter(CommonConstants.SCHEME_NAME,
                        subCreditorAgent, initCreditorAgent)) {

                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                                    PaymentSubmissionConstants.CREDITOR_AGENT_SCHEME_NAME_MISMATCH);
                }

                if (!ConsentValidatorUtil.compareOptionalParameter(CommonConstants.IDENTIFICATION,
                        subCreditorAgent, initCreditorAgent)) {

                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                                    PaymentSubmissionConstants.CREDITOR_AGENT_IDENTIFICATION_MISMATCH);
                }
            }

            // Validate Remittence Info
            JSONObject remittanceInfoPresenceError = validateFieldPresenceMatch(submissionInitiation,
                    consentInitiation, PaymentSubmissionConstants.REMITTANCE_INFO,
                            PaymentSubmissionConstants.REMMITANCE_INFO_MISMATCH);
            if (remittanceInfoPresenceError != null) {
                return remittanceInfoPresenceError;
            } else if (submissionInitiation.has(PaymentSubmissionConstants.REMITTANCE_INFO)) {

                JSONObject remittanceInformationSub = (JSONObject) submissionInitiation
                        .get(PaymentSubmissionConstants.REMITTANCE_INFO);
                JSONObject remittanceInformationInit = (JSONObject) consentInitiation
                        .get(PaymentSubmissionConstants.REMITTANCE_INFO);

                validationResponse = ConsentValidatorUtil.validateRemittanceInfo(remittanceInformationSub,
                        remittanceInformationInit, invokedAPIVersion, requestId);
                if (validationResponse != null && validationResponse.toString().contains(CommonConstants.ERROR)) {
                    return validationResponse;
                }
            }

            //Validate Local Instrument
            JSONObject localInstrumentPresenceError = validateFieldPresenceMatch(submissionInitiation,
                    consentInitiation, PaymentSubmissionConstants.LOCAL_INSTRUMENT,
                            PaymentSubmissionConstants.LOCAL_INSTRUMENT_NOT_FOUND);
            if (localInstrumentPresenceError != null) {
                return localInstrumentPresenceError;
            } else if (!ConsentValidatorUtil.compareOptionalParameter(PaymentSubmissionConstants.LOCAL_INSTRUMENT,
                    submissionInitiation, consentInitiation)) {

                return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                        ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                        PaymentSubmissionConstants.LOCAL_INSTRUMENT_MISMATCH);
            }
            //Validate Reference
            JSONObject referencePresenceError = validateFieldPresenceMatch(submissionInitiation, consentInitiation,
                    CommonConstants.REFERENCE, PaymentSubmissionConstants.REFERENCE_NOT_FOUND);
            if (referencePresenceError != null) {
                return referencePresenceError;
            } else if (!ConsentValidatorUtil.compareOptionalParameter(CommonConstants.REFERENCE,
                    submissionInitiation, consentInitiation)) {

                return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                        ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                        PaymentSubmissionConstants.REFERENCE_MISMATCH);
            }
            //Validate Final Payment Date Time
            if (CommonConstants.UKApiVersion.UK_API_V400.equals(invokedAPIVersion)) {
                JSONObject submissionMandateRelatedInfo = null;
                JSONObject initiationMandateRelatedInfo = null;
                if (submissionInitiation.has(CommonConstants.MANDATE_RELATED_INFORMATION)) {
                    submissionMandateRelatedInfo = (JSONObject) submissionInitiation.get(
                            CommonConstants.MANDATE_RELATED_INFORMATION);
                    if (consentInitiation.has(CommonConstants.MANDATE_RELATED_INFORMATION)) {
                        initiationMandateRelatedInfo = (JSONObject) consentInitiation.get(
                                CommonConstants.MANDATE_RELATED_INFORMATION);
                    } else {
                        return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                                ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                                ErrorConstants.MANDATE_RELATED_INFORMATION_MISMATCH);
                    }
                }
                if (submissionMandateRelatedInfo != null && initiationMandateRelatedInfo != null) {
                    if (!ConsentValidatorUtil.compareOptionalParameter(CommonConstants.FINAL_PAYMENT_DATE,
                            submissionMandateRelatedInfo, initiationMandateRelatedInfo)) {
                        return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                                ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                                ErrorConstants.FINAL_PAYMENT_DATE_MISMATCH_API_V4);
                    }
                }
            } else {
                if (!ConsentValidatorUtil.compareOptionalParameter(CommonConstants.FINAL_PAYMENT_DATE,
                        submissionInitiation, consentInitiation)) {
                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                            PaymentSubmissionConstants.FINAL_PAYMENT_DATE_MISMATCH);
                }
            }

            //Validate Recurring Payment Date Time
            if (CommonConstants.UKApiVersion.UK_API_V400.equals(invokedAPIVersion)) {
                JSONObject submissionMandateRelatedInfo = null;
                JSONObject initiationMandateRelatedInfo = null;
                if (submissionInitiation.has(CommonConstants.MANDATE_RELATED_INFORMATION)) {
                    submissionMandateRelatedInfo = (JSONObject) submissionInitiation.get(
                            CommonConstants.MANDATE_RELATED_INFORMATION);
                    if (consentInitiation.has(CommonConstants.MANDATE_RELATED_INFORMATION)) {
                        initiationMandateRelatedInfo = (JSONObject) consentInitiation.get(
                                CommonConstants.MANDATE_RELATED_INFORMATION);
                    } else {
                        return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                                ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                                ErrorConstants.MANDATE_RELATED_INFORMATION_MISMATCH);
                    }
                }
                if (submissionMandateRelatedInfo != null && initiationMandateRelatedInfo != null) {
                    if (!ConsentValidatorUtil.compareOptionalParameter(CommonConstants.RECURRING_PAYMENT_DATE,
                            submissionMandateRelatedInfo, initiationMandateRelatedInfo)) {
                        return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                                ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                                ErrorConstants.RECURRING_PAYMENT_DATE_MISMATCH_API_V4);
                    }
                }
            } else {
                if (!ConsentValidatorUtil.compareOptionalParameter(CommonConstants.RECURRING_PAYMENT_DATE,
                        submissionInitiation, consentInitiation)) {

                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                            PaymentSubmissionConstants.RECURRING_PAYMENT_DATE_MISMATCH);
                }
            }

            //Validate Instruction Priority
            JSONObject instructionPriorityPresenceError = validateFieldPresenceMatch(submissionInitiation,
                    consentInitiation, PaymentSubmissionConstants.INSTRUCTION_PRIORITY,
                    PaymentSubmissionConstants.INSTRUCTION_PRIORITY_MISSING);
            if (instructionPriorityPresenceError != null) {
                return instructionPriorityPresenceError;
            } else if (!ConsentValidatorUtil.compareOptionalParameter(PaymentSubmissionConstants.INSTRUCTION_PRIORITY,
                    submissionInitiation, consentInitiation)) {

                return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                        ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                        PaymentSubmissionConstants.INSTRUCTION_PRIORITY_MISMATCH);
            }
            //Validate Purpose
            JSONObject purposePresenceError = validateFieldPresenceMatch(submissionInitiation, consentInitiation,
                    PaymentSubmissionConstants.PURPOSE, PaymentSubmissionConstants.PURPOSE_MISSING);
            if (purposePresenceError != null) {
                return purposePresenceError;
            } else if (!ConsentValidatorUtil.compareOptionalParameter(PaymentSubmissionConstants.PURPOSE,
                    submissionInitiation, consentInitiation)) {

                return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                        ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                        PaymentSubmissionConstants.PURPOSE_MISMATCH);
            }
            //Validate Extended Purpose
            JSONObject extendedPurposePresenceError = validateFieldPresenceMatch(submissionInitiation,
                    consentInitiation, PaymentSubmissionConstants.EXTENDED_PURPOSE,
                            PaymentSubmissionConstants.EXTENDED_PURPOSE_MISSING);
            if (extendedPurposePresenceError != null) {
                return extendedPurposePresenceError;
            } else if (!ConsentValidatorUtil.compareOptionalParameter(PaymentSubmissionConstants.EXTENDED_PURPOSE,
                    submissionInitiation, consentInitiation)) {

                return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                        ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                        PaymentSubmissionConstants.EXTENDED_PURPOSE_MISMATCH);
            }
            //Validate Final Payment Amount
            JSONObject finalPaymentAmountPresenceError = validateFieldPresenceMatch(submissionInitiation,
                    consentInitiation, CommonConstants.FINAL_PAYMENT_AMOUNT,
                    PaymentSubmissionConstants.FINAL_PAYMENT_AMOUNT_NOT_FOUND);
            if (finalPaymentAmountPresenceError != null) {
                return finalPaymentAmountPresenceError;
            } else if (submissionInitiation.has(CommonConstants.FINAL_PAYMENT_AMOUNT)) {

                JSONObject subFinalPaymentAmount = (JSONObject) submissionInitiation
                        .get(CommonConstants.FINAL_PAYMENT_AMOUNT);
                JSONObject initFinalPaymentAmount = (JSONObject) consentInitiation
                        .get(CommonConstants.FINAL_PAYMENT_AMOUNT);

                if (subFinalPaymentAmount.has(CommonConstants.AMOUNT) &&
                        initFinalPaymentAmount.has(CommonConstants.AMOUNT) &&
                        !ConsentValidatorUtil.compareMandatoryParameter(
                                subFinalPaymentAmount.getString(CommonConstants.AMOUNT),
                                initFinalPaymentAmount.getString(CommonConstants.AMOUNT))) {

                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                            PaymentSubmissionConstants.FINAL_PAYMENT_AMOUNT_MISMATCH);
                }

                if (subFinalPaymentAmount.has(CommonConstants.CURRENCY) &&
                        initFinalPaymentAmount.has(CommonConstants.CURRENCY) &&
                        !ConsentValidatorUtil.compareMandatoryParameter(
                                subFinalPaymentAmount.getString(CommonConstants.CURRENCY),
                                initFinalPaymentAmount.getString(CommonConstants.CURRENCY))) {

                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                            PaymentSubmissionConstants.FINAL_PAYMENT_CURRENCY_MISMATCH);
                }
            }

            //Validate Recurring Payment Amount
            JSONObject recurringAmountPresenceError = validateFieldPresenceMatch(submissionInitiation,
                    consentInitiation, CommonConstants.RECURRING_AMOUNT,
                    PaymentSubmissionConstants.RECURRING_PAYMENT_AMOUNT_NOT_FOUND);
            if (recurringAmountPresenceError != null) {
                return recurringAmountPresenceError;
            } else if (submissionInitiation.has(CommonConstants.RECURRING_AMOUNT)) {

                JSONObject subRecPaymentAmount = (JSONObject) submissionInitiation
                        .get(CommonConstants.RECURRING_AMOUNT);
                JSONObject initRecPaymentAmount = (JSONObject) consentInitiation
                        .get(CommonConstants.RECURRING_AMOUNT);

                if (subRecPaymentAmount.has(CommonConstants.AMOUNT) &&
                        initRecPaymentAmount.has(CommonConstants.AMOUNT) &&
                        !ConsentValidatorUtil.compareMandatoryParameter(
                                subRecPaymentAmount.getString(CommonConstants.AMOUNT),
                                initRecPaymentAmount.getString(CommonConstants.AMOUNT))) {

                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                            PaymentSubmissionConstants.RECURRING_PAYMENT_AMOUNT_MISMATCH);
                }

                if (subRecPaymentAmount.has(CommonConstants.CURRENCY) &&
                        initRecPaymentAmount.has(CommonConstants.CURRENCY) &&
                        !ConsentValidatorUtil.compareMandatoryParameter(
                                subRecPaymentAmount.getString(CommonConstants.CURRENCY),
                                initRecPaymentAmount.getString(CommonConstants.CURRENCY))) {

                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                                    PaymentSubmissionConstants.RECURRING_PAYMENT_CURRENCY_MISMATCH);
                }
            }

            //Validate Exchange Rate Info
            JSONObject exchangeRateInfoPresenceError = validateFieldPresenceMatch(submissionInitiation,
                    consentInitiation, PaymentSubmissionConstants.EXCHANGE_RATE_INFO,
                            PaymentSubmissionConstants.EXCHANGE_RATE_INFO_MISMATCH);
            if (exchangeRateInfoPresenceError != null) {
                return exchangeRateInfoPresenceError;
            } else if (submissionInitiation.has(PaymentSubmissionConstants.EXCHANGE_RATE_INFO)) {

                JSONObject subExchangeRateInfo = (JSONObject) submissionInitiation
                        .get(PaymentSubmissionConstants.EXCHANGE_RATE_INFO);
                JSONObject initExchangeRateInfo = (JSONObject) consentInitiation
                        .get(PaymentSubmissionConstants.EXCHANGE_RATE_INFO);

                if (!subExchangeRateInfo.has(PaymentSubmissionConstants.UNIT_CURRENCY) ||
                        !initExchangeRateInfo.has(PaymentSubmissionConstants.UNIT_CURRENCY)) {

                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                            PaymentSubmissionConstants.EXCHANGE_RATE_UNIT_CURRENCY_NOT_FOUND);
                }

                if (subExchangeRateInfo.has(PaymentSubmissionConstants.UNIT_CURRENCY) &&
                        initExchangeRateInfo.has(PaymentSubmissionConstants.UNIT_CURRENCY) &&
                        !ConsentValidatorUtil.compareMandatoryParameter(
                                subExchangeRateInfo.getString(PaymentSubmissionConstants.UNIT_CURRENCY),
                                initExchangeRateInfo.getString(PaymentSubmissionConstants.UNIT_CURRENCY))) {

                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                                    PaymentSubmissionConstants.EXCHNAGE_RATE_UNIT_CURRENCY_MISMATCH);
                }

                if (subExchangeRateInfo.has(PaymentSubmissionConstants.EXCHANGE_RATE) &&
                        !initExchangeRateInfo.has(PaymentSubmissionConstants.EXCHANGE_RATE) ||
                        !subExchangeRateInfo.has(PaymentSubmissionConstants.EXCHANGE_RATE) &&
                        initExchangeRateInfo.has(PaymentSubmissionConstants.EXCHANGE_RATE)) {

                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                            PaymentSubmissionConstants.EXCHANGE_RATE_NOT_FOUND);
                }

                if (subExchangeRateInfo.has(PaymentSubmissionConstants.EXCHANGE_RATE) &&
                        initExchangeRateInfo.has(PaymentSubmissionConstants.EXCHANGE_RATE) &&
                        subExchangeRateInfo.getDouble(PaymentSubmissionConstants.EXCHANGE_RATE) !=
                                initExchangeRateInfo.getDouble(PaymentSubmissionConstants.EXCHANGE_RATE)) {

                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                            PaymentSubmissionConstants.EXCHANGE_RATE_MISMATCH);
                }

                if (!subExchangeRateInfo.has(PaymentSubmissionConstants.RATE_TYPE) ||
                        !initExchangeRateInfo.has(PaymentSubmissionConstants.RATE_TYPE)) {

                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                            PaymentSubmissionConstants.EXCHANGE_RATE_TYPE_NOT_FOUND);
                }

                if (subExchangeRateInfo.has(PaymentSubmissionConstants.RATE_TYPE) &&
                        initExchangeRateInfo.has(PaymentSubmissionConstants.RATE_TYPE) &&
                        !ConsentValidatorUtil.compareMandatoryParameter(
                                subExchangeRateInfo.getString(PaymentSubmissionConstants.RATE_TYPE),
                                initExchangeRateInfo.getString(PaymentSubmissionConstants.RATE_TYPE))) {

                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                            PaymentSubmissionConstants.EXCHANGE_RATE_TYPE_MISMATCH);
                }

                if (subExchangeRateInfo.has(PaymentSubmissionConstants.CONTRACT_IDENTIFICATION) &&
                        !initExchangeRateInfo.has(PaymentSubmissionConstants.CONTRACT_IDENTIFICATION) ||
                        !subExchangeRateInfo.has(PaymentSubmissionConstants.CONTRACT_IDENTIFICATION) &&
                                initExchangeRateInfo.has(PaymentSubmissionConstants.CONTRACT_IDENTIFICATION)) {

                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                            PaymentSubmissionConstants.CONTRACT_IDENTIFICATION_NOT_FOUND);
                }

                if (!ConsentValidatorUtil.compareOptionalParameter(PaymentSubmissionConstants.CONTRACT_IDENTIFICATION,
                        subExchangeRateInfo, initExchangeRateInfo)) {
                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                            PaymentSubmissionConstants.CONTRACT_IDENTIFICATION_MISMATCH);
                }
            }

            //Validate Creditor
            JSONObject creditorPresenceError = validateFieldPresenceMatch(submissionInitiation, consentInitiation,
                    PaymentSubmissionConstants.CREDITOR, PaymentSubmissionConstants.CREDITOR_MISMATCH);
            if (creditorPresenceError != null) {
                return creditorPresenceError;
            } else if (submissionInitiation.has(PaymentSubmissionConstants.CREDITOR)) {

                JSONObject subCreditor = (JSONObject) submissionInitiation.get(PaymentSubmissionConstants.CREDITOR);
                JSONObject initCreditor = (JSONObject) consentInitiation.get(PaymentSubmissionConstants.CREDITOR);

                if (subCreditor.has(CommonConstants.NAME) &&
                        initCreditor.has(CommonConstants.NAME) &&
                        !ConsentValidatorUtil.compareMandatoryParameter(
                                subCreditor.getString(CommonConstants.NAME),
                                initCreditor.getString(CommonConstants.NAME))) {

                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                            PaymentSubmissionConstants.CREDITOR_NAME_MISMATCH);
                }

                if (!ConsentValidatorUtil.isValidAddress((JSONObject) subCreditor
                                .get(CommonConstants.POSTAL_ADDRESS),
                        (JSONObject) initCreditor.get(CommonConstants.POSTAL_ADDRESS))) {

                    return CommonUtil.getErrorResponse(CommonConstants.BAD_REQUEST,
                            ErrorConstants.RESOURCE_CONSENT_MISMATCH,
                            PaymentSubmissionConstants.POSTAL_ADDRESS_MISMATCH);
                }
            }

            return CommonUtil.getSuccessResponse(requestId);
        }

        return CommonUtil.getErrorResponse(400, ErrorConstants.FIELD_INVALID,
                "Missing initiation object");

    }
}
