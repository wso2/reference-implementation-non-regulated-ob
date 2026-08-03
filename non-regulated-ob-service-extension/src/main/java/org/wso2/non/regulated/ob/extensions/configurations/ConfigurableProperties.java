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

package org.wso2.non.regulated.ob.extensions.configurations;

import java.util.ArrayList;
import java.util.List;

/**
 * This class holds configurable properties.
 */
public class ConfigurableProperties {

    public static final String SHARABLE_ENDPOINT = "http://localhost:9766/non/regulated/ob/demo/backend/services/" +
            "bankaccounts/bankaccountservice/sharable-accounts";
    public static final String PAYABLE_ENDPOINT = "http://localhost:9766/non/regulated/ob/demo/backend/services/" +
            "bankaccounts/bankaccountservice/payable-accounts";

    public static final String MAX_INSTRUCTED_AMOUNT = "1000.00";
    public static final String MAX_FUTURE_PAYMENT_DAYS = "900";
    public static final String CUTOFF_DATE_ENABLED = "false";
    public static final String CUTOFF_DATE_POLICY = "ACCEPT";
    public static final String DAILY_CUTOFF = "12:30:30+00:01";
    public static final List<String> RESTRICTED_PERMISSIONS = new ArrayList<>(List.of(""));
    public static final String ZONE_ID = "Asia/Colombo";
    public static final String ACCOUNT_REFRESH_TOKEN_LAST_AUTHORIZED_DATE_LIMIT = "90";
    public static final String VALIDATE_ACCOUNT_ID = "true";
    public static final String VALIDATE_DEBTOR_ACC = "true";
    public static final String ACCOUNTS_CONSENT_VALIDITY_PERIOD = "90";
}
