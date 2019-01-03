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
package com.wwt.webapp.userwebapp.domain.request;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author benw-at-wwt
 *
 */
@XmlRootElement
@SuppressWarnings("unused")
public class RegistrationRequest implements InternalRequest {
	
	private String loginId;
	private String password;
	private String emailAddress;

    public RegistrationRequest() {
    }

    public RegistrationRequest(String loginId, String password, String emailAddress) {
        this.loginId = loginId;
        this.password = password;
        this.emailAddress = emailAddress;
    }

    @XmlElement
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    @XmlElement
    public void setPassword(String password) {
        this.password = password;
    }

    @XmlElement
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getLoginId() {
        return loginId;
    }

    public String getPassword() {
        return password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public final String toString() {
        return "RegistrationRequest{" +
                "loginId='" + loginId + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                '}';
    }
}
