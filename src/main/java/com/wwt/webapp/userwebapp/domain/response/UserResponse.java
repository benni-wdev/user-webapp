package com.wwt.webapp.userwebapp.domain.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * @author benw-at-wwt
 */
@XmlRootElement
public class UserResponse implements InternalResponse {


    private final String uuid;
    @JsonFormat(pattern="dd.MM.yyyy HH:mm")
    private final Date createdAt;
    private final int version;
    private final String loginId;
    private final String emailAddress;
    @JsonFormat(pattern="dd.MM.yyyy HH:mm")
    private final Date emailChangedAt;
    @JsonFormat(pattern="dd.MM.yyyy HH:mm")
    private final Date passwordChangedAt;
    @JsonFormat(pattern="dd.MM.yyyy HH:mm")
    private final Date lastLoggedInAt;

    public UserResponse(String uuid, Date createdAt, int version,String loginId, String emailAddress, Date emailChangedAt, Date passwordChangedAt, Date lastLoggedInAt) {
        this.uuid = uuid;
        this.createdAt = createdAt;
        this.version = version;
        this.loginId = loginId;
        this.emailAddress = emailAddress;
        this.emailChangedAt = emailChangedAt;
        this.passwordChangedAt = passwordChangedAt;
        this.lastLoggedInAt = lastLoggedInAt;
    }

    @Override
    public boolean isSuccessful() {
        return true;
    }

    @Override
    public String getMessageText() {
        return (MessageCode.OPERATION_SUCCESSFUL).toString();
    }

    @Override
    public MessageCode getMessageCode() {
        return MessageCode.OPERATION_SUCCESSFUL;
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

    @Override
    public String toString() {
        return "UserResponse{" +
                "uuid='" + uuid + '\'' +
                ", createdAt=" + createdAt +
                ", loginId='" + loginId + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", emailChangedAt=" + emailChangedAt +
                ", passwordChangedAt=" + passwordChangedAt +
                ", lastLoggedInAt=" + lastLoggedInAt +
                '}';
    }

}
