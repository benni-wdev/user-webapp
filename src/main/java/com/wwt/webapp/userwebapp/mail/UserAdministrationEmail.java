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


import java.util.HashMap;
import java.util.Map;

/**
 * @author benw-at-wwt
 */
public abstract class UserAdministrationEmail implements Email {

    protected final Map<String,String> model;
    private final String emailAddress;

    public UserAdministrationEmail(String emailAddress, String loginId) {
        this.emailAddress = emailAddress;
        model = new HashMap<>();
        model.put(Email.NAME,loginId);
        model.put(Email.BODY1, "");
        model.put(Email.BODY2, "");
        model.put(Email.BODY3, "");
    }

    @Override
    public Map<String, String> getModel() {
        return model;
    }

    @Override
    public String getEmailAddress() {
        return emailAddress;
    }

}
