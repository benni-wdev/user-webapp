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
package com.wwt.webapp.userwebapp.domain;

import com.wwt.webapp.userwebapp.domain.response.UserResponse;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author benw-at-wwt
 */
@SuppressWarnings("unused")
public class UserDto {

    private final String uuid;
    private Date createdAt;
    private int version;
    private String loginId;
    private String emailAddress;
    private Date emailChangedAt;
    private Date passwordChangedAt;
    private ActivationStatus activationStatus;
    private Date activationStatusChangedAt;
    private Date lastLoggedInAt;

    /**
     * Telescope constructor because of the JPA projection
     */

    public UserDto(String uuid) {
        this.uuid = uuid;
    }

    public UserDto(@NotNull String uuid, @NotNull Date createdAt, int version, String loginId, String emailAddress, Date emailChangedAt,
                   Date passwordChangedAt, ActivationStatus activationStatus, Date activationStatusChangedAt,
                   Date lastLoggedInAt) {
        this.uuid = uuid;
        this.createdAt = createdAt;
        this.version = version;
        this.loginId = loginId;
        this.emailAddress = emailAddress;
        this.emailChangedAt = emailChangedAt;
        this.passwordChangedAt = passwordChangedAt;
        this.activationStatus = activationStatus;
        this.activationStatusChangedAt = activationStatusChangedAt;
        this.lastLoggedInAt = lastLoggedInAt;
    }

    public String getUuid() {
        return uuid;
    }

    public Date getCreatedAt() {
        return createdAt;
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

    public ActivationStatus getActivationStatus() {
        return activationStatus;
    }

    public Date getActivationStatusChangedAt() {
        return activationStatusChangedAt;
    }

    public int getVersion() { return version; }

    public Date getLastLoggedInAt() { return lastLoggedInAt; }

    @Override
    public String toString() {
        return "UserDto{" +
                "uuid='" + uuid + '\'' +
                ", createdAt=" + createdAt +
                ", version=" + version +
                ", loginId='" + loginId + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", emailChangedAt=" + emailChangedAt +
                ", passwordChangedAt=" + passwordChangedAt +
                ", activationStatus='" + activationStatus + '\'' +
                ", activationStatusChangedAt=" + activationStatusChangedAt + '\'' +
                ", lastLoggedInAt=" + lastLoggedInAt +
                '}';
    }

    public UserResponse convert() {
        return new UserResponse(uuid,createdAt,version,loginId,emailAddress,emailChangedAt,passwordChangedAt,lastLoggedInAt);
    }
}
