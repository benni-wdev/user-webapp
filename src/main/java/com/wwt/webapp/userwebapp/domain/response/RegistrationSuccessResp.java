package com.wwt.webapp.userwebapp.domain.response;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author benw-at-wwt
 */
@XmlRootElement
@SuppressWarnings("unused")
public class RegistrationSuccessResp implements InternalResponse {

    private final String emailAddress;

    public RegistrationSuccessResp(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public boolean isSuccessful() {
        return true;
    }

    @Override
    public MessageCode getMessageCode() {
        return MessageCode.OPERATION_SUCCESSFUL;
    }

    @Override
    public String getMessageText() {
        return MessageCode.OPERATION_SUCCESSFUL.getMessage();
    }

    @Override
    public String toString() {
        return "RegistrationSuccessResp{" +
                "emailAddress='" + emailAddress + '\'' +
                '}';
    }
}
