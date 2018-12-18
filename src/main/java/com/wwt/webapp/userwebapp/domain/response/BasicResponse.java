package com.wwt.webapp.userwebapp.domain.response;

import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author benw-at-wwt
 */
@XmlRootElement
public class BasicResponse implements InternalResponse {
    private final boolean isSuccessful;
    private final MessageCode messageCode;

    public BasicResponse(boolean isSuccessful, MessageCode messageCode) {
        this.isSuccessful = isSuccessful;
        this.messageCode = messageCode;
    }

    @Override
    public boolean isSuccessful() {
        return isSuccessful;
    }

    @Override
    public String getMessageText() {
        return messageCode.getMessage();
    }

    @Override
    public MessageCode getMessageCode() {
        return messageCode;
    }

    @Override
    public String toString() {
        return "BasicResponse{" +
                "isSuccessful=" + isSuccessful +
                ", messageCode=" + messageCode +
                '}';
    }
}
