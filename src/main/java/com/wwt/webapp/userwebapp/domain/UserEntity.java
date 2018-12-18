package com.wwt.webapp.userwebapp.domain;

import com.wwt.webapp.userwebapp.util.StaticHelper;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * @author benw-at-wwt
 */
@Entity
@Table(name = "USR_USER")
@SuppressWarnings("unused")
public class UserEntity {

    @Id
    @Column(name = "UUID")
    private String uuid;

    @Column(name = "CREATED_AT")
    private Timestamp createdAt;

    @Column(name= "VERSION")
    private int version;

    @Column(name = "LOGIN_ID", unique=true)
    private String loginId;

    @Column(name = "EMAIL_ADDRESS", unique=true)
    private String emailAddress;

    @Column(name = "EMAIL_CHANGED_AT")
    private Timestamp emailChangedAt;

    @Column(name = "PASSWORD_HASH")
    private String passwordHash;

    @Column(name = "PASSWORD_CHANGED_AT")
    private Timestamp passwordChangedAt;

    @Column(name = "ACTIVATION_STATUS")
    @Enumerated(EnumType.STRING)
    private ActivationStatus activationStatus;

    @Column(name = "ACTIVATION_STATUS_CHANGED_AT")
    private Timestamp activationStatusChangedAt;

    @Column(name = "ACTIVATION_TOKEN", unique=true)
    private String activationToken;

    @Column(name = "ACTIVATION_TOKEN_EXPIRES_AT")
    private Timestamp activationTokenExpiresAt;

    @Column(name = "FAILED_LOGINS")
    private int failedLogins;

    @Column(name = "LAST_LOGGED_IN_AT")
    private Timestamp lastLoggedInAt;

    @Column(name = "PASSWORD_RECOVERY_TOKEN", unique=true)
    private String passwordRecoveryToken;

    @Column(name = "PASSW_RECOVERY_TOKEN_EXPIRES_AT")
    private Timestamp passwordRecoveryTokenExpiresAt;

    UserEntity() {}

    public UserEntity(String loginId, String emailAddress, String passwordHash, UserStatusChangeToken activationToken) {
        this.uuid = UUID.randomUUID().toString();
        this.version = 1;
        this.loginId = loginId;
        this.emailAddress = emailAddress;
        this.passwordHash = passwordHash;
        this.activationToken = activationToken.getToken();
        this.activationTokenExpiresAt = activationToken.getTokenExpiresAt();
        this.activationStatus = ActivationStatus.ESTABLISHED;
        this.createdAt = StaticHelper.getNowAsUtcTimestamp();
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        this.emailChangedAt = StaticHelper.getNowAsUtcTimestamp();
        this.version++;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
        this.passwordChangedAt = StaticHelper.getNowAsUtcTimestamp();
        this.version++;
    }

    public void setActivationStatus(ActivationStatus activationStatus) {
        this.activationStatus = activationStatus;
        this.activationStatusChangedAt = StaticHelper.getNowAsUtcTimestamp();
        this.version++;
    }

    public void setActivationToken(UserStatusChangeToken activationToken) {
        this.activationToken = activationToken.getToken();
        this.activationTokenExpiresAt = activationToken.getTokenExpiresAt();
        this.version++;
    }

    public void setPasswordRecoveryToken(UserStatusChangeToken passwordRecoveryToken) {
        this.passwordRecoveryToken = passwordRecoveryToken.getToken();
        this.passwordRecoveryTokenExpiresAt = passwordRecoveryToken.getTokenExpiresAt();
        this.version++;
    }

    public void setFailedLogins(int failedLogins) {
        this.failedLogins = failedLogins;
        this.version++;
    }

    public void setLastLoggedInAt(Timestamp lastLoggedInAt) {
        this.lastLoggedInAt = lastLoggedInAt;
        this.failedLogins   = 0;
        this.version++;
    }

    public Timestamp getLastLoggedInAt() {
        return lastLoggedInAt;
    }

    public int getFailedLogins() {
        return failedLogins;
    }

    public String getUuid() {
        return uuid;
    }

    public Timestamp getCreatedAt() { return createdAt; }

    public String getLoginId() {
        return loginId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public Timestamp getEmailChangedAt() { return emailChangedAt; }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Timestamp getPasswordChangedAt() { return passwordChangedAt; }

    public ActivationStatus getActivationStatus() {
        return activationStatus;
    }

    public Timestamp getActivationStatusChangedAt() { return activationStatusChangedAt; }

    public String getActivationToken() {
        return activationToken;
    }

    public Timestamp getActivationTokenExpiresAt() {
        return activationTokenExpiresAt;
    }

    public String getPasswordRecoveryToken() {
        return passwordRecoveryToken;
    }

    public Timestamp getPasswordRecoveryTokenExpiresAt() {
        return passwordRecoveryTokenExpiresAt;
    }

    public int getVersion() { return version; }


    @Override
    public String toString() {
        return "UserEntity{" +
                "uuid='" + uuid + '\'' +
                ", createdAt=" + createdAt +
                ", version=" + version +
                ", loginId='" + loginId + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", emailChangedAt=" + emailChangedAt +
                ", passwordChangedAt=" + passwordChangedAt +
                ", activationStatus='" + activationStatus + '\'' +
                ", activationStatusChangedAt=" + activationStatusChangedAt +
                ", activationToken='" + activationToken + '\'' +
                ", activationTokenExpiresAt=" + activationTokenExpiresAt +
                ", passwordRecoveryToken='" + passwordRecoveryToken + '\'' +
                ", passwordRecoveryTokenExpiresAt=" + passwordRecoveryTokenExpiresAt +
                '}';
    }
}
