package com.wwt.webapp.userwebapp.mail;

import org.junit.Test;
import static org.junit.Assert.*;

@SuppressWarnings("SpellCheckingInspection")
public class EmailFactoryTest {

    @Test
    public void produceActivationEmail() {
        String emailAddress = "test@test.test";
        String token = "abcde";
        Email email = EmailFactory.produceEmail(EmailType.ACTIVATION_MAIL,emailAddress,token);
        assertTrue(email instanceof ActivationEmail);
        assertEquals(emailAddress,email.getEmailAddress());
        assertTrue(email.getBody().contains(token));
    }

    @Test
    public void producePasswordRecoveryEmail() {
        String emailAddress = "test@test.test";
        String token = "abcde";
        Email email = EmailFactory.produceEmail(EmailType.PASSWORD_RECOVERY_MAIL,emailAddress,token);
        assertTrue(email instanceof PasswordRecoveryEmail);
        assertEquals(emailAddress,email.getEmailAddress());
        assertTrue(email.getBody().contains(token));
    }

    @Test
    public void produceUserSuspendedEmail() {
        String emailAddress = "test@test.test";
        String token = "abcde";
        Email email = EmailFactory.produceEmail(EmailType.USER_SUSPENDED_MAIL,emailAddress,token);
        assertTrue(email instanceof UserSuspendedEmail);
        assertEquals(emailAddress,email.getEmailAddress());
        assertFalse(email.getBody().contains(token));
    }
}