package com.wwt.webapp.userwebapp.domain.request;


import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author benw-at-wwt
 */
@XmlRootElement
@SuppressWarnings("unused")
public class InitiateRecoveryRequest implements InternalRequest {

    private String email;

    public InitiateRecoveryRequest() {
    }

    public InitiateRecoveryRequest(String email) {
        this.email = email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "InitiateRecoveryRequest{" +
                ", email='" + email + '\'' +
                '}';
    }
}
