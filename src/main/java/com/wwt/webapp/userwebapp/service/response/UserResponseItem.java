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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wwt.webapp.userwebapp.domain.ActivationStatus;
import com.wwt.webapp.userwebapp.domain.AdminRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Getter
@AllArgsConstructor
@ToString
public class UserResponseItem {

    private final String uuid;
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private final Date createdAt;
    private final int version;
    private final String loginId;
    private final String emailAddress;
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private final Date emailChangedAt;
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private final Date passwordChangedAt;
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private final Date lastLoggedInAt;
    private final AdminRole adminRole;
    private final ActivationStatus activationStatus;

}
