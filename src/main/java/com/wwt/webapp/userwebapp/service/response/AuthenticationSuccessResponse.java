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
package com.wwt.webapp.userwebapp.service.response;

/**
 * @author benw-at-wwt
 */
public class AuthenticationSuccessResponse extends BasicSuccessResponse {

    private final String token;
    private final String refreshToken;
    private final String loginId;
    private final String adminRole;

    public AuthenticationSuccessResponse(String token,String refreshToken, String loginId, String adminRole) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.loginId = loginId;
        this.adminRole = adminRole;
    }

    public AuthenticationSuccessResponse(String token, String loginId, String adminRole) {
        this.token = token;
        this.refreshToken = null;
        this.loginId = loginId;
        this.adminRole = adminRole;
    }

    public String getToken() {
        return token;
    }

    @SuppressWarnings("unused")
    public String getLoginId() {
        return loginId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getAdminRole() { return adminRole; }

    @Override
    public String toString() {
        return "AuthenticationSuccessResponse{" +
                "token='" + token + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", loginId='" + loginId + '\'' +
                ", adminRole='" + adminRole + '\'' +
                '}';
    }
}
