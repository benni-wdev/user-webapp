package com.wwt.webapp.userwebapp.service;

import com.wwt.webapp.userwebapp.domain.UserDto;
import com.wwt.webapp.userwebapp.domain.UserEntity;
import com.wwt.webapp.userwebapp.domain.UserStatusChangeToken;
import com.wwt.webapp.userwebapp.domain.UserStatusChangeTokenImpl;
import com.wwt.webapp.userwebapp.domain.request.RegistrationRequest;
import com.wwt.webapp.userwebapp.domain.response.BasicResponse;
import com.wwt.webapp.userwebapp.domain.response.InternalResponse;
import com.wwt.webapp.userwebapp.domain.response.MessageCode;
import com.wwt.webapp.userwebapp.domain.response.RegistrationSuccessResp;
import com.wwt.webapp.userwebapp.mail.EmailType;
import com.wwt.webapp.userwebapp.security.PasswordHash;
import com.wwt.webapp.userwebapp.util.EntityManagerUtil;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author benw-at-wwt
 */
public class RegisterUserServiceImpl extends BaseService implements RegisterUserService {

    private final static Logger logger = Logger.getLogger(RegisterUserServiceImpl.class);


    @Override
    public InternalResponse registerUser(final RegistrationRequest registrationRequest) {
        InternalResponse returnValue;
        em = EntityManagerUtil.getEntityManager();
        em.getTransaction().begin();
        if(!isValidEmailAddress(registrationRequest.getEmailAddress())) {
            logger.debug("registerUser: email address not valid "+registrationRequest.getEmailAddress());
            returnValue =  new BasicResponse(false,MessageCode.EMAIL_ADDRESS_NOT_VALID);
        }
        else if(!isValidLoginId(registrationRequest.getLoginId())) {
            logger.debug("registerUser: email address already used "+registrationRequest.getEmailAddress());
            returnValue =  new BasicResponse(false,MessageCode.LOGIN_ID_NOT_VALID);
        }
        else if(!isEmailUnique(registrationRequest.getEmailAddress(),em)) {
            logger.debug("registerUser: email address already used "+registrationRequest.getEmailAddress());
            returnValue =  new BasicResponse(false,MessageCode.EMAIL_ADDRESS_ALREADY_EXISTS);
        }
        else if(!isLoginIdUnique(registrationRequest.getLoginId(),em)) {
            logger.debug("registerUser: login id already used "+registrationRequest.getLoginId());
            returnValue =  new BasicResponse(false,MessageCode.LOGIN_ID_ALREADY_EXISTS);
        }
        else {
            PasswordHash passwordHash = PasswordHash.newInstance(registrationRequest.getPassword());
            UserStatusChangeToken userStatusChangeToken = UserStatusChangeTokenImpl.newInstance();
            UserEntity user = new UserEntity(registrationRequest.getLoginId(),
                    registrationRequest.getEmailAddress(), passwordHash.getPasswordHash(), userStatusChangeToken);
            em.persist(user);
            if(mailProcessor.isSendMailSuccessful(EmailType.ACTIVATION_MAIL,user.getEmailAddress(),userStatusChangeToken.getToken())) {
                logger.log(Level.OFF,"registerUser: Registered new UserResponse "+user.getUuid());
                returnValue = new RegistrationSuccessResp(user.getEmailAddress());
            }
            else {
                logger.error("registerUser: send email failed "+user.getUuid());
                returnValue = new BasicResponse(false,MessageCode.UNEXPECTED_ERROR);
            }
        }
        handleTransaction(returnValue);
        return returnValue;
    }

    private boolean isLoginIdUnique(String loginId, EntityManager em) {
        List<UserDto> users = em.createQuery("select new com.wwt.webapp.userwebapp.domain.UserDto(u.uuid) from UserEntity u " +
                "where u.loginId = :loginId",UserDto.class)
                .setParameter("loginId",loginId)
                .getResultList();
        return (users.size() <= 0);
    }
}
