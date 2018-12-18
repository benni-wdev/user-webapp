package com.wwt.webapp.userwebapp.mail;

/**
 * @author benw-at-wwt
 */
public interface MailProcessor {

    boolean isSendMailSuccessful(EmailType emailType, String emailAddress, String token);
}
