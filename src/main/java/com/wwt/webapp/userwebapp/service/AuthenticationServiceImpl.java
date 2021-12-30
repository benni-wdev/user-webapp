/* Copyright 2018-2021 Wehe Web Technologies
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
package com.wwt.webapp.userwebapp.service;

import com.wwt.webapp.userwebapp.domain.ActivationStatus;
import com.wwt.webapp.userwebapp.domain.relational.UserRepository;
import com.wwt.webapp.userwebapp.domain.relational.entity.UserEntity;
import com.wwt.webapp.userwebapp.helper.TimestampHelper;
import com.wwt.webapp.userwebapp.mail.EmailType;
import com.wwt.webapp.userwebapp.mail.MailProcessor;
import com.wwt.webapp.userwebapp.security.IdToken;
import com.wwt.webapp.userwebapp.security.PasswordHash;
import com.wwt.webapp.userwebapp.service.response.AuthenticationSuccessResponse;
import com.wwt.webapp.userwebapp.service.response.BasicResponse;
import com.wwt.webapp.userwebapp.service.response.InternalResponse;
import com.wwt.webapp.userwebapp.service.response.MessageCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

import static com.wwt.webapp.userwebapp.helper.ConfigProvider.getConfigIntValue;
import static com.wwt.webapp.userwebapp.helper.ConfigProvider.getConfigValue;


/**
 * @author benw-at-wwt
 */
@Service
public class AuthenticationServiceImpl extends BaseUserService implements AuthenticationService {

    private final static Logger logger = LoggerFactory.getLogger( AuthenticationServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailProcessor mailProcessor;

    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional
    public InternalResponse authenticate(String loginId, String password, boolean isLongSession) {
        Optional<UserEntity> userOpt = userRepository.getUserByLoginId(loginId);
        if(!userOpt.isPresent()) {
            userOpt = userRepository.getOperationalUsersByEmailAddress(loginId);
            if(!userOpt.isPresent()) {
                logger.warn("authenticate: Not exactly one user found: "+loginId);
                return BasicResponse.LOGIN_OR_PASSWORD_WRONG;
            }
        }
        UserEntity user = userOpt.get();
        if(!user.getActivationStatus().equals( ActivationStatus.ACTIVE)) {
            logger.warn("authenticate: user not active "+user.getUuid());
            return new BasicResponse(false, MessageCode.USER_NOT_ACTIVE);
        }
        if(user.getFailedLogins() > Integer.parseInt( getConfigValue("maxFailedLoginsUntilSuspension"))) {
            user.setActivationStatus(ActivationStatus.SUSPENDED);
            mailProcessor.sendEmail( EmailType.USER_SUSPENDED_MAIL,user.getEmailAddress(),user.getLoginId(),null);
            logger.warn("authenticate: too many failed logins "+user.getUuid());
            return new BasicResponse(false, MessageCode.TOO_MANY_FAILED_LOGINS);
        }
        if(PasswordHash.getInstance(user.getPasswordHash()).isPasswordHashEquals(password)) {
            logger.error("authenticate: user authenticated " + user.getUuid());
            user.setLastLoggedInAt( TimestampHelper.getNowAsUtcTimestamp());
            IdToken idToken = IdToken.newInstance(getConfigValue("appName"),
                    user.getUuid(),
                    isLongSession? getConfigIntValue("ttlLong"):getConfigIntValue("ttl"),
                    user.getAdminRole().toString());
            if(isLongSession) {
                RefreshToken r = RefreshToken.newInstance();
                user.setRefreshToken(r.toString());
                return new AuthenticationSuccessResponse(idToken.convertToSignedJwt(),r.toString(), user.getLoginId(),user.getAdminRole().toString());
            }
            else {
                return new AuthenticationSuccessResponse(idToken.convertToSignedJwt(), user.getLoginId(),user.getAdminRole().toString());
            }
        }
        else {
            user.setFailedLogins((user.getFailedLogins())+1);
            logger.warn("authenticate: user password check failed "+user.getUuid());
            return BasicResponse.LOGIN_OR_PASSWORD_WRONG;
        }
    }

    @Override
    @Transactional
    public InternalResponse refreshAuthentication(String refreshToken) {
        if( refreshToken == null || refreshToken.equals("")) {
            logger.warn("refreshAuthentication: not valid refresh token: "+refreshToken);
            return BasicResponse.LOGIN_OR_PASSWORD_WRONG;
        }
        Optional<UserEntity> userOpt = userRepository.getUserByRefreshToken(refreshToken);
        if(!userOpt.isPresent()) {
            logger.warn("refreshAuthentication: Not exactly one user found: "+refreshToken);
            return BasicResponse.LOGIN_OR_PASSWORD_WRONG;
        }
        UserEntity user = userOpt.get();
        if(!user.getActivationStatus().equals(ActivationStatus.ACTIVE)) {
            logger.warn("refreshAuthentication: user not active "+user.getUuid());
            return new BasicResponse(false, MessageCode.USER_NOT_ACTIVE);
        }
        logger.error("refreshAuthentication: user authenticated " + user.getUuid());
        user.setLastLoggedInAt(TimestampHelper.getNowAsUtcTimestamp());
        IdToken idToken = IdToken.newInstance(getConfigValue("appName"),
                user.getUuid(),
                getConfigIntValue("ttlLong"),
                user.getAdminRole().toString());
        RefreshToken r = RefreshToken.newInstance();
        user.setRefreshToken(r.toString());
        return new AuthenticationSuccessResponse(idToken.convertToSignedJwt(),r.toString(),user.getLoginId(),user.getAdminRole().toString());
    }

    @Override
    @Transactional
    public InternalResponse logout(String userUuid) {
        Optional<UserEntity> userOpt = userRepository.findById(userUuid);
        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            user.setRefreshToken(RefreshToken.getNullInstance().toString());
            logger.info("logout: user logged out");
            return BasicResponse.SUCCESS;
        }
        else {
            logger.warn("readUser: Not exactly one user found: " + userUuid);
            return BasicResponse.LOGIN_OR_PASSWORD_WRONG;
        }
    }
}
