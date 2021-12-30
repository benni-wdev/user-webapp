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


/**
 * @author benw-at-wwt
 */
public enum MessageCode {

    /*Registration */
    OPERATION_SUCCESSFUL("Operation successful"),
    LOGIN_ID_ALREADY_EXISTS("Login ID already exists"),                                         //400
    EMAIL_ADDRESS_ALREADY_EXISTS("Email address already exists"),                               //400
    LOGIN_OR_PASSWORD_WRONG("Login or Password wrong"),                                         //404
    TOO_MANY_FAILED_LOGINS("Too many failed logins"),                                           //423
    EMAIL_ADDRESS_NOT_VALID("Email address value invalid"),                                     //404
    LOGIN_ID_NOT_VALID("Login ID value invalid"),                                               //404
    REGISTRATION_CODE_NOT_VALID("Check your registration code, registration currently only possible for invited users."), //403
    /* Account Recovery */
    PASSWORD_TOKEN_NOT_KNOWN("Password recovery token not known"),                              //404
    PASSWORD_RECOVERY_TOKEN_EXPIRED("Recovery token expired"),                                  //401
    RECOVERY_ALREADY_DONE_OR_NOT_POSSIBLE("Recovery is already done or not possible"),          //400
    /* Email change */
    /* Activation */
    ACTIVATION_TOKEN_NOT_KNOWN("Activation token not known"),                                   //404
    ACTIVATION_TOKEN_EXPIRED("Activation token expired"),                                       //401
    ACTIVATION_ALREADY_DONE_OR_NOT_POSSIBLE("Activation is already done or not possible"),      //400
    /* Session */
    SESSION_INVALID("Session invalid"),                                                         //401
    SESSION_EXPIRED("Session expired"),                                                         //401
    USER_NOT_ACTIVE("User not active"),                                                         //423
    /* Misc */
    NO_CHANGE_NO_UPDATE("No change in value"),
    CONCURRENT_MODIFICATION("Concurrent modification"),                                         //409
    FORBIDDEN("Not authorzied for Operation"),                                                  //403
    UNEXPECTED_ERROR("Operation not possible at the moment"),                                   //500
    ITEM_NOT_KNOWN("Item not known"),                                                           //404
    INPUT_NOT_VALID("Input not valid")                                                           //400
    ;
    private final String message;

    MessageCode(String s) {
        message = s;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return this.getMessage();
    }

}