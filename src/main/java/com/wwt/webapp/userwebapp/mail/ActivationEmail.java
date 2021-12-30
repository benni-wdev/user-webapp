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
class ActivationEmail extends UserAdministrationEmail {

    private static final String subject = ConfigProvider.getConfigValue("appName")+" - Email verification";
    private static final String preHeader = "Verify your email to activate your account";
    private static final String bodyText = "Please access the following link for email verification: ";
    private final String token;

    public ActivationEmail(String emailAddress, String loginId, String token) {
        super(emailAddress,loginId);
        this.token = token;
        model.put( Email.TITLE,getSubject());
        model.put(Email.PREHEADER,preHeader);
        model.put(Email.BODY1, bodyText);
        model.put(Email.BODY2, getLink());
    }

    @Override
    public String getSubject() {
        return subject;
    }


    private String getLink() {
        String url = ConfigProvider.getConfigValue("baseUrl")+ConfigProvider.getConfigValue("activationPath");
        return  "<a href =\""+url + token +"\">"+url + token+"</a>";
    }

    @Override
    public String toString() {
        return "ActivationEmail{" +
                "emailAddress='" + getEmailAddress() + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
