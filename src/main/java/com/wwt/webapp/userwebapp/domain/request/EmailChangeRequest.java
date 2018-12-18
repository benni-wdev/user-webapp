package com.wwt.webapp.userwebapp.domain.request;

import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author benw-at-wwt
 */
@XmlRootElement
@SuppressWarnings("unused")
public class EmailChangeRequest extends AuthenticatedRequest implements InternalRequest {

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
