/* Copyright 2018-2021 Wehe Web Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wwt.webapp.userwebapp.service.response;

import lombok.*;

/**
 * @author benw-at-wwt
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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


    @Override
    public String getMessageText() {
        return messageCode.getMessage();
    }

}
