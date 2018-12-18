package com.wwt.webapp.userwebapp.service;

import com.wwt.webapp.userwebapp.domain.UserDto;
import com.wwt.webapp.userwebapp.domain.response.BasicResponse;
import com.wwt.webapp.userwebapp.domain.response.InternalResponse;
import com.wwt.webapp.userwebapp.domain.response.MessageCode;
import com.wwt.webapp.userwebapp.mail.MailProcessor;
import com.wwt.webapp.userwebapp.mail.MailProcessorImpl;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author benw-at-wwt
 */
abstract class BaseService {

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    EntityManager em;

    MailProcessor mailProcessor = new MailProcessorImpl();

    void handleTransaction(InternalResponse r) {
        if(r.isSuccessful()) {
            em.getTransaction().commit();
        }
        else {
            em.getTransaction().rollback();
        }
        em.close();
        em = null;
    }

    void handleTransaction(InternalResponse r,boolean commitOverride) {
        if(commitOverride) {
            em.getTransaction().commit();
            em.close();
        }
        else {
            handleTransaction(r);
        }
    }

    InternalResponse createOperationSuccessfulResponse() {
        return new BasicResponse(true,MessageCode.OPERATION_SUCCESSFUL);
    }

    InternalResponse createLoginOrPasswordWrong(Logger logger, String log) {
        logger.warn(log);
        return new BasicResponse(false,MessageCode.LOGIN_OR_PASSWORD_WRONG);
    }


    boolean isEmailUnique(String emailAddress, EntityManager em) {
        List<UserDto> users = em.createQuery("select new com.wwt.webapp.userwebapp.domain.UserDto( u.uuid ) from UserEntity u " +
                "where u.emailAddress = :emailAddress and u.activationStatus <> 'ARCHIVED' ",UserDto.class)
                .setParameter("emailAddress",emailAddress)
                .getResultList();
        return (users.size() <= 0);
    }

    boolean isValidLoginId(String loginId) {
        return loginId.matches("[a-zA-Z0-9_]+");
    }

    boolean isValidEmailAddress(String emailAddress) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailAddress);
        return matcher.find();
    }


}
