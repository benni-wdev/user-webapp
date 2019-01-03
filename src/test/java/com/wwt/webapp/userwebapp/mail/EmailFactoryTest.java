/* Copyright 2018-2019 Wehe Web Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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