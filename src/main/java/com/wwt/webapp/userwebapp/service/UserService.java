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
package com.wwt.webapp.userwebapp.service;


import com.wwt.webapp.userwebapp.domain.ActivationStatus;
import com.wwt.webapp.userwebapp.domain.AdminRole;
import com.wwt.webapp.userwebapp.service.response.InternalResponse;

/**
 * @author benw-at-wwt
 */
public interface UserService {


    InternalResponse readUser(String userUuid);
    InternalResponse changePassword(String userUuid, String oldPassword, String newPassword);
    InternalResponse changeEmail(String userUuid, String newEmailAddress);
    InternalResponse archiveUser(String userUuid, String password);
    InternalResponse readAllUsers();
    InternalResponse updateUser(String userUuid, String email, AdminRole adminRole, ActivationStatus activationStatus);

}
