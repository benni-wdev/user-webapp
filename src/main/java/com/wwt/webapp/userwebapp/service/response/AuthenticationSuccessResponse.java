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
