package com.wwt.webapp.userwebapp.domain.response;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author benw-at-wwt
 */
@XmlRootElement
public class AuthenticationSuccessResponse implements InternalResponse {

    private final String token;
    private final String loginId;


    public AuthenticationSuccessResponse(String token, String loginId) {
        this.token = token;
        this.loginId = loginId;
    }

    public String getToken() {
        return token;
    }

    @SuppressWarnings("unused")
    public String getLoginId() {
        return loginId;
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
        return (MessageCode.OPERATION_SUCCESSFUL).getMessage();
    }

    @Override
    public String toString() {
        return "AuthenticationSuccessResponse{" +
                "token='" + token + '\'' +
                "loginId='" + loginId + '\'' +
                '}';
    }
}
