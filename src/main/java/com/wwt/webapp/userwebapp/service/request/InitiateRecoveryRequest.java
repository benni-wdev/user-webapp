package com.wwt.webapp.userwebapp.service.request;


@SuppressWarnings("unused")
public class InitiateRecoveryRequest implements InternalRequest {

    private String email;

    public InitiateRecoveryRequest() {
    }

    public InitiateRecoveryRequest(String email) {
        this.email = email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "InitiateRecoveryRequest{" +
                ", email='" + email + '\'' +
                '}';
    }
}
