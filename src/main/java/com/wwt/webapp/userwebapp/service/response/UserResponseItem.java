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

import java.util.Date;

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

    UserResponseItem(String uuid, Date createdAt, int version, String loginId, String emailAddress, Date emailChangedAt, Date passwordChangedAt, Date lastLoggedInAt, AdminRole adminRole,ActivationStatus activationStatus) {
        this.uuid = uuid;
        this.createdAt = createdAt;
        this.version = version;
        this.loginId = loginId;
        this.emailAddress = emailAddress;
        this.emailChangedAt = emailChangedAt;
        this.passwordChangedAt = passwordChangedAt;
        this.lastLoggedInAt = lastLoggedInAt;
        this.adminRole = adminRole;
        this.activationStatus = activationStatus;
    }

    public String getUuid() {
        return uuid;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public int getVersion() {
        return version;
    }

    public String getLoginId() {
        return loginId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public Date getEmailChangedAt() {
        return emailChangedAt;
    }

    public Date getPasswordChangedAt() {
        return passwordChangedAt;
    }

    public Date getLastLoggedInAt() {
        return lastLoggedInAt;
    }

    public AdminRole getAdminRole() {
        return adminRole;
    }

    public ActivationStatus getActivationStatus() { return activationStatus; }

    @Override
    public String toString() {
        return "UserResponseItem{" +
                "uuid='" + uuid + '\'' +
                ", createdAt=" + createdAt +
                ", version=" + version +
                ", loginId='" + loginId + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", emailChangedAt=" + emailChangedAt +
                ", passwordChangedAt=" + passwordChangedAt +
                ", lastLoggedInAt=" + lastLoggedInAt +
                ", adminRole='" + adminRole + '\'' +
                ", status=" + activationStatus +
                '}';
    }
}
