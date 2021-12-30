package com.wwt.webapp.userwebapp.service.response;


/**
 * @author beng
 */

public class RegistrationSuccessResp extends BasicSuccessResponse {

    private final String emailAddress;

    public RegistrationSuccessResp(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public String toString() {
        return "RegistrationSuccessResp{" +
                "emailAddress='" + emailAddress + '\'' +
                '}';
    }
}
