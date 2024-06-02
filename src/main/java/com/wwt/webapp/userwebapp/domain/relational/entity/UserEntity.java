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
package com.wwt.webapp.userwebapp.domain.relational.entity;

import com.wwt.webapp.userwebapp.domain.ActivationStatus;
import com.wwt.webapp.userwebapp.domain.AdminRole;
import com.wwt.webapp.userwebapp.helper.TimestampHelper;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@Entity
@Table(
        name = "USR_USER"
)
@SuppressWarnings("unused")
public class UserEntity extends BaseEntity {

    @Getter
    @Column(name = "LOGIN_ID",length = 36, unique=true)
    private String loginId;

    @Getter
    @Column(name = "EMAIL_ADDRESS", unique=true)
    private String emailAddress;

    @Getter
    @Column(name = "EMAIL_CHANGED_AT")
    private Timestamp emailChangedAt;

    @Getter
    @Column(name = "PASSWORD_HASH")
    private String passwordHash;

    @Getter
    @Column(name = "PASSWORD_CHANGED_AT")
    private Timestamp passwordChangedAt;

    @Getter
    @Column(name = "ACTIVATION_STATUS",length = 36,nullable = false)
    @Enumerated(EnumType.STRING)
    private ActivationStatus activationStatus;

    @Getter
    @Column(name = "ACTIVATION_STATUS_CHANGED_AT")
    private Timestamp activationStatusChangedAt;

    @Getter
    @Column(name = "ACTIVATION_TOKEN", unique=true)
    private String activationToken;

    @Getter
    @Column(name = "ACTIVATION_TOKEN_EXPIRES_AT")
    private Timestamp activationTokenExpiresAt;

    @Getter
    @Column(name = "FAILED_LOGINS")
    private int failedLogins;

    @Getter
    @Column(name = "LAST_LOGGED_IN_AT")
    private Timestamp lastLoggedInAt;

    @Getter
    @Column(name = "PASSWORD_RECOVERY_TOKEN", unique=true)
    private String passwordRecoveryToken;

    @Getter
    @Column(name = "PASSW_RECOVERY_TOKEN_EXPIRES_AT")
    private Timestamp passwordRecoveryTokenExpiresAt;

    @Getter
    @Column(name = "REFRESH_TOKEN", unique=true)
    private String refreshToken;

    @Column(name = "ADMIN_ROLE",length = 36, nullable = false)
    @Enumerated(EnumType.STRING)
    private AdminRole adminRole;


    public UserEntity(String loginId, String emailAddress, String passwordHash, String activationToken, Timestamp activationTokenExpiresAt) {
        this.loginId = loginId;
        this.emailAddress = emailAddress;
        this.passwordHash = passwordHash;
        this.activationToken = activationToken;
        this.activationTokenExpiresAt = activationTokenExpiresAt;
        this.activationStatus = ActivationStatus.ESTABLISHED;
        this.adminRole = AdminRole.NO_ROLE;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        this.emailChangedAt = TimestampHelper.getNowAsUtcTimestamp();
        updateBase();
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        updateBase();
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
        this.passwordChangedAt = TimestampHelper.getNowAsUtcTimestamp();
        updateBase();
    }

    public void setActivationStatus(ActivationStatus activationStatus) {
        this.activationStatus = activationStatus;
        this.activationStatusChangedAt = TimestampHelper.getNowAsUtcTimestamp();
        updateBase();
    }

    public void setActivationToken(String activationToken, Timestamp activationTokenExpiresAt) {
        this.activationToken = activationToken;
        this.activationTokenExpiresAt = activationTokenExpiresAt;
        updateBase();
    }

    public void setPasswordRecoveryToken(String passwordRecoveryToken, Timestamp passwordRecoveryTokenExpiresAt) {
        this.passwordRecoveryToken = passwordRecoveryToken;
        this.passwordRecoveryTokenExpiresAt = passwordRecoveryTokenExpiresAt;
        updateBase();
    }

    public void setFailedLogins(int failedLogins) {
        this.failedLogins = failedLogins;
        updateBase();
    }

    public void setLastLoggedInAt(Timestamp lastLoggedInAt) {
        this.lastLoggedInAt = lastLoggedInAt;
        this.failedLogins   = 0;
        updateBase();
    }

    public void setAdminRole(AdminRole adminRole) {
        this.adminRole = adminRole;
        updateBase();
    }


    public AdminRole getAdminRole() { return adminRole == null ? AdminRole.NO_ROLE:adminRole; }


    @Override
    public String toString() {
        return "UserEntity{" +
                super.toString()+
                ", loginId='" + loginId + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", emailChangedAt=" + emailChangedAt +
                ", passwordChangedAt=" + passwordChangedAt +
                ", activationStatus=" + activationStatus +
                ", activationStatusChangedAt=" + activationStatusChangedAt +
                ", activationTokenExpiresAt=" + activationTokenExpiresAt +
                ", failedLogins=" + failedLogins +
                ", lastLoggedInAt=" + lastLoggedInAt +
                ", passwordRecoveryTokenExpiresAt=" + passwordRecoveryTokenExpiresAt +
                ", refreshToken='" + refreshToken + '\'' +
                ", adminRole=" + adminRole +
                '}';
    }
}
