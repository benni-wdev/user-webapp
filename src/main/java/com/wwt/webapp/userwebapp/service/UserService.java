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
package com.wwt.webapp.userwebapp.service;

import com.wwt.webapp.userwebapp.domain.request.ArchiveRequest;
import com.wwt.webapp.userwebapp.domain.request.AuthenticatedRequest;
import com.wwt.webapp.userwebapp.domain.request.EmailChangeRequest;
import com.wwt.webapp.userwebapp.domain.request.PasswordChangeRequest;
import com.wwt.webapp.userwebapp.domain.response.InternalResponse;


/**
 * @author benw-at-wwt
 */
public interface UserService {


    InternalResponse readUser(AuthenticatedRequest request);
    InternalResponse changePassword(PasswordChangeRequest passwordChangeReq);
    InternalResponse changeEmail(EmailChangeRequest emailChangeReq);
    InternalResponse archiveUser(ArchiveRequest archiveRequestRequest);

}
