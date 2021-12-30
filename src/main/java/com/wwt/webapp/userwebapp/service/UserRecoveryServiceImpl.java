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
import com.wwt.webapp.userwebapp.security.PasswordHash;
import com.wwt.webapp.userwebapp.service.response.BasicResponse;
import com.wwt.webapp.userwebapp.service.response.InternalResponse;
import com.wwt.webapp.userwebapp.service.response.MessageCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author benw-at-wwt
 */
@Service
public class UserRecoveryServiceImpl extends BaseUserService implements UserRecoveryService {

    private final static Logger logger = LoggerFactory.getLogger( UserRecoveryServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailProcessor mailProcessor;

    @Override
    @Transactional
    public InternalResponse initiateRecovery(String email) {
        Optional<UserEntity> userOpt = userRepository.getOperationalUsersByEmailAddress(email);
        if(!userOpt.isPresent()) {
            logger.error("initiateRecovery: Not exactly one user found: "+email);
            // We have to give success to prevent data mining
            return BasicResponse.SUCCESS;
        }
        UserEntity user = userOpt.get();
        user.setActivationStatus( ActivationStatus.SUSPENDED);
        UserStatusChangeToken userStatusChangeToken = UserStatusChangeTokenImpl.newInstance();
        user.setPasswordRecoveryToken(userStatusChangeToken.getToken(),userStatusChangeToken.getTokenExpiresAt());
        logger.info( "password recovery token :"+userStatusChangeToken.getToken() );
        mailProcessor.sendEmail( EmailType.PASSWORD_RECOVERY_MAIL,user.getEmailAddress(),user.getLoginId(),userStatusChangeToken.getToken());
        logger.error("initiateRecovery: user locked, mail triggered" + user.getUuid());
        userRepository.save(user);
        return BasicResponse.SUCCESS;
    }

    @Override
    @Transactional
    public InternalResponse recoverUser(String passwordToken, String newPassword) {
        Optional<UserEntity> userOpt = userRepository.getUserByPasswordRecoveryToken(passwordToken);
        if(!userOpt.isPresent()) {
            logger.error("executeRecovery: Not exactly one user found: "+ passwordToken);
            return new BasicResponse(false, MessageCode.PASSWORD_TOKEN_NOT_KNOWN);
        }
        UserEntity user = userOpt.get();
        if(!user.getActivationStatus().equals(ActivationStatus.SUSPENDED)) {
            logger.warn("executeRecovery: user not in an activation state "+user.getPasswordRecoveryToken());
            return new BasicResponse(false,MessageCode.RECOVERY_ALREADY_DONE_OR_NOT_POSSIBLE);
        }
        if(user.getPasswordRecoveryTokenExpiresAt().after( TimestampHelper.getNowAsUtcTimestamp())) {
            user.setActivationStatus(ActivationStatus.ACTIVE);
            PasswordHash passwordHash = PasswordHash.newInstance(newPassword);
            user.setPasswordHash(passwordHash.getPasswordHash());
            logger.error("executeRecovery: user recovered "+user.getUuid());
            userRepository.save(user);
            return BasicResponse.SUCCESS;
        }
        else {
            logger.warn("executeRecovery: token expired "+user.getPasswordRecoveryToken());
            return new BasicResponse(false,MessageCode.PASSWORD_RECOVERY_TOKEN_EXPIRED);
        }
    }
}
