/* Copyright 2018-2019 Wehe Web Technologies
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
