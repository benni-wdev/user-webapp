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
import com.wwt.webapp.userwebapp.helper.ConfigProvider;
import com.wwt.webapp.userwebapp.mail.EmailType;
import com.wwt.webapp.userwebapp.mail.MailProcessor;
import com.wwt.webapp.userwebapp.security.PasswordHash;
import com.wwt.webapp.userwebapp.service.request.RegistrationRequest;
import com.wwt.webapp.userwebapp.service.response.BasicResponse;
import com.wwt.webapp.userwebapp.service.response.InternalResponse;
import com.wwt.webapp.userwebapp.service.response.RegistrationSuccessResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


/**
 * @author benw-at-wwt
 */
@Service
@EnableAutoConfiguration
public class RegisterUserServiceImpl extends BaseUserService implements RegisterUserService {

    private final static Logger logger = LoggerFactory.getLogger( RegisterUserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailProcessor mailProcessor;

    @Override
    @Transactional
    public InternalResponse registerUser(final RegistrationRequest registrationRequest) {
        if(!isValidEmailAddress(registrationRequest.getEmailAddress())) {
            logger.debug("registerUser: email address not valid {}",registrationRequest.getEmailAddress());
            return BasicResponse.EMAIL_ADDRESS_NOT_VALID;
        }
        if(!isValidLoginId(registrationRequest.getLoginId())) {
            logger.debug("registerUser: loginId not valid {}",registrationRequest.getLoginId());
            return BasicResponse.LOGIN_ID_NOT_VALID;
        }
        if(!isEmailUnique(registrationRequest.getEmailAddress(),userRepository)) {
            logger.debug("registerUser: email address already used {}",registrationRequest.getEmailAddress());
            return BasicResponse.EMAIL_ADDRESS_ALREADY_EXISTS;
        }
        if(!isLoginIdUnique(registrationRequest.getLoginId(),userRepository)) {
            logger.debug("registerUser: login id already used {}",registrationRequest.getLoginId());
            return BasicResponse.LOGIN_ID_ALREADY_EXISTS;
        }
        else {
            PasswordHash passwordHash = PasswordHash.newInstance(registrationRequest.getPassword());
            UserStatusChangeToken userStatusChangeToken = UserStatusChangeTokenImpl.newInstance();
            UserEntity user = new UserEntity(registrationRequest.getLoginId(),
                    registrationRequest.getEmailAddress(), passwordHash.getPasswordHash(), userStatusChangeToken.getToken(),userStatusChangeToken.getTokenExpiresAt());
            logger.info("User activation token {}",userStatusChangeToken.getToken() );
            if(ConfigProvider.getConfigBoolean("isUserActiveOnRegistration")) {
                user.setActivationStatus( ActivationStatus.ACTIVE);
            }
            userRepository.save(user);
            mailProcessor.sendEmail( EmailType.ACTIVATION_MAIL,user.getEmailAddress(),user.getLoginId(),userStatusChangeToken.getToken());
            logger.error("registerUser: Registered new UserResponse {}",user.getUuid());
            return new RegistrationSuccessResp(user.getEmailAddress());
        }
    }


    private boolean isLoginIdUnique(String loginId, UserRepository userRepository) {
        Optional<UserEntity> userOpt = userRepository.getUserByLoginId(loginId);
        return userOpt.isEmpty();
    }

}
