package com.wwt.webapp.userwebapp.mail;

/**
 * @author benw-at-wwt
 */
public interface MailProcessor {

    void sendEmail(EmailType emailType, String emailAddress,String loginId, String token);
}
