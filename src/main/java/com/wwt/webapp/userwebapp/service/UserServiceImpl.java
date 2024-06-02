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
import com.wwt.webapp.userwebapp.domain.AdminRole;
import com.wwt.webapp.userwebapp.domain.relational.UserRepository;
import com.wwt.webapp.userwebapp.domain.relational.entity.UserEntity;
import com.wwt.webapp.userwebapp.mail.EmailType;
import com.wwt.webapp.userwebapp.mail.MailProcessor;
import com.wwt.webapp.userwebapp.security.PasswordHash;
import com.wwt.webapp.userwebapp.service.response.BasicResponse;
import com.wwt.webapp.userwebapp.service.response.InternalResponse;
import com.wwt.webapp.userwebapp.service.response.UserResponse;
import com.wwt.webapp.userwebapp.service.response.UserResponseList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


/**
 *
 */
@Service
public class UserServiceImpl extends BaseUserService implements UserService {


    private final static Logger logger = LoggerFactory.getLogger( UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailProcessor mailProcessor;

    /**
     * ADMIN Method
     */
    @Override
    @Transactional
    public InternalResponse readAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        logger.info("readAllUsers: user returned");
        return UserResponseList.convertToUserResponseList(users);
    }


    @Override
    @Transactional
    public InternalResponse readUser(String userUuid) {
        if(userUuid == null) {
            logger.error("readUser: userUuid null");
            return BasicResponse.LOGIN_OR_PASSWORD_WRONG;
        }
        Optional<UserEntity> userOpt = userRepository.findById(userUuid);
        if(userOpt.isEmpty()) {
            logger.warn("readUser: Not exactly one user found: {}",userUuid);
            return BasicResponse.LOGIN_OR_PASSWORD_WRONG;
        }
        UserEntity user = userOpt.get();
        logger.info("readUser: user returned");
        return UserResponse.convertToUserResponse(user);
    }

    @Override
    @Transactional
    public InternalResponse changePassword(String userUuid, String oldPassword, String newPassword) {
        Optional<UserEntity> userOpt = userRepository.findById(userUuid);
        if(userOpt.isEmpty()) {
            logger.warn("changePassword: Not exactly one user found: {}",userUuid);
            return BasicResponse.LOGIN_OR_PASSWORD_WRONG;
        }
        UserEntity user = userOpt.get();
        if (PasswordHash.getInstance(user.getPasswordHash()).isPasswordHashEquals(oldPassword)) {
            user.setPasswordHash(PasswordHash.newInstance(newPassword).getPasswordHash());
            userRepository.save(user);
            logger.error("changePassword: Password changed {}",user.getUuid());
            return BasicResponse.SUCCESS;
        }
        else {
            logger.info("changePassword: current password wrong");
            return BasicResponse.LOGIN_OR_PASSWORD_WRONG;
        }
    }

    @Override
    @Transactional
    public InternalResponse changeEmail(String userUuid, String newEmailAddress) {
        Optional<UserEntity> userOpt = userRepository.findById(userUuid);
        if(userOpt.isEmpty()) {
            logger.warn("changeEmail: Not exactly one user found: {}",userUuid);
            return BasicResponse.LOGIN_OR_PASSWORD_WRONG;
        }
        UserEntity user = userOpt.get();
        if (!isValidEmailAddress(newEmailAddress)) {
            logger.info("changeEmail: email not valid {}",newEmailAddress);
            return BasicResponse.EMAIL_ADDRESS_NOT_VALID;
        }
        if (user.getEmailAddress().equals(newEmailAddress)) {
            logger.debug("changeEmail: email address not changed {}",newEmailAddress);
            return BasicResponse.NO_CHANGE_NO_UPDATE;
        }
        if (!isEmailUnique(newEmailAddress,userRepository)) {
            logger.warn("changeEmail: email address already used {}",newEmailAddress);
            return BasicResponse.EMAIL_ADDRESS_ALREADY_EXISTS;
        }
        user.setEmailAddress(newEmailAddress);
        user.setActivationStatus( ActivationStatus.ESTABLISHED);
        //for changing the email re-activation in necessary
        UserStatusChangeToken userStatusChangeToken = UserStatusChangeTokenImpl.newInstance();
        logger.info("Email change Token: {}",userStatusChangeToken.getToken());
        user.setActivationToken(userStatusChangeToken.getToken(),userStatusChangeToken.getTokenExpiresAt());
        mailProcessor.sendEmail( EmailType.ACTIVATION_MAIL, user.getEmailAddress(),user.getLoginId(), userStatusChangeToken.getToken());
        logger.error("changeEmail: email changed {}",user.getUuid());
        userRepository.save(user);
        return BasicResponse.SUCCESS;
    }


    @Override
    @Transactional
    public InternalResponse archiveUser(String userUuid, String password) {
        Optional<UserEntity> userOpt = userRepository.findById(userUuid);
        if(userOpt.isEmpty()) {
            logger.warn("archiveUser: Not exactly one user found: {}",userUuid);
            return BasicResponse.LOGIN_OR_PASSWORD_WRONG;
        }
        UserEntity user = userOpt.get();
        if (!user.getActivationStatus().equals(ActivationStatus.ACTIVE)) {
            logger.warn("archiveUser: user not active {}",user.getUuid());
            return BasicResponse.USER_NOT_ACTIVE;
        }
        if (!PasswordHash.getInstance(user.getPasswordHash()).isPasswordHashEquals(password)) {
            logger.warn("archiveUser: user password check failed {}",user.getUuid());
            return BasicResponse.LOGIN_OR_PASSWORD_WRONG;
        }
        logger.error("archiveUser: user archived {}",user.getUuid());
        user.setActivationStatus(ActivationStatus.ARCHIVED);
        return BasicResponse.SUCCESS;
    }

    @Override
    @Transactional
    public InternalResponse updateUser(String userUuid, String email, AdminRole adminRole, ActivationStatus activationStatus) {
        Optional<UserEntity> userOpt = userRepository.findById(userUuid);
        if(userOpt.isEmpty()) {
            logger.warn("updateUser: Not exactly one user found: {}",userUuid);
            return BasicResponse.LOGIN_OR_PASSWORD_WRONG;
        }
        UserEntity user = userOpt.get();
        if (user.getActivationStatus().equals(activationStatus) &&
            user.getAdminRole().equals(adminRole) &&
            user.getEmailAddress().equals(email)
        ) {
            logger.warn("updateUser: no change {}",user.getUuid());
            return BasicResponse.NO_CHANGE_NO_UPDATE;
        }
        if(!user.getEmailAddress().equals(email)) {
            if (!isValidEmailAddress(email)) {
                logger.info("updateUser: email not valid {}",email);
                return BasicResponse.EMAIL_ADDRESS_NOT_VALID;
            }
            else if (!isEmailUnique(email,userRepository)) {
                logger.warn("updateUser: email address already used {}",email);
                return BasicResponse.EMAIL_ADDRESS_ALREADY_EXISTS;
            }
            else {
                user.setEmailAddress(email);
            }
        }
        if(!user.getActivationStatus().equals(activationStatus)) user.setActivationStatus(activationStatus);
        if(!user.getAdminRole().equals(adminRole)) user.setAdminRole(adminRole);
        logger.error("updateUser: user updated {}",user.getUuid());
        return BasicResponse.SUCCESS;
    }
}
