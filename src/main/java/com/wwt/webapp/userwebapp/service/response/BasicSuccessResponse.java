/*
 *  Copyright 2021  Wehe Web Technologies - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Benjamin Wehe (contact@wehe-web-technologies.ch)
 *
 */

package com.wwt.webapp.userwebapp.service.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BasicSuccessResponse implements InternalResponse {

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
}
