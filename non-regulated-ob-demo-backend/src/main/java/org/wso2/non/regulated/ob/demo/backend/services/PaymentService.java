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

package org.wso2.non.regulated.ob.demo.backend.services;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.non.regulated.ob.demo.backend.BankException;
import org.wso2.non.regulated.ob.demo.backend.configurations.ConfigurableProperties;
import org.wso2.non.regulated.ob.demo.backend.util.CommonConstants;
import org.wso2.non.regulated.ob.demo.backend.util.UKCommonUtil;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

/**
 * PaymentService class.
 */
@Path("/paymentservice/")
public class PaymentService {

    private static final Log log = LogFactory.getLog(PaymentService.class);
    private static final int MAX_LIMIT = 500;

    // New ones added for v3
    private static final Map<String, PaymentResponseCacheModel> domesticPayments = new HashMap<>();
    private static final Map<String, PaymentResponseCacheModel> domesticScheduledPayment = new HashMap<>();
    private static final Map<String, PaymentResponseCacheModel> domesticStandingOrder = new HashMap<>();
    private static final Map<String, PaymentResponseCacheModel> internationalPayments = new HashMap<>();
    private static final Map<String, PaymentResponseCacheModel> internationalScheduledPayment = new HashMap<>();
    private static final Map<String, PaymentResponseCacheModel> internationalStandingOrder = new HashMap<>();
    private static final Map<String, PaymentResponseCacheModel> filePayment = new HashMap<>();
    private static final Map<String, PaymentResponseCacheModel> domesticVRPs = new HashMap<>();

    private static final Queue<String> domesticPaymentsIdQueue = new LinkedList<>();
    private static final Queue<String> domesticVRPsIdQueue = new LinkedList<>();
    private static final Queue<String> domesticScheduledPaymentIdQueue = new LinkedList<>();
    private static final Queue<String> domesticStandingOrderIdQueue = new LinkedList<>();
    private static final Queue<String> internationalPaymentsIdQueue = new LinkedList<>();
    private static final Queue<String> internationalScheduledPaymentIdQueue = new LinkedList<>();
    private static final Queue<String> internationalStandingOrderIdQueue = new LinkedList<>();
    private static final Queue<String> filePaymentIdQueue = new LinkedList<>();
    public static final String CUT_OFF_DATE_TIME = "CutOffDateTime";
    public static final String EXPECTED_EXECUTION_TIME = "ExpectedExecutionDateTime";
    public static final String EXPECTED_SETTLEMENT_TIME = "ExpectedSettlementDateTime";
    public static final String DOMESTIC_VRPS = ".*domestic-vrps";

    private static final Map<String, String> ISO_20022_UPDATED_ADDRESS_TYPE_MAP;
    private static final Map<String, String> ADDRESS_TYPE_MAP = new HashMap<>();

    static {
        Map<String, String> updatedAddressTypeMap = new HashMap<>();
        updatedAddressTypeMap.put("Business", "BIZZ");
        updatedAddressTypeMap.put("Correspondence", "CORR");
        updatedAddressTypeMap.put("DeliveryTo", "DLVY");
        updatedAddressTypeMap.put("MailTo", "MLTO");
        updatedAddressTypeMap.put("POBox", "PBOX");
        updatedAddressTypeMap.put("Postal", "ADDR");
        updatedAddressTypeMap.put("Residential", "HOME");
        updatedAddressTypeMap.put("Statement", "STAT");
        ISO_20022_UPDATED_ADDRESS_TYPE_MAP = updatedAddressTypeMap;
    }

    @POST
    @Path("/{paymentType}")
    @Produces("application/json; charset=utf-8")
    public Response paymentSubmission(String requestString, @PathParam("paymentType") String paymentType,
                                      @HeaderParam("x-fapi-interaction-id") String fid,
                                      @HeaderParam("Account-Request-Information") String accountRequestInfo)
            throws BankException {
        JSONObject jsonObject;
        JSONObject accountRequestInformation;

        try {
            accountRequestInformation = getRequest(paymentType, accountRequestInfo);
            JSONParser parser = new JSONParser(JSONParser.MODE_PERMISSIVE);
            jsonObject = (JSONObject) parser.parse(requestString);
        } catch (ParseException e) {
            log.error("Error in casting JSON body " + e.toString());
            throw new BankException("Error in casting JSON body " + e);
        }
        CommonConstants.UKApiVersion specVersion = getAPISpecVersionFromRequestInfo(accountRequestInformation);
        JSONObject additionalConsentInfo = (JSONObject) accountRequestInformation.get("additionalConsentInfo");

        JSONObject response = cacheAndGetPaymentResponse(paymentType, jsonObject, additionalConsentInfo, specVersion);
        return Response.status(201).entity(response.toString())
                .header("x-fapi-interaction-id", fid)
                .build();

    }

    @GET
    @Path("/{paymentType}/{paymentId}")
    @Produces("application/json; charset=utf-8")
    public Response getPaymentTypePayment(@HeaderParam("x-wso2-endpoint-uri") String endpointURI,
                                          @PathParam("paymentType") String paymentType,
                                          @PathParam("paymentId") String paymentId) {

        JSONObject responseObject = null;
        CommonConstants.UKApiVersion submissionAPIVersion = null;
        CommonConstants.UKApiVersion invokedAPIVersion =
                UKCommonUtil.getConsentAPIVersionFromRequestPath(endpointURI);
        if (StringUtils.isNotBlank(paymentType) || StringUtils.isNotBlank(paymentId)) {
            if (CommonConstants.DOMESTIC_PAYMENT.equals(paymentType)) {
                responseObject = domesticPayments.get(paymentId).getCacheResponse();
                submissionAPIVersion = domesticPayments.get(paymentId).getApiSpecVersion();
            } else if (CommonConstants.INTERNATIONAL_PAYMENT.equals(paymentType)) {
                responseObject = internationalPayments.get(paymentId).getCacheResponse();
                submissionAPIVersion = internationalPayments.get(paymentId).getApiSpecVersion();
            } else if (CommonConstants.DOMESTIC_SCHEDULED_PAYMENT.equals(paymentType)) {
                responseObject = domesticScheduledPayment.get(paymentId).getCacheResponse();
                submissionAPIVersion = domesticScheduledPayment.get(paymentId).getApiSpecVersion();
            } else if (CommonConstants.INTERNATIONAL_SCHEDULED_PAYMENT.equals(paymentType)) {
                responseObject = internationalScheduledPayment.get(paymentId).getCacheResponse();
                submissionAPIVersion = internationalScheduledPayment.get(paymentId).getApiSpecVersion();
            } else if (CommonConstants.DOMESTIC_STANDING_ORDER_PAYMENT.equals(paymentType)) {
                responseObject = domesticStandingOrder.get(paymentId).getCacheResponse();
                submissionAPIVersion = domesticStandingOrder.get(paymentId).getApiSpecVersion();
            } else if (CommonConstants.INTERNATIONAL_STANDING_ORDER_PAYMENT.equals(paymentType)) {
                responseObject = internationalStandingOrder.get(paymentId).getCacheResponse();
                submissionAPIVersion = internationalStandingOrder.get(paymentId).getApiSpecVersion();
            } else if (CommonConstants.FILE_PAYMENT.equals(paymentType)) {
                responseObject = filePayment.get(paymentId).getCacheResponse();
                submissionAPIVersion = filePayment.get(paymentId).getApiSpecVersion();
            } else if (CommonConstants.DOMESTIC_VRP_PAYMENT.equals(paymentType)) {
                responseObject = domesticVRPs.get(paymentId).getCacheResponse();
                submissionAPIVersion = domesticVRPs.get(paymentId).getApiSpecVersion();
            } else {
                responseObject = new JSONObject();
                responseObject.put("Data", "Invalid payment type");
            }
        }
        if (responseObject == null) {
            responseObject = new JSONObject();
        }
        if (submissionAPIVersion == null || (submissionAPIVersion == CommonConstants.UKApiVersion.UK_API_V310 &&
                invokedAPIVersion == CommonConstants.UKApiVersion.UK_API_V400)) {
            // Submissions done on older API versions should be accessible (GET) by new API version
            // for payment order resources
            updateAPIv3PaymentOrderResourcesResponseToAPIv4(responseObject, paymentType);
        }

        return Response.status(200).entity(responseObject.toString())
                .header("x-jws-signature", "V2hhdCB3ZSBnb3QgaGVyZQ0K..aXMgZmFpbHVyZSB0byBjb21tdW5pY2F0ZQ0K")
                .header("x-fapi-interaction-id", "93bac548-d2de-4546-b106-880a5018460d")
                .build();
    }

    @GET
    @Path("/{paymentType}/{paymentId}/payment-details")
    @Produces("application/json; charset=utf-8")
    public Response getPaymentDetails(@Context HttpServletRequest request,
                                      @HeaderParam("x-wso2-endpoint-uri") String endpointURI,
                                      @PathParam("paymentType") String paymentType,
                                      @PathParam("paymentId") String paymentId) {

        Instant currentDate = Instant.now();

        CommonConstants.UKApiVersion invokedAPIVersion =
                UKCommonUtil.getConsentAPIVersionFromRequestPath(endpointURI);

        String response = "";
        if (invokedAPIVersion.equals(CommonConstants.UKApiVersion.UK_API_V400)) {
            response = "{\n" +
                    "    \"Data\": [\n" +
                    "        {\n" +
                    "            \"PaymentStatus\": {\n" +
                    "                \"PaymentTransactionId\": \"" + paymentId + "\",\n" +
                    "                \"Status\": \"PDNG\",\n" +
                    "                \"StatusDetail\": {\n" +
                    "                    \"LocalInstrument\": \"UK.OBIE.BACS\",\n" +
                    "                    \"Status\": \"COND\",\n" +
                    "                    \"StatusReason\": \"U035\",\n" +
                    "                    \"StatusReasonDescription\":\"Consent already consumed\"" +
                    "                },\n" +
                    "                \"StatusUpdateDateTime\": \"" + currentDate.toString() + "\"\n" +
                    "            }\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}";
        } else {
            response = "{" +
                    "   \"Data\": {" +
                    "       \"PaymentStatus\": [" +
                    "           {" +
                    "               \"PaymentTransactionId\": \"" + paymentId + "\"," +
                    "               \"Status\":\"Accepted\"," +
                    "               \"StatusUpdateDateTime\": \"" + currentDate.toString() + "\"," +
                    "               \"StatusDetail\":{" +
                    "                   \"LocalInstrument\":\"UK.OBIE.BACS\"," +
                    "                   \"Status\":\"Accepted\"," +
                    "                   \"StatusReason\":\"PendingFailingSettlement\"," +
                    "                   \"StatusReasonDescription\":\"Enough amount is in your account to " +
                    "                                           complete the transaction.\"" +
                    "               }" +
                    "           }," +
                    "           {" +
                    "               \"PaymentTransactionId\": \"" + paymentId + "\"," +
                    "               \"Status\":\"AcceptedCustomerProfile\"," +
                    "               \"StatusUpdateDateTime\": \"" + currentDate.toString() + "\"," +
                    "               \"StatusDetail\":{" +
                    "                   \"LocalInstrument\":\"UK.OBIE.BACS\"," +
                    "                   \"Status\":\"Accepted\"," +
                    "                   \"StatusReason\":\"PendingFailingSettlement\"," +
                    "                   \"StatusReasonDescription\":\"Enough amount is in your account to " +
                    "                                           complete the transaction.\"" +
                    "               }" +
                    "           }" +
                    "       ]" +
                    "   }" +
                    "}";
        }

        return Response.status(200).entity(response)
                .header("x-fapi-interaction-id", "93bac548-d2de-4546-b106-880a5018460d")
                .build();
    }

    @GET
    @Path("/{paymentType}/{paymentId}/funds-confirmation")
    @Produces("application/json; charset=utf-8")
    public Response getPaymentTypeFundsConfirmation(@PathParam("paymentType") String paymentType,
                                                    @PathParam("paymentId") String paymentId) {

        Instant currentDate = Instant.now();

        String response = "{\n" +
                "    \"Data\": {\n" +
                "        \"FundsAvailableResult\": {\n" +
                "            \"FundsAvailableDateTime\": \"" + currentDate.toString() + "\",\n" +
                "            \"FundsAvailable\": true\n" +
                "        }\n" +
                "    },\n" +
                "    \"Links\": {\n" +
                "        \"Self\": \"/pisp/" + paymentType + "/" + paymentId + "/funds-confirmation\"\n" +
                "    },\n" +
                "    \"Meta\": {}\n" +
                "}";

        return Response.status(200).entity(response)
                .header("x-fapi-interaction-id", UUID.randomUUID().toString())
                .build();
    }

    @GET
    @Path("/{paymentType}/{paymentSubmissionId}/funds-confirmation")
    @Produces("application/json")
    public Response getPaymentsFundsConfirmation(@HeaderParam("x-fapi-interaction-id") String xFapiInteractionId,
                                                 @PathParam("paymentType") String paymentType,
                                                 @PathParam("paymentSubmissionId") String paymentId) {

        if (StringUtils.isNotBlank(paymentId)) {
            String response = "{\n" +
                    "  \"Data\": {\n" +
                    "       \"FundsAvailableResult\": { " +
                    "           \"FundsAvailableDateTime\": \"" + LocalDateTime.now() + "\"," +
                    "           \"FundsAvailable\": \"true\"" +
                    "         },\n" +
                    "  \"SupplementaryData\": {} " +
                    "  },\n" +
                    "  \"Links\": {\n" +
                    "    \"Self\": \"https://obank.com/open-banking/v3.1/" + paymentType + "payment-submissions/"
                    + paymentId + "/funds-confirmation\"\n" +
                    "  },\n" +
                    " \"Meta\": {" +
                    "   \"TotalPages\": 0," +
                    "   \"FirstAvailableDateTime\": \"" + LocalDateTime.now().minusDays(3) + "\"," +
                    "   \"LastAvailableDateTime\": \"" + LocalDateTime.now() + "\"" +
                    "   }\n" +
                    "}";
            return Response.status(200).entity(response)
                    .header("x-fapi-interaction-id", xFapiInteractionId)
                    .header("Content-Type", "application/json").build();
        }
        return Response.status(400).entity("Payment Submission Id required.").build();
    }

    @POST
    @Path("/{paymentType}/{paymentSubmissionId}/funds-confirmation")
    @Produces("application/json")
    public Response getPaymentsFundsConfirmation(@HeaderParam("x-fapi-interaction-id") String xFapiInteractionId,
                                                 @PathParam("paymentSubmissionId") String consentId) {
        String fundsConfirmationId = UUID.randomUUID().toString();
        if (StringUtils.isNotBlank(consentId)) {
            String response = "{\n" +
                    "  \"Data\": {\n" +
                    "  \"FundsConfirmationId\" : \"" + fundsConfirmationId + "\"," +
                    "  \"ConsentId\": \"" + consentId + "\"," +
                    "  \"CreationDateTime\" : \"" + Instant.now().toString() + "\"," +
                    "  \"Reference\": \"Purchase02\"," +
                    "  \"FundsAvailableResult\": {\n" +
                    "       \"FundsAvailableDateTime\": \"" + Instant.now().toString() + "\"," +
                    "       \"FundsAvailable\": \"Available\"" +
                    "       },\n" +
                    "  \"InstructedAmount\" : {\n" +
                    "       \"Amount\": \"20.00\"," +
                    "       \"Currency\": \"GBP\"" +
                    "       }\n" +
                    "  }\n" +
                    "}";
            return Response.status(201).entity(response)
                    .header("x-jws-signature", "V2hhdCB3ZSBnb3QgaGVyZQ0K..aXMgZmFpbHVyZSB0byBjb21tdW5pY2F0ZQ0K")
                    .header("x-fapi-interaction-id", xFapiInteractionId)
                    .header("Content-Type", "application/json").build();
        }
        return Response.status(400).entity("Payment Submission Id required.").build();
    }

    @GET
    @Path("/{paymentType}/{paymentId}/report-file")
    @Produces("*/*")
    public Response getFilePaymentExecutionReport(@PathParam("paymentType") String paymentType,
                                                  @PathParam("paymentId") String paymentId) {

        String response = getFilePaymentExecutionReportResponse(paymentType, paymentId);

        return Response.status(200).entity(response)
                .header("x-jws-signature", "V2hhdCB3ZSBnb3QgaGVyZQ0K..aXMgZmFpbHVyZSB0byBjb21tdW5pY2F0ZQ0K")
                .header("Content-Type", "text/xml")
                .header("x-fapi-interaction-id", "93bac548-d2de-4546-b106-880a5018460d")
                .build();
    }

    private JSONObject cacheAndGetPaymentResponse(String paymentType, JSONObject requestObject,
                                                  JSONObject additionalConsentInfo,
                                                  CommonConstants.UKApiVersion specVersion)
            throws BankException {
        JSONObject responseObject;

        int randomPIN = new SecureRandom().nextInt(100);
        String paymentIdType = getPaymentType(paymentType);

        if ("".equals(paymentIdType)) {
            responseObject = new JSONObject();
            responseObject.put("Data", "Invalid payment type");

            return responseObject;
        }

        String status;
        String paymentIdValue;

        paymentIdValue = ((JSONObject) requestObject.get("Data")).getAsString("ConsentId");
        paymentIdValue = paymentIdValue + "-" + randomPIN;

        if (paymentType.matches(".*domestic-payments")
                || paymentType.matches(".*international-payments") || paymentType.matches(DOMESTIC_VRPS)) {
            status = "AcceptedSettlementCompleted";
            if (specVersion.equals(CommonConstants.UKApiVersion.UK_API_V400)) {
                status = "ACSP";
            }
        } else if (paymentType.matches(".*standing-orders") || paymentType.matches(".*scheduled-payments")
                || paymentType.matches(".*file-payments")) {
            status = "InitiationCompleted";
            if (specVersion.equals(CommonConstants.UKApiVersion.UK_API_V400)) {
                if (paymentType.matches("domestic.*") || paymentType.matches(".*file-payments")) {
                    status = "RCVD";
                } else {
                    status = "ACSP";
                }
            }
        } else {
            status = "AcceptedSettlementInProcess";
            if (specVersion.equals(CommonConstants.UKApiVersion.UK_API_V400)) {
                status = "ACSP";
            }
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        Date date = new Date();
        String currentDate = dateFormat.format(date);

        String readRefundAccount = additionalConsentInfo.getAsString("ReadRefundAccount");
        String cutOffTimeAcceptable = additionalConsentInfo.getAsString("CutOffTimeAcceptable");

        try {
            JSONParser parser = new JSONParser(JSONParser.MODE_PERMISSIVE);
            responseObject = (JSONObject) parser.parse(requestObject.toString());

            JSONObject dataObject = (JSONObject) responseObject.get("Data");

            dataObject.put(paymentIdType, paymentIdValue);
            if (specVersion.equals(CommonConstants.UKApiVersion.UK_API_V400)) {
                JSONArray statusReasonArray = new JSONArray();
                JSONObject statusReason = new JSONObject();
                statusReason.put("Path", "string");
                statusReason.put("StatusReasonCode", "ERIN");
                statusReason.put("StatusReasonDescription", "string");
                statusReasonArray.add(statusReason);
                dataObject.put("StatusReason", statusReasonArray);
            }
            dataObject.put("Status", status);
            dataObject.put("CreationDateTime", currentDate);
            dataObject.put("StatusUpdateDateTime", currentDate);

            if ("domestic-vrps".equals(paymentType)) {
                JSONObject debtorAccount = new JSONObject();
                debtorAccount.put("SchemeName", "SortCodeAccountNumber");
                debtorAccount.put("SecondaryIdentification", "Roll 2901");
                debtorAccount.put("Name", "Deb Mal");
                debtorAccount.put("Identification", additionalConsentInfo.getAsString("AccountIds")
                        .split(":")[0].replace("[\"", ""));

                dataObject.put("DebtorAccount", debtorAccount);

            }

            // Add refund account details if requested during consent initiation
            if (Boolean.parseBoolean(readRefundAccount) && !paymentType.matches(".*file-payments")) {
                addRefundAccount(dataObject);
            }

            if (Boolean.parseBoolean(cutOffTimeAcceptable)) {
                //Setting random values for below
                if (!paymentType.matches(".*standing-orders") && !paymentType.matches(".*file-payments")) {
                    dataObject.put(EXPECTED_EXECUTION_TIME, constructDateTime(1L,
                            ConfigurableProperties.EXPECTED_EXECUTION_TIME));
                    dataObject.put(EXPECTED_SETTLEMENT_TIME, constructDateTime(1L,
                            ConfigurableProperties.EXPECTED_SETTLEMENT_TIME));
                }
            }
            JSONObject linksObject = new JSONObject();
            linksObject.put("Self", "/" + paymentType + "/" + paymentIdValue);
            responseObject.put("Links", linksObject);

            JSONObject metaObject = new JSONObject();
            responseObject.put("Meta", metaObject);

            if ("domestic-vrps".equals(paymentType)) {
                dataObject.remove("PSUAuthenticationMethod");
            } else {
                responseObject.remove("Risk");
            }
        } catch (ParseException e) {
            throw new BankException(e);
        }
        addToCache(paymentType, paymentIdValue, responseObject, specVersion);
        return responseObject;
    }

    /**
     * Add Refund account details to the response.
     *
     * @param dataObject
     */
    private void addRefundAccount(JSONObject dataObject) {

        String schemeName = "UK.OBIE.SortCodeAccountNumber";
        String identification = "Identification";
        String name = "NTPC Inc";

        JSONObject accountData = new JSONObject();
        accountData.put("SchemeName", schemeName);
        accountData.put("Identification", identification);
        accountData.put("Name", name);

        JSONObject account = new JSONObject();
        account.put("Account", accountData);

        dataObject.put("Refund", account);
    }

    private String getPaymentType(String paymentType) {
        String paymentId;
        switch (paymentType) {
            case "domestic-payments":
                paymentId = "DomesticPaymentId";
                break;
            case "domestic-vrps":
                paymentId = "DomesticVRPId";
                break;
            case "international-payments":
                paymentId = "InternationalPaymentId";
                break;
            case "domestic-scheduled-payments":
                paymentId = "DomesticScheduledPaymentId";
                break;
            case "international-scheduled-payments":
                paymentId = "InternationalScheduledPaymentId";
                break;
            case "domestic-standing-orders":
                paymentId = "DomesticStandingOrderId";
                break;
            case "international-standing-orders":
                paymentId = "InternationalStandingOrderId";
                break;
            case "file-payments":
                paymentId = "FilePaymentId";
                break;
            default:
                paymentId = "";
                break;
        }
        return paymentId;
    }

    private void addToCache(String paymentType, String paymentIdValue, JSONObject responseObject,
                            CommonConstants.UKApiVersion specVersion) {
        if ("domestic-payments".equals(paymentType)) {
            if (domesticPayments.size() > MAX_LIMIT) {
                // Max limit reached
                domesticPayments.remove(domesticPaymentsIdQueue.poll());
            }
            domesticPayments.put(paymentIdValue, new PaymentResponseCacheModel(specVersion, responseObject));
            domesticPaymentsIdQueue.add(paymentIdValue);

        } else if ("domestic-vrps".equals(paymentType)) {
            if (domesticVRPs.size() > MAX_LIMIT) {
                // Max limit reached
                domesticVRPs.remove(domesticVRPsIdQueue.poll());
            }
            domesticVRPs.put(paymentIdValue, new PaymentResponseCacheModel(specVersion, responseObject));
            domesticVRPsIdQueue.add(paymentIdValue);
        } else if ("international-payments".equals(paymentType)) {
            if (internationalPayments.size() > MAX_LIMIT) {
                // Max limit reached
                internationalPayments.remove(internationalPaymentsIdQueue.poll());
            }
            internationalPayments.put(paymentIdValue, new PaymentResponseCacheModel(specVersion, responseObject));
            internationalPaymentsIdQueue.add(paymentIdValue);
        } else if ("domestic-scheduled-payments".equals(paymentType)) {
            if (domesticScheduledPayment.size() > MAX_LIMIT) {
                // Max limit reached
                domesticScheduledPayment.remove(domesticScheduledPaymentIdQueue.poll());
            }
            domesticScheduledPayment.put(paymentIdValue, new PaymentResponseCacheModel(specVersion, responseObject));
            domesticScheduledPaymentIdQueue.add(paymentIdValue);
        } else if ("international-scheduled-payments".equals(paymentType)) {
            if (internationalScheduledPayment.size() > MAX_LIMIT) {
                // Max limit reached
                internationalScheduledPayment.remove(internationalScheduledPaymentIdQueue.poll());
            }
            internationalScheduledPayment.put(paymentIdValue,
                    new PaymentResponseCacheModel(specVersion, responseObject));
            internationalScheduledPaymentIdQueue.add(paymentIdValue);
        } else if ("domestic-standing-orders".equals(paymentType)) {
            if (domesticStandingOrder.size() > MAX_LIMIT) {
                // Max limit reached
                domesticStandingOrder.remove(domesticStandingOrderIdQueue.poll());
            }
            domesticStandingOrder.put(paymentIdValue, new PaymentResponseCacheModel(specVersion, responseObject));
            domesticStandingOrderIdQueue.add(paymentIdValue);
        } else if ("international-standing-orders".equals(paymentType)) {
            if (internationalStandingOrder.size() > MAX_LIMIT) {
                // Max limit reached
                internationalStandingOrder.remove(internationalStandingOrderIdQueue.poll());
            }
            internationalStandingOrder.put(paymentIdValue, new PaymentResponseCacheModel(specVersion, responseObject));
            internationalStandingOrderIdQueue.add(paymentIdValue);
        } else if ("file-payments".equals(paymentType)) {
            if (filePayment.size() > MAX_LIMIT) {
                //Max limit reached
                filePayment.remove(filePaymentIdQueue.poll());
            }
            filePayment.put(paymentIdValue, new PaymentResponseCacheModel(specVersion, responseObject));
            filePaymentIdQueue.add(paymentIdValue);
        }
    }

    private String getFilePaymentExecutionReportResponse(String paymentType, String paymentId) {
        /* Since we are uploading an XML file in file-payments initiation, we are downloading the same content type as
           requested by the spec. Changed the sample string to support XML type for the moment.
        */

        String file = "<Document xmlns=\"urn:iso:std:iso:20022:tech:xsd:pain.001.001.08\" xmlns:xsi=\"http://www." +
                "                               w3.org/2001/XMLSchema- instance\">\n" +
                "  <CstmrCdtTrfInitn>\n" +
                "    <GrpHdr>\n" +
                "      <MsgId>ABC/120928/CCT001</MsgId>\n" +
                "      <CreDtTm>2012-09-28T14:07:00</CreDtTm>\n" +
                "      <NbOfTxs>2</NbOfTxs>\n" +
                "      <CtrlSum>70</CtrlSum>\n" +
                "      <InitgPty>\n" +
                "        <Nm>ABC Corporation</Nm>\n" +
                "        <PstlAdr>\n" +
                "          <StrtNm>Times Square</StrtNm>\n" +
                "          <BldgNb>7</BldgNb>\n" +
                "          <PstCd>NY 10036</PstCd>\n" +
                "          <TwnNm>New York</TwnNm>\n" +
                "          <Ctry>US</Ctry>\n" +
                "        </PstlAdr>\n" +
                "      </InitgPty>\n" +
                "    </GrpHdr>\n" +
                "    <PmtInf>\n" +
                "      <PmtInfId>ABC/086</PmtInfId>\n" +
                "      <PmtMtd>TRF</PmtMtd>\n" +
                "      <BtchBookg>false</BtchBookg>\n" +
                "      <ReqdExctnDt>\n" +
                "        <Dt>2023-10-01</Dt>\n" +
                "      </ReqdExctnDt>\n" +
                "      <PmtTpInf>\n" +
                "        <LclInstrm>UK.OBIE.BAC</LclInstrm>\n" +
                "      </PmtTpInf>\n" +
                "      <Dbtr>\n" +
                "        <Nm>ABC Corporation</Nm>\n" +
                "        <PstlAdr>\n" +
                "          <StrtNm>Times Square</StrtNm>\n" +
                "          <BldgNb>7</BldgNb>\n" +
                "          <PstCd>NY 10036</PstCd>\n" +
                "          <TwnNm>New York</TwnNm>\n" +
                "          <Ctry>US</Ctry>\n" +
                "        </PstlAdr>\n" +
                "      </Dbtr>\n" +
                "      <DbtrAcct>\n" +
                "        <Id>\n" +
                "          <Othr>\n" +
                "            <Id>30080012343456</Id>\n" +
                "          </Othr>\n" +
                "        </Id>\n" +
                "      </DbtrAcct>\n" +
                "      <DbtrAgt>\n" +
                "        <FinInstnId>\n" +
                "          <BICFI>BBBBUS33</BICFI>\n" +
                "        </FinInstnId>\n" +
                "      </DbtrAgt>\n" +
                "      <CdtTrfTxInf>\n" +
                "        <PmtId>\n" +
                "          <InstrId>ABC/120928/CCT001/01</InstrId>\n" +
                "          <EndToEndId>ABC/4562/2012-09-08</EndToEndId>\n" +
                "        </PmtId>\n" +
                "        <Amt>\n" +
                "          <InstdAmt Ccy=\"JPY\">20</InstdAmt>\n" +
                "        </Amt>\n" +
                "        <ChrgBr>SHAR</ChrgBr>\n" +
                "        <CdtrAgt>\n" +
                "          <FinInstnId>\n" +
                "            <BICFI>AAAAGB2L</BICFI>\n" +
                "          </FinInstnId>\n" +
                "        </CdtrAgt>\n" +
                "        <Cdtr>\n" +
                "          <Nm>DEF Electronics</Nm>\n" +
                "          <PstlAdr>\n" +
                "            <AdrLine>Corn Exchange 5th Floor</AdrLine>\n" +
                "            <AdrLine>Mark Lane 55</AdrLine>\n" +
                "            <AdrLine>EC3R7NE London</AdrLine>\n" +
                "            <AdrLine>GB</AdrLine>\n" +
                "          </PstlAdr>\n" +
                "        </Cdtr>\n" +
                "        <CdtrAcct>\n" +
                "          <Id>\n" +
                "            <Othr>\n" +
                "              <Id>23683707994125</Id>\n" +
                "            </Othr>\n" +
                "          </Id>\n" +
                "        </CdtrAcct>\n" +
                "        <Purp>\n" +
                "          <Cd>GDDS</Cd>\n" +
                "        </Purp>\n" +
                "        <RmtInf>\n" +
                "          <Strd>\n" +
                "            <RfrdDocInf>\n" +
                "              <Tp>\n" +
                "                <CdOrPrtry>\n" +
                "                  <Cd>CINV</Cd>\n" +
                "                </CdOrPrtry>\n" +
                "              </Tp>\n" +
                "              <Nb>4562</Nb>\n" +
                "              <RltdDt>2012-09-08</RltdDt>\n" +
                "            </RfrdDocInf>\n" +
                "          </Strd>\n" +
                "        </RmtInf>\n" +
                "      </CdtTrfTxInf>\n" +
                "      <CdtTrfTxInf>\n" +
                "        <PmtId>\n" +
                "          <InstrId>ABC/120928/CCT001/2</InstrId>\n" +
                "          <EndToEndId>ABC/ABC-13679/2012-09-15</EndToEndId>\n" +
                "        </PmtId>\n" +
                "        <Amt>\n" +
                "          <InstdAmt Ccy=\"EUR\">50</InstdAmt>\n" +
                "        </Amt>\n" +
                "        <ChrgBr>CRED</ChrgBr>\n" +
                "        <CdtrAgt>\n" +
                "          <FinInstnId>\n" +
                "            <BICFI>DDDDBEBB</BICFI>\n" +
                "          </FinInstnId>\n" +
                "        </CdtrAgt>\n" +
                "        <Cdtr>\n" +
                "          <Nm>GHI Semiconductors</Nm>\n" +
                "          <PstlAdr>\n" +
                "            <StrtNm>Avenue Brugmann</StrtNm>\n" +
                "            <BldgNb>415</BldgNb>\n" +
                "            <PstCd>1180</PstCd>\n" +
                "            <TwnNm>Brussels</TwnNm>\n" +
                "            <Ctry>BE</Ctry>\n" +
                "          </PstlAdr>\n" +
                "        </Cdtr>\n" +
                "        <CdtrAcct>\n" +
                "          <Id>\n" +
                "            <IBAN>BE30001216371411</IBAN>\n" +
                "          </Id>\n" +
                "        </CdtrAcct>\n" +
                "        <InstrForCdtrAgt>\n" +
                "          <Cd>PHOB</Cd>\n" +
                "          <InstrInf>+32/2/2222222</InstrInf>\n" +
                "        </InstrForCdtrAgt>\n" +
                "        <Purp>\n" +
                "          <Cd>GDDS</Cd>\n" +
                "        </Purp>\n" +
                "        <RmtInf>\n" +
                "          <Strd>\n" +
                "            <RfrdDocInf>\n" +
                "              <Tp>\n" +
                "                <CdOrPrtry>\n" +
                "                  <Cd>CINV</Cd>\n" +
                "                </CdOrPrtry>\n" +
                "              </Tp>\n" +
                "              <Nb>ABC-13679</Nb>\n" +
                "              <RltdDt>2012-09-15</RltdDt>\n" +
                "            </RfrdDocInf>\n" +
                "          </Strd>\n" +
                "        </RmtInf>\n" +
                "      </CdtTrfTxInf>\n" +
                "    </PmtInf>\n" +
                "  </CstmrCdtTrfInitn>\n" +
                "</Document>";

        return file;
    }

    private static JSONObject getRequest(String paymentType, String json) throws ParseException {
        String[] splitString = json.split("\\.");
        String base64EncodedBody = splitString[1];
        String decodedString = null;
        if (!paymentType.matches(".*file-payments")) {
            decodedString = new String(Base64.getDecoder().decode(base64EncodedBody.getBytes(StandardCharsets.UTF_8)),
                    StandardCharsets.UTF_8);
        } else {
            decodedString = new String(Base64.getUrlDecoder()
                    .decode(base64EncodedBody.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        }
        JSONParser parser = new JSONParser(JSONParser.MODE_PERMISSIVE);
        JSONObject jsonObject = (JSONObject) parser.parse(decodedString);
        return jsonObject;
    }

    public static String constructDateTime(long daysToAdd, String time) {

        String dateValue = LocalDate.now().plusDays(daysToAdd) + "T" + (OffsetTime.parse(time));

        OffsetDateTime offSetDateVal = OffsetDateTime.parse(dateValue);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        return dateTimeFormatter.format(offSetDateVal);
    }

    /**
     * Get the API spec version from the request information.
     *
     * @param accountRequestInformation Request information
     * @return API spec version
     */
    private static CommonConstants.UKApiVersion getAPISpecVersionFromRequestInfo(
            JSONObject accountRequestInformation) {

        JSONObject additionalConsentInfo = (JSONObject) accountRequestInformation.get("additionalConsentInfo");
        CommonConstants.UKApiVersion ukApiVersion = CommonConstants.UKApiVersion.UK_API_V310;
        if (additionalConsentInfo != null && additionalConsentInfo.containsKey(CommonConstants.INVOKED_API_VERSION)) {
            String specVersion = additionalConsentInfo.get(CommonConstants.INVOKED_API_VERSION).toString();
            if (CommonConstants.UK_API_V4_ATTRIBUTE.equalsIgnoreCase(specVersion)) {
                ukApiVersion = CommonConstants.UKApiVersion.UK_API_V400;
            }
        }
        return ukApiVersion;
    }

    /**
     * Update the API v3 payment order resources response to API v4.
     *
     * @param v3responseObject API v3 response object
     * @param paymentType
     */
    private void updateAPIv3PaymentOrderResourcesResponseToAPIv4(JSONObject v3responseObject, String paymentType) {
        handleStatus(v3responseObject, paymentType);
        handleRiskObject(v3responseObject);
        handleCreditorAgent(v3responseObject);
        handleCreditorPostalAddress(v3responseObject);
        handleRemittanceInformation(v3responseObject);
        handleMandateRelatedInformation(v3responseObject);
    }

    /**
     * Update the API v3 payment order resources Status object to API v4.
     *
     * @param v3responseObject API v4 Status response
     * @param paymentType      Payment type
     */
    private void handleStatus(JSONObject v3responseObject, String paymentType) {

        if (v3responseObject.containsKey("Data")) {
            JSONObject data = (JSONObject) v3responseObject.get("Data");
            if (data.containsKey("Status")) {
                String status = data.get("Status").toString();
                String apiv4Status = getAPIV4Status(paymentType, status);
                data.put("Status", apiv4Status);
            }
        }
    }

    /**
     * Get the API v4 status from the API v3 status.
     *
     * @param paymentType Payment type
     * @param status      API v3 status
     * @return API v4 status
     */
    public static String getAPIV4Status(String paymentType, String status) {

        if (status.equals("AcceptedCreditSettlementCompleted")) {
            return "ACCC";
        } else if (status.equals("AcceptedSettlementCompleted")) {
            return "ACCC";
        } else if (status.equals("AcceptedSettlementInProcess")) {
            return "ACSP";
        } else if (status.equals("AcceptedWithoutPosting")) {
            return "ACWP";
        } else if (status.equals("Pending")) {
            return "PDNG";
        } else if (status.equals("Rejected")) {
            return "RJCT";
        } else if (status.equals("InitiationFailed")) {
            return "RJCT";
        } else if (status.equals("Cancelled")) {
            if (paymentType.equals(CommonConstants.DOMESTIC_STANDING_ORDER_PAYMENT)) {
                return "CANC";
            } else {
                return "RJCT";
            }
        } else if (status.equals("InitiationPending")) {
            if (paymentType.equals(CommonConstants.INTERNATIONAL_SCHEDULED_PAYMENT)) {
                return "PDNG";
            } else {
                return "RCVD";
            }
        } else if (status.equals("InitiationCompleted")) {
            switch (paymentType) {
                case CommonConstants.DOMESTIC_STANDING_ORDER_PAYMENT:
                    return "RCVD";
                case CommonConstants.DOMESTIC_SCHEDULED_PAYMENT:
                case CommonConstants.INTERNATIONAL_STANDING_ORDER_PAYMENT:
                case CommonConstants.INTERNATIONAL_SCHEDULED_PAYMENT:
                case CommonConstants.DOMESTIC_PAYMENT:
                    return "ACSP";
                case CommonConstants.FILE_PAYMENT:
                    return "ASCP";
            }
        }
        // Returning v3 status as default status
        return status;
    }


    /**
     * Update the API v3 payment order resources Risk object to API v4.
     *
     * @param v3responseObject API v4 Risk response object
     */
    private void handleRiskObject(JSONObject v3responseObject) {
        if (v3responseObject.containsKey("Risk")) {
            JSONObject risk = (JSONObject) v3responseObject.get("Risk");
            if (risk.containsKey("DeliveryAddress")) {
                JSONObject deliveryAddress = (JSONObject) risk.get("DeliveryAddress");
                if (deliveryAddress.containsKey("AddressType")) {
                    deliveryAddress.put("AddressType",
                            ISO_20022_UPDATED_ADDRESS_TYPE_MAP.get(deliveryAddress.get("AddressType")));
                }
            }
            if (risk.containsKey("ContractPresentInidicator")) {
                JSONObject contractPresentInidicator = (JSONObject) risk.get("ContractPresentInidicator");
                risk.remove("ContractPresentInidicator");
                risk.appendField("ContractPresentIndicator", contractPresentInidicator);
            }
        }
    }

    /**
     * Update the API v3 payment order resources CreditorAgent object to API v4.
     *
     * @param v3responseObject API v4 CreditorAgent response object
     */
    private void handleCreditorAgent(JSONObject v3responseObject) {
        if (v3responseObject.containsKey("Data")) {
            JSONObject data = (JSONObject) v3responseObject.get("Data");
            if (data.containsKey("Initiation")) {
                JSONObject initiation = (JSONObject) data.get("Initiation");
                if (initiation.containsKey("CreditorAgent")) {
                    JSONObject creditorAgent = (JSONObject) initiation.get("CreditorAgent");
                    if (creditorAgent.containsKey("PostalAddress")) {
                        JSONObject postalAddress = (JSONObject) creditorAgent.get("PostalAddress");
                        if (postalAddress.containsKey("AddressType")) {
                            postalAddress.put("AddressType",
                                    ISO_20022_UPDATED_ADDRESS_TYPE_MAP.get(postalAddress.get("AddressType")));
                        }
                    }
                }
            }
        }
    }

    /**
     * Update the API v3 payment order resources RemittanceInformation object to API v4.
     *
     * @param v3responseObject API v4 RemittanceInformation response object
     */
    private void handleRemittanceInformation(JSONObject v3responseObject) {
        if (v3responseObject.containsKey("Data")) {
            JSONObject data = (JSONObject) v3responseObject.get("Data");
            if (data.containsKey("Initiation")) {
                JSONObject initiation = (JSONObject) data.get("Initiation");
                if (initiation.containsKey("RemittanceInformation")) {
                    JSONObject creditorPostalAddress = (JSONObject) initiation.get("RemittanceInformation");
                    creditorPostalAddress.remove("Reference");
                    if (creditorPostalAddress.containsKey("Unstructured")) {
                        String unstructured = (String) creditorPostalAddress.get("Unstructured");
                        JSONArray unstructuredArray = new JSONArray();
                        unstructuredArray.add(unstructured);
                        creditorPostalAddress.put("Unstructured", unstructuredArray);
                    }
                }
            }
        }
    }

    /**
     * Update the API v3 payment order resources MandateRelatedInformation object to API v4.
     *
     * @param v3responseObject API v4 MandateRelatedInformation response object
     */
    private void handleMandateRelatedInformation(JSONObject v3responseObject) {
        if (v3responseObject.containsKey("Data")) {
            JSONObject data = (JSONObject) v3responseObject.get("Data");
            if (data.containsKey("Initiation")) {
                JSONObject initiation = (JSONObject) data.get("Initiation");
                if (data.containsKey("Frequency")) {
                    JSONObject mandateRelatedInformation = new JSONObject();
                    initiation.remove("Reference");
                    initiation.remove("NumberOfPayments");
                    initiation.remove("Frequency");
                    if (initiation.containsKey("FirstPaymentDateTime")) {
                        JSONObject firstPaymentDateTime = (JSONObject) initiation.get("FirstPaymentDateTime");
                        mandateRelatedInformation.put("FirstPaymentDateTime", firstPaymentDateTime);
                    }
                    if (initiation.containsKey("FinalPaymentDateTime")) {
                        JSONObject firstPaymentDateTime = (JSONObject) initiation.get("FinalPaymentDateTime");
                        mandateRelatedInformation.put("FinalPaymentDateTime", firstPaymentDateTime);
                    }
                    if (initiation.containsKey("RecurringPaymentDateTime")) {
                        JSONObject firstPaymentDateTime = (JSONObject) initiation.get("RecurringPaymentDateTime");
                        mandateRelatedInformation.put("RecurringPaymentDateTime", firstPaymentDateTime);
                    }
                    JSONObject frequency = new JSONObject();
                    frequency.put("Type", "DAIL");
                    mandateRelatedInformation.put("Frequency", frequency);
                    initiation.put("MandateRelatedInformation", mandateRelatedInformation);
                }
            }
        }
    }

    /**
     * Update the API v3 payment order resources CreditorPostalAddress object to API v4.
     *
     * @param v3responseObject API v4 CreditorPostalAddress response object
     */
    private void handleCreditorPostalAddress(JSONObject v3responseObject) {
        if (v3responseObject.containsKey("Data")) {
            JSONObject data = (JSONObject) v3responseObject.get("Data");
            if (data.containsKey("Initiation")) {
                JSONObject initiation = (JSONObject) data.get("Initiation");
                if (initiation.containsKey("CreditorPostalAddress")) {
                    JSONObject creditorPostalAddress = (JSONObject) initiation.get("CreditorPostalAddress");
                    if (creditorPostalAddress.containsKey("AddressType")) {
                        creditorPostalAddress.put("AddressType",
                                ISO_20022_UPDATED_ADDRESS_TYPE_MAP.get(creditorPostalAddress.get("AddressType")));
                    }
                }
            }
        }
    }

    /**
     * Class PaymentResponseCacheModel
     * Static inter class to wrap the payment response cache model with API spec version.
     */
    private static class PaymentResponseCacheModel {
        private CommonConstants.UKApiVersion apiSpecVersion;
        private JSONObject cacheResponse;

        public PaymentResponseCacheModel(CommonConstants.UKApiVersion apiSpecVersion, JSONObject cacheResponse) {
            this.apiSpecVersion = apiSpecVersion;
            this.cacheResponse = cacheResponse;
        }

        public CommonConstants.UKApiVersion getApiSpecVersion() {
            return apiSpecVersion;
        }

        public void setApiSpecVersion(CommonConstants.UKApiVersion apiSpecVersion) {
            this.apiSpecVersion = apiSpecVersion;
        }

        public JSONObject getCacheResponse() {
            return cacheResponse;
        }

        public void setCacheResponse(JSONObject cacheResponse) {
            this.cacheResponse = cacheResponse;
        }
    }
}
