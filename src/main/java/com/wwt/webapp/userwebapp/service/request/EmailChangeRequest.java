package com.wwt.webapp.userwebapp.service.request;

@SuppressWarnings("unused")
public class EmailChangeRequest implements InternalRequest {

	private String email;

    public EmailChangeRequest() {
    }

    public EmailChangeRequest(String email) {
        this.email = email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public final String toString() {
        return "EmailChangeRequest{" +
                (super.toString())+
                ", email='" + email + '\'' +
                '}';
    }
}
