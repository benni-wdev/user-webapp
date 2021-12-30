/*
 *  Copyright 2021  Wehe Web Technologies - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Benjamin Wehe (contact@wehe-web-technologies.ch)
 *
 */

package com.wwt.webapp.userwebapp.service.response;

/**
 * @author benw-at-wwt
 */
public class BasicResponse implements InternalResponse {

    public static final BasicResponse SESSION_INVALID                         = new BasicResponse(false, MessageCode.SESSION_INVALID);
    public static final BasicResponse SUCCESS                                 = new BasicResponse(true, MessageCode.OPERATION_SUCCESSFUL);
    public static final BasicResponse LOGIN_OR_PASSWORD_WRONG                 = new BasicResponse(false, MessageCode.LOGIN_OR_PASSWORD_WRONG);
    public static final BasicResponse USER_NOT_ACTIVE                         = new BasicResponse(false, MessageCode.USER_NOT_ACTIVE);
    public static final BasicResponse EMAIL_ADDRESS_NOT_VALID                 = new BasicResponse(false, MessageCode.EMAIL_ADDRESS_NOT_VALID);
    public static final BasicResponse NO_CHANGE_NO_UPDATE                     = new BasicResponse(false, MessageCode.NO_CHANGE_NO_UPDATE);
    public static final BasicResponse INPUT_NOT_VALID                          = new BasicResponse(false, MessageCode.INPUT_NOT_VALID);
    public static final BasicResponse EMAIL_ADDRESS_ALREADY_EXISTS            = new BasicResponse(false, MessageCode.EMAIL_ADDRESS_ALREADY_EXISTS);
    public static final BasicResponse LOGIN_ID_NOT_VALID                      = new BasicResponse(false, MessageCode.LOGIN_ID_NOT_VALID);
    public static final BasicResponse LOGIN_ID_ALREADY_EXISTS                 = new BasicResponse(false, MessageCode.LOGIN_ID_ALREADY_EXISTS);
    public static final BasicResponse ACTIVATION_TOKEN_NOT_KNOWN              = new BasicResponse(false, MessageCode.ACTIVATION_TOKEN_NOT_KNOWN);
    public static final BasicResponse ACTIVATION_ALREADY_DONE_OR_NOT_POSSIBLE = new BasicResponse(false, MessageCode.ACTIVATION_ALREADY_DONE_OR_NOT_POSSIBLE);
    public static final BasicResponse ACTIVATION_TOKEN_EXPIRED                = new BasicResponse(false, MessageCode.ACTIVATION_TOKEN_EXPIRED);
    public static final BasicResponse REGISTRATION_CODE_NOT_VALID             = new BasicResponse(false,MessageCode.REGISTRATION_CODE_NOT_VALID);
    public static final BasicResponse FORBIDDEN                               = new BasicResponse(false, MessageCode.FORBIDDEN);
    public static final BasicResponse ITEM_NOT_KNOWN                          = new BasicResponse(false, MessageCode.ITEM_NOT_KNOWN);
    public static final BasicResponse UNEXPECTED_ERROR                        = new BasicResponse(false, MessageCode.UNEXPECTED_ERROR);
    private boolean isSuccessful;
    private MessageCode messageCode;

    public BasicResponse() {
    }

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

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public void setMessageCode(MessageCode messageCode) {
        this.messageCode = messageCode;
    }

    @Override
    public String toString() {
        return "BasicResponse{" +
                "isSuccessful=" + isSuccessful +
                ", messageCode=" + messageCode +
                '}';
    }
}
