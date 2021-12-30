package com.wwt.webapp.userwebapp.service.request;


@SuppressWarnings("unused")
public class PasswordChangeRequest implements InternalRequest {

	private String oldPassword;
	private String newPassword;

    public PasswordChangeRequest() {
    }

    public PasswordChangeRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    @Override
    public String toString() {
        return "PasswordChangeRequest{CONTENT HIDED}";
    }
}