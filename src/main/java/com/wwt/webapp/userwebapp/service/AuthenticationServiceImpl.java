package com.wwt.webapp.userwebapp.service;

import com.wwt.webapp.userwebapp.domain.ActivationStatus;
import com.wwt.webapp.userwebapp.domain.UserEntity;
import com.wwt.webapp.userwebapp.domain.request.AuthenticatedRequest;
import com.wwt.webapp.userwebapp.domain.response.AuthenticationSuccessResponse;
import com.wwt.webapp.userwebapp.domain.response.BasicResponse;
import com.wwt.webapp.userwebapp.domain.response.InternalResponse;
import com.wwt.webapp.userwebapp.domain.response.MessageCode;
import com.wwt.webapp.userwebapp.mail.EmailType;
import com.wwt.webapp.userwebapp.security.IdToken;
import com.wwt.webapp.userwebapp.security.PasswordHash;
import com.wwt.webapp.userwebapp.util.ConfigProvider;
import com.wwt.webapp.userwebapp.util.EntityManagerUtil;
import com.wwt.webapp.userwebapp.util.StaticHelper;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import static com.wwt.webapp.userwebapp.domain.response.MessageCode.USER_NOT_ACTIVE;

import java.util.List;


/**
 * @author benw-at-wwt
 */
public class AuthenticationServiceImpl extends AuthenticatedService implements AuthenticationService {

    private final static Logger logger = Logger.getLogger(AuthenticationServiceImpl.class);

    @Override
    public InternalResponse authenticate(String loginId, String password,boolean isLongSession) {
        InternalResponse returnValue;
        boolean commitNotSuccessfulResponse = false;
        em = EntityManagerUtil.getEntityManager();
        em.getTransaction().begin();
        List<UserEntity> userEntities = getUserEntityWithLoginId(loginId);
        if(userEntities.size() == 1) {
            UserEntity user = userEntities.get(0);
            if(user.getActivationStatus().equals(ActivationStatus.ACTIVE)) {
                if(user.getFailedLogins() <= Integer.parseInt( ConfigProvider.getConfigValue("maxFailedLoginsUntilSuspension"))) {
                    if(PasswordHash.getInstance(user.getPasswordHash()).isPasswordHashEquals(password)) {
                        logger.log(Level.OFF,"authenticate: user authenticated " + user.getUuid());
                        user.setLastLoggedInAt( StaticHelper.getNowAsUtcTimestamp());
                        IdToken idToken = IdToken.newInstance( ConfigProvider.getConfigValue("appName"),
                                                              user.getUuid(),
                                isLongSession? ConfigProvider.getConfigIntValue("ttlLong"): ConfigProvider.getConfigIntValue("ttl"));

                        returnValue = new AuthenticationSuccessResponse(idToken.convertToSignedJwt(), user.getLoginId());

                    }
                    else {
                        user.setFailedLogins((user.getFailedLogins())+1);
                        commitNotSuccessfulResponse = true;
                        returnValue = createLoginOrPasswordWrong(logger,"authenticate: user password check failed "+user.getUuid());
                    }
                }
                else {
                    user.setActivationStatus(ActivationStatus.SUSPENDED);
                    commitNotSuccessfulResponse = true;
                    if(mailProcessor.isSendMailSuccessful(EmailType.USER_SUSPENDED_MAIL,user.getEmailAddress(),null)) {
                        logger.warn("authenticate: too many failed logins "+user.getUuid());
                        returnValue = new BasicResponse(false,MessageCode.TOO_MANY_FAILED_LOGINS);
                    }
                    else {
                        // We need a little bit more escalation here, because user is suspended without notification
                        logger.error("authenticate: send email failed "+user.getUuid());
                        returnValue = new BasicResponse(false,MessageCode.UNEXPECTED_ERROR);
                    }
                }
            }
            else {
                logger.warn("authenticate: user not active "+user.getUuid());
                returnValue = new BasicResponse(false, USER_NOT_ACTIVE);
            }
        }
        else {
            returnValue = createLoginOrPasswordWrong(logger,"authenticate: Not exactly one user found: "+loginId);
        }
        handleTransaction(returnValue,commitNotSuccessfulResponse);
        return returnValue;
    }


    private List<UserEntity> getUserEntityWithLoginId(String loginId) {
        return em.createQuery("select u from UserEntity u where u.loginId = :loginId",UserEntity.class)
                .setParameter("loginId",loginId)
                .getResultList();
    }

}
