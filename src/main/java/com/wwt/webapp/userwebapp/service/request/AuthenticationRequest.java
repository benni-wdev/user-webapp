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
package com.wwt.webapp.userwebapp.service.request;


/**
 * 
 * @author beng
 *
 */
@SuppressWarnings("ALL")
public class AuthenticationRequest implements InternalRequest {
	
	private String loginId;
	private String password;
	private boolean longSession;

    public AuthenticationRequest() {
    }

    public AuthenticationRequest(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
        longSession = false;
    }

    public boolean isLongSession() { return longSession; }

    public void setLongSession(boolean isLongSession) { this.longSession = isLongSession; }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLoginId() {
        return loginId;
    }

    public String getPassword() {
        return password;
    }


    @Override
    public String toString() {
        return "AuthenticationRequest{" +
                "loginId='" + loginId + '\'' +
                ", longSession=" + longSession +
                '}';
    }
}
