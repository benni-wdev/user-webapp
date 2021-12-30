package com.wwt.webapp.userwebapp.service.request;



/**
 * @author benw-at-wwt
 */
@SuppressWarnings("unused")
public class RefreshAuthenticationRequest implements InternalRequest {

    private String refreshToken;

    public RefreshAuthenticationRequest() {
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }


    @Override
    public String toString() {
        return "RefreshAuthenticationRequest{" +
                "refreshToken='" + refreshToken + '\'' +
                '}';
    }
}
