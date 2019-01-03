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
import com.wwt.webapp.userwebapp.domain.response.BasicResponse;
import com.wwt.webapp.userwebapp.domain.response.InternalResponse;
import com.wwt.webapp.userwebapp.domain.response.MessageCode;
import com.wwt.webapp.userwebapp.util.EntityManagerUtil;
import com.wwt.webapp.userwebapp.util.StaticHelper;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.List;


/**
 * @author benw-at-wwt
 */
public class ActivationServiceImpl extends BaseService implements ActivationService {

    private static final Logger logger = Logger.getLogger(ActivationServiceImpl.class);

    @Override
    public InternalResponse activateUser(final String activationToken) {
        InternalResponse returnValue;
        em = EntityManagerUtil.getEntityManager();
        em.getTransaction().begin();
        List<UserEntity> userEntities = em.createQuery("select u from UserEntity u where u.activationToken = :activationToken",UserEntity.class)
                .setParameter("activationToken",activationToken)
                .getResultList();
        if(userEntities.size() == 1) {
            UserEntity user = userEntities.get(0);
            if(user.getActivationStatus().equals(ActivationStatus.ESTABLISHED)) {
                if(user.getActivationTokenExpiresAt().after( StaticHelper.getNowAsUtcTimestamp())) {
                    user.setActivationStatus(ActivationStatus.ACTIVE);
                    logger.log(Level.OFF,"activateUser: user activated "+user.getUuid());
                    returnValue = createOperationSuccessfulResponse();
                }
                else {
                    logger.warn("activateUser: token expired "+user.getActivationToken());
                    returnValue = new BasicResponse(false,MessageCode.ACTIVATION_TOKEN_EXPIRED);
                }
            }
            else {
                logger.warn("activateUser: user not in an activation state "+user.getActivationToken());
                returnValue = new BasicResponse(false,MessageCode.ACTIVATION_ALREADY_DONE_OR_NOT_POSSIBLE);
            }
        }
        else {
            logger.error("activateUser: Not exactly one user found: "+activationToken);
            returnValue = new BasicResponse(false,MessageCode.ACTIVATION_TOKEN_NOT_KNOWN);
        }
        handleTransaction(returnValue);
        return returnValue;
    }
}
