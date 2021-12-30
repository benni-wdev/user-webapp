package com.wwt.webapp.userwebapp.mail;


import com.wwt.webapp.userwebapp.helper.ConfigProvider;

/**
 * @author benw-at-wwt
 */
class ActivationEmail extends UserAdministrationEmail {

    private static final String subject = ConfigProvider.getConfigValue("appName")+" - Email verification";
    private static final String preHeader = "Verify your email to activate your account";
    private static final String bodyText = "Please access the following link for email verification: ";
    private final String token;

    public ActivationEmail(String emailAddress, String loginId, String token) {
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
        String url = ConfigProvider.getConfigValue("baseUrl")+ConfigProvider.getConfigValue("activationPath");
        return  "<a href =\""+url + token +"\">"+url + token+"</a>";
    }

    @Override
    public String toString() {
        return "ActivationEmail{" +
                "emailAddress='" + getEmailAddress() + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
