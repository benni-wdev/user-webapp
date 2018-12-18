package com.wwt.webapp.userwebapp.domain.request;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author benw-at-wwt
 */
@XmlRootElement
public class ArchiveRequest extends AuthenticatedRequest implements InternalRequest {


    private String password;

    @SuppressWarnings("unused")
    public ArchiveRequest() {
    }

    public ArchiveRequest(String password) {
        this.password = password;
    }

    @SuppressWarnings("unused")
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public final String toString() {
        return "ArchiveRequest{" +
               super.toString()+
                '}';
    }
}
