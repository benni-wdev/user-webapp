package com.wwt.webapp.userwebapp.domain.request;

import javax.validation.constraints.NotNull;

/**
 * @author benw-at-wwt
 */
public class AuthenticatedRequest implements InternalRequest {

    private String idToken;

    public AuthenticatedRequest() {
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(@NotNull String idToken) {
        this.idToken = idToken;
    }


    @Override
    public String toString() {
        return "AuthenticatedRequest{" +
                "idToken='" + idToken + '\'' +
                '}';
    }
}
