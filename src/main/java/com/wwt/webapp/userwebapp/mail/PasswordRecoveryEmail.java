/* Copyright 2018-2019 Wehe Web Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wwt.webapp.userwebapp.mail;

import com.wwt.webapp.userwebapp.util.ConfigProvider;

/**
 * @author benw-at-wwt
 */
class PasswordRecoveryEmail implements Email {

    private static final String subject = ConfigProvider.getConfigValue("appName")+" Password recovery";
    private static final String bodyText = "Please access the following link for email verification: \n";
    private final String emailAddress;
    private final String token;

    PasswordRecoveryEmail(String emailAddress,String token) {
        this.emailAddress = emailAddress;
        this.token = token;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public String getBody() {
        return bodyText + "<a href =\""+  ConfigProvider.getConfigValue("passwordRecoveryBaseUrl")+ token+"\">"
                + ConfigProvider.getConfigValue("passwordRecoveryBaseUrl") + token+"</a>";
    }

    @Override
    public String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public String toString() {
        return "PasswordRecoveryEmail{" +
                "emailAddress='" + emailAddress + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
