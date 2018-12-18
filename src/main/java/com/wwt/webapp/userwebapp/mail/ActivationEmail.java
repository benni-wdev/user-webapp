package com.wwt.webapp.userwebapp.mail;

import com.wwt.webapp.userwebapp.util.ConfigProvider;

/**
 * @author benw-at-wwt
 */
class ActivationEmail implements Email {

    private static final String subject = ConfigProvider.getConfigValue("appName")+" email verification";
    private static final String bodyText = "Please access the following link for email verification: \n";
    private final String emailAddress;
    private final String token;

    ActivationEmail(String emailAddress, String token) {
        this.emailAddress = emailAddress;
        this.token = token;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public String getBody() {
        return bodyText + "<a href =\""+ ConfigProvider.getConfigValue("activationBaseUrl") + token
                +"\">"+ConfigProvider.getConfigValue("activationBaseUrl") + token+"</a>";
    }

    @Override
    public String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public String toString() {
        return "ActivationEmail{" +
                "emailAddress='" + emailAddress + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
