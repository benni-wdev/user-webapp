package com.wwt.webapp.userwebapp.mail;

/**
 * @author benw-at-wwt
 */
@SuppressWarnings("WeakerAccess")
public interface Email {

    String getSubject();
    String getBody();
    String getEmailAddress();

}
