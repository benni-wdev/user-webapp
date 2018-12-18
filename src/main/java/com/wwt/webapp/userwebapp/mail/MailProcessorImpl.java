package com.wwt.webapp.userwebapp.mail;

import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.mail.MessagingException;

/**
 * @author benw-at-wwt
 */
@Stateless
public class MailProcessorImpl implements MailProcessor {

    private static final Logger logger = Logger.getLogger(MailProcessorImpl.class);

    private final EmailSender emailSender = new EmailSender();

    @Override
    public boolean isSendMailSuccessful(EmailType emailType, String emailAddress, String token) {
        Email email = EmailFactory.produceEmail(emailType,emailAddress,token);
        try {
            emailSender.sendMail(email);
            return true;
        }
        catch (MessagingException e) {
            logger.error("Mail could not be sent ",e);
            return false;
        }
    }
}
