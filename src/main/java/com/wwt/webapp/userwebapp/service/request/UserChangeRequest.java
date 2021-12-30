package com.wwt.webapp.userwebapp.service.request;


import com.wwt.webapp.userwebapp.domain.ActivationStatus;
import com.wwt.webapp.userwebapp.domain.AdminRole;

public class UserChangeRequest implements InternalRequest {

    private String emailAddress;
    private ActivationStatus activationStatus;
    private AdminRole adminRole;

    public UserChangeRequest() {}

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public ActivationStatus getActivationStatus() {
        return activationStatus;
    }

    public void setActivationStatus(ActivationStatus activationStatus) {
        this.activationStatus = activationStatus;
    }

    public AdminRole getAdminRole() {
        return adminRole;
    }

    public void setAdminRole(AdminRole adminRole) {
        this.adminRole = adminRole;
    }

    @Override
    public String toString() {
        return "UserChangeRequest{" +
                "emailAddress='" + emailAddress + '\'' +
                ", activationStatus=" + activationStatus +
                ", adminRole=" + adminRole +
                '}';
    }
}
