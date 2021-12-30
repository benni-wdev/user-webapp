/*
 *  Copyright 2020  Wehe Web Technologies - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Benjamin Wehe (contact@wehe-web-technologies.ch)
 *
 */

package com.wwt.webapp.userwebapp.mail;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.wwt.webapp.userwebapp.helper.ConfigProvider.getConfigBoolean;
import static com.wwt.webapp.userwebapp.helper.ConfigProvider.getConfigValue;


@Service
public class SharedMailSenderImpl implements SharedMailSender {

    private static final Logger logger = LoggerFactory.getLogger(SharedMailSenderImpl.class);


    @Autowired(required = false)
    private JavaMailSender javaMailSender;


    @Autowired
    private Configuration freemarkerConfig;

    @Override
    @Async
    public void sendMail(Email email) {
        String fromEmail = getConfigValue("fromEmail");

        if (getConfigBoolean("isMailingEnabled") ) {
            String html = "";
            try {
                freemarkerConfig.setClassForTemplateLoading( this.getClass(), "/templates" );
                Template t = freemarkerConfig.getTemplate( "basic.ftl" );
                html = FreeMarkerTemplateUtils.processTemplateIntoString( t, email.getModel() );
            } catch ( IOException | TemplateException e) {
                logger.error( "Error creating email from template ", e );
                return;
            }
            try {
                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper( message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name() );
                helper.setTo( email.getEmailAddress() );
                helper.setFrom( fromEmail );
                helper.setSubject( email.getSubject() );
                helper.setText( html, true );
                javaMailSender.send( message );
            } catch (MessagingException  e) {
                logger.error( "Error sending old mail ", e );
            }

        } else {
            logger.debug(" Email sending disabled");
        }
    }
}
