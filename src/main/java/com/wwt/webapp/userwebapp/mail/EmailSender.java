package com.wwt.webapp.userwebapp.mail;

import com.wwt.webapp.userwebapp.util.ConfigProvider;
import org.apache.log4j.Logger;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @author benw-at-wwt
 */
@SuppressWarnings("SpellCheckingInspection")
@Stateless
class EmailSender {

    private static final Logger logger = Logger.getLogger(EmailSender.class);

    @Asynchronous
	public void sendMail(Email email ) throws MessagingException {
		String fromEmail 	= ConfigProvider.getConfigValue("fromEmail");
		String smtpUserName = ConfigProvider.getConfigValue("smtpUserName");
		String smtpPassword = ConfigProvider.getConfigValue("smtpPassword");
		String host         = ConfigProvider.getConfigValue("smtpHost");
		String port   		= ConfigProvider.getConfigValue("smtpPort");

        Properties properties = System.getProperties();
        properties.put("mail.response.protocol", "smtp");
        properties.put("mail.smtp.smtpPort", port);
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(properties);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(email.getEmailAddress()));
        message.setSubject(email.getSubject());
        message.setContent(email.getBody(),"text/html");
        Transport transport = session.getTransport();
        if (Boolean.valueOf( ConfigProvider.getConfigValue("isMailingEnabled"))) {
            logger.debug(" Email sending activated to " + email.getEmailAddress());
            transport.connect(host, Integer.parseInt(port), smtpUserName, smtpPassword);
            transport.sendMessage(message, message.getAllRecipients());
        }
        else {
            logger.debug(" Email sending disabled");
        }
	}
}
