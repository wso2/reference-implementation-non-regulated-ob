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

import org.apache.commons.lang3.StringUtils;
import org.wso2.non.regulated.ob.demo.backend.BankException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * BankAccountService class.
 */
@Path("/bankaccountservice/")
public class BankAccountService {

    private static final String accountList = "{\n" +
            "    \"data\": [\n" +
            "        {\n" +
            "            \"account_id\": \"30080012343456\",\n" +
            "            \"display_name\": \"account_1\",\n" +
            "            \"accountId\": \"30080012343456\",\n" +
            "            \"accountName\": \"account_1\",\n" +
            "            \"authorizationMethod\": \"single\",\n" +
            "            \"nickName\": \"not-working\",\n" +
            "            \"customerAccountType\": \"Individual\",\n" +
            "            \"type\": \"TRANS_AND_SAVINGS_ACCOUNTS\",\n" +
            "            \"isEligible\": true,\n" +
            "            \"isJointAccount\": false,\n" +
            "            \"jointAccountConsentElectionStatus\": false\n" +
            "        },\n" +
            "        {\n" +
            "            \"account_id\": \"30080098763459\",\n" +
            "            \"display_name\": \"account_2\",\n" +
            "            \"accountId\": \"30080098763459\",\n" +
            "            \"accountName\": \"account_2\",\n" +
            "            \"authorizationMethod\": \"single\",\n" +
            "            \"nickName\": \"not-working\",\n" +
            "            \"customerAccountType\": \"Individual\",\n" +
            "            \"type\": \"TRANS_AND_SAVINGS_ACCOUNTS\",\n" +
            "            \"isEligible\": true,\n" +
            "            \"isJointAccount\": false,\n" +
            "            \"jointAccountConsentElectionStatus\": false\n" +
            "        },\n" +
            "        {\n" +
            "            \"account_id\": \"30080098971337\",\n" +
            "            \"display_name\": \"multi_auth_account\",\n" +
            "            \"accountId\": \"30080098971337\",\n" +
            "            \"accountName\": \"multi_auth_account\",\n" +
            "            \"authorizationMethod\": \"multiple\",\n" +
            "            \"nickName\": \"not-working\",\n" +
            "            \"customerAccountType\": \"Individual\",\n" +
            "            \"type\": \"TRANS_AND_SAVINGS_ACCOUNTS\",\n" +
            "            \"isEligible\": true,\n" +
            "            \"isJointAccount\": false,\n" +
            "            \"jointAccountConsentElectionStatus\": false,\n" +
            "            \"authorizationUsers\": [\n" +
            "                {\n" +
            "                    \"customer_id\": \"123\",\n" +
            "                    \"user_id\": \"psu1@wso2.com@carbon.super\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"customer_id\": \"456\",\n" +
            "                    \"user_id\": \"psu2@wso2.com@carbon.super\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"accountId\": \"650-000 N1232\",\n" +
            "            \"display_name\": \"Extra_account\",\n" +
            "            \"account_id\": \"650-000 N1232\",\n" +
            "            \"authorizationMethod\": \"multiple\",\n" +
            "            \"accountName\": \"Extra Account\",\n" +
            "            \"nickName\": \"not-working\",\n" +
            "            \"customerAccountType\": \"Individual\",\n" +
            "            \"type\": \"TRANS_AND_SAVINGS_ACCOUNTS\",\n" +
            "            \"isEligible\": true,\n" +
            "            \"isJointAccount\": true,\n" +
            "            \"jointAccountConsentElectionStatus\": true,\n" +
            "            \"jointAccountinfo\": {\n" +
            "              \"LinkedMember\": [\n" +
            "                {\n" +
            "                  \"memberId\": \"user1@wso2.com@carbon.super\",\n" +
            "                  \"meta\": {}\n" +
            "                }\n" +
            "              ]\n" +
            "            },\n" +
            "            \"meta\": {}\n" +
            "      }\n" +
            "    ]\n" +
            "}";
    @GET
    @Path("/payable-accounts")
    @Produces("application/json; charset=utf-8")
    public Response getPayableAccounts() throws BankException {
        return Response.status(200).entity(accountList).build();
    }

    @GET
    @Path("/sharable-accounts")
    @Produces("application/json; charset=utf-8")
    public Response getSharableAccounts() throws BankException {
        return Response.status(200).entity(accountList).build();
    }

    @POST
    @Path("/payment-charges")
    @Produces("application/json; charset=utf-8")
    @Consumes("application/json")
    public Response calculatePaymentCharges(String request) throws BankException {
        if (StringUtils.isNotBlank(request)) {
            String response = "{\n" +
                    "    \"payment_charges\": \"1.0\",\n" +
                    "    \"payment_currency\": \"GBP\",\n" +
                    "    \"payment_exchange_rate\": \"0.1\"\n" +
                    "}";

            return Response.status(200).entity(response).build();
        }
        return Response.status(403).build();
    }
}
