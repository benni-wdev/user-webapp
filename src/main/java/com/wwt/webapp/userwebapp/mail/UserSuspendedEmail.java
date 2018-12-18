package com.wwt.webapp.userwebapp.mail;

import com.wwt.webapp.userwebapp.util.ConfigProvider;

/**
 * @author benw-at-wwt
 */
class UserSuspendedEmail implements Email {

    private static final String subject = ConfigProvider.getConfigValue("appName")+" your user is suspended";
    private static final String bodyText = "Your user was suspended due to a consecutive number of failed logins.\n" +
            "You have to use the password recovery flow to recover the user. Please go to " +
            "<a href =\""+ ConfigProvider.getConfigValue("passwordRecoveryBaseUrl")+"\">"
            +ConfigProvider.getConfigValue("passwordRecoveryBaseUrl") + "</a>";
    private final String emailAddress;

    UserSuspendedEmail(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public String getBody() {
        return bodyText;
    }

    @Override
    public String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public String toString() {
        return "UserSuspendedEmail{" +
                "emailAddress='" + emailAddress + '\'' +
                '}';
    }
}
