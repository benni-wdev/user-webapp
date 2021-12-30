package com.wwt.webapp.userwebapp.service.request;

/**
 * @author benw-at-wwt
 */
public class ArchiveRequest implements InternalRequest {


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
