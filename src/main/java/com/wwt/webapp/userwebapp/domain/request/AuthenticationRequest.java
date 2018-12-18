package com.wwt.webapp.userwebapp.domain.request;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author benw-at-wwt
 *
 */
@SuppressWarnings("ALL")
@XmlRootElement
public class AuthenticationRequest implements InternalRequest {
	
	private String loginId;
	private String password;
	private boolean longSession;

    public AuthenticationRequest() {
    }

    public AuthenticationRequest(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
        longSession = false;
    }

    public boolean isLongSession() { return longSession; }

    public void setLongSession(boolean isLongSession) { this.longSession = isLongSession; }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLoginId() {
        return loginId;
    }

    public String getPassword() {
        return password;
    }


    @Override
    public String toString() {
        return "AuthenticationRequest{" +
                "loginId='" + loginId + '\'' +
                ", longSession=" + longSession +
                '}';
    }
}
