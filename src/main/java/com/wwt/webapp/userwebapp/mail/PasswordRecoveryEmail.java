package com.wwt.webapp.userwebapp.mail;


import com.wwt.webapp.userwebapp.helper.ConfigProvider;

/**
 * @author benw-at-wwt
 */
public class PasswordRecoveryEmail extends UserAdministrationEmail {

    private static final String subject = ConfigProvider.getConfigValue("appName")+" - Password recovery";
    private static final String preHeader = "Follow the link for password reset";
    private static final String bodyText = "Please access the following link to create a new password: ";
    private final String token;

    public PasswordRecoveryEmail(String emailAddress, String loginId,String token) {
        super(emailAddress,loginId);
        this.token = token;
        model.put( Email.TITLE,getSubject());
        model.put(Email.PREHEADER,preHeader);
        model.put(Email.BODY1, bodyText);
        model.put(Email.BODY2, getLink());
    }

    @Override
    public String getSubject() {
        return subject;
    }

    private String getLink() {
        String url = ConfigProvider.getConfigValue("baseUrl")+ConfigProvider.getConfigValue("passwordRecoveryPath");
        return "<a href =\""+  url+ token+"\">"+ url+ token+"</a>";
    }


    @Override
    public String toString() {
        return "PasswordRecoveryEmail{" +
                "emailAddress='" + getEmailAddress() + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
