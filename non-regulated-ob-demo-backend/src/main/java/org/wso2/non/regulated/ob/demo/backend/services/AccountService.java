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
import org.wso2.non.regulated.ob.demo.backend.util.CommonConstants;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * AccountService class.
 */
@Path("/accountservice/")
public class AccountService {

    @GET
    @Path("/accounts")
    @Produces("application/json; charset=utf-8")
    public Response getAccounts(@HeaderParam("x-fapi-interaction-id") String xFapiInteractionId,
                                @HeaderParam("Account-Request-Information") String accountRequestInfo)
            throws ParseException {

        JSONObject accountRequestInformation = getRequest(accountRequestInfo);
        List<String> accountRequestIds = getAccountIds(accountRequestInformation);
        JSONArray permissions = getPermissions(accountRequestInformation);
        CommonConstants.UKApiVersion specVersion = getAPISpecVersionFromRequestInfo(accountRequestInformation);
        // API v4 updated parameters.
        String accountType = "AccountType";
        String accountSubType = "AccountSubType";
        String accountSubTypeValue = "CurrentAccount";
        String sortCodeAccountNumber = "SortCodeAccountNumber";

        if (specVersion.equals(CommonConstants.UKApiVersion.UK_API_V400)) {
            accountType = "AccountCategory";
            accountSubType = "AccountTypeCode";
            accountSubTypeValue = "CACC";
            sortCodeAccountNumber = "CLAV";
        }

        StringBuilder builder = new StringBuilder();
        for (String accountId : accountRequestIds) {
            String temp = "";
            if (permissions.contains(OBExternalPermissions1Code.ReadAccountsDetail.name())) {
                temp =
                    "      {\n" +
                    "        \"AccountId\": \"" + accountId + "\",\n" +
                    "        \"Status\": \"Enabled\",\n" +
                    "        \"StatusUpdateDateTime\": \"2020-04-16T06:06:06+00:00\",\n" +
                    "        \"Currency\": \"GBP\",\n" +
                    "        \"" + accountType + "\": \"Personal\",\n" +
                    "        \"" + accountSubType + "\": \"" + accountSubTypeValue + "\",\n" +
                    "        \"Nickname\": \"Bills\",\n" +
                    "        \"OpeningDate\": \"2020-01-16T06:06:06+00:00\",\n" +
                    "        \"MaturityDate\": \"2025-04-16T06:06:06+00:00\",\n" +
                    "        \"Account\": [{\n" +
                    "          \"SchemeName\": \"" + sortCodeAccountNumber + "\",\n" +
                    "          \"Identification\": \"" + accountId + "\",\n" +
                    "          \"Name\": \"Mr Kevin\",\n" +
                    "          \"SecondaryIdentification\": \"00021\"\n" +
                    "        }]\n" +
                    "      }";
            } else if (permissions.contains(OBExternalPermissions1Code.ReadAccountsBasic.name())) {
                temp =
                    "      {\n" +
                    "        \"AccountId\": \"" + accountId + "\",\n" +
                    "        \"Status\": \"Enabled\",\n" +
                    "        \"StatusUpdateDateTime\": \"2020-04-16T06:06:06+00:00\",\n" +
                    "        \"Currency\": \"GBP\",\n" +
                    "        \"" + accountType + "\": \"Personal\",\n" +
                    "        \"" + accountSubType + "\": \"" + accountSubTypeValue + "\",\n" +
                    "        \"Nickname\": \"Bills\",\n" +
                    "        \"OpeningDate\": \"2020-01-16T06:06:06+00:00\",\n" +
                    "        \"MaturityDate\": \"2025-04-16T06:06:06+00:00\"\n" +
                    "      }";
            }
            if (builder.length() > 0 && temp.length() > 0) {
                builder.append(",\n").append(temp);
            } else {
                builder.append(temp);
            }
        }
        if (builder.length() > 0) {
            String finalRespose = "{\n" +
                    "  \"Data\": {\n" +
                    "    \"Account\": [\n" +
                    builder.toString() +  "\n" +
                    "    ]\n" +
                    "  },\n" +
                    "  \"Links\": {\n" +
                    "    \"Self\": \"https://api.alphabank.com/open-banking/v3.0/accounts\"\n" +
                    "  },\n" +
                    "  \"Meta\": {\n" +
                    "    \"TotalPages\": 1\n" +
                    "  }\n" +
                    "}";
            return Response.status(200).entity(finalRespose)
                    .header("x-fapi-interaction-id", xFapiInteractionId).build();
        }
        return Response.status(403).build();
    }

    @GET
    @Path("/accounts/{AccountId}")
    @Produces("application/json; charset=utf-8")
    public Response getOneAccount(@PathParam("AccountId") String accountId,
                                  @HeaderParam("x-fapi-interaction-id") String xFapiInteractionId,
                                  @HeaderParam("Account-Request-Information") String accountRequestInfo)
            throws ParseException {

        JSONObject accountRequestInformation = getRequest(accountRequestInfo);
        JSONArray permissions = getPermissions(accountRequestInformation);
        CommonConstants.UKApiVersion specVersion = getAPISpecVersionFromRequestInfo(accountRequestInformation);
        // API v4 updated parameters.
        String accountType = "AccountType";
        String accountSubType = "AccountSubType";
        String accountSubTypeValue = "CurrentAccount";
        String sortCodeAccountNumber = "SortCodeAccountNumber";

        if (specVersion.equals(CommonConstants.UKApiVersion.UK_API_V400)) {
            accountType = "AccountCategory";
            accountSubType = "AccountTypeCode";
            accountSubTypeValue = "CACC";
            sortCodeAccountNumber = "CLAV";
        }
        if (permissions.contains(OBExternalPermissions1Code.ReadAccountsDetail.name())) {
            String response = "{\n" +
                "  \"Data\": {\n" +
                "    \"Account\": [\n" +
                "      {\n" +
                "        \"AccountId\": \"" + accountId + "\",\n" +
                "        \"Status\": \"Enabled\",\n" +
                "        \"StatusUpdateDateTime\": \"2020-04-16T06:06:06+00:00\",\n" +
                "        \"Currency\": \"GBP\",\n" +
                "        \"" + accountType + "\": \"Personal\",\n" +
                "        \"" + accountSubType + "\": \"" + accountSubTypeValue + "\",\n" +
                "        \"Nickname\": \"Bills\",\n" +
                "        \"OpeningDate\": \"2020-01-16T06:06:06+00:00\",\n" +
                "        \"MaturityDate\": \"2025-04-16T06:06:06+00:00\",\n" +
                "        \"Account\": [{\n" +
                "          \"SchemeName\": \"" + sortCodeAccountNumber + "\",\n" +
                "          \"Identification\": \"" + accountId + "\",\n" +
                "          \"Name\": \"Mr Kevin\",\n" +
                "          \"SecondaryIdentification\": \"00021\"\n" +
                "        }]\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"Links\": {\n" +
                "    \"Self\": \"https://api.alphabank.com/open-banking/v3.0/accounts/" + accountId +
                "\"\n" +
                "  },\n" +
                "  \"Meta\": {\n" +
                "    \"TotalPages\": 1\n" +
                "  }\n" +
                "}";
            return Response.status(200).entity(response)
                    .header("x-fapi-interaction-id", xFapiInteractionId).build();
        } else if (permissions.contains(OBExternalPermissions1Code.ReadAccountsBasic.name())) {
            String response = "{\n" +
                "  \"Data\": {\n" +
                "    \"Account\": [\n" +
                "      {\n" +
                "        \"AccountId\": \"" + accountId + "\",\n" +
                "        \"Status\": \"Enabled\",\n" +
                "        \"StatusUpdateDateTime\": \"2020-04-16T06:06:06+00:00\",\n" +
                "        \"Currency\": \"GBP\",\n" +
                "        \"" + accountType + "\": \"Personal\",\n" +
                "        \"" + accountSubType + "\": \"" + accountSubTypeValue + "\",\n" +
                "        \"Nickname\": \"Bills\",\n" +
                "        \"OpeningDate\": \"2020-01-16T06:06:06+00:00\",\n" +
                "        \"MaturityDate\": \"2025-04-16T06:06:06+00:00\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"Links\": {\n" +
                "    \"Self\": \"https://api.alphabank.com/open-banking/v3.0/accounts/" + accountId +
                "\"\n" +
                "  },\n" +
                "  \"Meta\": {\n" +
                "    \"TotalPages\": 1\n" +
                "  }\n" +
                "}";
            return Response.status(200).entity(response)
                    .header("x-fapi-interaction-id", xFapiInteractionId).build();
        }

        return Response.status(403).build();
    }

    @GET
    @Path("/accounts/{AccountId}/transactions")
    @Produces("application/json; charset=utf-8")
    public Response getAccountTransactions(@PathParam("AccountId") String accountId,
                                           @HeaderParam("x-fapi-interaction-id") String xFapiInteractionId,
                                           @HeaderParam("Account-Request-Information") String accountRequestInfo)
            throws ParseException {

        JSONObject accountRequestInformation = getRequest(accountRequestInfo);
        JSONArray permissions = getPermissions(accountRequestInformation);
        String creditDebitIndicator = permissions.contains(
                OBExternalPermissions1Code.ReadTransactionsDebits.name()) &&
                !permissions.contains(
                        OBExternalPermissions1Code.ReadTransactionsCredits.name()) ? "Debit" : "Credit";
        CommonConstants.UKApiVersion specVersion = getAPISpecVersionFromRequestInfo(accountRequestInformation);
        // API v4 updated parameters.
        String balanceTypeValue = "InterimBooked";
        String status = "Booked";

        if (specVersion.equals(CommonConstants.UKApiVersion.UK_API_V400)) {
            balanceTypeValue = "ITBD";
            status = "BOOK";
        }
        if (permissions.contains(OBExternalPermissions1Code.ReadTransactionsDetail.name())) {
            String response = "{\n" +
                "  \"Data\": {\n" +
                "    \"Transaction\": [\n" +
                "      {\n" +
                "        \"AccountId\": \"" + accountId + "\",\n" +
                "        \"TransactionId\": \"123\",\n" +
                "        \"TransactionReference\": \"Ref 1\",\n" +
                "        \"Amount\": {\n" +
                "          \"Amount\": \"10.00\",\n" +
                "          \"Currency\": \"GBP\"\n" +
                "        },\n" +
                "        \"CreditDebitIndicator\": \"" + creditDebitIndicator + "\",\n" +
                "        \"Status\": \"" + status + "\",\n" +
                "        \"BookingDateTime\": \"2017-04-05T10:43:07+00:00\",\n" +
                "        \"ValueDateTime\": \"2017-04-05T10:45:22+00:00\",\n" +
                "        \"TransactionInformation\": \"Cash from Aubrey\",\n" +
                "        \"BankTransactionCode\": {\n" +
                "          \"Code\": \"str\",\n" +
                "          \"SubCode\": \"str\"\n" +
                "        },\n" +
                "        \"ProprietaryBankTransactionCode\": {\n" +
                "          \"Code\": \"Transfer\",\n" +
                "          \"Issuer\": \"AlphaBank\"\n" +
                "        },\n" +
                "        \"Balance\": {\n" +
                "          \"Amount\": {\n" +
                "            \"Amount\": \"230.00\",\n" +
                "            \"Currency\": \"GBP\"\n" +
                "          },\n" +
                "          \"CreditDebitIndicator\": \"Credit\",\n" +
                "          \"Type\": \"" + balanceTypeValue + "\"\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"Links\": {\n" +
                "    \"Self\": \"https://api.alphabank.com/open-banking/v3.0/accounts/" + accountId +
                "/transactions/\"\n" +
                "  },\n" +
                "  \"Meta\": {\n" +
                "    \"TotalPages\": 1,\n" +
                "    \"FirstAvailableDateTime\": \"2017-05-03T00:00:00+00:00\",\n" +
                "    \"LastAvailableDateTime\": \"2017-12-03T00:00:00+00:00\"\n" +
                "  }\n" +
                "}";
            return Response.status(200).entity(response)
                    .header("x-fapi-interaction-id", xFapiInteractionId).build();
        } else if (permissions.contains(OBExternalPermissions1Code.ReadTransactionsBasic.name())) {
            String response = "{\n" +
                "  \"Data\": {\n" +
                "    \"Transaction\": [\n" +
                "      {\n" +
                "        \"AccountId\": \"" + accountId + "\",\n" +
                "        \"TransactionId\": \"123\",\n" +
                "        \"TransactionReference\": \"Ref 1\",\n" +
                "        \"Amount\": {\n" +
                "          \"Amount\": \"10.00\",\n" +
                "          \"Currency\": \"GBP\"\n" +
                "        },\n" +
                "        \"CreditDebitIndicator\": \"" + creditDebitIndicator + "\",\n" +
                "        \"Status\": \"" + status + "\",\n" +
                "        \"BookingDateTime\": \"2017-04-05T10:43:07+00:00\",\n" +
                "        \"ValueDateTime\": \"2017-04-05T10:45:22+00:00\",\n" +
                "        \"BankTransactionCode\": {\n" +
                "          \"Code\": \"str\",\n" +
                "          \"SubCode\": \"str\"\n" +
                "        },\n" +
                "        \"ProprietaryBankTransactionCode\": {\n" +
                "          \"Code\": \"Transfer\",\n" +
                "          \"Issuer\": \"AlphaBank\"\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"Links\": {\n" +
                "    \"Self\": \"https://api.alphabank.com/open-banking/v3.0/accounts/" + accountId +
                "/transactions/\"\n" +
                "  },\n" +
                "  \"Meta\": {\n" +
                "    \"TotalPages\": 1,\n" +
                "    \"FirstAvailableDateTime\": \"2017-05-03T00:00:00+00:00\",\n" +
                "    \"LastAvailableDateTime\": \"2017-12-03T00:00:00+00:00\"\n" +
                "  }\n" +
                "}";
            return Response.status(200).entity(response)
                    .header("x-fapi-interaction-id", xFapiInteractionId).build();

        }

        return Response.status(403).build();
    }

    @GET
    @Path("/transactions")
    @Produces("application/json; charset=utf-8")
    public Response getBulkTransactions(@HeaderParam("x-fapi-interaction-id") String xFapiInteractionId,
                                        @HeaderParam("Account-Request-Information") String accountRequestInfo)
            throws ParseException {

        JSONObject accountRequestInformation = getRequest(accountRequestInfo);
        List<String> accountRequestIds = getAccountIds(accountRequestInformation);
        JSONArray permissions = getPermissions(accountRequestInformation);
        CommonConstants.UKApiVersion specVersion = getAPISpecVersionFromRequestInfo(accountRequestInformation);
        // API v4 updated parameters.
        String balanceTypeValue = "InterimBooked";
        String status = "Booked";

        if (specVersion.equals(CommonConstants.UKApiVersion.UK_API_V400)) {
            balanceTypeValue = "ITBD";
            status = "BOOK";
        }
        StringBuilder builder = new StringBuilder();
        for (String accountId : accountRequestIds) {
            String creditDebitIndicator =
                    permissions.contains(OBExternalPermissions1Code.ReadTransactionsDebits.name())
                            && !permissions
                            .contains(OBExternalPermissions1Code.ReadTransactionsCredits.name()) ? "Debit" : "Credit";
            String temp = "";
            if (permissions.contains(OBExternalPermissions1Code.ReadTransactionsDetail.name())) {
                temp = "{\n" +
                    "        \"AccountId\": \"" + accountId + "\",\n" +
                    "        \"TransactionId\": \"123\",\n" +
                    "        \"TransactionReference\": \"Ref 1\",\n" +
                    "        \"Amount\": {\n" +
                    "          \"Amount\": \"10.00\",\n" +
                    "          \"Currency\": \"GBP\"\n" +
                    "        },\n" +
                    "        \"CreditDebitIndicator\": \"" + creditDebitIndicator + "\",\n" +
                    "        \"Status\": \"" + status + "\",\n" +
                    "        \"BookingDateTime\": \"2017-04-05T10:43:07+00:00\",\n" +
                    "        \"ValueDateTime\": \"2017-04-05T10:45:22+00:00\",\n" +
                    "        \"TransactionInformation\": \"Cash from Aubrey\",\n" +
                    "        \"BankTransactionCode\": {\n" +
                    "          \"Code\": \"str\",\n" +
                    "          \"SubCode\": \"str\"\n" +
                    "        },\n" +
                    "        \"ProprietaryBankTransactionCode\": {\n" +
                    "          \"Code\": \"Transfer\",\n" +
                    "          \"Issuer\": \"AlphaBank\"\n" +
                    "        },\n" +
                    "        \"Balance\": {\n" +
                    "          \"Amount\": {\n" +
                    "            \"Amount\": \"230.00\",\n" +
                    "            \"Currency\": \"GBP\"\n" +
                    "          },\n" +
                    "          \"CreditDebitIndicator\": \"Credit\",\n" +
                    "          \"Type\": \"" + balanceTypeValue + "\"\n" +
                    "        }\n" +
                    "      }";
            } else if (permissions.contains(OBExternalPermissions1Code.ReadTransactionsBasic.name())) {
                temp = "{\n" +
                    "        \"AccountId\": \"" + accountId + "\",\n" +
                    "        \"TransactionId\": \"123\",\n" +
                    "        \"TransactionReference\": \"Ref 1\",\n" +
                    "        \"Amount\": {\n" +
                    "          \"Amount\": \"10.00\",\n" +
                    "          \"Currency\": \"GBP\"\n" +
                    "        },\n" +
                    "        \"CreditDebitIndicator\": \"" + creditDebitIndicator + "\",\n" +
                    "        \"Status\": \"" + status + "\",\n" +
                    "        \"BookingDateTime\": \"2017-04-05T10:43:07+00:00\",\n" +
                    "        \"ValueDateTime\": \"2017-04-05T10:45:22+00:00\",\n" +
                    "        \"BankTransactionCode\": {\n" +
                    "          \"Code\": \"str\",\n" +
                    "          \"SubCode\": \"str\"\n" +
                    "        },\n" +
                    "        \"ProprietaryBankTransactionCode\": {\n" +
                    "          \"Code\": \"Transfer\",\n" +
                    "          \"Issuer\": \"AlphaBank\"\n" +
                    "        }\n" +
                    "      }";
            }
            if (builder.length() > 0 && temp.length() > 0) {
                builder.append(",\n").append(temp);
            } else {
                builder.append(temp);
            }
        }

        if (builder.length() > 0) {
            String finalRespose = "{\n" +
                "  \"Data\": {\n" +
                "    \"Transaction\": [\n" +
                builder.toString() +  "\n" +
                "    ]\n" +
                "  },\n" +
                "  \"Links\": {\n" +
                "    \"Self\": \"https://api.alphabank.com/open-banking/v3.0/transactions/\"\n" +
                "  },\n" +
                "  \"Meta\": {\n" +
                "    \"TotalPages\": 1\n" +
                "  }\n" +
                "}";
            return Response.status(200).entity(finalRespose)
                    .header("x-fapi-interaction-id", xFapiInteractionId).build();
        }
        return Response.status(403).build();
    }

    @GET
    @Path("/accounts/{AccountId}/beneficiaries")
    @Produces("application/json; charset=utf-8")
    public Response getAccountBeneficiaries(@PathParam("AccountId") String accountId,
                                            @HeaderParam("x-fapi-interaction-id") String xFapiInteractionId,
                                            @HeaderParam("Account-Request-Information") String accountRequestInfo)
            throws ParseException {

        JSONObject accountRequestInformation = getRequest(accountRequestInfo);
        JSONArray permissions = getPermissions(accountRequestInformation);
        CommonConstants.UKApiVersion specVersion = getAPISpecVersionFromRequestInfo(accountRequestInformation);
        // API v4 updated parameters.
        String creditorAccountSchemaValue = "SortCodeAccountNumber";

        if (specVersion.equals(CommonConstants.UKApiVersion.UK_API_V400)) {
            creditorAccountSchemaValue = "CLAV";
        }
        if (permissions.contains(OBExternalPermissions1Code.ReadBeneficiariesDetail.name())) {
            String response = "{\n" +
                "  \"Data\": {\n" +
                "    \"Beneficiary\": [\n" +
                "      {\n" +
                "        \"AccountId\": \"" + accountId + "\",\n" +
                "        \"BeneficiaryId\": \"Ben1\",\n" +
                "        \"Reference\": \"Towbar Club\",\n" +
                "        \"CreditorAccount\": {\n" +
                "          \"SchemeName\": \"" + creditorAccountSchemaValue + "\",\n" +
                "          \"Identification\": \"80200112345678\",\n" +
                "          \"Name\": \"Mrs Juniper\"\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"Links\": {\n" +
                "    \"Self\": \"https://api.alphabank.com/open-banking/v3.0/accounts/" + accountId +
                "/beneficiaries/\"\n" +
                "  },\n" +
                "  \"Meta\": {\n" +
                "    \"TotalPages\": 1\n" +
                "  }\n" +
                "}";
            return Response.status(200).entity(response).header("x-fapi-interaction-id", xFapiInteractionId)
                    .build();
        } else if (permissions.contains(OBExternalPermissions1Code.ReadBeneficiariesBasic.name())) {
            String response = "{\n" +
                "  \"Data\": {\n" +
                "    \"Beneficiary\": [\n" +
                "      {\n" +
                "        \"AccountId\": \"" + accountId + "\",\n" +
                "        \"BeneficiaryId\": \"Ben1\",\n" +
                "        \"Reference\": \"Towbar Club\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"Links\": {\n" +
                "    \"Self\": \"https://api.alphabank.com/open-banking/v3.0/accounts/" + accountId +
                "/beneficiaries/\"\n" +
                "  },\n" +
                "  \"Meta\": {\n" +
                "    \"TotalPages\": 1\n" +
                "  }\n" +
                "}";
            return Response.status(200).entity(response).header("x-fapi-interaction-id", xFapiInteractionId)
                    .build();
        }
        return Response.status(403).build();
    }

    @GET
    @Path("/beneficiaries")
    @Produces("application/json; charset=utf-8")
    public Response getBulkBeneficiaries(@HeaderParam("x-fapi-interaction-id") String xFapiInteractionId,
                                         @HeaderParam("Account-Request-Information") String accountRequestInfo)
            throws ParseException {

        JSONObject accountRequestInformation = getRequest(accountRequestInfo);
        List<String> accountRequestIds = getAccountIds(accountRequestInformation);
        JSONArray permissions = getPermissions(accountRequestInformation);
        CommonConstants.UKApiVersion specVersion = getAPISpecVersionFromRequestInfo(accountRequestInformation);
        // API v4 updated parameters.
        String creditorAccountSchemaValue = "SortCodeAccountNumber";

        if (specVersion.equals(CommonConstants.UKApiVersion.UK_API_V400)) {
            creditorAccountSchemaValue = "CLAV";
        }
        StringBuilder builder = new StringBuilder();
        for (String accountId : accountRequestIds) {
            String temp = "";
            if (permissions.contains(OBExternalPermissions1Code.ReadBeneficiariesDetail.name())) {
                temp = "{\n" +
                   "        \"AccountId\": \"" + accountId + "\",\n" +
                   "        \"BeneficiaryId\": \"Ben1\",\n" +
                   "        \"Reference\": \"Towbar Club\",\n" +
                   "        \"CreditorAccount\": {\n" +
                   "          \"SchemeName\": \"" + creditorAccountSchemaValue + "\",\n" +
                   "          \"Identification\": \"80200112345678\",\n" +
                   "          \"Name\": \"Mrs Juniper\"\n" +
                   "        }\n" +
                   "      }";
            } else if (permissions.contains(OBExternalPermissions1Code.ReadBeneficiariesBasic.name())) {
                temp = "{\n" +
                    "        \"AccountId\": \"" + accountId + "\",\n" +
                    "        \"BeneficiaryId\": \"Ben1\",\n" +
                    "        \"Reference\": \"Towbar Club\"\n" +
                    "      }";
            }
            if (builder.length() > 0 && temp.length() > 0) {
                builder.append(",\n").append(temp);
            } else {
                builder.append(temp);
            }
        }

        if (builder.length() > 0) {
            String finalRespose = "{\n" +
                "  \"Data\": {\n" +
                "    \"Beneficiary\": [\n" +
                builder.toString() +  "\n" +
                "    ]\n" +
                "  },\n" +
                "  \"Links\": {\n" +
                "    \"Self\": \"https://api.alphabank.com/open-banking/v3.0/beneficiaries/\"\n" +
                "  },\n" +
                "  \"Meta\": {\n" +
                "    \"TotalPages\": 1\n" +
                "  }\n" +
                "}";
            return Response.status(200).entity(finalRespose)
                    .header("x-fapi-interaction-id", xFapiInteractionId).build();
        }
        return Response.status(403).build();
    }

    @GET
    @Path("/accounts/{AccountId}/balances")
    @Produces("application/json; charset=utf-8")
    public Response getAccountBalance(@PathParam("AccountId") String accountId,
                                      @HeaderParam("x-fapi-interaction-id") String xFapiInteractionId,
                                      @HeaderParam("Account-Request-Information") String accountRequestInfo)
            throws ParseException {

        JSONObject accountRequestInformation = getRequest(accountRequestInfo);
        JSONArray permissions = getPermissions(accountRequestInformation);
        CommonConstants.UKApiVersion specVersion = getAPISpecVersionFromRequestInfo(accountRequestInformation);
        // API v4 updated parameters.
        String balanceTypeValue = "InterimAvailable";

        if (specVersion.equals(CommonConstants.UKApiVersion.UK_API_V400)) {
            balanceTypeValue = "CLAV";
        }
        if (permissions.contains(OBExternalPermissions1Code.ReadBalances.name())) {
            String response = "{\n" +
                "  \"Data\": {\n" +
                "    \"Balance\": [\n" +
                "      {\n" +
                "        \"AccountId\": \"" + accountId + "\",\n" +
                "        \"Amount\": {\n" +
                "          \"Amount\": \"1230.00\",\n" +
                "          \"Currency\": \"GBP\"\n" +
                "        },\n" +
                "        \"CreditDebitIndicator\": \"Credit\",\n" +
                "        \"Type\": \"" + balanceTypeValue + "\",\n" +
                "        \"DateTime\": \"2017-04-05T10:43:07+00:00\",\n" +
                "        \"CreditLine\": [\n" +
                "          {\n" +
                "            \"Included\": true,\n" +
                "            \"Amount\": {\n" +
                "              \"Amount\": \"1000.00\",\n" +
                "              \"Currency\": \"GBP\"\n" +
                "            },\n" +
                "            \"Type\": \"Pre-Agreed\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"Links\": {\n" +
                "    \"Self\": \"https://api.alphabank.com/open-banking/v3.0/accounts/" + accountId +
                "/balances/\"\n" +
                "  },\n" +
                "  \"Meta\": {\n" +
                "    \"TotalPages\": 1\n" +
                "  }\n" +
                "}";
            return Response.status(200).entity(response)
                    .header("x-fapi-interaction-id", xFapiInteractionId).build();
        } else {
            return Response.status(403).build();
        }
    }

    @GET
    @Path("/balances")
    @Produces("application/json; charset=utf-8")
    public Response getBulkBalance(@HeaderParam("x-fapi-interaction-id") String xFapiInteractionId,
                                   @HeaderParam("Account-Request-Information") String accountRequestInfo)
            throws ParseException {

        JSONObject accountRequestInformation = getRequest(accountRequestInfo);
        List<String> accountRequestIds = getAccountIds(accountRequestInformation);
        JSONArray permissions = getPermissions(accountRequestInformation);
        CommonConstants.UKApiVersion specVersion = getAPISpecVersionFromRequestInfo(accountRequestInformation);
        // API v4 updated parameters.
        String typeValue = "InterimBooked";

        if (specVersion.equals(CommonConstants.UKApiVersion.UK_API_V400)) {
            typeValue = "ITBD";
        }
        StringBuilder builder = new StringBuilder();
        for (String accountId : accountRequestIds) {
            String temp = "";
            if (permissions.contains(OBExternalPermissions1Code.ReadBalances.name())) {
                temp = "{\n" +
                    "        \"AccountId\": \"" + accountId + "\",\n" +
                    "        \"Amount\": {\n" +
                    "          \"Amount\": \"57.36\",\n" +
                    "          \"Currency\": \"GBP\"\n" +
                    "        },\n" +
                    "        \"CreditDebitIndicator\": \"Debit\",\n" +
                    "        \"Type\": \"" + typeValue + "\",\n" +
                    "        \"DateTime\": \"2017-05-02T14:22:09+00:00\"\n" +
                    "      }";
            }
            if (builder.length() > 0 && temp.length() > 0) {
                builder.append(",\n").append(temp);
            } else {
                builder.append(temp);
            }
        }

        if (builder.length() > 0) {
            String finalRespose = "{\n" +
                "  \"Data\": {\n" +
                "    \"Balance\": [\n" +
                builder.toString() + "\n" +
                "    ]\n" +
                "  },\n" +
                "  \"Links\": {\n" +
                "    \"Self\": \"https://api.alphabank.com/open-banking/v3.0/balances/\"\n" +
                "  },\n" +
                "  \"Meta\": {\n" +
                "    \"TotalPages\": 1\n" +
                "  }\n" +
                "}";
            return Response.status(200).entity(finalRespose)
                    .header("x-fapi-interaction-id", xFapiInteractionId).build();
        }
        return Response.status(403).build();
    }

    @GET
    @Path("/accounts/{AccountId}/direct-debits")
    @Produces("application/json; charset=utf-8")
    public Response getAccountDirectDebits(@PathParam("AccountId") String accountId,
                                           @HeaderParam("x-fapi-interaction-id") String xFapiInteractionId,
                                           @HeaderParam("Account-Request-Information") String accountRequestInfo)
            throws ParseException {

        JSONObject accountRequestInformation = getRequest(accountRequestInfo);
        JSONArray permissions = getPermissions(accountRequestInformation);
        CommonConstants.UKApiVersion specVersion = getAPISpecVersionFromRequestInfo(accountRequestInformation);

        // API v4 updated parameters.
        String directDebitStatusCode = "Active";

        if (specVersion.equals(CommonConstants.UKApiVersion.UK_API_V400)) {
            directDebitStatusCode = "ACTV";
        }

        if (permissions.contains(OBExternalPermissions1Code.ReadDirectDebits.name())) {
            StringBuilder response = new StringBuilder("{\n" +
                "  \"Data\": {\n" +
                "    \"DirectDebit\": [\n" +
                "      {\n" +
                "        \"AccountId\": \"" + accountId + "\",\n" +
                "        \"DirectDebitId\": \"DD03\",\n");
        if (specVersion.equals(CommonConstants.UKApiVersion.UK_API_V400)) {
            response.append(
                "        \"MandateRelatedInformation\": {\n" +
                "            \"CategoryPurposeCode\": \"BONU\",\n" +
                "            \"Classification\": \"FIXE\",\n" +
                "            \"FinalPaymentDateTime\": \"2024-04-25T12:46:49.425Z\",\n" +
                "            \"FirstPaymentDateTime\": \"2024-04-25T12:46:49.425Z\",\n" +
                "            \"Frequency\": {\n" +
                "                \"CountPerPeriod\": 1,\n" +
                "                \"PointInTime\": \"00\",\n" +
                "                \"Type\": \"MNTH\"\n" +
                "            },\n" +
                "            \"MandateIdentification\": \"Golfers\",\n" +
                "            \"Reason\": \"To pay monthly memebership\",\n" +
                "            \"RecurringPaymentDateTime\": \"2024-04-25T12:46:49.425Z\"\n" +
                "       },");
        } else {
            response.append(
                "        \"MandateIdentification\": \"Caravanners\",\n" +
                "        \"Frequency\": \"UK.OBIE.Annual\",\n"
            );
        }
        response.append(
                "        \"DirectDebitStatusCode\": \"" + directDebitStatusCode + "\",\n" +
                "        \"Name\": \"Towbar Club 3 - We Love Towbars\",\n" +
                "        \"PreviousPaymentDateTime\": \"2017-04-05T10:43:07+00:00\",\n" +
                "        \"PreviousPaymentAmount\": {\n" +
                "          \"Amount\": \"0.57\",\n" +
                "          \"Currency\": \"GBP\"\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"Links\": {\n" +
                "    \"Self\": \"https://api.alphabank.com/open-banking/v3.0/accounts/" + accountId +
                "/direct-debits/\"\n" +
                "  },\n" +
                "  \"Meta\": {\n" +
                "    \"TotalPages\": 1\n" +
                "  }\n" +
                "}");
            return Response.status(200).entity(response.toString())
                    .header("x-fapi-interaction-id", xFapiInteractionId).build();
        } else {
            return Response.status(403).build();
        }
    }

    @GET
    @Path("/direct-debits")
    @Produces("application/json; charset=utf-8")
    public Response getBulkDirectDebits(@HeaderParam("x-fapi-interaction-id") String xFapiInteractionId,
                                        @HeaderParam("Account-Request-Information") String accountRequestInfo)
            throws ParseException {

        JSONObject accountRequestInformation = getRequest(accountRequestInfo);
        List<String> accountRequestIds = getAccountIds(accountRequestInformation);
        JSONArray permissions = getPermissions(accountRequestInformation);
        CommonConstants.UKApiVersion specVersion = getAPISpecVersionFromRequestInfo(accountRequestInformation);

        // API v4 updated parameters.
        String directDebitStatusCode = "Active";

        if (specVersion.equals(CommonConstants.UKApiVersion.UK_API_V400)) {
            directDebitStatusCode = "ACTV";
        }

        StringBuilder builder = new StringBuilder();
        for (String accountId : accountRequestIds) {
            StringBuilder temp = new StringBuilder();
            if (permissions.contains(OBExternalPermissions1Code.ReadDirectDebits.name())) {
                temp.append("{\n" +
                    "        \"AccountId\": \"" + accountId + "\",\n" +
                    "        \"DirectDebitId\": \"DD03\",\n");
                if (specVersion.equals(CommonConstants.UKApiVersion.UK_API_V400)) {
                    temp.append(
                    "        \"MandateRelatedInformation\": {\n" +
                    "            \"CategoryPurposeCode\": \"BONU\",\n" +
                    "            \"Classification\": \"FIXE\",\n" +
                    "            \"FinalPaymentDateTime\": \"2024-04-25T12:46:49.425Z\",\n" +
                    "            \"FirstPaymentDateTime\": \"2024-04-25T12:46:49.425Z\",\n" +
                    "            \"Frequency\": {\n" +
                    "                \"CountPerPeriod\": 1,\n" +
                    "                \"PointInTime\": \"00\",\n" +
                    "                \"Type\": \"MNTH\"\n" +
                    "            },\n" +
                    "            \"MandateIdentification\": \"Golfers\",\n" +
                    "            \"Reason\": \"To pay monthly memebership\",\n" +
                    "            \"RecurringPaymentDateTime\": \"2024-04-25T12:46:49.425Z\"\n" +
                    "       },");
                } else {
                    temp.append(
                    "        \"MandateIdentification\": \"Caravanners\",\n" +
                    "        \"Frequency\": \"UK.OBIE.Annual\",\n"
                    );
                }
                temp.append(
                    "        \"DirectDebitStatusCode\": \"" + directDebitStatusCode + "\",\n" +
                    "        \"Name\": \"Towbar Club 3 - We Love Towbars\",\n" +
                    "        \"PreviousPaymentDateTime\": \"2017-04-05T10:43:07+00:00\",\n" +
                    "        \"PreviousPaymentAmount\": {\n" +
                    "          \"Amount\": \"0.57\",\n" +
                    "          \"Currency\": \"GBP\"\n" +
                    "        }\n" +
                    "      }");
            }
            if (builder.length() > 0 && !temp.toString().isEmpty()) {
                builder.append(",\n").append(temp);
            } else {
                builder.append(temp);
            }
        }

        if (builder.length() > 0) {
            String finalRespose = "{\n" +
                "  \"Data\": {\n" +
                "    \"DirectDebit\": [\n" +
                builder.toString() + "\n" +
                "    ]\n" +
                "  },\n" +
                "  \"Links\": {\n" +
                "    \"Self\": \"https://api.alphabank.com/open-banking/v3.0/direct-debits/\"\n" +
                "  },\n" +
                "  \"Meta\": {\n" +
                "    \"TotalPages\": 1\n" +
                "  }\n" +
                "}";
            return Response.status(200).entity(finalRespose)
                    .header("x-fapi-interaction-id", xFapiInteractionId).build();
        }
        return Response.status(403).build();
    }

    @GET
    @Path("/accounts/{AccountId}/standing-orders")
    @Produces("application/json; charset=utf-8")
    public Response getAccountStandingOrders(@PathParam("AccountId") String accountId,
                                             @HeaderParam("x-fapi-interaction-id") String xFapiInteractionId,
                                             @HeaderParam("Account-Request-Information") String accountRequestInfo)
            throws ParseException {

        JSONObject accountRequestInformation = getRequest(accountRequestInfo);
        JSONArray permissions = getPermissions(accountRequestInformation);
        CommonConstants.UKApiVersion specVersion = getAPISpecVersionFromRequestInfo(accountRequestInformation);
        // API v4 updated parameters.
        String creditorAccountSchemaValue = "SortCodeAccountNumber";
        String standingOrderStatusCodeValue = "StandingOrderStatusCode";

        if (specVersion.equals(CommonConstants.UKApiVersion.UK_API_V400)) {
            creditorAccountSchemaValue = "CLAV";
            standingOrderStatusCodeValue = "ACTV";
        }

        if (permissions.contains(OBExternalPermissions1Code.ReadStandingOrdersDetail.name())) {
            StringBuilder response = new StringBuilder("{\n" +
                "  \"Data\": {\n" +
                "    \"StandingOrder\": [\n" +
                "      {\n" +
                "        \"AccountId\": \"" + accountId + "\",\n" +
                "        \"StandingOrderId\": \"Ben3\",\n");
            if (specVersion.equals(CommonConstants.UKApiVersion.UK_API_V400)) {
                response.append(
                        "        \"MandateRelatedInformation\": {\n" +
                        "                    \"CategoryPurposeCode\": \"BONU\",\n" +
                        "                    \"Classification\": \"FIXE\",\n" +
                        "                    \"FinalPaymentDateTime\": \"2024-04-25T12:46:49.425Z\",\n" +
                        "                    \"FirstPaymentDateTime\": \"2024-04-25T12:46:49.425Z\",\n" +
                        "                    \"Frequency\": {\n" +
                        "                        \"CountPerPeriod\": 1,\n" +
                        "                        \"PointInTime\": \"00\",\n" +
                        "                        \"Type\": \"MNTH\"\n" +
                        "                    },\n" +
                        "                    \"MandateIdentification\": \"Golfers\",\n" +
                        "                    \"Reason\": \"To pay monthly memebership\",\n" +
                        "                    \"RecurringPaymentDateTime\": \"2024-04-25T12:46:49.425Z\"\n" +
                        "                },");
            } else {
                response.append(
                        "        \"Frequency\": \"EvryWorkgDay\",\n" +
                        "        \"Reference\": \"Towbar Club 2 - We Love Towbars\",\n" +
                        "        \"FirstPaymentDateTime\": \"2017-08-12T00:00:00+00:00\",\n" +
                        "        \"FinalPaymentDateTime\": \"2027-08-12T00:00:00+00:00\",\n"
                );
            }
            response.append(
                "        \"FirstPaymentAmount\": {\n" +
                "          \"Amount\": \"0.57\",\n" +
                "          \"Currency\": \"GBP\"\n" +
                "        },\n" +
                "        \"NextPaymentDateTime\": \"2017-08-13T00:00:00+00:00\",\n" +
                "        \"NextPaymentAmount\": {\n" +
                "          \"Amount\": \"0.56\",\n" +
                "          \"Currency\": \"GBP\"\n" +
                "        },\n" +
                "        \"FinalPaymentAmount\": {\n" +
                "          \"Amount\": \"0.56\",\n" +
                "          \"Currency\": \"GBP\"\n" +
                "        },\n" +
                "        \"StandingOrderStatusCode\": \"" + standingOrderStatusCodeValue + "\",\n" +
                "        \"CreditorAccount\": {\n" +
                "          \"SchemeName\": \"" + creditorAccountSchemaValue + "\",\n" +
                "          \"Identification\": \"80200112345678\",\n" +
                "          \"Name\": \"Mrs Juniper\"\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"Links\": {\n" +
                "    \"Self\": \"https://api.alphabank.com/open-banking/v3.0/accounts/" + accountId +
                "/standing-orders/\"\n" +
                "  },\n" +
                "  \"Meta\": {\n" +
                "    \"TotalPages\": 1\n" +
                "  }\n" +
                "}");

            return Response.status(200).entity(response.toString())
                    .header("x-fapi-interaction-id", xFapiInteractionId).build();
        } else if (permissions.contains(OBExternalPermissions1Code.ReadStandingOrdersBasic.name())) {
            StringBuilder response = new StringBuilder("{\n" +
                "  \"Data\": {\n" +
                "    \"StandingOrder\": [\n" +
                "      {\n" +
                "        \"AccountId\": \"" + accountId + "\",\n" +
                "        \"StandingOrderId\": \"Ben3\",\n");
            if (specVersion.equals(CommonConstants.UKApiVersion.UK_API_V400)) {
                response.append(
                        "        \"MandateRelatedInformation\": {\n" +
                        "                    \"CategoryPurposeCode\": \"BONU\",\n" +
                        "                    \"Classification\": \"FIXE\",\n" +
                        "                    \"FinalPaymentDateTime\": \"2024-04-25T12:46:49.425Z\",\n" +
                        "                    \"FirstPaymentDateTime\": \"2024-04-25T12:46:49.425Z\",\n" +
                        "                    \"Frequency\": {\n" +
                        "                        \"CountPerPeriod\": 1,\n" +
                        "                        \"PointInTime\": \"00\",\n" +
                        "                        \"Type\": \"MNTH\"\n" +
                        "                    },\n" +
                        "                    \"MandateIdentification\": \"Golfers\",\n" +
                        "                    \"Reason\": \"To pay monthly memebership\",\n" +
                        "                    \"RecurringPaymentDateTime\": \"2024-04-25T12:46:49.425Z\"\n" +
                        "                },");
            } else {
                response.append(
                        "        \"Frequency\": \"EvryWorkgDay\",\n" +
                        "        \"Reference\": \"Towbar Club 2 - We Love Towbars\",\n" +
                        "        \"FirstPaymentDateTime\": \"2017-08-12T00:00:00+00:00\",\n" +
                        "        \"FinalPaymentDateTime\": \"2027-08-12T00:00:00+00:00\",\n"
                );
            }
            response.append(
                "        \"FirstPaymentAmount\": {\n" +
                "          \"Amount\": \"0.57\",\n" +
                "          \"Currency\": \"GBP\"\n" +
                "        },\n" +
                "        \"NextPaymentDateTime\": \"2017-08-13T00:00:00+00:00\",\n" +
                "        \"NextPaymentAmount\": {\n" +
                "          \"Amount\": \"0.56\",\n" +
                "          \"Currency\": \"GBP\"\n" +
                "        },\n" +
                "        \"FinalPaymentAmount\": {\n" +
                "          \"Amount\": \"0.56\",\n" +
                "          \"Currency\": \"GBP\"\n" +
                "        },\n" +
                "        \"StandingOrderStatusCode\": \"" + standingOrderStatusCodeValue + "\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"Links\": {\n" +
                "    \"Self\": \"https://api.alphabank.com/open-banking/v3.0/accounts/" + accountId +
                "/standing-orders/\"\n" +
                "  },\n" +
                "  \"Meta\": {\n" +
                "    \"TotalPages\": 1\n" +
                "  }\n" +
                "}");
            return Response.status(200).entity(response.toString())
                    .header("x-fapi-interaction-id", xFapiInteractionId).build();
        }

        return Response.status(403).build();
    }

    @GET
    @Path("/standing-orders")
    @Produces("application/json; charset=utf-8")
    public Response getBulkStandingOrders(@HeaderParam("x-fapi-interaction-id") String xFapiInteractionId,
                                          @HeaderParam("Account-Request-Information") String accountRequestInfo)
            throws ParseException {

        JSONObject accountRequestInformation = getRequest(accountRequestInfo);
        List<String> accountRequestIds = getAccountIds(accountRequestInformation);
        JSONArray permissions = getPermissions(accountRequestInformation);
        CommonConstants.UKApiVersion specVersion = getAPISpecVersionFromRequestInfo(accountRequestInformation);
        // API v4 updated parameters.
        String creditorAccountSchemaValue = "SortCodeAccountNumber";
        String standingOrderStatusCodeValue = "StandingOrderStatusCode";

        if (specVersion.equals(CommonConstants.UKApiVersion.UK_API_V400)) {
            creditorAccountSchemaValue = "CLAV";
            standingOrderStatusCodeValue = "ACTV";
        }
        StringBuilder builder = new StringBuilder();
        for (String accountId : accountRequestIds) {
            StringBuilder temp = new StringBuilder();
            if (permissions.contains(OBExternalPermissions1Code.ReadStandingOrdersDetail.name())) {
                temp.append("{\n" +
                    "        \"AccountId\": \"" + accountId + "\",\n" +
                    "        \"StandingOrderId\": \"Ben3\",\n");
                if (specVersion.equals(CommonConstants.UKApiVersion.UK_API_V400)) {
                    temp.append(
                            "        \"MandateRelatedInformation\": {\n" +
                            "                    \"CategoryPurposeCode\": \"BONU\",\n" +
                            "                    \"Classification\": \"FIXE\",\n" +
                            "                    \"FinalPaymentDateTime\": \"2024-04-25T12:46:49.425Z\",\n" +
                            "                    \"FirstPaymentDateTime\": \"2024-04-25T12:46:49.425Z\",\n" +
                            "                    \"Frequency\": {\n" +
                            "                        \"CountPerPeriod\": 1,\n" +
                            "                        \"PointInTime\": \"00\",\n" +
                            "                        \"Type\": \"MNTH\"\n" +
                            "                    },\n" +
                            "                    \"MandateIdentification\": \"Golfers\",\n" +
                            "                    \"Reason\": \"To pay monthly memebership\",\n" +
                            "                    \"RecurringPaymentDateTime\": \"2024-04-25T12:46:49.425Z\"\n" +
                            "                },");
                } else {
                    temp.append(
                            "        \"Frequency\": \"EvryWorkgDay\",\n" +
                            "        \"Reference\": \"Towbar Club 2 - We Love Towbars\",\n" +
                            "        \"FirstPaymentDateTime\": \"2017-08-12T00:00:00+00:00\",\n" +
                            "        \"FinalPaymentDateTime\": \"2027-08-12T00:00:00+00:00\",\n"
                    );
                }
                temp.append(
                    "        \"FirstPaymentAmount\": {\n" +
                    "          \"Amount\": \"0.57\",\n" +
                    "          \"Currency\": \"GBP\"\n" +
                    "        },\n" +
                    "        \"NextPaymentDateTime\": \"2017-08-13T00:00:00+00:00\",\n" +
                    "        \"NextPaymentAmount\": {\n" +
                    "          \"Amount\": \"0.56\",\n" +
                    "          \"Currency\": \"GBP\"\n" +
                    "        },\n" +
                    "        \"FinalPaymentAmount\": {\n" +
                    "          \"Amount\": \"0.56\",\n" +
                    "          \"Currency\": \"GBP\"\n" +
                    "        },\n" +
                    "        \"StandingOrderStatusCode\": \"" + standingOrderStatusCodeValue + "\",\n" +
                    "        \"CreditorAccount\": {\n" +
                    "          \"SchemeName\": \"" + creditorAccountSchemaValue + "\",\n" +
                    "          \"Identification\": \"80200112345678\",\n" +
                    "          \"Name\": \"Mrs Juniper\"\n" +
                    "        }\n" +
                    "      }");
            } else if (permissions.contains(OBExternalPermissions1Code.ReadStandingOrdersBasic.name())) {
                temp.append("{\n" +
                    "        \"AccountId\": \"" + accountId + "\",\n" +
                    "        \"StandingOrderId\": \"Ben3\",\n");
                if (specVersion.equals(CommonConstants.UKApiVersion.UK_API_V400)) {
                    temp.append(
                            "        \"MandateRelatedInformation\": {\n" +
                            "                    \"CategoryPurposeCode\": \"BONU\",\n" +
                            "                    \"Classification\": \"FIXE\",\n" +
                            "                    \"FinalPaymentDateTime\": \"2024-04-25T12:46:49.425Z\",\n" +
                            "                    \"FirstPaymentDateTime\": \"2024-04-25T12:46:49.425Z\",\n" +
                            "                    \"Frequency\": {\n" +
                            "                        \"CountPerPeriod\": 1,\n" +
                            "                        \"PointInTime\": \"00\",\n" +
                            "                        \"Type\": \"MNTH\"\n" +
                            "                    },\n" +
                            "                    \"MandateIdentification\": \"Golfers\",\n" +
                            "                    \"Reason\": \"To pay monthly memebership\",\n" +
                            "                    \"RecurringPaymentDateTime\": \"2024-04-25T12:46:49.425Z\"\n" +
                            "                },");
                } else {
                    temp.append(
                            "        \"Frequency\": \"EvryWorkgDay\",\n" +
                            "        \"Reference\": \"Towbar Club 2 - We Love Towbars\",\n" +
                            "        \"FirstPaymentDateTime\": \"2017-08-12T00:00:00+00:00\",\n" +
                            "        \"FinalPaymentDateTime\": \"2027-08-12T00:00:00+00:00\",\n"
                    );
                }
                temp.append(
                    "        \"FirstPaymentAmount\": {\n" +
                    "          \"Amount\": \"0.57\",\n" +
                    "          \"Currency\": \"GBP\"\n" +
                    "        },\n" +
                    "        \"NextPaymentDateTime\": \"2017-08-13T00:00:00+00:00\",\n" +
                    "        \"NextPaymentAmount\": {\n" +
                    "          \"Amount\": \"0.56\",\n" +
                    "          \"Currency\": \"GBP\"\n" +
                    "        },\n" +
                    "        \"FinalPaymentAmount\": {\n" +
                    "          \"Amount\": \"0.56\",\n" +
                    "          \"Currency\": \"GBP\"\n" +
                    "        },\n" +
                    "        \"StandingOrderStatusCode\": \"" + standingOrderStatusCodeValue + "\"\n" +
                    "      }");
            }
            if (builder.length() > 0 && !temp.toString().isEmpty()) {
                builder.append(",\n").append(temp);
            } else {
                builder.append(temp);
            }
        }

        if (builder.length() > 0) {
            String finalRespose = "{\n" +
                "  \"Data\": {\n" +
                "    \"StandingOrder\": [\n" +
                builder.toString() +  "\n" +
                "    ]\n" +
                "  },\n" +
                "  \"Links\": {\n" +
                "    \"Self\": \"https://api.alphabank.com/open-banking/v3.0/standing-orders/\"\n" +
                "  },\n" +
                "  \"Meta\": {\n" +
                "    \"TotalPages\": 1\n" +
                "  }\n" +
                "}";
            return Response.status(200).entity(finalRespose)
                    .header("x-fapi-interaction-id", xFapiInteractionId).build();
        }
        return Response.status(403).build();
    }

    @GET
    @Path("/accounts/{AccountId}/product")
    @Produces("application/json; charset=utf-8")
    public Response getAccountProducts(@PathParam("AccountId") String accountId,
                                       @HeaderParam("x-fapi-interaction-id") String xFapiInteractionId,
                                       @HeaderParam("Account-Request-Information") String accountRequestInfo)
            throws ParseException {

        JSONObject accountRequestInformation = getRequest(accountRequestInfo);
        JSONArray permissions = getPermissions(accountRequestInformation);
        if (permissions.contains(OBExternalPermissions1Code.ReadProducts.name())) {
            String response = "{\n" +
                "  \"Data\": {\n" +
                "    \"Product\": [\n" +
                "      {\n" +
                "        \"AccountId\": \"" + accountId + "\",\n" +
                "        \"ProductId\": \"51B\",\n" +
                "        \"ProductType\": \"PersonalCurrentAccount\",\n" +
                "        \"ProductName\": \"321 Product\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"Links\": {\n" +
                "    \"Self\": \"https://api.alphabank.com/open-banking/v3.0/accounts/" + accountId +
                "/product\"\n" +
                "  },\n" +
                "  \"Meta\": {\n" +
                "    \"TotalPages\": 1\n" +
                "  }\n" +
                "}";
            return Response.status(200).entity(response)
                    .header("x-fapi-interaction-id", xFapiInteractionId).build();
        } else {
            return Response.status(403).build();
        }
    }

    @GET
    @Path("/products")
    @Produces("application/json; charset=utf-8")
    public Response getBulkProducts(@HeaderParam("x-fapi-interaction-id") String xFapiInteractionId,
                                    @HeaderParam("Account-Request-Information") String accountRequestInfo)
            throws ParseException {

        JSONObject accountRequestInformation = null;
        accountRequestInformation = getRequest(accountRequestInfo);
        List<String> accountRequestIds = getAccountIds(accountRequestInformation);
        JSONArray permissions = getPermissions(accountRequestInformation);
        StringBuilder builder = new StringBuilder();
        for (String accountId : accountRequestIds) {
            String temp = "";
            if (permissions.contains(OBExternalPermissions1Code.ReadProducts.name())) {
                temp = "{\n" +
                    "        \"AccountId\": \"" + accountId + "\",\n" +
                    "        \"ProductId\": \"51B\",\n" +
                    "        \"ProductType\": \"PersonalCurrentAccount\",\n" +
                    "        \"ProductName\": \"321 Product\"\n" +
                    "      }";
            }
            if (builder.length() > 0 && temp.length() > 0) {
                builder.append(",\n").append(temp);
            } else {
                builder.append(temp);
            }
        }

        if (builder.length() > 0) {
            String finalRespose = "{\n" +
                "  \"Data\": {\n" +
                "    \"Product\": [\n" +
                builder.toString() + "\n" +
                "    ]\n" +
                "  },\n" +
                "  \"Links\": {\n" +
                "    \"Self\": \"https://api.alphabank.com/open-banking/v3.0/products/\"\n" +
                "  },\n" +
                "  \"Meta\": {\n" +
                "    \"TotalPages\": 1\n" +
                "  }\n" +
                "}";
            return Response.status(200).entity(finalRespose)
                    .header("x-fapi-interaction-id", xFapiInteractionId).build();
        }
        return Response.status(403).build();
    }

    @GET
    @Path("/accounts/{AccountId}/offers")
    @Produces("application/json; charset=utf-8")
    public Response getAccountOffers(@PathParam("AccountId") String accountId,
                                     @HeaderParam("x-fapi-interaction-id") String xFapiInteractionId,
                                     @HeaderParam("Account-Request-Information") String accountRequestInfo)
            throws ParseException {

        JSONObject accountRequestInformation = getRequest(accountRequestInfo);
        JSONArray permissions = getPermissions(accountRequestInformation);
        if (permissions.contains(OBExternalPermissions1Code.ReadOffers.name())) {
            String response = "{\n" +
                "  \"Data\": {\n" +
                "    \"Offer\": [\n" +
                "      {\n" +
                "        \"AccountId\": \"" + accountId + "\",\n" +
                "        \"OfferId\": \"Offer1\",\n" +
                "        \"OfferType\": \"LimitIncrease\",\n" +
                "        \"Description\": \"Credit limit increase for the account up to £10000.00\",\n" +
                "        \"Amount\": {\n" +
                "          \"Amount\": \"10000.00\",\n" +
                "          \"Currency\": \"GBP\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"AccountId\": \"" + accountId + "\",\n" +
                "        \"OfferId\": \"Offer2\",\n" +
                "        \"OfferType\": \"BalanceTransfer\",\n" +
                "        \"Description\": \"Balance transfer offer up to £2000\",\n" +
                "        \"Amount\": {\n" +
                "          \"Amount\": \"2000.00\",\n" +
                "          \"Currency\": \"GBP\"\n" +
                "        }\n" +
                "      }     \n" +
                "    ]\n" +
                "  },\n" +
                "  \"Links\": {\n" +
                "    \"Self\": \"https://api.alphabank.com/open-banking/v3.0/accounts/" + accountId +
                "/offers/\"\n" +
                "  },\n" +
                "  \"Meta\": {\n" +
                "    \"TotalPages\": 1\n" +
                "  }\n" +
                "}";
            return Response.status(200).entity(response)
                    .header("x-fapi-interaction-id", xFapiInteractionId).build();
        } else {
            return Response.status(403).build();
        }
    }

    @GET
    @Path("/offers")
    @Produces("application/json; charset=utf-8")
    public Response getBulkOffers(@HeaderParam("x-fapi-interaction-id") String xFapiInteractionId,
                                  @HeaderParam("Account-Request-Information") String accountRequestInfo)
            throws ParseException {

        JSONObject accountRequestInformation = getRequest(accountRequestInfo);
        List<String> accountRequestIds = getAccountIds(accountRequestInformation);
        JSONArray permissions = getPermissions(accountRequestInformation);
        StringBuilder builder = new StringBuilder();
        for (String accountId : accountRequestIds) {
            String temp = "";
            if (permissions.contains(OBExternalPermissions1Code.ReadOffers.name())) {
                temp = "{\n" +
                    "        \"AccountId\": \"" + accountId + "\",\n" +
                    "        \"OfferId\": \"Offer1\",\n" +
                    "        \"OfferType\": \"LimitIncrease\",\n" +
                    "        \"Description\": \"Credit limit increase for the account up to £10000.00\",\n" +
                    "        \"Amount\": {\n" +
                    "          \"Amount\": \"10000.00\",\n" +
                    "          \"Currency\": \"GBP\"\n" +
                    "        }\n" +
                    "      }";
            }
            if (builder.length() > 0 && temp.length() > 0) {
                builder.append(",\n").append(temp);
            } else {
                builder.append(temp);
            }
        }

        if (builder.length() > 0) {
            String finalRespose = "{\n" +
                "  \"Data\": {\n" +
                "    \"Offer\": [\n" +
                builder.toString() + "\n" +
                "    ]\n" +
                "  },\n" +
                "  \"Links\": {\n" +
                "    \"Self\": \"https://api.alphabank.com/open-banking/v3.0/offers/\"\n" +
                "  },\n" +
                "  \"Meta\": {\n" +
                "    \"TotalPages\": 1\n" +
                "  }\n" +
                "}";
            return Response.status(200).entity(finalRespose)
                    .header("x-fapi-interaction-id", xFapiInteractionId).build();
        }
        return Response.status(403).build();
    }

    @GET
    @Path("/accounts/{AccountId}/parties")
    @Produces("application/json; charset=utf-8")
    public Response getAccountParties(@PathParam("AccountId") String accountId,
                                    @HeaderParam("x-fapi-interaction-id") String xFapiInteractionId,
                                    @HeaderParam("Account-Request-Information") String accountRequestInfo)
            throws ParseException {

        JSONObject accountRequestInformation = getRequest(accountRequestInfo);
        JSONArray permissions = getPermissions(accountRequestInformation);
        CommonConstants.UKApiVersion specVersion = getAPISpecVersionFromRequestInfo(accountRequestInformation);
        // API v4 updated parameters.
        String addressTypeValue = "Business";

        if (specVersion.equals(CommonConstants.UKApiVersion.UK_API_V400)) {
            addressTypeValue = "BIZZ";
        }
        if (permissions.contains(OBExternalPermissions1Code.ReadParty.name())) {
            String response = "{\n" +
                "  \"Data\": {\n" +
                "    \"Party\": [\n" +
                "      {\n" +
                "        \"PartyId\": \"PABC123\",\n" +
                "        \"PartyType\": \"Sole\",\n" +
                "        \"Name\": \"Semiotec\",\n" +
                "        \"FullLegalName\": \"Semiotec Limited\",\n" +
                "        \"LegalStructure\": \"UK.OBIE.PrivateLimitedCompany\",\n" +
                "        \"BeneficialOwnership\": true,\n" +
                "        \"AccountRole\": \"UK.OBIE.Principal\",\n" +
                "        \"EmailAddress\": \"contact@semiotec.co.jp\",\n" +
                "        \"Relationships\": {\n" +
                "          \"Account\": {\n" +
                "            \"Related\": \"/accounts/" + accountId + "\",\n" +
                "            \"Id\": \"" + accountId + "\"\n" +
                "          }\n" +
                "        },\n" +
                "        \"Address\": [\n" +
                "          {\n" +
                "            \"AddressType\": \"" + addressTypeValue + "\",\n" +
                "            \"StreetName\": \"Street\",\n" +
                "            \"BuildingNumber\": \"15\",\n" +
                "            \"PostCode\": \"NW1 1AB\",\n" +
                "            \"TownName\": \"London\",\n" +
                "            \"Country\": \"GB\"\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"PartyId\": \"PXSIF023\",\n" +
                "        \"PartyNumber\": \"0000007456\",\n" +
                "        \"PartyType\": \"Delegate\",\n" +
                "        \"Name\": \"Kevin Atkinson\",\n" +
                "        \"FullLegalName\": \"Mr Kevin Bartholmew Atkinson\",\n" +
                "        \"LegalStructure\": \"UK.OBIE.Individual\",\n" +
                "        \"BeneficialOwnership\": false,\n" +
                "        \"AccountRole\": \"UK.OBIE.Administrator\",\n" +
                "        \"EmailAddress\": \"kev@semiotec.co.jp\",\n" +
                "        \"Relationships\": {\n" +
                "          \"Account\": {\n" +
                "            \"Related\": \"/accounts/" + accountId + "\",\n" +
                "            \"Id\": \"" + accountId + "\"\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"Links\": {\n" +
                "    \"Self\": \"https://api.alphabank.com/open-banking/v3.0/accounts/" + accountId + "/parties/\"\n" +
                "  },\n" +
                "  \"Meta\": {\n" +
                "    \"TotalPages\": 1\n" +
                "  }\n" +
                "}";
            return Response.status(200).entity(response)
                    .header("x-fapi-interaction-id", xFapiInteractionId).build();
        } else {
            return Response.status(403).build();
        }
    }


    @GET
    @Path("/accounts/{AccountId}/party")
    @Produces("application/json; charset=utf-8")
    public Response getAccountParty(@PathParam("AccountId") String accountId,
                                    @HeaderParam("x-fapi-interaction-id") String xFapiInteractionId,
                                    @HeaderParam("Account-Request-Information") String accountRequestInfo)
            throws ParseException {

        JSONObject accountRequestInformation = getRequest(accountRequestInfo);
        JSONArray permissions = getPermissions(accountRequestInformation);
        CommonConstants.UKApiVersion specVersion = getAPISpecVersionFromRequestInfo(accountRequestInformation);
        // API v4 updated parameters.
        String addressTypeValue = "Business";

        if (specVersion.equals(CommonConstants.UKApiVersion.UK_API_V400)) {
            addressTypeValue = "BIZZ";
        }
        if (permissions.contains(OBExternalPermissions1Code.ReadParty.name())) {
            String response = "{\n" +
                "  \"Data\": {\n" +
                "    \"Party\": {\n" +
                "      \"PartyId\": \"PABC123\",\n" +
                "      \"PartyType\": \"Sole\",\n" +
                "      \"Name\": \"Semiotec\",\n" +
                "      \"EmailAddress\": \"contact@semiotec.co.jp\",\n" +
                "      \"Address\": [{\n" +
                "        \"AddressType\": \"" + addressTypeValue + "\",\n" +
                "        \"StreetName\": \"Street\",\n" +
                "        \"BuildingNumber\": \"15\",\n" +
                "        \"PostCode\": \"NW1 1AB\",\n" +
                "        \"TownName\": \"London\",       \n" +
                "        \"Country\": \"GB\"       \n" +
                "      }]\n" +
                "    }\n" +
                "  },\n" +
                "  \"Links\": {\n" +
                "    \"Self\": \"https://api.alphabank.com/open-banking/v3.0/accounts/" + accountId +
                "/party/\"\n" +
                "  },\n" +
                "  \"Meta\": {\n" +
                "    \"TotalPages\": 1\n" +
                "  }\n" +
                "}";
            return Response.status(200).entity(response)
                    .header("x-fapi-interaction-id", xFapiInteractionId).build();
        } else {
            return Response.status(403).build();
        }
    }

    @GET
    @Path("/party")
    @Produces("application/json; charset=utf-8")
    public Response getBulkParties(@HeaderParam("x-fapi-interaction-id") String xFapiInteractionId,
                                   @HeaderParam("Account-Request-Information") String accountRequestInfo)
            throws ParseException {

        JSONObject accountRequestInformation = getRequest(accountRequestInfo);
        JSONArray permissions = getPermissions(accountRequestInformation);
        if (permissions.contains(OBExternalPermissions1Code.ReadPartyPSU.name())) {
            String response = "{\n" +
                    "  \"Data\": {\n" +
                    "    \"Party\": {\n" +
                    "      \"PartyId\": \"PXSIF023\",\n" +
                    "      \"PartyType\": \"Delegate\",\n" +
                    "      \"Name\": \"Mr D User\",\n" +
                    "      \"EmailAddress\": \"d.user@semiotec.co.jp\"\n" +
                    "    }\n" +
                    "  },\n" +
                    "  \"Links\": {\n" +
                    "    \"Self\": \"https://api.alphabank.com/open-banking/v3.0/party/\"\n" +
                    "  },\n" +
                    "  \"Meta\": {\n" +
                    "    \"TotalPages\": 1\n" +
                    "  }\n" +
                    "}";
            return Response.status(200).entity(response)
                    .header("x-fapi-interaction-id", xFapiInteractionId).build();
        }

        return Response.status(403).build();
    }

    @GET
    @Path("/accounts/{AccountId}/scheduled-payments")
    @Produces("application/json; charset=utf-8")
    public Response getAccountScheduledPayments(@PathParam("AccountId") String accountId,
                                                @HeaderParam("x-fapi-interaction-id") String xFapiInteractionId,
                                                @HeaderParam("Account-Request-Information") String accountRequestInfo)
            throws ParseException {

        JSONObject accountRequestInformation = getRequest(accountRequestInfo);
        JSONArray permissions = getPermissions(accountRequestInformation);
        CommonConstants.UKApiVersion specVersion = getAPISpecVersionFromRequestInfo(accountRequestInformation);
        // API v4 updated parameters.
        String creditorAccountSchemaValue = "SortCodeAccountNumber";

        if (specVersion.equals(CommonConstants.UKApiVersion.UK_API_V400)) {
            creditorAccountSchemaValue = "CLAV";
        }
        if (permissions.contains(OBExternalPermissions1Code.ReadScheduledPaymentsDetail.name())) {
            String response = "{\n" +
                "  \"Data\": {\n" +
                "    \"ScheduledPayment\": [\n" +
                "      {\n" +
                "        \"AccountId\": \"" + accountId + "\",\n" +
                "        \"ScheduledPaymentId\": \"SP03\",\n" +
                "        \"ScheduledPaymentDateTime\": \"2017-05-05T00:00:00+00:00\",\n" +
                "        \"ScheduledType\": \"Execution\",\n" +
                "        \"InstructedAmount\": {\n" +
                "          \"Amount\": \"10.00\",\n" +
                "          \"Currency\": \"GBP\"\n" +
                "        },\n" +
                "        \"CreditorAccount\": {\n" +
                "          \"SchemeName\": \"" + creditorAccountSchemaValue + "\",\n" +
                "          \"Identification\": \"23605490179017\",\n" +
                "          \"Name\": \"Mr Tee\"\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"Links\": {\n" +
                "    \"Self\": \"https://api.alphabank.com/open-banking/v3.0/accounts/" + accountId +
                "/scheduled-payments/\"\n" +
                "  },\n" +
                "  \"Meta\": {\n" +
                "    \"TotalPages\": 1\n" +
                "  }\n" +
                "}";
            return Response.status(200).entity(response)
                    .header("x-fapi-interaction-id", xFapiInteractionId).build();
        } else if (permissions.contains(
                OBExternalPermissions1Code.ReadScheduledPaymentsBasic.name())) {
            String response = "{\n" +
                "  \"Data\": {\n" +
                "    \"ScheduledPayment\": [\n" +
                "      {\n" +
                "        \"AccountId\": \"" + accountId + "\",\n" +
                "        \"ScheduledPaymentId\": \"SP03\",\n" +
                "        \"ScheduledPaymentDateTime\": \"2017-05-05T00:00:00+00:00\",\n" +
                "        \"ScheduledType\": \"Execution\",\n" +
                "        \"InstructedAmount\": {\n" +
                "          \"Amount\": \"10.00\",\n" +
                "          \"Currency\": \"GBP\"\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"Links\": {\n" +
                "    \"Self\": \"https://api.alphabank.com/open-banking/v3.0/accounts/" + accountId +
                "/scheduled-payments/\"\n" +
                "  },\n" +
                "  \"Meta\": {\n" +
                "    \"TotalPages\": 1\n" +
                "  }\n" +
                "}";
            return Response.status(200).entity(response)
                    .header("x-fapi-interaction-id", xFapiInteractionId).build();
        }

        return Response.status(403).build();
    }

    @GET
    @Path("/scheduled-payments")
    @Produces("application/json; charset=utf-8")
    public Response getBulkScheduledPayments(@HeaderParam("x-fapi-interaction-id") String xFapiInteractionId,
                                             @HeaderParam("Account-Request-Information") String accountRequestInfo)
            throws ParseException {

        JSONObject accountRequestInformation = getRequest(accountRequestInfo);
        List<String> accountRequestIds = getAccountIds(accountRequestInformation);
        JSONArray permissions = getPermissions(accountRequestInformation);
        CommonConstants.UKApiVersion specVersion = getAPISpecVersionFromRequestInfo(accountRequestInformation);
        // API v4 updated parameters.
        String creditorAccountSchemaValue = "SortCodeAccountNumber";

        if (specVersion.equals(CommonConstants.UKApiVersion.UK_API_V400)) {
            creditorAccountSchemaValue = "CLAV";
        }
        StringBuilder builder = new StringBuilder();
        for (String accountId : accountRequestIds) {
            String temp = "";
            if (permissions.contains(OBExternalPermissions1Code.ReadScheduledPaymentsDetail.name())) {
                temp = "{\n" +
                    "        \"AccountId\": \"" + accountId + "\",\n" +
                    "        \"ScheduledPaymentId\": \"SP03\",\n" +
                    "        \"ScheduledPaymentDateTime\": \"2017-05-05T00:00:00+00:00\",\n" +
                    "        \"ScheduledType\": \"Execution\",\n" +
                    "        \"InstructedAmount\": {\n" +
                    "          \"Amount\": \"10.00\",\n" +
                    "          \"Currency\": \"GBP\"\n" +
                    "        },\n" +
                    "        \"CreditorAccount\": {\n" +
                    "          \"SchemeName\": \"" + creditorAccountSchemaValue + "\",\n" +
                    "          \"Identification\": \"23605490179017\",\n" +
                    "          \"Name\": \"Mr Tee\"\n" +
                    "        }\n" +
                    "      }";
            } else if (permissions.contains(OBExternalPermissions1Code.ReadScheduledPaymentsBasic.name())) {
                temp = "{\n" +
                    "        \"AccountId\": \"" + accountId + "\",\n" +
                    "        \"ScheduledPaymentId\": \"SP03\",\n" +
                    "        \"ScheduledPaymentDateTime\": \"2017-05-05T00:00:00+00:00\",\n" +
                    "        \"ScheduledType\": \"Execution\",\n" +
                    "        \"InstructedAmount\": {\n" +
                    "          \"Amount\": \"10.00\",\n" +
                    "          \"Currency\": \"GBP\"\n" +
                    "        }\n" +
                    "      }";
            }
            if (builder.length() > 0 && temp.length() > 0) {
                builder.append(",\n").append(temp);
            } else {
                builder.append(temp);
            }
        }

        if (builder.length() > 0) {
            String finalRespose = "{\n" +
                "  \"Data\": {\n" +
                "    \"ScheduledPayment\": [\n" +
                builder.toString() +  "\n" +
                "    ]\n" +
                "  },\n" +
                "  \"Links\": {\n" +
                "    \"Self\": \"https://api.alphabank.com/open-banking/v3.0/scheduled-payments/\"\n" +
                "  },\n" +
                "  \"Meta\": {\n" +
                "    \"TotalPages\": 1\n" +
                "  }\n" +
                "}";
            return Response.status(200).entity(finalRespose)
                    .header("x-fapi-interaction-id", xFapiInteractionId).build();
        }
        return Response.status(403).build();
    }

    @GET
    @Path("/accounts/{AccountId}/statements")
    @Produces("application/json; charset=utf-8")
    public Response getAccountStatements(@PathParam("AccountId") String accountId,
                                         @HeaderParam("x-fapi-interaction-id") String xFapiInteractionId,
                                         @HeaderParam("Account-Request-Information") String accountRequestInfo)
            throws ParseException {

        JSONObject accountRequestInformation = getRequest(accountRequestInfo);
        JSONArray permissions = getPermissions(accountRequestInformation);
        if (permissions.contains(OBExternalPermissions1Code.ReadStatementsDetail.name())) {
            String response = "{\n" +
                "  \"Data\": {\n" +
                "    \"Statement\": [\n" +
                "    {\n" +
                "      \"AccountId\": \"" + accountId + "\",\n" +
                "      \"StatementId\": \"8sfhke-sifhkeuf-97813\",\n" +
                "      \"StatementReference\": \"002\",\n" +
                "      \"Type\": \"RegularPeriodic\",\n" +
                "      \"StartDateTime\": \"2017-08-01T00:00:00+00:00\",\n" +
                "      \"EndDateTime\": \"2017-08-31T23:59:59+00:00\",\n" +
                "      \"CreationDateTime\": \"2017-09-01T00:00:00+00:00\",\n" +
                "      \"StatementDescription\": [\"August 2017 Statement\", \"One Free Uber Ride\"],\n" +
                "      \"StatementAmount\": [\n" +
                "      {\n" +
                "        \"Amount\": {\n" +
                "          \"Amount\": \"400.00\",\n" +
                "          \"Currency\": \"GBP\"\n" +
                "        },\n" +
                "        \"CreditDebitIndicator\": \"Credit\",       \n" +
                "        \"Type\": \"ClosingBalance\"       \n" +
                "      },\n" +
                "      {\n" +
                "        \"Amount\": {\n" +
                "          \"Amount\": \"600.00\",\n" +
                "          \"Currency\": \"GBP\"\n" +
                "        },\n" +
                "        \"CreditDebitIndicator\": \"Credit\",\n" +
                "        \"Type\": \"PreviousClosingBalance\"\n" +
                "      }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"AccountId\": \"" + accountId + "\",\n" +
                "      \"StatementId\": \"34hj24u-324h33-31i3p4\",\n" +
                "      \"StatementReference\": \"003\",\n" +
                "      \"Type\": \"RegularPeriodic\",\n" +
                "      \"StartDateTime\": \"2017-09-01T00:00:00+00:00\",\n" +
                "      \"EndDateTime\": \"2017-09-30T23:59:59+00:00\",\n" +
                "      \"CreationDateTime\": \"2017-10-01T00:00:00+00:00\",\n" +
                "      \"StatementDescription\": [\"September 2017 Statement\"],\n" +
                "      \"StatementAmount\": [\n" +
                "      {\n" +
                "        \"Amount\": {\n" +
                "          \"Amount\": \"200.00\",\n" +
                "          \"Currency\": \"GBP\"\n" +
                "        },\n" +
                "        \"CreditDebitIndicator\": \"Credit\",\n" +
                "        \"Type\": \"PreviousClosingBalance\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"Amount\": {\n" +
                "          \"Amount\": \"400.00\",\n" +
                "          \"Currency\": \"GBP\"\n" +
                "        },\n" +
                "        \"CreditDebitIndicator\": \"Credit\",\n" +
                "        \"Type\": \"PreviousClosingBalance\"\n" +
                "      }\n" +
                "      ]\n" +
                "    }]\n" +
                "  }\n" +
                "}";
            return Response.status(200).entity(response)
                    .header("x-fapi-interaction-id", xFapiInteractionId).build();
        } else if (permissions.contains(OBExternalPermissions1Code.ReadStatementsBasic.name())) {
            String response = "{\n" +
                "  \"Data\": {\n" +
                "    \"Statement\": [\n" +
                "    {\n" +
                "      \"AccountId\": \"" + accountId + "\",\n" +
                "      \"StatementId\": \"8sfhke-sifhkeuf-97813\",\n" +
                "      \"StatementReference\": \"002\",\n" +
                "      \"Type\": \"RegularPeriodic\",\n" +
                "      \"StartDateTime\": \"2017-08-01T00:00:00+00:00\",\n" +
                "      \"EndDateTime\": \"2017-08-31T23:59:59+00:00\",\n" +
                "      \"CreationDateTime\": \"2017-09-01T00:00:00+00:00\",\n" +
                "      \"StatementDescription\": [\"August 2017 Statement\", \"One Free Uber Ride\"]\n" +
                "    },\n" +
                "    {\n" +
                "      \"AccountId\": \"" + accountId + "\",\n" +
                "      \"StatementId\": \"34hj24u-324h33-31i3p4\",\n" +
                "      \"StatementReference\": \"003\",\n" +
                "      \"Type\": \"RegularPeriodic\",\n" +
                "      \"StartDateTime\": \"2017-09-01T00:00:00+00:00\",\n" +
                "      \"EndDateTime\": \"2017-09-30T23:59:59+00:00\",\n" +
                "      \"CreationDateTime\": \"2017-10-01T00:00:00+00:00\",\n" +
                "      \"StatementDescription\": [\"September 2017 Statement\"]\n" +
                "    }]\n" +
                "  }\n" +
                "}";
            return Response.status(200).entity(response)
                    .header("x-fapi-interaction-id", xFapiInteractionId).build();
        }

        return Response.status(403).build();
    }

    @GET
    @Path("/accounts/{AccountId}/statements/{StatementId}")
    @Produces("application/json; charset=utf-8")
    public Response getAccountStatements(@PathParam("AccountId") String accountId,
                                         @PathParam("StatementId") String statementId,
                                         @HeaderParam("x-fapi-interaction-id") String xFapiInteractionId,
                                         @HeaderParam("Account-Request-Information") String accountRequestInfo)
            throws ParseException {

        JSONObject accountRequestInformation = getRequest(accountRequestInfo);
        JSONArray permissions = getPermissions(accountRequestInformation);
        if (permissions.contains(OBExternalPermissions1Code.ReadStatementsDetail.name())) {
            String response = "{\n" +
                "  \"Data\": {\n" +
                "    \"Statement\": [\n" +
                "    {\n" +
                "      \"AccountId\": \"" + accountId + "\",\n" +
                "      \"StatementId\": \"" + statementId + "\",\n" +
                "      \"StatementReference\": \"002\",\n" +
                "      \"Type\": \"RegularPeriodic\",\n" +
                "      \"StartDateTime\": \"2017-08-01T00:00:00+00:00\",\n" +
                "      \"EndDateTime\": \"2017-08-31T23:59:59+00:00\",\n" +
                "      \"CreationDateTime\": \"2017-09-01T00:00:00+00:00\",\n" +
                "      \"StatementDescription\": [\"August 2017 Statement\", \"One Free Uber Ride\"],\n" +
                "      \"StatementAmount\": [\n" +
                "      {\n" +
                "        \"Amount\": {\n" +
                "          \"Amount\": \"400.00\",\n" +
                "          \"Currency\": \"GBP\"\n" +
                "        },\n" +
                "        \"CreditDebitIndicator\": \"Credit\",       \n" +
                "        \"Type\": \"ClosingBalance\"       \n" +
                "      },\n" +
                "      {\n" +
                "        \"Amount\": {\n" +
                "          \"Amount\": \"600.00\",\n" +
                "          \"Currency\": \"GBP\"\n" +
                "        },\n" +
                "        \"CreditDebitIndicator\": \"Credit\",\n" +
                "        \"Type\": \"PreviousClosingBalance\"\n" +
                "      }\n" +
                "      ]\n" +
                "    }]\n" +
                "  },\n" +
                "    \"Links\": {\n" +
                "      \"Self\": \"https://api.alphabank.com/open-banking/v3.0/accounts/" + accountId +
                "/statements/" + statementId + "\"\n" +
                "    },\n" +
                "    \"Meta\": {\n" +
                "      \"TotalPages\": 1\n" +
                "    }\n" +
                "}";
            return Response.status(200).entity(response)
                    .header("x-fapi-interaction-id", xFapiInteractionId).build();
        } else if (permissions.contains(OBExternalPermissions1Code.ReadStatementsBasic.name())) {
            String response = "{\n" +
                "  \"Data\": {\n" +
                "    \"Statement\": [\n" +
                "    {\n" +
                "      \"AccountId\": \"" + accountId + "\",\n" +
                "      \"StatementId\": \"" + statementId + "\",\n" +
                "      \"StatementReference\": \"002\",\n" +
                "      \"Type\": \"RegularPeriodic\",\n" +
                "      \"StartDateTime\": \"2017-08-01T00:00:00+00:00\",\n" +
                "      \"EndDateTime\": \"2017-08-31T23:59:59+00:00\",\n" +
                "      \"CreationDateTime\": \"2017-09-01T00:00:00+00:00\",\n" +
                "      \"StatementDescription\": [\"August 2017 Statement\", \"One Free Uber Ride\"]\n" +
                "    }],\n" +
                "  }\n" +
                "    \"Links\": {\n" +
                "      \"Self\": \"https://api.alphabank.com/open-banking/v3.0/accounts/" + accountId +
                "/statements/" + statementId + "\"\n" +
                "    },\n" +
                "    \"Meta\": {\n" +
                "      \"TotalPages\": 1\n" +
                "    }\n" +
                "}";
            return Response.status(200).entity(response)
                    .header("x-fapi-interaction-id", xFapiInteractionId).build();
        }

        return Response.status(403).build();
    }

    @GET
    @Path("/accounts/{AccountId}/statements/{StatementId}/transactions")
    @Produces("application/json; charset=utf-8")
    public Response getAccountStatementTransactions(@PathParam("AccountId") String accountId,
                                                    @PathParam("StatementId") String statementId,
                                                    @HeaderParam("x-fapi-interaction-id") String xFapiInteractionId,
                                                    @HeaderParam("Account-Request-Information")
                                                            String accountRequestInfo)
            throws ParseException {

        JSONObject accountRequestInformation = getRequest(accountRequestInfo);
        JSONArray permissions = getPermissions(accountRequestInformation);
        String creditDebitIndicator =
                permissions.contains(OBExternalPermissions1Code.ReadTransactionsDebits.name())
                        && !permissions
                        .contains(OBExternalPermissions1Code.ReadTransactionsCredits.name()) ? "Debit" : "Credit";
        CommonConstants.UKApiVersion specVersion = getAPISpecVersionFromRequestInfo(accountRequestInformation);
        // API v4 updated parameters.
        String typeValue = "InterimBooked";
        String status = "Booked";

        if (specVersion.equals(CommonConstants.UKApiVersion.UK_API_V400)) {
            typeValue = "CLAV";
            status = "BOOK";
        }
        if (permissions.contains(OBExternalPermissions1Code.ReadTransactionsDetail.name())) {
            String response = "{\n" +
                "  \"Data\": {\n" +
                "    \"Transaction\": [\n" +
                "      {\n" +
                "        \"AccountId\": \"" + accountId + "\",\n" +
                "        \"TransactionId\": \"123\",\n" +
                "        \"TransactionReference\": \"Ref 1\",\n" +
                "        \"StatementReference\": [\"Ref " + statementId + "\"],\n" +
                "        \"Amount\": {\n" +
                "          \"Amount\": \"10.00\",\n" +
                "          \"Currency\": \"GBP\"\n" +
                "        },\n" +
                "        \"CreditDebitIndicator\": \"" + creditDebitIndicator + "\",\n" +
                "        \"Status\": \"" + status + "\",\n" +
                "        \"BookingDateTime\": \"2017-04-05T10:43:07+00:00\",\n" +
                "        \"ValueDateTime\": \"2017-04-05T10:45:22+00:00\",\n" +
                "        \"TransactionInformation\": \"Cash from Aubrey\",\n" +
                "        \"BankTransactionCode\": {\n" +
                "          \"Code\": \"str\",\n" +
                "          \"SubCode\": \"str\"\n" +
                "        },\n" +
                "        \"ProprietaryBankTransactionCode\": {\n" +
                "          \"Code\": \"Transfer\",\n" +
                "          \"Issuer\": \"AlphaBank\"\n" +
                "        },\n" +
                "        \"Balance\": {\n" +
                "          \"Amount\": {\n" +
                "            \"Amount\": \"230.00\",\n" +
                "            \"Currency\": \"GBP\"\n" +
                "          },\n" +
                "          \"CreditDebitIndicator\": \"Credit\",\n" +
                "          \"Type\": \"" + typeValue + "\"\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
            return Response.status(200).entity(response)
                    .header("x-fapi-interaction-id", xFapiInteractionId).build();
        } else if (permissions.contains(OBExternalPermissions1Code.ReadTransactionsBasic.name())) {
            String response = "{\n" +
                "  \"Data\": {\n" +
                "    \"Transaction\": [\n" +
                "      {\n" +
                "        \"AccountId\": \"" + accountId + "\",\n" +
                "        \"TransactionId\": \"123\",\n" +
                "        \"TransactionReference\": \"Ref 1\",\n" +
                "        \"StatementReference\": [\"Ref " + statementId + "\"],\n" +
                "        \"Amount\": {\n" +
                "          \"Amount\": \"10.00\",\n" +
                "          \"Currency\": \"GBP\"\n" +
                "        },\n" +
                "        \"CreditDebitIndicator\": \"" + creditDebitIndicator + "\",\n" +
                "        \"Status\": \"" + status + "\",\n" +
                "        \"BookingDateTime\": \"2017-04-05T10:43:07+00:00\",\n" +
                "        \"ValueDateTime\": \"2017-04-05T10:45:22+00:00\",\n" +
                "        \"BankTransactionCode\": {\n" +
                "          \"Code\": \"str\",\n" +
                "          \"SubCode\": \"str\"\n" +
                "        },\n" +
                "        \"ProprietaryBankTransactionCode\": {\n" +
                "          \"Code\": \"Transfer\",\n" +
                "          \"Issuer\": \"AlphaBank\"\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
            return Response.status(200).entity(response)
                    .header("x-fapi-interaction-id", xFapiInteractionId).build();

        }

        return Response.status(403).build();
    }

    @GET
    @Path("/accounts/{AccountId}/statements/{StatementId}/file")
    @Produces("multipart/form-data; boundary=''")
    public Response getAccountStatementFile(@PathParam("AccountId") String accountId,
                                            @PathParam("StatementId") String statementId,
                                            @HeaderParam("x-fapi-interaction-id") String xFapiInteractionId,
                                            @HeaderParam("Account-Request-Information") String accountRequestInfo)
            throws ParseException {

        JSONObject accountRequestInformation = getRequest(accountRequestInfo);
        JSONArray permissions = getPermissions(accountRequestInformation);
        if (permissions.contains(OBExternalPermissions1Code.ReadStatementsDetail.name())) {

            /* Removed getting a file from resource since we cannot get a file using getResource method from a
            packed JAR or WAR. Hence used getResourceAsStream and got the file as a stream and passed it*/
            InputStream stream = Objects.requireNonNull(getClass().getClassLoader()
                    .getResourceAsStream("/statement.pdf"));

            return Response.status(200).entity((Object) stream)
                    .header("Content-Disposition",
                            "attachment; filename=statement.pdf")
                    .header("x-fapi-interaction-id", xFapiInteractionId)
                    .build();

        } else if (permissions.contains(OBExternalPermissions1Code.ReadStatementsBasic.name())) {
            return Response.status(403)
                    .header("x-fapi-interaction-id", xFapiInteractionId).build();
        }

        return Response.status(403).build();
    }

    @GET
    @Path("/statements")
    @Produces("application/json; charset=utf-8")
    public Response getBulkStatements(@HeaderParam("x-fapi-interaction-id") String xFapiInteractionId,
                                      @HeaderParam("Account-Request-Information") String accountRequestInfo)
            throws ParseException {

        JSONObject accountRequestInformation = getRequest(accountRequestInfo);
        List<String> accountRequestIds = getAccountIds(accountRequestInformation);
        JSONArray permissions = getPermissions(accountRequestInformation);
        StringBuilder builder = new StringBuilder();
        for (String accountId : accountRequestIds) {
            String temp = "";
            if (permissions.contains(OBExternalPermissions1Code.ReadStatementsDetail.name())) {
                temp =
                    "    {\n" +
                    "      \"AccountId\": \"" + accountId + "\",\n" +
                    "      \"StatementId\": \"8sfhke-sifhkeuf-97813\",\n" +
                    "      \"StatementReference\": \"002\",\n" +
                    "      \"Type\": \"RegularPeriodic\",\n" +
                    "      \"StartDateTime\": \"2017-08-01T00:00:00+00:00\",\n" +
                    "      \"EndDateTime\": \"2017-08-31T23:59:59+00:00\",\n" +
                    "      \"CreationDateTime\": \"2017-09-01T00:00:00+00:00\",\n" +
                    "      \"StatementDescription\": [\"August 2017 Statement\", \"One Free Uber Ride\"],\n" +
                    "      \"StatementAmount\": [\n" +
                    "      {\n" +
                    "        \"Amount\": {\n" +
                    "          \"Amount\": \"400.00\",\n" +
                    "          \"Currency\": \"GBP\"\n" +
                    "        },\n" +
                    "        \"CreditDebitIndicator\": \"Credit\",       \n" +
                    "        \"Type\": \"ClosingBalance\"       \n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"Amount\": {\n" +
                    "          \"Amount\": \"600.00\",\n" +
                    "          \"Currency\": \"GBP\"\n" +
                    "        },\n" +
                    "        \"CreditDebitIndicator\": \"Credit\",\n" +
                    "        \"Type\": \"PreviousClosingBalance\"\n" +
                    "      }\n" +
                    "      ]\n" +
                    "    }";
            } else if (permissions.contains(OBExternalPermissions1Code.ReadStatementsBasic.name())) {
                temp = "    {\n" +
                    "      \"AccountId\": \"" + accountId + "\",\n" +
                    "      \"StatementId\": \"8sfhke-sifhkeuf-97813\",\n" +
                    "      \"StatementReference\": \"002\",\n" +
                    "      \"Type\": \"RegularPeriodic\",\n" +
                    "      \"StartDateTime\": \"2017-08-01T00:00:00+00:00\",\n" +
                    "      \"EndDateTime\": \"2017-08-31T23:59:59+00:00\",\n" +
                    "      \"CreationDateTime\": \"2017-09-01T00:00:00+00:00\",\n" +
                    "      \"StatementDescription\": [\"August 2017 Statement\", \"One Free Uber Ride\"]\n" +
                    "    }";
            }
            if (builder.length() > 0 && temp.length() > 0) {
                builder.append(",\n").append(temp);
            } else {
                builder.append(temp);
            }
        }

        if (builder.length() > 0) {
            String finalRespose = "{\n" +
                "  \"Data\": {\n" +
                "    \"Statement\": [\n" +
                builder.toString() +  "\n" +
                "    ]\n" +
                "  },\n" +
                "  \"Links\": {\n" +
                "    \"Self\": \"https://api.alphabank.com/open-banking/v3.0/statements/\"\n" +
                "  },\n" +
                "  \"Meta\": {\n" +
                "    \"TotalPages\": 1\n" +
                "  }\n" +
                "}";
            return Response.status(200).entity(finalRespose)
                    .header("x-fapi-interaction-id", xFapiInteractionId).build();
        }
        return Response.status(403).build();
    }

    /**
     * OBExternalPermissions1Code enum.
     */
    public enum OBExternalPermissions1Code {
        ReadAccountsBasic("Allow access to read basic account information."),
        //"Permission to read basic account information."),
        ReadAccountsDetail("Allow access to additional elements in the account payload"),
        //("Access to additional elements in the account payload."),
        ReadBalances("Allow access to read all balance information."),
        //("Permission to read all balance information."),
        ReadBeneficiariesBasic("Allow access to read basic beneficiary details"),
        //("Permission to read basic beneficiary details."),
        ReadBeneficiariesDetail("Allow access to additional elements in the beneficiaries payload"),
        //("Access to additional elements in the beneficiaries payload."),
        ReadDirectDebits("Allow access to read all direct debit information"),
        //("Permission to read all direct debit information."),
        ReadStandingOrdersBasic("Allow access to read basic standing order information."),
        //("Permission to read standing order information."),
        ReadStandingOrdersDetail("Allow access to read detailed standing order information."),
        //("Access to additional elements in the standing-orders payload."),
        ReadTransactionsBasic("Allow access to read basic transactions information."),
        //("Permission to read basic transaction information."),
        ReadTransactionsDetail("Allow access to read detailed transactions information."),
        //("Access to additional elements in the transactions payload."),
        ReadTransactionsCredits("Allow access to read credit transactions information."),
        //("Access to only credit transactions."),
        ReadTransactionsDebits("Allow access to read debit transactions information."),
        //("Access to only debit transactions.")
        ReadProducts("Allow access to read product information."),
        //"Permission to read all product information."

        ReadStatementsBasic("Allow access to read basic statement details."),

        ReadStatementsDetail("Allow access to read detailed statement details."),

        ReadOffers("Allow access to read all offer information."),

        ReadParty("Allow access to read party information on the account owner."),

        ReadPartyPSU("Allow access to read party information on the PSU logged in."),

        ReadScheduledPaymentsBasic("Allow access to read basic scheduled payments details."),

        ReadScheduledPaymentsDetail("Allow access to read detailed scheduled payments details."),

        ReadPAN("Request to access PAN in the clear across the available endpoints.");

        private String oBExternalPermissions1Code;

        OBExternalPermissions1Code(String desc) {
            this.oBExternalPermissions1Code = desc;
        }

        public String getoBExternalPermissions1Code() {
            return this.oBExternalPermissions1Code;
        }
    }


    private static JSONObject getRequest(String json) throws ParseException {
        String[] splitString = json.split("\\.");
        String base64EncodedBody = splitString[1];
        String decodedString = new String(Base64.getDecoder()
                .decode(base64EncodedBody.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        JSONParser parser = new JSONParser(JSONParser.MODE_PERMISSIVE);
        JSONObject jsonObject = (JSONObject) parser.parse(decodedString);
        return jsonObject;
    }

    private static List<String> getAccountIds(JSONObject json) {
        List<String> accountIds = new ArrayList<>();
        JSONArray mappingResources = (JSONArray) json.get("consentMappingResources");
        for (int i = 0; i < mappingResources.size(); i++) {
            JSONObject resource = (JSONObject) mappingResources.get(i);
            accountIds.add((String) resource.get("accountId"));
        }
        return accountIds;
    }

    private static JSONArray getPermissions(JSONObject json) throws ParseException {
        JSONObject receipt = (JSONObject) json.get("receipt");
        JSONObject data = (JSONObject) receipt.get("Data");
        return (JSONArray) data.get("Permissions");

    }

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

}
