package com.wwt.webapp.userwebapp.service;

import com.wwt.webapp.userwebapp.domain.*;
import com.wwt.webapp.userwebapp.domain.request.ArchiveRequest;
import com.wwt.webapp.userwebapp.domain.request.AuthenticatedRequest;
import com.wwt.webapp.userwebapp.domain.request.EmailChangeRequest;
import com.wwt.webapp.userwebapp.domain.request.PasswordChangeRequest;
import com.wwt.webapp.userwebapp.domain.response.BasicResponse;
import com.wwt.webapp.userwebapp.domain.response.InternalResponse;
import com.wwt.webapp.userwebapp.domain.response.MessageCode;
import com.wwt.webapp.userwebapp.mail.EmailType;
import com.wwt.webapp.userwebapp.security.PasswordHash;
import com.wwt.webapp.userwebapp.util.EntityManagerUtil;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import java.util.List;


/**
 * @author benw-at-wwt
 */
@SuppressWarnings("JavaDoc")
public class UserServiceImpl extends AuthenticatedService implements UserService {


    private static final Logger logger = Logger.getLogger(UserServiceImpl.class);


    @Override
    public InternalResponse readUser(AuthenticatedRequest request) {
        InternalResponse returnValue = isAuthenticated(request);
        if(returnValue.isSuccessful()) {
            String authenticatedUuid = ((AuthenticatedResponse) returnValue).getAuthenticatedUuid();
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            List<UserDto> userDtos = em.createQuery("select new com.wwt.webapp.userwebapp.domain.UserDto( u.uuid,u.createdAt,u.version," +
                    "u.loginId,u.emailAddress,u.emailChangedAt,u.passwordChangedAt,u.activationStatus,u.activationStatusChangedAt,"+
                    "u.lastLoggedInAt) from UserEntity u where u.uuid = :uuid",UserDto.class)
                    .setParameter("uuid",authenticatedUuid)
                    .getResultList();
            if (userDtos.size() == 1) {
                logger.info("readUser: user returned");
                returnValue = userDtos.get(0).convert();
            }
            else {
                returnValue = createLoginOrPasswordWrong(logger, "readUser: Not exactly one user found: " + request.getIdToken());
            }
            handleTransaction(returnValue);

        }
        return returnValue;
    }

    @Override
    public InternalResponse changePassword(PasswordChangeRequest passwordChangeReq) {
        InternalResponse returnValue = isAuthenticated(passwordChangeReq);
        if(returnValue.isSuccessful()) {
            String authenticatedUuid = ((AuthenticatedResponse) returnValue).getAuthenticatedUuid();
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            List<UserEntity> userEntities = getUserEntityWithUuid(authenticatedUuid);
            if (userEntities.size() == 1) {
                UserEntity user = userEntities.get(0);
                if (PasswordHash.getInstance(user.getPasswordHash()).isPasswordHashEquals(passwordChangeReq.getOldPassword())) {
                    user.setPasswordHash(PasswordHash.newInstance(passwordChangeReq.getNewPassword()).getPasswordHash());
                    logger.log(Level.OFF, "changePassword: Password changed" + user.getUuid());
                    returnValue = createOperationSuccessfulResponse();
                }
                else {
                    logger.info("changePassword: current password wrong");
                    returnValue = new BasicResponse(false, MessageCode.LOGIN_OR_PASSWORD_WRONG);
                }
            }
            else {
                returnValue = createLoginOrPasswordWrong(logger, "changePassword: Not exactly one user found: " + passwordChangeReq.getIdToken());
            }
            handleTransaction(returnValue);
        }
        return returnValue;
    }

    @Override
    public InternalResponse changeEmail(EmailChangeRequest emailChangeReq) {
        InternalResponse returnValue = isAuthenticated(emailChangeReq);
        if(returnValue.isSuccessful()) {
            if (isValidEmailAddress(emailChangeReq.getEmail())) {
                String authenticatedUuid = ((AuthenticatedResponse) returnValue).getAuthenticatedUuid();
                em = EntityManagerUtil.getEntityManager();
                em.getTransaction().begin();
                List<UserEntity> userEntities = getUserEntityWithUuid(authenticatedUuid);
                if (userEntities.size() == 1) {
                    UserEntity user = userEntities.get(0);
                    if (!user.getEmailAddress().equals(emailChangeReq.getEmail())) {
                        if (isEmailUnique(emailChangeReq.getEmail(), em)) {
                            user.setEmailAddress(emailChangeReq.getEmail());
                            user.setActivationStatus( ActivationStatus.ESTABLISHED);
                            //for changing the email re-activation in necessary
                            UserStatusChangeToken userStatusChangeToken = UserStatusChangeTokenImpl.newInstance();
                            user.setActivationToken(userStatusChangeToken);
                            if (mailProcessor.isSendMailSuccessful(EmailType.ACTIVATION_MAIL, user.getEmailAddress(), userStatusChangeToken.getToken())) {
                                logger.log(Level.OFF, "changeEmail: email changed" + user.getUuid());
                                returnValue = createOperationSuccessfulResponse();
                            }
                            else {
                                logger.error("changeEmail: send email failed " + user.getUuid());
                                returnValue = new BasicResponse(false, MessageCode.UNEXPECTED_ERROR);
                            }

                        }
                        else {
                            logger.warn("changeEmail: email address already used " + emailChangeReq.getEmail());
                            returnValue = new BasicResponse(false, MessageCode.EMAIL_ADDRESS_ALREADY_EXISTS);
                        }
                    }
                    else {
                        logger.debug("changeEmail: email address not changed " + emailChangeReq.getEmail());
                        returnValue = new BasicResponse(false, MessageCode.NO_CHANGE_NO_UPDATE);
                    }
                } else {
                    returnValue = createLoginOrPasswordWrong(logger, "changeEmail: Not exactly one user found: " + emailChangeReq.getIdToken());
                }
                handleTransaction(returnValue);
            }
            else {
                logger.info("changeEmail: email not valid " + emailChangeReq.getEmail());
                returnValue = new BasicResponse(false, MessageCode.EMAIL_ADDRESS_NOT_VALID);
            }
        }
        return returnValue;
    }


    @Override
    public InternalResponse archiveUser(ArchiveRequest archiveRequestRequest) {
        InternalResponse returnValue = isAuthenticated(archiveRequestRequest);
        if(returnValue.isSuccessful()) {
            String authenticatedUuid = ((AuthenticatedResponse) returnValue).getAuthenticatedUuid();
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            List<UserEntity> userEntities = getUserEntityWithUuid(authenticatedUuid);
            if (userEntities.size() == 1) {
                UserEntity user = userEntities.get(0);
                if (user.getActivationStatus().equals(ActivationStatus.ACTIVE)) {
                    if (PasswordHash.getInstance(user.getPasswordHash()).isPasswordHashEquals(archiveRequestRequest.getPassword())) {
                        logger.log(Level.OFF, "archiveUser: user archived " + user.getUuid());
                        user.setActivationStatus(ActivationStatus.ARCHIVED);
                        returnValue = createOperationSuccessfulResponse();
                    }
                    else {
                        returnValue = createLoginOrPasswordWrong(logger, "archiveUser: user password check failed  " + user.getUuid());
                    }
                }
                else {
                    logger.warn("archiveUser: user not active " + user.getUuid());
                    returnValue = new BasicResponse(false, MessageCode.USER_NOT_ACTIVE);
                }
            }
            else {
                returnValue = createLoginOrPasswordWrong(logger, "archiveUser: Not exactly one user found: " + archiveRequestRequest.getIdToken());
            }
            handleTransaction(returnValue);
        }
        return returnValue;
    }


}
