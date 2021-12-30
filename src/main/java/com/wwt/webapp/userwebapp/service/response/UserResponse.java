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

import com.wwt.webapp.userwebapp.domain.relational.entity.UserEntity;

import java.util.Date;

/**
 * @author beng
 */
public class UserResponse extends BasicSuccessResponse {


    private final UserResponseItem userResponseItem;

    private UserResponse(UserResponseItem userResponseItem) {
        this.userResponseItem = userResponseItem;
    }

    public String getUuid() {
        return userResponseItem.getUuid();
    }

    public Date getCreatedAt() {
        return userResponseItem.getCreatedAt();
    }

    public int getVersion() {
        return userResponseItem.getVersion();
    }

    public String getLoginId() {
        return userResponseItem.getLoginId();
    }

    public String getEmailAddress() {
        return userResponseItem.getEmailAddress();
    }

    public Date getEmailChangedAt() {
        return userResponseItem.getEmailChangedAt();
    }

    public Date getPasswordChangedAt() { return userResponseItem.getPasswordChangedAt(); }

    public Date getLastLoggedInAt() { return userResponseItem.getLastLoggedInAt(); }


    @Override
    public String toString() {
        return "UserResponse{" +
                ", userResponseItem=" + userResponseItem +
                '}';
    }

    public static UserResponse convertToUserResponse(UserEntity user) {
        return new UserResponse(new UserResponseItem(user.getUuid(),user.getCreatedAt(),user.getVersion(),user.getLoginId(),user.getEmailAddress(),
                user.getEmailChangedAt(),user.getPasswordChangedAt(),user.getLastLoggedInAt(),user.getAdminRole(), user.getActivationStatus()));
    }

}
