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
import com.wwt.webapp.userwebapp.service.response.BasicResponse;
import com.wwt.webapp.userwebapp.service.response.InternalResponse;
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
public class ActivationServiceImpl extends BaseUserService implements ActivationService {

    private final static Logger logger = LoggerFactory.getLogger( ActivationServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public InternalResponse activateUser(final String activationToken) {
        Optional<UserEntity> userOpt = userRepository.getUserByActivationToken(activationToken);
        if(userOpt.isEmpty()) {
            logger.warn("activateUser: Not exactly one user found: {}",activationToken);
            return BasicResponse.ACTIVATION_TOKEN_NOT_KNOWN;
        }
        UserEntity user = userOpt.get();
        if(!user.getActivationStatus().equals( ActivationStatus.ESTABLISHED)) {
            logger.warn("activateUser: user not in an activation state {}",user.getActivationToken());
            return BasicResponse.ACTIVATION_ALREADY_DONE_OR_NOT_POSSIBLE;
        }
        if(user.getActivationTokenExpiresAt().after( TimestampHelper.getNowAsUtcTimestamp())) {
            user.setActivationStatus(ActivationStatus.ACTIVE);
            logger.error("activateUser: user activated {}",user.getUuid());
            return BasicResponse.SUCCESS;
        }
        else {
            logger.warn("activateUser: token expired {}",user.getActivationToken());
            return BasicResponse.ACTIVATION_TOKEN_EXPIRED;
        }
    }
}
