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

import java.util.ArrayList;
import java.util.List;

public class UserResponseList  extends BasicSuccessResponse {

    private List<UserResponseItem> users;

    @SuppressWarnings("WeakerAccess")
    public UserResponseList() {
        users = new ArrayList<>();
    }

    public List<UserResponseItem> getUsers() {
        return users;
    }

    public void setUsers(List<UserResponseItem> users) {
        this.users = users;
    }

    private void addUser(UserResponseItem user) {
        users.add(user);
    }


    public static UserResponseList convertToUserResponseList(List<UserEntity> users) {
        UserResponseList l = new UserResponseList();
        for (UserEntity user : users) {
            l.addUser(new UserResponseItem(user.getUuid(), user.getCreatedAt(), user.getVersion(), user.getLoginId(), user.getEmailAddress(),
                    user.getEmailChangedAt(), user.getPasswordChangedAt(), user.getLastLoggedInAt(),user.getAdminRole(),user.getActivationStatus()));
        }
        return l;
    }
}
