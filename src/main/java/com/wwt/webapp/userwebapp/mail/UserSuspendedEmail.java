/* Copyright 2018-2021 Wehe Web Technologies
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


import com.wwt.webapp.userwebapp.helper.ConfigProvider;

/**
 * @author benw-at-wwt
 */
public class UserSuspendedEmail extends UserAdministrationEmail {

    private static final String subject = ConfigProvider.getConfigValue("appName")+" - User suspended";
    private static final String preHeader = "Too many failed logins";
    private static final String bodyText1 = "Your user was suspended due to a consecutive number of failed logins.";
    private static final String bodyText2 = "You have to use the password recovery flow to recover the user. Please use the following link.";
    private static final String bodyText3 = "<a href =\""+ ConfigProvider.getConfigValue("passwordRecoveryBaseUrl")+"\">" +ConfigProvider.getConfigValue("passwordRecoveryBaseUrl") + "</a>";

    public UserSuspendedEmail(String emailAddress, String loginId) {
        super(emailAddress,loginId);
        model.put( Email.TITLE,getSubject());
        model.put(Email.PREHEADER,preHeader);
        model.put(Email.BODY1, bodyText1);
        model.put(Email.BODY2, bodyText2);
        model.put(Email.BODY3, bodyText3);
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public String toString() {
        return "UserSuspendedEmail{" +
                "emailAddress='" + getEmailAddress() + '\'' +
                '}';
    }
}
