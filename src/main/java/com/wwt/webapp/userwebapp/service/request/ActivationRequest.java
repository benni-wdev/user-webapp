package com.wwt.webapp.userwebapp.service.request;


@SuppressWarnings("unused")
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
