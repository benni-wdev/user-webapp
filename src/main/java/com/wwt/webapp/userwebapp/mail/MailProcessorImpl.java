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

import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.mail.MessagingException;

/**
 * @author benw-at-wwt
 */
@Stateless
public class MailProcessorImpl implements MailProcessor {

    private static final Logger logger = Logger.getLogger(MailProcessorImpl.class);

    private final EmailSender emailSender = new EmailSender();

    @Override
    public boolean isSendMailSuccessful(EmailType emailType, String emailAddress, String token) {
        Email email = EmailFactory.produceEmail(emailType,emailAddress,token);
        try {
            emailSender.sendMail(email);
            return true;
        }
        catch (MessagingException e) {
            logger.error("Mail could not be sent ",e);
            return false;
        }
    }
}
