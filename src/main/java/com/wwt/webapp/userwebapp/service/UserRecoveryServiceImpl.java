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
package com.wwt.webapp.userwebapp.service;

import com.wwt.webapp.userwebapp.domain.ActivationStatus;
import com.wwt.webapp.userwebapp.domain.UserEntity;
import com.wwt.webapp.userwebapp.domain.UserStatusChangeToken;
import com.wwt.webapp.userwebapp.domain.UserStatusChangeTokenImpl;
import com.wwt.webapp.userwebapp.domain.request.ExecuteRecoveryRequest;
import com.wwt.webapp.userwebapp.domain.response.BasicResponse;
import com.wwt.webapp.userwebapp.domain.response.InternalResponse;
import com.wwt.webapp.userwebapp.domain.response.MessageCode;
import com.wwt.webapp.userwebapp.mail.EmailType;
import com.wwt.webapp.userwebapp.security.PasswordHash;
import com.wwt.webapp.userwebapp.util.EntityManagerUtil;
import com.wwt.webapp.userwebapp.util.StaticHelper;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * @author benw-at-wwt
 */
public class UserRecoveryServiceImpl extends BaseService implements UserRecoveryService {

    private static final Logger logger = Logger.getLogger(UserRecoveryServiceImpl.class);

    @Override
    public InternalResponse initiateRecovery(String email) {
        InternalResponse returnValue;
        em = EntityManagerUtil.getEntityManager();
        em.getTransaction().begin();
        List<UserEntity> userEntities = em.createQuery("select u from UserEntity u where u.emailAddress = :emailAddress",UserEntity.class)
                .setParameter("emailAddress",email)
                .getResultList();
        if(userEntities.size() == 1) {
            UserEntity user = userEntities.get(0);
            user.setActivationStatus(ActivationStatus.SUSPENDED);
            UserStatusChangeToken userStatusChangeToken = UserStatusChangeTokenImpl.newInstance();
            user.setPasswordRecoveryToken(userStatusChangeToken);
            if(mailProcessor.isSendMailSuccessful(EmailType.PASSWORD_RECOVERY_MAIL,user.getEmailAddress(),userStatusChangeToken.getToken())) {
                logger.log(Level.OFF, "initiateRecovery: user locked, mail triggered" + user.getUuid());
                returnValue = createOperationSuccessfulResponse();
            }
            else {
                logger.error("initiateRecovery: send email failed "+user.getUuid());
                returnValue = new BasicResponse(false,MessageCode.UNEXPECTED_ERROR);
            }
        }
        else {
            logger.error("initiateRecovery: Not exactly one user found: "+email);
            // We have to give success to prevent data mining
            returnValue = createOperationSuccessfulResponse();
        }
        handleTransaction(returnValue);
        return returnValue;
    }

    @Override
    public InternalResponse recoverUser(ExecuteRecoveryRequest executeRecoveryRequest) {
        InternalResponse returnValue;
        em = EntityManagerUtil.getEntityManager();
        em.getTransaction().begin();
        List<UserEntity> userEntities = em.createQuery("select u from UserEntity u where u.passwordRecoveryToken = :passwordRecoveryToken",UserEntity.class)
                .setParameter("passwordRecoveryToken", executeRecoveryRequest.getPasswordToken())
                .getResultList();
        if(userEntities.size() == 1) {
            UserEntity user = userEntities.get(0);
            if(user.getActivationStatus().equals(ActivationStatus.SUSPENDED)) {
                if(user.getPasswordRecoveryTokenExpiresAt().after( StaticHelper.getNowAsUtcTimestamp())) {
                    user.setActivationStatus(ActivationStatus.ACTIVE);
                    PasswordHash passwordHash = PasswordHash.newInstance(executeRecoveryRequest.getNewPassword());
                    user.setPasswordHash(passwordHash.getPasswordHash());
                    logger.log(Level.OFF,"executeRecovery: user recovered "+user.getUuid());
                    returnValue = createOperationSuccessfulResponse();
                }
                else {
                    logger.warn("executeRecovery: token expired "+user.getPasswordRecoveryToken());
                    returnValue = new BasicResponse(false,MessageCode.PASSWORD_RECOVERY_TOKEN_EXPIRED);
                }
            }
            else {
                logger.warn("executeRecovery: user not in an activation state "+user.getPasswordRecoveryToken());
                returnValue = new BasicResponse(false,MessageCode.RECOVERY_ALREADY_DONE_OR_NOT_POSSIBLE);
            }
        }
        else {
            logger.error("executeRecovery: Not exactly one user found: "+ executeRecoveryRequest.getPasswordToken());
            returnValue = new BasicResponse(false,MessageCode.PASSWORD_TOKEN_NOT_KNOWN);
        }
        handleTransaction(returnValue);
        return returnValue;
    }
}
