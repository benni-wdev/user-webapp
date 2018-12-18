package com.wwt.webapp.userwebapp.mail;

import com.wwt.webapp.userwebapp.util.ConfigProvider;

/**
 * @author benw-at-wwt
 */
class PasswordRecoveryEmail implements Email {

    private static final String subject = ConfigProvider.getConfigValue("appName")+" Password recovery";
    private static final String bodyText = "Please access the following link for email verification: \n";
    private final String emailAddress;
    private final String token;

    PasswordRecoveryEmail(String emailAddress,String token) {
        this.emailAddress = emailAddress;
        this.token = token;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public String getBody() {
        return bodyText + "<a href =\""+  ConfigProvider.getConfigValue("passwordRecoveryBaseUrl")+ token+"\">"
                + ConfigProvider.getConfigValue("passwordRecoveryBaseUrl") + token+"</a>";
    }

    @Override
    public String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public String toString() {
        return "PasswordRecoveryEmail{" +
                "emailAddress='" + emailAddress + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
