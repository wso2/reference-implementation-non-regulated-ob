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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.non.regulated.ob.demo.backend.BankException;

import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * BankService class.
 */
@Path("/bankservice/")
public class BankService {
    private static final Log log = LogFactory.getLog(BankService.class);

    private static final String BANK_4020_01 = "{\n" +
            "\t\"meta\": {\n" +
            "\t\t\"Copyright\": \"Copyright OBank Open Data 2017\",\n" +
            "\t\t\"LastUpdated\": \"2017-07-09T07:52:02.253Z\",\n" +
            "\t\t\"TotalResults\": 2\n" +
            "\t},\n" +
            "\t\"data\": {\n" +
            "\t\t\"branches\": [{\n" +
            "\t\t\t\"id\": \"400e-842\",\n" +
            "\t\t\t\"name\": \"OBank-842\",\n" +
            "\t\t\t\"type\": \"Physical\",\n" +
            "\t\t\t\"description\": \"This branch is OBank-42 located in Madrid\",\n" +
            "\n" +
            "\t\t\t\"address\": {\n" +
            "\t\t\t\t\"line_1\": \"PZA.LA PIÑA\",\n" +
            "\t\t\t\t\"line_2\": \"NA\",\n" +
            "\t\t\t\t\"line_3\": \"NA\",\n" +
            "\t\t\t\t\"city\": \"VALDEMORO\",\n" +
            "\t\t\t\t\"state\": \"MADRID\",\n" +
            "\t\t\t\t\"postcode\": \"28340\",\n" +
            "\t\t\t\t\"country\": \"ES\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"geo-location\": {\n" +
            "\t\t\t\t\"latitude\": 4.0442364E7,\n" +
            "\t\t\t\t\"longitude\": -3678378.0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"telephoneNumber\": \"+44 203 696 6510\",\n" +
            "\t\t\t\"faxNumber\": \"+44 203 696 6510\",\n" +
            "\t\t\t\"currency\": \"EUR\",\n" +
            "\t\t\t\"DaysOfTheWeek\": \"7\",\n" +
            "\n" +
            "\t\t\t\"openingTimes\": [{\n" +
            "\t\t\t\t\t\"OpeningDay\": \"Monday-Saturday\",\n" +
            "\t\t\t\t\t\"OpeningTime\": \"0900\",\n" +
            "\t\t\t\t\t\"ClosingTime\": \"1800\"\n" +
            "\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t{\n" +
            "\t\t\t\t\t\"OpeningDay\": \"Sunday\",\n" +
            "\t\t\t\t\t\"OpeningTime\": \"0900\",\n" +
            "\t\t\t\t\t\"ClosingTime\": \"1200\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"PlannedBranchClosure\": [{\n" +
            "\t\t\t\t\"StartDate\": \"12/20/17\",\n" +
            "\t\t\t\t\"EndDate\": \"12/29/17\"\n" +
            "\t\t\t}],\n" +
            "\t\t\t\"AccessibilityTypes\": \"AudioCashMachine\",\n" +
            "\t\t\t\"BranchSelfServeServiceName\": [\n" +
            "\t\t\t\t\"AccountVerificationService\"\n" +
            "\t\t\t],\n" +
            "\t\t\t\"BranchOtherSelfServices\": [\n" +
            "\t\t\t\t\"CashDeposit\"\n" +
            "\t\t\t],\n" +
            "\t\t\t\"BranchFacilitiesName\": [\n" +
            "\t\t\t\t\"BusinessITSupport\"\n" +
            "\t\t\t],\n" +
            "\n" +
            "\t\t\t\"ATMAtBranch\": true\n" +
            "\n" +
            "\n" +
            "\t\t}, {\n" +
            "\t\t\t\"id\": \"400e-270\",\n" +
            "\t\t\t\"name\": \"OBank-270\",\n" +
            "\t\t\t\"type\": \"Physical\",\n" +
            "\t\t\t\"description\": \"This branch is OBank-270 located in Vergara,Madrid\",\n" +
            "\n" +
            "\t\t\t\"address\": {\n" +
            "\t\t\t\t\"line_1\": \"PRINCIPE DE VERGARA, 126\",\n" +
            "\t\t\t\t\"line_2\": \"NA\",\n" +
            "\t\t\t\t\"line_3\": \"NA\",\n" +
            "\t\t\t\t\"city\": \"MADRID\",\n" +
            "\t\t\t\t\"state\": \"MADRID\",\n" +
            "\t\t\t\t\"postcode\": \"28002\",\n" +
            "\t\t\t\t\"country\": \"ES\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"geo-location\": {\n" +
            "\t\t\t\t\"latitude\": 4.0442364E7,\n" +
            "\t\t\t\t\"longitude\": -3678378.0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"telephoneNumber\": \"+44 303 696 6510\",\n" +
            "\t\t\t\"faxNumber\": \"+44 303 696 6510\",\n" +
            "\t\t\t\"currency\": \"EUR\",\n" +
            "\t\t\t\"DaysOfTheWeek\": \"7\",\n" +
            "\n" +
            "\t\t\t\"openingTimes\": [{\n" +
            "\t\t\t\t\t\"OpeningDay\": \"Monday-Friday\",\n" +
            "\t\t\t\t\t\"OpeningTime\": \"0900\",\n" +
            "\t\t\t\t\t\"ClosingTime\": \"1800\"\n" +
            "\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t{\n" +
            "\t\t\t\t\t\"OpeningDay\": \"Saturday\",\n" +
            "\t\t\t\t\t\"OpeningTime\": \"0900\",\n" +
            "\t\t\t\t\t\"ClosingTime\": \"1200\"\n" +
            "\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"PlannedBranchClosure\": [{\n" +
            "\t\t\t\t\"StartDate\": \"12/20/17\",\n" +
            "\t\t\t\t\"EndDate\": \"12/29/17\"\n" +
            "\t\t\t}],\n" +
            "\t\t\t\"AccessibilityTypes\": \"AudioCashMachine\",\n" +
            "\t\t\t\"BranchSelfServeServiceName\": [\n" +
            "\t\t\t\t\"AccountVerificationService\"\n" +
            "\t\t\t],\n" +
            "\t\t\t\"BranchOtherSelfServices\": [\n" +
            "\t\t\t\t\"CashDeposit\"\n" +
            "\t\t\t],\n" +
            "\t\t\t\"BranchFacilitiesName\": [\n" +
            "\t\t\t\t\"BusinessITSupport\"\n" +
            "\t\t\t],\n" +
            "\n" +
            "\t\t\t\"ATMAtBranch\": true\n" +
            "\t\t}]\n" +
            "\t}\n" +
            "}";

    private static final String BANK_8020_01 = "{\n" +
            "\t\"meta\": {\n" +
            "\t\t\"Copyright\": \"Copyright OBank Open Data 2017\",\n" +
            "\t\t\"LastUpdated\": \"2017-07-09T07:52:02.253Z\",\n" +
            "\t\t\"TotalResults\": 2\n" +
            "\t},\n" +
            "\t\"data\": {\n" +
            "\t\t\"branches\": [{\n" +
            "\t\t\t\"id\": \"800e-842\",\n" +
            "\t\t\t\"name\": \"OBank-842\",\n" +
            "\t\t\t\"type\": \"Physical\",\n" +
            "\t\t\t\"description\": \"This branch is OBank-42 located in Madrid\",\n" +
            "\n" +
            "\t\t\t\"address\": {\n" +
            "\t\t\t\t\"line_1\": \"LA PIÑA\",\n" +
            "\t\t\t\t\"line_2\": \"NA\",\n" +
            "\t\t\t\t\"line_3\": \"NA\",\n" +
            "\t\t\t\t\"city\": \"VALDEMORO\",\n" +
            "\t\t\t\t\"state\": \"MADRID\",\n" +
            "\t\t\t\t\"postcode\": \"28340\",\n" +
            "\t\t\t\t\"country\": \"ES\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"geo-location\": {\n" +
            "\t\t\t\t\"latitude\": 4.0442364E7,\n" +
            "\t\t\t\t\"longitude\": -3678378.0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"telephoneNumber\": \"+44 303 696 6501\",\n" +
            "\t\t\t\"faxNumber\": \"+44 403 696 6410\",\n" +
            "\t\t\t\"currency\": \"EUR\",\n" +
            "\t\t\t\"DaysOfTheWeek\": \"7\",\n" +
            "\n" +
            "\t\t\t\"openingTimes\": [{\n" +
            "\t\t\t\t\t\"OpeningDay\": \"Monday-Saturday\",\n" +
            "\t\t\t\t\t\"OpeningTime\": \"0900\",\n" +
            "\t\t\t\t\t\"ClosingTime\": \"1800\"\n" +
            "\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t{\n" +
            "\t\t\t\t\t\"OpeningDay\": \"Sunday\",\n" +
            "\t\t\t\t\t\"OpeningTime\": \"0900\",\n" +
            "\t\t\t\t\t\"ClosingTime\": \"1200\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"PlannedBranchClosure\": [{\n" +
            "\t\t\t\t\"StartDate\": \"11/20/17\",\n" +
            "\t\t\t\t\"EndDate\": \"12/29/17\"\n" +
            "\t\t\t}],\n" +
            "\t\t\t\"AccessibilityTypes\": \"AudioCashMachine\",\n" +
            "\t\t\t\"BranchSelfServeServiceName\": [\n" +
            "\t\t\t\t\"AccountVerificationService\"\n" +
            "\t\t\t],\n" +
            "\t\t\t\"BranchOtherSelfServices\": [\n" +
            "\t\t\t\t\"CashDeposit\"\n" +
            "\t\t\t],\n" +
            "\t\t\t\"BranchFacilitiesName\": [\n" +
            "\t\t\t\t\"BusinessITSupport\"\n" +
            "\t\t\t],\n" +
            "\n" +
            "\t\t\t\"ATMAtBranch\": true\n" +
            "\n" +
            "\n" +
            "\t\t}, {\n" +
            "\t\t\t\"id\": \"800e-270\",\n" +
            "\t\t\t\"name\": \"OBank-270\",\n" +
            "\t\t\t\"type\": \"Physical\",\n" +
            "\t\t\t\"description\": \"This branch is OBank-270 located in Vergara,Madrid\",\n" +
            "\n" +
            "\t\t\t\"address\": {\n" +
            "\t\t\t\t\"line_1\": \"PRINCIPE DE VERGARA, 126\",\n" +
            "\t\t\t\t\"line_2\": \"NA\",\n" +
            "\t\t\t\t\"line_3\": \"NA\",\n" +
            "\t\t\t\t\"city\": \"MADRID\",\n" +
            "\t\t\t\t\"state\": \"MADRID\",\n" +
            "\t\t\t\t\"postcode\": \"28002\",\n" +
            "\t\t\t\t\"country\": \"ES\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"geo-location\": {\n" +
            "\t\t\t\t\"latitude\": 4.0442364E7,\n" +
            "\t\t\t\t\"longitude\": -3678378.0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"telephoneNumber\": \"+44 303 696 6510\",\n" +
            "\t\t\t\"faxNumber\": \"+44 303 696 6510\",\n" +
            "\t\t\t\"currency\": \"EUR\",\n" +
            "\t\t\t\"DaysOfTheWeek\": \"7\",\n" +
            "\n" +
            "\t\t\t\"openingTimes\": [{\n" +
            "\t\t\t\t\t\"OpeningDay\": \"Monday-Friday\",\n" +
            "\t\t\t\t\t\"OpeningTime\": \"0900\",\n" +
            "\t\t\t\t\t\"ClosingTime\": \"1800\"\n" +
            "\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t{\n" +
            "\t\t\t\t\t\"OpeningDay\": \"Saturday\",\n" +
            "\t\t\t\t\t\"OpeningTime\": \"0900\",\n" +
            "\t\t\t\t\t\"ClosingTime\": \"1200\"\n" +
            "\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"PlannedBranchClosure\": [{\n" +
            "\t\t\t\t\"StartDate\": \"12/20/17\",\n" +
            "\t\t\t\t\"EndDate\": \"12/29/17\"\n" +
            "\t\t\t}],\n" +
            "\t\t\t\"AccessibilityTypes\": \"AudioCashMachine\",\n" +
            "\t\t\t\"BranchSelfServeServiceName\": [\n" +
            "\t\t\t\t\"AccountVerificationService\"\n" +
            "\t\t\t],\n" +
            "\t\t\t\"BranchOtherSelfServices\": [\n" +
            "\t\t\t\t\"CashDeposit\"\n" +
            "\t\t\t],\n" +
            "\t\t\t\"BranchFacilitiesName\": [\n" +
            "\t\t\t\t\"BusinessITSupport\"\n" +
            "\t\t\t],\n" +
            "\n" +
            "\t\t\t\"ATMAtBranch\": true\n" +
            "\t\t}]\n" +
            "\t}\n" +
            "}";
    private static final String BANK_3020_01 = "{\n" +
            "\t\"meta\": {\n" +
            "\t\t\"Copyright\": \"Copyright OBank Open Data 2017\",\n" +
            "\t\t\"LastUpdated\": \"2017-07-09T07:52:02.253Z\",\n" +
            "\t\t\"TotalResults\": 2\n" +
            "\t},\n" +
            "\t\"data\": {\n" +
            "\t\t\"branches\": [{\n" +
            "\t\t\t\"id\": \"400e-842\",\n" +
            "\t\t\t\"name\": \"OBank-842\",\n" +
            "\t\t\t\"type\": \"Physical\",\n" +
            "\t\t\t\"description\": \"This branch is OBank-42 located in Madrid\",\n" +
            "\n" +
            "\t\t\t\"address\": {\n" +
            "\t\t\t\t\"line_1\": \"PZA.LA PIÑA\",\n" +
            "\t\t\t\t\"line_2\": \"NA\",\n" +
            "\t\t\t\t\"line_3\": \"NA\",\n" +
            "\t\t\t\t\"city\": \"VALDEMORO\",\n" +
            "\t\t\t\t\"state\": \"MADRID\",\n" +
            "\t\t\t\t\"postcode\": \"28340\",\n" +
            "\t\t\t\t\"country\": \"ES\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"geo-location\": {\n" +
            "\t\t\t\t\"latitude\": 4.0442364E7,\n" +
            "\t\t\t\t\"longitude\": -3678378.0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"telephoneNumber\": \"+44 203 696 6510\",\n" +
            "\t\t\t\"faxNumber\": \"+44 203 696 6510\",\n" +
            "\t\t\t\"currency\": \"EUR\",\n" +
            "\t\t\t\"DaysOfTheWeek\": \"7\",\n" +
            "\n" +
            "\t\t\t\"openingTimes\": [{\n" +
            "\t\t\t\t\t\"OpeningDay\": \"Monday-Saturday\",\n" +
            "\t\t\t\t\t\"OpeningTime\": \"0900\",\n" +
            "\t\t\t\t\t\"ClosingTime\": \"1800\"\n" +
            "\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t{\n" +
            "\t\t\t\t\t\"OpeningDay\": \"Sunday\",\n" +
            "\t\t\t\t\t\"OpeningTime\": \"0900\",\n" +
            "\t\t\t\t\t\"ClosingTime\": \"1200\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"PlannedBranchClosure\": [{\n" +
            "\t\t\t\t\"StartDate\": \"12/20/17\",\n" +
            "\t\t\t\t\"EndDate\": \"12/29/17\"\n" +
            "\t\t\t}],\n" +
            "\t\t\t\"AccessibilityTypes\": \"AudioCashMachine\",\n" +
            "\t\t\t\"BranchSelfServeServiceName\": [\n" +
            "\t\t\t\t\"AccountVerificationService\"\n" +
            "\t\t\t],\n" +
            "\t\t\t\"BranchOtherSelfServices\": [\n" +
            "\t\t\t\t\"CashDeposit\"\n" +
            "\t\t\t],\n" +
            "\t\t\t\"BranchFacilitiesName\": [\n" +
            "\t\t\t\t\"BusinessITSupport\"\n" +
            "\t\t\t],\n" +
            "\n" +
            "\t\t\t\"ATMAtBranch\": true\n" +
            "\n" +
            "\n" +
            "\t\t}, {\n" +
            "\t\t\t\"id\": \"400e-270\",\n" +
            "\t\t\t\"name\": \"OBank-270\",\n" +
            "\t\t\t\"type\": \"Physical\",\n" +
            "\t\t\t\"description\": \"This branch is OBank-270 located in Vergara,Madrid\",\n" +
            "\n" +
            "\t\t\t\"address\": {\n" +
            "\t\t\t\t\"line_1\": \"PRINCIPE DE VERGARA, 126\",\n" +
            "\t\t\t\t\"line_2\": \"NA\",\n" +
            "\t\t\t\t\"line_3\": \"NA\",\n" +
            "\t\t\t\t\"city\": \"MADRID\",\n" +
            "\t\t\t\t\"state\": \"MADRID\",\n" +
            "\t\t\t\t\"postcode\": \"28002\",\n" +
            "\t\t\t\t\"country\": \"ES\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"geo-location\": {\n" +
            "\t\t\t\t\"latitude\": 4.0442364E7,\n" +
            "\t\t\t\t\"longitude\": -3678378.0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"telephoneNumber\": \"+44 303 696 6510\",\n" +
            "\t\t\t\"faxNumber\": \"+44 303 696 6510\",\n" +
            "\t\t\t\"currency\": \"EUR\",\n" +
            "\t\t\t\"DaysOfTheWeek\": \"7\",\n" +
            "\n" +
            "\t\t\t\"openingTimes\": [{\n" +
            "\t\t\t\t\t\"OpeningDay\": \"Monday-Friday\",\n" +
            "\t\t\t\t\t\"OpeningTime\": \"0900\",\n" +
            "\t\t\t\t\t\"ClosingTime\": \"1800\"\n" +
            "\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t{\n" +
            "\t\t\t\t\t\"OpeningDay\": \"Saturday\",\n" +
            "\t\t\t\t\t\"OpeningTime\": \"0900\",\n" +
            "\t\t\t\t\t\"ClosingTime\": \"1200\"\n" +
            "\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"PlannedBranchClosure\": [{\n" +
            "\t\t\t\t\"StartDate\": \"12/20/17\",\n" +
            "\t\t\t\t\"EndDate\": \"12/29/17\"\n" +
            "\t\t\t}],\n" +
            "\t\t\t\"AccessibilityTypes\": \"AudioCashMachine\",\n" +
            "\t\t\t\"BranchSelfServeServiceName\": [\n" +
            "\t\t\t\t\"AccountVerificationService\"\n" +
            "\t\t\t],\n" +
            "\t\t\t\"BranchOtherSelfServices\": [\n" +
            "\t\t\t\t\"CashDeposit\"\n" +
            "\t\t\t],\n" +
            "\t\t\t\"BranchFacilitiesName\": [\n" +
            "\t\t\t\t\"BusinessITSupport\"\n" +
            "\t\t\t],\n" +
            "\n" +
            "\t\t\t\"ATMAtBranch\": true\n" +
            "\t\t}]\n" +
            "\t}\n" +
            "}";
    private static final String BANK_5020_01 = "{\n" +
            "\t\"meta\": {\n" +
            "\t\t\"Copyright\": \"Copyright OBank Open Data 2017\",\n" +
            "\t\t\"LastUpdated\": \"2017-07-09T07:52:02.253Z\",\n" +
            "\t\t\"TotalResults\": 2\n" +
            "\t},\n" +
            "\t\"data\": {\n" +
            "\t\t\"branches\": [{\n" +
            "\t\t\t\"id\": \"500e-842\",\n" +
            "\t\t\t\"name\": \"OBank-842\",\n" +
            "\t\t\t\"type\": \"Physical\",\n" +
            "\t\t\t\"description\": \"This branch is OBank-500e-842 located in Madrid\",\n" +
            "\n" +
            "\t\t\t\"address\": {\n" +
            "\t\t\t\t\"line_1\": \"PZA.LA PIÑA\",\n" +
            "\t\t\t\t\"line_2\": \"NA\",\n" +
            "\t\t\t\t\"line_3\": \"NA\",\n" +
            "\t\t\t\t\"city\": \"VALDEMORO\",\n" +
            "\t\t\t\t\"state\": \"MADRID\",\n" +
            "\t\t\t\t\"postcode\": \"28340\",\n" +
            "\t\t\t\t\"country\": \"ES\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"geo-location\": {\n" +
            "\t\t\t\t\"latitude\": 4.0442364E7,\n" +
            "\t\t\t\t\"longitude\": -3678378.0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"telephoneNumber\": \"+44 203 696 6510\",\n" +
            "\t\t\t\"faxNumber\": \"+44 203 696 6510\",\n" +
            "\t\t\t\"currency\": \"EUR\",\n" +
            "\t\t\t\"DaysOfTheWeek\": \"7\",\n" +
            "\n" +
            "\t\t\t\"openingTimes\": [{\n" +
            "\t\t\t\t\t\"OpeningDay\": \"Monday-Saturday\",\n" +
            "\t\t\t\t\t\"OpeningTime\": \"0900\",\n" +
            "\t\t\t\t\t\"ClosingTime\": \"1800\"\n" +
            "\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t{\n" +
            "\t\t\t\t\t\"OpeningDay\": \"Sunday\",\n" +
            "\t\t\t\t\t\"OpeningTime\": \"0900\",\n" +
            "\t\t\t\t\t\"ClosingTime\": \"1200\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"PlannedBranchClosure\": [{\n" +
            "\t\t\t\t\"StartDate\": \"12/20/17\",\n" +
            "\t\t\t\t\"EndDate\": \"12/29/17\"\n" +
            "\t\t\t}],\n" +
            "\t\t\t\"AccessibilityTypes\": \"AudioCashMachine\",\n" +
            "\t\t\t\"BranchSelfServeServiceName\": [\n" +
            "\t\t\t\t\"AccountVerificationService\"\n" +
            "\t\t\t],\n" +
            "\t\t\t\"BranchOtherSelfServices\": [\n" +
            "\t\t\t\t\"CashDeposit\"\n" +
            "\t\t\t],\n" +
            "\t\t\t\"BranchFacilitiesName\": [\n" +
            "\t\t\t\t\"BusinessITSupport\"\n" +
            "\t\t\t],\n" +
            "\n" +
            "\t\t\t\"ATMAtBranch\": true\n" +
            "\n" +
            "\n" +
            "\t\t}, {\n" +
            "\t\t\t\"id\": \"400e-270\",\n" +
            "\t\t\t\"name\": \"OBank-270\",\n" +
            "\t\t\t\"type\": \"Physical\",\n" +
            "\t\t\t\"description\": \"This branch is OBank-270 located in Vergara,Madrid\",\n" +
            "\n" +
            "\t\t\t\"address\": {\n" +
            "\t\t\t\t\"line_1\": \"PRINCIPE DE VERGARA, 126\",\n" +
            "\t\t\t\t\"line_2\": \"NA\",\n" +
            "\t\t\t\t\"line_3\": \"NA\",\n" +
            "\t\t\t\t\"city\": \"MADRID\",\n" +
            "\t\t\t\t\"state\": \"MADRID\",\n" +
            "\t\t\t\t\"postcode\": \"28002\",\n" +
            "\t\t\t\t\"country\": \"ES\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"geo-location\": {\n" +
            "\t\t\t\t\"latitude\": 4.0442364E7,\n" +
            "\t\t\t\t\"longitude\": -3678378.0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"telephoneNumber\": \"+44 303 696 6510\",\n" +
            "\t\t\t\"faxNumber\": \"+44 303 696 6510\",\n" +
            "\t\t\t\"currency\": \"EUR\",\n" +
            "\t\t\t\"DaysOfTheWeek\": \"7\",\n" +
            "\n" +
            "\t\t\t\"openingTimes\": [{\n" +
            "\t\t\t\t\t\"OpeningDay\": \"Monday-Friday\",\n" +
            "\t\t\t\t\t\"OpeningTime\": \"0900\",\n" +
            "\t\t\t\t\t\"ClosingTime\": \"1800\"\n" +
            "\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t{\n" +
            "\t\t\t\t\t\"OpeningDay\": \"Saturday\",\n" +
            "\t\t\t\t\t\"OpeningTime\": \"0900\",\n" +
            "\t\t\t\t\t\"ClosingTime\": \"1200\"\n" +
            "\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"PlannedBranchClosure\": [{\n" +
            "\t\t\t\t\"StartDate\": \"12/20/17\",\n" +
            "\t\t\t\t\"EndDate\": \"12/29/17\"\n" +
            "\t\t\t}],\n" +
            "\t\t\t\"AccessibilityTypes\": \"AudioCashMachine\",\n" +
            "\t\t\t\"BranchSelfServeServiceName\": [\n" +
            "\t\t\t\t\"AccountVerificationService\"\n" +
            "\t\t\t],\n" +
            "\t\t\t\"BranchOtherSelfServices\": [\n" +
            "\t\t\t\t\"CashDeposit\"\n" +
            "\t\t\t],\n" +
            "\t\t\t\"BranchFacilitiesName\": [\n" +
            "\t\t\t\t\"BusinessITSupport\"\n" +
            "\t\t\t],\n" +
            "\n" +
            "\t\t\t\"ATMAtBranch\": true\n" +
            "\t\t}]\n" +
            "\t}\n" +
            "}";


    @GET
    @Path("/banks/{bank_id}/branches")
    @Produces("application/json; charset=utf-8")
    public Response getBankBranches(@PathParam("bank_id") String bankId) throws BankException {
        if (log.isDebugEnabled()) {
            log.debug("----invoking the method,getBankBranches() method.Bank id is:" + bankId);

        }
        Response r;

        if (StringUtils.isNotBlank(bankId)) {
            HashMap<String, String> banksMap = new HashMap<String, String>();
            banksMap.put("bank-4020-01", BANK_4020_01);
            banksMap.put("bank-8020-01", BANK_8020_01);
            banksMap.put("bank-3020-01", BANK_3020_01);
            banksMap.put("bank-5020-01", BANK_5020_01);
            if (banksMap.containsKey(bankId)) {
                r = Response.ok().entity(banksMap.get(bankId)).build();
            } else {
                r = Response.status(404).entity("{\n" +
                        "    \"Error\":{        \n" +
                        "        \"message\":\"Bank cannot be found. Please specify a valid value" +
                        " for Bank ID.Example valid Bank ID- bank-4020-01.\"       \n" +
                        "    }\n" +
                        "}").build();
            }
        } else {
            r = Response.status(400).entity("{\n" +
                    "    \"Error\":{        \n" +
                    "        \"message\":\"The specified Bank ID is empty/null.\"        \n" +
                    "    }\n" +
                    "}").build();
        }

        return r;
    }

    @GET
    @Path("/banks/{bank_id}/products")
    @Produces("application/json; charset=utf-8")
    public Response getBankProducts(@PathParam("bank_id") String bankId) throws BankException {
        if (log.isDebugEnabled()) {
            log.debug("----invoking the method,getBankProducts() method.Bank id is:" + bankId);

        }
        Response r;

        if (StringUtils.isNotBlank(bankId)) {
            if (bankId.equals("bank-4020-01")) {
                r = Response.ok().entity("{\"meta\": {\n" +
                        "\t\t\"License\": \"Copyright OBank Open Data 2017\",\n" +
                        "\t\t\"LastUpdated\": \"2017-07-09T07:52:02.253Z\",\n" +
                        "\t\t\"TotalResults\": 2\n" +
                        "\t},\n" +
                        "\t\"data\": [{\n" +
                        "\n" +
                        "\n" +
                        "    \"name\": \"Savings Account\",\n" +
                        "    \"category\": \"Banking\",\n" +
                        "    \"landing-page\": \"http://obank.com/accounts\",\n" +
                        "    \"additional-info\": {\n" +
                        "      \"rate-of-interest\": 4\n" +
                        "    },\n" +
                        "    \"sub-category\": \"Personal Banking\",\n" +
                        "    \"id\": \"SVA\"\n" +
                        "  },  \n" +
                        "  {\n" +
                        "    \"name\": \"Fixed Deposits\",   \n" +
                        "    \"category\": \"Investments\",\n" +
                        "    \"landing-page\": \"http://obank.com/accounts\",\n" +
                        "    \"additional-info\": {\n" +
                        "      \"interest-slabs\": [\n" +
                        "        {\n" +
                        "          \"rate-of-interest\": 6,\n" +
                        "          \"max-period\": 3,\n" +
                        "          \"text\": \"0 to 3 months\",\n" +
                        "          \"min-period\": 0\n" +
                        "        },       \n" +
                        "        {\n" +
                        "          \"rate-of-interest\": 7.5,\n" +
                        "          \"text\": \"> 1 year\",\n" +
                        "          \"min-period\": 12\n" +
                        "        }\n" +
                        "      ]\n" +
                        "    },\n" +
                        "    \"sub-category\": \"Deposits\",\n" +
                        "    \"id\": \"FD1\"\n" +
                        "  },\n" +
                        "  {\n" +
                        "    \"name\": \"Overdraft Account\",\n" +
                        "    \"category\": \"Banking\",\n" +
                        "    \"landing-page\": \"http://obank.com/accounts\",\n" +
                        "    \"additional-info\": {\n" +
                        "      \"rate-of-interest\": 0\n" +
                        "    },\n" +
                        "    \"sub-category\": \"Personal Banking\",\n" +
                        "    \"id\": \"OD1\"\n" +
                        "  },  \n" +
                        " \n" +
                        "  {\n" +
                        "    \"name\": \"Home Loan\",\n" +
                        "    \"category\": \"Loans\",\n" +
                        "    \"landing-page\": \"http://obank.com/loans\",\n" +
                        "    \"additional-info\": {\n" +
                        "      \"rate-of-interest\": 10,\n" +
                        "      \"maximum-tenure\": 240\n" +
                        "    },\n" +
                        "    \"sub-category\": \"Mortgages\",\n" +
                        "    \"id\": \"HL1\"\n" +
                        "  },\n" +
                        "  {\n" +
                        "    \"name\": \"Personal Loan\",\n" +
                        "    \"category\": \"Loans\",\n" +
                        "    \"landing-page\": \"http://obank.com/loans\",\n" +
                        "    \"additional-info\": {\n" +
                        "      \"rate-of-interest\": 16,\n" +
                        "      \"maximum-tenure\": 96\n" +
                        "    },\n" +
                        "    \"sub-category\": \"Loans\",\n" +
                        "    \"id\": \"PL1\"\n" +
                        "  }\n" +
                        "]}").build();

            } else {
                r = Response.status(404).entity("{\n" +
                        "    \"Error\":{        \n" +
                        "        \"message\":\"Bank cannot be found. Please specify a valid value " +
                        "for Bank ID.Example valid Bank ID- bank-4020-01. \"    \n" +
                        "    }\n" +
                        "}").build();
            }


        } else {
            r = Response.status(400).entity("{\n" +
                    "    \"Error\":{        \n" +
                    "        \"message\":\"The Bank ID is empty/null.\"       \n" +
                    "    }\n" +
                    "}").build();
        }
        return r;
    }

    @GET
    @Path("/banks/{bank_id}/products/{product-id}")
    @Produces("application/json; charset=utf-8")
    public Response getBankProductsPersonalAccounts(@PathParam("bank_id") String bankId, @PathParam("product-id")
            String prodId)
            throws BankException {
        if (log.isDebugEnabled()) {
            log.debug("----invoking the method,getBankProducts() method.Bank id is:" + bankId);

        }
        Response r;

        if (StringUtils.isNotBlank(bankId)) {
            if (bankId.equals("bank-4020-01")) {
                if ("pcAccounts".equals(prodId)) {
                    r = Response.ok().entity("{\n" +
                            "  \"meta\": {\n" +
                            "    \"License\": \"Copyright OBank Open Data 2017\",\n" +
                            "\t\t\"LastUpdated\": \"2017-07-09T07:52:02.253Z\",\n" +
                            "\t\t\"TotalResults\": 2\n" +
                            "  },\n" +
                            "  \"data\": [\n" +
                            "    {\n" +
                            "      \n" +
                            "      \"ProductType\": \"PCA\",\n" +
                            "      \"ProductName\": \"string\",\n" +
                            "      \"ProductSegment\": [\n" +
                            "        \"Basic\"\n" +
                            "      ],\n" +
                            "      \"InternationalPaymentsSupported\": true,\n" +
                            "      \"ProductIdentifier\": \"PCA1\",\n" +
                            "      \"CardWithdrawalLimit\": \"1000000\",\n" +
                            "      \"ProductDescription\": \"Personal Current Account\",     \n" +
                            "      \"AccessChannels\": [\n" +
                            "        \"ATM\"\n" +
                            "      ],\n" +
                            "      \"CardType\": [\n" +
                            "        \"BasicCard\"\n" +
                            "      ],\n" +
                            "      \"Contactless\": true,\n" +
                            "      \"MobileWallet\": [\n" +
                            "        \"AndroidPay\"\n" +
                            "      ],     \n" +
                            "      \"ChequeBookAvailable\": true,\n" +
                            "      \"CreditScoringPartOfAccountOpeningForGettingAnAccount\": true,\n" +
                            "      \"CreditScoringPartOfAccountOpeningIsAHardOrSoftCreditScore\": [\n" +
                            "        \"Hard\"\n" +
                            "      ],\n" +
                            "      \"CreditScoringPartOfAccountOpeningText\": \"string\",\n" +
                            "      \"CreditScoringPartOfAccountOpeningForIDVerification\": true,\n" +
                            "      \"CreditScoringPartOfAccountOpeningIDVerificationIsAHardOrSoftCreditScore\": [\n" +
                            "        \"Hard\"\n" +
                            "      ],\n" +
                            "      \"CreditScoringPartOfAccountOpeningIDVerificationText\": [\n" +
                            "        \"string\"\n" +
                            "      ],\n" +
                            "      \"MaximumMonthlyCharge\": \"EUR5\",\n" +
                            "      \"ProductURL\": [\n" +
                            "        \"http://obank/personal-current-accounts\"\n" +
                            "      ],\n" +
                            "      \"Currency\": [\n" +
                            "        \"EUR\"\n" +
                            "      ],\n" +
                            "      \"OverdraftOffered\": true,\n" +
                            "      \n" +
                            "     \n" +
                            "      \"Eligibility\": {\n" +
                            "        \"AgeRestricted\": true,\n" +
                            "        \"MinimumAge\": 20,\n" +
                            "        \"MaximumAge\": 50,\n" +
                            "        \"MaximumAgeToOpen\": 40,\n" +
                            "        \"OtherFinancialHoldingRequired\": true,\n" +
                            "        \"Description\": \"string\",\n" +
                            "        \"IncomeTurnoverRelated\": true,\n" +
                            "        \"SingleJointIncome\": \"Joint\",\n" +
                            "        \"MinimumIncomeTurnoverAmount\": \"1200\",\n" +
                            "        \"MinimumIncomeTurnoverCurrency\": \"EUR\",\n" +
                            "        \"IncomeCondition\": \"Annual\",\n" +
                            "        \"MinIncomeTurnoverPaidIntoAccount\": 0,\n" +
                            "        \"MinimumIncomeFrequency\": \"AcademicTerm\",\n" +
                            "        \"AnnualBusinessTurnover\": \"2000\",\n" +
                            "        \"AnnualBusinessTurnoverCurrency\": \"EUR\",\n" +
                            "        \"ResidencyRestricted\": true,\n" +
                            "        \"ResidencyRestrictedRegion\": \"UK\",\n" +
                            "        \"MaxNumberOfAccounts\": \"2\",\n" +
                            "        \"ThirdSectorOrganisations\": true,\n" +
                            "        \"MinimumDeposit\": true,\n" +
                            "        \"OpeningDepositMinimum\": \"100\",\n" +
                            "        \"OpeningDepositMinimumCurrency\": \"EUR\",\n" +
                            "        \"MinimumOperatingBalanceExists\": true,\n" +
                            "        \"MinimumOperatingBalance\": \"100\",\n" +
                            "        \"MinimumOperatingBalanceCurrency\": \"EUR\",\n" +
                            "        \"MaximumOpeningAmount\": \"3000\",\n" +
                            "        \"OpeningDepositMaximumAmount\": \"3000\",\n" +
                            "        \"OpeningDepositMaximumCurrency\": \"EUR\",\n" +
                            "        \"EligibilityName\": \"PCA Eligibity\",\n" +
                            "        \"EligibilityType\": \"AnyBusinessCustomer\",\n" +
                            "        \"EligibilityNotes\": \"Any permenet residents in UK\",\n" +
                            "        \"PreviousBankruptcy\": true,\n" +
                            "        \"MarketingEligibility\": [\n" +
                            "          \"ExistingCustomers\"\n" +
                            "        ]\n" +
                            "      },\n" +
                            "      \"CreditInterest\": {\n" +
                            "        \"CreditCharged\": true,\n" +
                            "        \"CreditInterestGroup\": [\n" +
                            "          {\n" +
                            "            \"InterestTierSubType\": \"FutureMultipleTerms\",\n" +
                            "            \"CreditInterestItem\": {\n" +
                            "              \"StartPromotionOrFutureTerms\": \"string\",\n" +
                            "              \"StopPromotionOrFutureTerms\": \"string\",\n" +
                            "              \"LengthPromotionalInDays\": 0,\n" +
                            "              \"DateOfChange\": \"string\",\n" +
                            "              \"StartDate\": \"string\",\n" +
                            "              \"EndDate\": \"string\",\n" +
                            "              \"CalculationFrequency\": \"Month\",\n" +
                            "              \"PaymentMethod\": \"Compound\",\n" +
                            "              \"InterestRateType\": \"Fixed\",\n" +
                            "              \"FixedInterestLength\": 0,\n" +
                            "              \"CalculationMethod\": \"Banded\",\n" +
                            "              \"InterestTiers\": [\n" +
                            "                {\n" +
                            "                  \"InterestTier\": \"string\",\n" +
                            "                  \"TierValueMinimum\": \"string\",\n" +
                            "                  \"DailyChargeForMinimum\": \"string\",\n" +
                            "                  \"TierValueMaximum\": \"string\",\n" +
                            "                  \"DailyChargeForMaximum\": \"string\",\n" +
                            "                  \"Rate\": \"string\",\n" +
                            "                  \"RateComparisonType\": \"APR\",\n" +
                            "                  \"APRAERRate\": \"string\"\n" +
                            "                }\n" +
                            "              ],\n" +
                            "              \"InterestNotes\": \"string\"\n" +
                            "            }\n" +
                            "          }\n" +
                            "        ]\n" +
                            "      }\n" +
                            "      \n" +
                            "    }\n" +
                            "  ]\n" +
                            "}").build();
                } else if ("sme-loans".equals(prodId)) {
                    r = Response.ok().entity("{\n" +
                            "\t\"meta\": {\n" +
                            "\t\t\"License\": \"Copyright OBank Open Data 2017\",\n" +
                            "\t\t\"LastUpdated\": \"2017-07-09T07:52:02.253Z\",\n" +
                            "\t\t\"TotalResults\": 1\n" +
                            "\t},\n" +
                            "\t\"data\": [{\n" +
                            "\t\t\t\"ProductName\": \"SME loans\",\n" +
                            "\t\t\t\"ProductIdentifier\": \"SMEL\",\n" +
                            "\t\t\t\"ProductTypeName\": \"SmeUnsecuredLoan\",\n" +
                            "\t\t\t\"ProductURL\": [\n" +
                            "\t\t\t\t\"string\"\n" +
                            "\t\t\t],\n" +
                            "\t\t\t\"ProductSegment\": [\n" +
                            "\t\t\t\t\"AgricultureSector\"\n" +
                            "\t\t\t],\n" +
                            "\t\t\t\"ProductDescription\": \"This loan type aims SMEs in agricultural " +
                            "sector\",\n" +
                            "\t\t\t\"Currency\": [\n" +
                            "\t\t\t\t\"EUR\"\n" +
                            "\t\t\t],\n" +
                            "\t\t\t\"MinimumLoanTerm\": 4,\n" +
                            "\t\t\t\"MaximumLoanTerm\": 10,\n" +
                            "\t\t\t\"MinimumLoanAmount\": \"£2000\",\n" +
                            "\t\t\t\"MaximumLoanAmount\": \"£10000\",\n" +
                            "\t\t\t\"PaymentHoliday\": true,\n" +
                            "\t\t\t\"LoanItem\": [{\n" +
                            "\t\t\t\t\"ProductSubType\": \"FutureMultipleTerms\",\n" +
                            "\t\t\t\t\"LoanPricing\": [{\n" +
                            "\t\t\t\t\t\"SizeIncrement\": \"£5000\",\n" +
                            "\t\t\t\t\t\"LoanLengthIncrement\": \"10 year\",\n" +
                            "\t\t\t\t\t\"RepaymentFrequency\": [\n" +
                            "\t\t\t\t\t\t\"monthly\"\n" +
                            "\t\t\t\t\t],\n" +
                            "\t\t\t\t\t\"LoanSizeBandLower\": \"1000\",\n" +
                            "\t\t\t\t\t\"LoanSizeBandUpper\": \"3000\",\n" +
                            "\t\t\t\t\t\"LoanLengthIncrementLower\": 4,\n" +
                            "\t\t\t\t\t\"LoanLengthIncrementUpper\": 10,\n" +
                            "\t\t\t\t\t\"IndicativeRate\": \"5%\",\n" +
                            "\t\t\t\t\t\"RateComparisonType\": \"APR\",\n" +
                            "\t\t\t\t\t\"Negotiable\": true\n" +
                            "\t\t\t\t}],\n" +
                            "\t\t\t\t\"CCARegulatedEntity\": true,\n" +
                            "\t\t\t\t\"IsALowInterestRepaymentStartPossible\": true,\n" +
                            "\t\t\t\t\"IsThisAnInterestOnlyLoan\": true,\n" +
                            "\t\t\t\t\"WillTheLoanBePaidInTrancheDrawdowns\": true\n" +
                            "\t\t\t}],\n" +
                            "\t\t\t\"Eligibility\": {\n" +
                            "\t\t\t\t\"AgeRestricted\": true,\n" +
                            "\t\t\t\t\"MinimumAge\": \"25\",\n" +
                            "\t\t\t\t\"MaximumAge\": \"50\",\n" +
                            "\t\t\t\t\"MaximumAgeToOpen\": \"40\",\n" +
                            "\t\t\t\t\"OtherFinancialHoldingRequired\": true,\n" +
                            "\t\t\t\t\"IncomeTurnoverRelated\": true,\n" +
                            "\t\t\t\t\"SingleJointIncome\": \"Joint\",\n" +
                            "\t\t\t\t\"MinimumIncomeTurnoverAmount\": \"1000\",\n" +
                            "\t\t\t\t\"MinimumIncomeTurnoverCurrency\": \"EUR\",\n" +
                            "\t\t\t\t\"IncomeCondition\": \"steady\",\n" +
                            "\t\t\t\t\"MinIncomeTurnoverPaidIntoAccount\": 50,\n" +
                            "\t\t\t\t\"MinimumIncomeFrequency\": \"monthly\",\n" +
                            "\t\t\t\t\"AnnualBusinessTurnover\": \"2000\",\n" +
                            "\t\t\t\t\"AnnualBusinessTurnoverCurrency\": \"EUR\",\n" +
                            "\t\t\t\t\"ResidencyRestricted\": true,\n" +
                            "\t\t\t\t\"ResidencyRestrictedRegion\": \"UK\",\n" +
                            "\t\t\t\t\"MaxNumberOfAccounts\": \"2\",\n" +
                            "\t\t\t\t\"ThirdSectorOrganisations\": true,\n" +
                            "\t\t\t\t\"MinimumDeposit\": true,\n" +
                            "\t\t\t\t\"OpeningDepositMinimum\": \"200\",\n" +
                            "\t\t\t\t\"OpeningDepositMinimumCurrency\": \"EUR\",\n" +
                            "\t\t\t\t\"MinimumOperatingBalanceExists\": true,\n" +
                            "\t\t\t\t\"MinimumOperatingBalance\": \"250\",\n" +
                            "\t\t\t\t\"MinimumOperatingBalanceCurrency\": \"EUR\",\n" +
                            "\t\t\t\t\"MaximumOpeningAmount\": true,\n" +
                            "\t\t\t\t\"OpeningDepositMaximumAmount\": \"750\",\n" +
                            "\t\t\t\t\"OpeningDepositMaximumCurrency\": \"EUR\",\n" +
                            "\t\t\t\t\"EligibilityType\": \"AnyBusinessCustomer\",\n" +
                            "\t\t\t\t\"PreviousBankruptcy\": true,\n" +
                            "\t\t\t\t\"MarketingEligibility\": [\n" +
                            "\t\t\t\t\t\"ExistingCustomers\"\n" +
                            "\t\t\t\t]\n" +
                            "\t\t\t}\n" +
                            "\t\t}\n" +
                            "\n" +
                            "\t]\n" +
                            "}").build();
                } else {
                    r = Response.ok().entity("{\n" +
                            "\t\"meta\": {\n" +
                            "\t\t\"License\": \"Copyright OBank Open Data 2017\",\n" +
                            "\t\t\"TotalResults\": 0\n" +
                            "\t},\n" +
                            "\t\"data\": [{}]\n" + "}").build();

                }

            } else {
                r = Response.status(404).entity("{\n" +
                        "    \"Error\":{        \n" +
                        "        \"message\":\"Bank cannot be found. Please specify a valid value " +
                        "for Bank ID.Example valid Bank ID- bank-4020-01.    \" \n" +
                        "    }\n" +
                        "}").build();
            }


        } else {
            r = Response.status(400).entity("{\n" +
                    "    \"Error\":{        \n" +
                    "        \"message\":\"The BANK_ID is empty/null. \"      \n" +
                    "    }\n" +
                    "}").build();
        }
        return r;
    }


    @GET
    @Path("/banks/{bank_id}/atms")
    @Produces("application/json; charset=utf-8")
    public Response getBankAtms(@PathParam("bank_id") String bankId) throws BankException {
        if (log.isDebugEnabled()) {
            log.debug("----invoking the method,getBankAtms() method.Bank id is:" + bankId);

        }
        Response r;

        if (StringUtils.isNotBlank(bankId)) {
            if (bankId.equals("bank-4020-01")) {
                r = Response.ok().entity("{\n" +
                        "\t\"meta\": {\n" +
                        "\t\t\"License\": \"OBank Open Data 2017\",\n" +
                        "\t\t\"LastUpdated\": \"2017-07-09T07:52:02.253Z\",\n" +
                        "\t\t\"TotalResults\": 2\n" +
                        "\t},\n" +
                        "\t\"data\": [{\n" +
                        "                        \n" +
                        "\t\t\t\"branchIdentification\": \"400e-842\",\n" +
                        "\t\t\t\"ATMID\": \"400e-842a\",\n" +
                        "\t\t\t\"locationCategory\": \"Airport\",\n" +
                        "                        \"country\":\"ES\",\n" +
                        "\t\t\t\"address\": {\n" +
                        "\t\t\t\t\"line_1\": \"PZA.LA PIÑA\",\n" +
                        "\t\t\t\t\"line_2\": \"NA\",\n" +
                        "\t\t\t\t\"line_3\": \"NA\",\n" +
                        "\t\t\t\t\"city\": \"VALDEMORO\",\n" +
                        "\t\t\t\t\"state\": \"MADRID\",\n" +
                        "\t\t\t\t\"postcode\": \"28340\",\n" +
                        "\t\t\t\t\"country\": \"ES\"\n" +
                        "\t\t\t},\n" +
                        "\t\t\t\"geo-location\": {\n" +
                        "\t\t\t\t\"latitude\": 4.0442364E7,\n" +
                        "\t\t\t\t\"longitude\": -3678378.0\n" +
                        "\t\t\t},\n" +
                        "\t\t\t\"AccessibilityTypes\": [\n" +
                        "\t\t\t\t\"AudioCashMachine\"\n" +
                        "\t\t\t],\n" +
                        "\t\t\t\"SupportedLanguages\": [\n" +
                        "\t\t\t\t\"ENGLISH,SPANISH\"\n" +
                        "\t\t\t],\n" +
                        "\t\t\t\"ATMServices\": [\n" +
                        "\t\t\t\t\"Balance,Debit\"\n" +
                        "\t\t\t],\n" +
                        "\t\t\t\"Currency\": [\n" +
                        "\t\t\t\t\"EUR\"\n" +
                        "\t\t\t],\n" +
                        "\t\t\t\"MinimumValueDispensed\": \"£50\",\n" +
                        "\t\t\t\"access\": {\n" +
                        "\t\t\t\t\"wheelchair\": true\n" +
                        "\t\t\t}\n" +
                        "\t\t},\n" +
                        "\n" +
                        "\t\t{\n" +
                        "\n" +
                        "\t\t\t\"branchIdentification\": \"400e-270\",\n" +
                        "\t\t\t\"ATMID\": \"400e-270a\",\n" +
                        "\t\t\t\"locationCategory\": \"Belmore TownHall\",\n" +
                        "                        \"country\":\"ES\",\n" +
                        "\t\t\t\"address\": {\n" +
                        "\t\t\t\t\"line_1\": \"PRINCIPE DE VERGARA, 126\",\n" +
                        "\t\t\t\t\"line_2\": \"NA\",\n" +
                        "\t\t\t\t\"line_3\": \"NA\",\n" +
                        "\t\t\t\t\"city\": \"MADRID\",\n" +
                        "\t\t\t\t\"state\": \"MADRID\",\n" +
                        "\t\t\t\t\"postcode\": \"28002\",\n" +
                        "\t\t\t\t\"country\": \"ES\"\n" +
                        "\t\t\t},\n" +
                        "\t\t\t\"geo-location\": {\n" +
                        "\t\t\t\t\"latitude\": 4.0442364E7,\n" +
                        "\t\t\t\t\"longitude\": -3678378.0\n" +
                        "\t\t\t},\n" +
                        "\t\t\t\"AccessibilityTypes\": [\n" +
                        "\t\t\t\t\"AudioCashMachine\"\n" +
                        "\t\t\t],\n" +
                        "\t\t\t\"SupportedLanguages\": [\n" +
                        "\t\t\t\t\"ENGLISH\"\n" +
                        "\t\t\t],\n" +
                        "\t\t\t\"ATMServices\": [\n" +
                        "\t\t\t\t\"Balance,Debit\"\n" +
                        "\t\t\t],\n" +
                        "\t\t\t\"Currency\": [\n" +
                        "\t\t\t\t\"EUR\"\n" +
                        "\t\t\t],\n" +
                        "\t\t\t\"MinimumValueDispensed\": \"£50\",\n" +
                        "\t\t\t\"access\": {\n" +
                        "\t\t\t\t\"wheelchair\": false\n" +
                        "\t\t\t}\n" +
                        "\t\t}\n" +
                        "\t]\n" +
                        "}").build();

            } else {
                r = Response.status(404).entity("{\n" +
                        "    \"Error\":{        \n" +
                        "        \"message\":\"Bank cannot be found. Please specify a valid value " +
                        "for Bank ID.Example valid Bank ID- bank-4020-01.   \"   \n" +
                        "    }\n" +
                        "}").build();
            }


        } else {
            r = Response.status(400).entity("{\n" +
                    "    \"Error\":{        \n" +
                    "        \"message\":\"The Bank ID is empty/null. \"      \n" +
                    "    }\n" +
                    "}").build();
        }
        return r;
    }

    @GET
    @Path("/banks/{bank_id}/atms/country/{country_id}")
    @Produces("application/json; charset=utf-8")
    public Response getBankAtmsByCountry(@PathParam("bank_id") String bankId,
                                         @PathParam("country_id") String couId) throws BankException {
        if (log.isDebugEnabled()) {
            log.debug("----invoking the method,getBankAtmsByCountry() method.Bank id is:" + bankId);

        }
        Response r;

        if (StringUtils.isNotBlank(bankId) && StringUtils.isNotBlank(couId)) {
            if (bankId.equals("bank-4020-01") && couId.equals("ES")) {
                r = Response.ok().entity("{\n" +
                        "\t\"meta\": {\n" +
                        "\t\t\"License\": \"Copyright ENBDG 2016\",\n" +
                        "\t\t\"LastUpdated\": \"2017-07-09T07:52:02.253Z\",\n" +
                        "\t\t\"TotalResults\": 2\n" +
                        "\t},\n" +
                        "\t\"data\": [{\n" +
                        "                        \n" +
                        "\t\t\t\"branchIdentification\": \"400e-842\",\n" +
                        "\t\t\t\"ATMID\": \"400e-842a\",\n" +
                        "\t\t\t\"locationCategory\": \"Airport\",\n" +
                        "\t\t\t\"address\": {\n" +
                        "\t\t\t\t\"line_1\": \"PZA.LA PIÑA\",\n" +
                        "\t\t\t\t\"line_2\": \"NA\",\n" +
                        "\t\t\t\t\"line_3\": \"NA\",\n" +
                        "\t\t\t\t\"city\": \"VALDEMORO\",\n" +
                        "\t\t\t\t\"state\": \"MADRID\",\n" +
                        "\t\t\t\t\"postcode\": \"28340\",\n" +
                        "\t\t\t\t\"country\": \"ES\"\n" +
                        "\t\t\t},\n" +
                        "\t\t\t\"geo-location\": {\n" +
                        "\t\t\t\t\"latitude\": 4.0442364E7,\n" +
                        "\t\t\t\t\"longitude\": -3678378.0\n" +
                        "\t\t\t},\n" +
                        "\t\t\t\"AccessibilityTypes\": [\n" +
                        "\t\t\t\t\"AudioCashMachine\"\n" +
                        "\t\t\t],\n" +
                        "\t\t\t\"SupportedLanguages\": [\n" +
                        "\t\t\t\t\"ENGLISH,SPANISH\"\n" +
                        "\t\t\t],\n" +
                        "\t\t\t\"ATMServices\": [\n" +
                        "\t\t\t\t\"Balance,Debit\"\n" +
                        "\t\t\t],\n" +
                        "\t\t\t\"Currency\": [\n" +
                        "\t\t\t\t\"EUR\"\n" +
                        "\t\t\t],\n" +
                        "\t\t\t\"MinimumValueDispensed\": \"£50\",\n" +
                        "\t\t\t\"access\": {\n" +
                        "\t\t\t\t\"wheelchair\": true\n" +
                        "\t\t\t}\n" +
                        "\t\t},\n" +
                        "\n" +
                        "\t\t{\n" +
                        "\n" +
                        "\t\t\t\"branchIdentification\": \"400e-270\",\n" +
                        "\t\t\t\"ATMID\": \"400e-270a\",\n" +
                        "\t\t\t\"locationCategory\": \"Belmore TownHall\",\n" +
                        "\t\t\t\"address\": {\n" +
                        "\t\t\t\t\"line_1\": \"PRINCIPE DE VERGARA, 126\",\n" +
                        "\t\t\t\t\"line_2\": \"NA\",\n" +
                        "\t\t\t\t\"line_3\": \"NA\",\n" +
                        "\t\t\t\t\"city\": \"MADRID\",\n" +
                        "\t\t\t\t\"state\": \"MADRID\",\n" +
                        "\t\t\t\t\"postcode\": \"28002\",\n" +
                        "\t\t\t\t\"country\": \"ES\"\n" +
                        "\t\t\t},\n" +
                        "\t\t\t\"geo-location\": {\n" +
                        "\t\t\t\t\"latitude\": 4.0442364E7,\n" +
                        "\t\t\t\t\"longitude\": -3678378.0\n" +
                        "\t\t\t},\n" +
                        "\t\t\t\"AccessibilityTypes\": [\n" +
                        "\t\t\t\t\"AudioCashMachine\"\n" +
                        "\t\t\t],\n" +
                        "\t\t\t\"SupportedLanguages\": [\n" +
                        "\t\t\t\t\"ENGLISH\"\n" +
                        "\t\t\t],\n" +
                        "\t\t\t\"ATMServices\": [\n" +
                        "\t\t\t\t\"Balance,Debit\"\n" +
                        "\t\t\t],\n" +
                        "\t\t\t\"Currency\": [\n" +
                        "\t\t\t\t\"EUR\"\n" +
                        "\t\t\t],\n" +
                        "\t\t\t\"MinimumValueDispensed\": \"£50\",\n" +
                        "\t\t\t\"access\": {\n" +
                        "\t\t\t\t\"wheelchair\": false\n" +
                        "\t\t\t}\n" +
                        "\t\t}\n" +
                        "\t]\n" +
                        "}").build();

            } else {
                r = Response.status(404).entity("{\n" +
                        "    \"Error\":{        \n" +
                        "        \"message\":\"Please specify a valid value for Bank ID or Country." +
                        " Example valid Bank ID- bank-4020-01 and Country code -ES\"" +
                        "      \n" +
                        "    }\n" +
                        "}").build();
            }


        } else {
            r = Response.status(400).entity("{\n" +
                    "    \"Error\":{        \n" +
                    "        \"message\":\"The Bank ID is empty/null.  \"     \n" +
                    "    }\n" +
                    "}").build();
        }
        return r;

    }


}
