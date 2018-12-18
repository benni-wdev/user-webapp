package com.wwt.webapp.userwebapp.service;

import com.wwt.webapp.userwebapp.domain.UserEntity;
import com.wwt.webapp.userwebapp.domain.request.AuthenticatedRequest;
import com.wwt.webapp.userwebapp.domain.response.BasicResponse;
import com.wwt.webapp.userwebapp.domain.response.InternalResponse;
import com.wwt.webapp.userwebapp.domain.response.MessageCode;
import com.wwt.webapp.userwebapp.security.IdToken;
import com.wwt.webapp.userwebapp.util.StaticHelper;
import com.wwt.webapp.userwebapp.util.ConfigProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * @author benw-at-wwt
 */
abstract class AuthenticatedService extends BaseService {

    private final static Logger logger = Logger.getLogger(AuthenticatedService.class);

    List<UserEntity> getUserEntityWithUuid(String uuid) {
        return em.createQuery("select u from UserEntity u where u.uuid = :uuid",UserEntity.class)
                .setParameter("uuid",uuid)
                .getResultList();
    }


    InternalResponse isAuthenticated(AuthenticatedRequest r) {
        InternalResponse returnValue;
        if(r.getIdToken() == null) {
            logger.warn("token not present");
            returnValue = new BasicResponse(false, MessageCode.SESSION_EXPIRED);
        }
        else {
            try {
                IdToken tokenInfo = IdToken.parse(r.getIdToken());
                logger.info(tokenInfo);
                if (tokenInfo.getIssuer().equals( ConfigProvider.getConfigValue("appName"))) {
                    if (tokenInfo.getExpiresAt().isAfter( StaticHelper.getNowAsUtcInstant())) {
                        logger.info("Token valid");
                        returnValue = new UserServiceImpl.AuthenticatedResponse(tokenInfo.getSubject());
                    } else {
                        logger.info("Token expired");
                        returnValue = new BasicResponse(false, MessageCode.SESSION_EXPIRED);
                    }
                } else {
                    logger.error("Token invalid!!!");
                    returnValue = new BasicResponse(false, MessageCode.SESSION_INVALID);
                }
            } catch (ExpiredJwtException e1) {
                logger.info("Token expired "+r.getIdToken());
                returnValue = new BasicResponse(false, MessageCode.SESSION_EXPIRED);
            } catch (SignatureException | MalformedJwtException | IllegalArgumentException e2) {
                logger.error("Token invalid!!! "+r.getIdToken());
                returnValue = new BasicResponse(false, MessageCode.SESSION_INVALID);
            }
        }
        return returnValue;
    }

    class AuthenticatedResponse extends SuccessResponse implements InternalResponse {

        private final String authenticatedUuid;

        private AuthenticatedResponse(String authenticatedUuid) {
            this.authenticatedUuid = authenticatedUuid;
        }

        String getAuthenticatedUuid() {
            return authenticatedUuid;
        }
    }

    class SuccessResponse implements InternalResponse {

        @Override
        public boolean isSuccessful() { return true; }

        @Override
        public MessageCode getMessageCode() { return MessageCode.OPERATION_SUCCESSFUL; }

        @Override
        public String getMessageText() { return MessageCode.OPERATION_SUCCESSFUL.getMessage();}


    }
}
