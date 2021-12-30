package com.wwt.webapp.userwebapp.mail;


import com.wwt.webapp.userwebapp.helper.ConfigProvider;

/**
 * @author benw-at-wwt
 */
public class UserSuspendedEmail extends UserAdministrationEmail {

    private static final String subject = ConfigProvider.getConfigValue("appName")+" - User suspended";
    private static final String preHeader = "Too many failed logins";
    private static final String bodyText1 = "Your user was suspended due to a consecutive number of failed logins.";
    private static final String bodyText2 = "You have to use the password recovery flow to recover the user. Please use the following link.";
    private static final String bodyText3 = "<a href =\""+ ConfigProvider.getConfigValue("passwordRecoveryBaseUrl")+"\">" +ConfigProvider.getConfigValue("passwordRecoveryBaseUrl") + "</a>";

    public UserSuspendedEmail(String emailAddress, String loginId) {
        super(emailAddress,loginId);
        model.put( Email.TITLE,getSubject());
        model.put(Email.PREHEADER,preHeader);
        model.put(Email.BODY1, bodyText1);
        model.put(Email.BODY2, bodyText2);
        model.put(Email.BODY3, bodyText3);
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public String toString() {
        return "UserSuspendedEmail{" +
                "emailAddress='" + getEmailAddress() + '\'' +
                '}';
    }
}
