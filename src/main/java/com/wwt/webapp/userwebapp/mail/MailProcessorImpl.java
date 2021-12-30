package com.wwt.webapp.userwebapp.mail;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailProcessorImpl implements MailProcessor {

    @Autowired
    private SharedMailSender sharedMailSender;

    @Override
    public void sendEmail(EmailType emailType, String emailAddress,String loginId, String token) {
        Email email = EmailFactory.produceEmail(emailType,emailAddress,loginId,token);
        sharedMailSender.sendMail(email);
    }

}
