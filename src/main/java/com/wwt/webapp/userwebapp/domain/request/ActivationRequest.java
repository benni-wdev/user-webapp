package com.wwt.webapp.userwebapp.domain.request;

import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("unused")
@XmlRootElement
public class ActivationRequest implements InternalRequest {
	
	private  String activationToken;


    public ActivationRequest() {
    }

    public ActivationRequest(String activationToken) {
        this.activationToken = activationToken;
    }

    public void setActivationToken(String activationToken) {
        this.activationToken = activationToken;
    }

    public String getActivationToken() {
        return activationToken;
    }

    @Override
    public String toString() {
        return "ActivationRequest{" +
                "activationToken='" + activationToken + '\'' +
                '}';
    }
}
