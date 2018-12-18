package com.wwt.webapp.userwebapp.mail;

/**
 * @author benw-at-wwt
 */
class EmailFactory {


    static Email produceEmail(EmailType emailType,String emailAddress, String token) {
        Email returnValue;
        switch (emailType) {
            case ACTIVATION_MAIL:
                returnValue = new ActivationEmail(emailAddress,token);
                break;
            case USER_SUSPENDED_MAIL:
                returnValue = new UserSuspendedEmail(emailAddress);
                break;
            case PASSWORD_RECOVERY_MAIL:
                returnValue = new PasswordRecoveryEmail(emailAddress,token);
                break;
            default:
                throw new IllegalArgumentException("emailType not known "+emailType);
        }
        return returnValue;
    }
}
