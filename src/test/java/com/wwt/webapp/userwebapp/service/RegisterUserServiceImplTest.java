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
import com.wwt.webapp.userwebapp.security.PasswordHash;
import com.wwt.webapp.userwebapp.service.request.RegistrationRequest;
import com.wwt.webapp.userwebapp.service.response.InternalResponse;
import com.wwt.webapp.userwebapp.service.response.MessageCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class RegisterUserServiceImplTest extends BaseServiceTest {

    private static final UserStatusChangeToken token1 =  UserStatusChangeTokenImpl.newInstance();
    private static final String loginId1 = "RegisterUserServiceImplTest1";
    private static final String emailAddress1 = "RegisterUserServiceImplTest1@test.test";
    private static final PasswordHash pwHash1 = PasswordHash.newInstance("password1");

    private static final UserStatusChangeToken token2 =  UserStatusChangeTokenImpl.newInstance();
    private static final String loginId2 = "RegisterUserServiceImplTest2";
    private static final String emailAddress2 = "RegisterUserServiceImplTest2@test.test";
    private static final PasswordHash pwHash2 = PasswordHash.newInstance("password2");

    private static final String loginId3 = "RegisterUserServiceImplTest3";
    private static final String emailAddress3 = "RegisterUserServiceImplTest3@test.test";
    private static final String password3 = "password3";

    private static final String loginId4 = "RegisterUserServiceImplTest4";
    private static final String emailAddress5 = "RegisterUserServiceImplTest5@test.test";

    private static boolean initialized = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegisterUserService registerUserService;

    @BeforeEach
    public void createData() {
        if(!initialized) {
            assignDependenyObjects((BaseUserService)registerUserService);
            initialized = true;
        }
    }

    @Test
    public void registerUser() {
        InternalResponse internalResponse = registerUserService.registerUser(new RegistrationRequest(loginId3,password3,emailAddress3));
        assertTrue( internalResponse.isSuccessful());
        assertEquals( MessageCode.OPERATION_SUCCESSFUL, internalResponse.getMessageCode());
        Optional<UserEntity> userOpt = userRepository.getUserByLoginId(loginId3);
        assertTrue(userOpt.isPresent());
        UserEntity u = userOpt.get();
        assertEquals(emailAddress3,u.getEmailAddress());
        assertEquals( ActivationStatus.ESTABLISHED,u.getActivationStatus());
        assertTrue(PasswordHash.getInstance(u.getPasswordHash()).isPasswordHashEquals(password3));
    }



    @Test
    public void emailAlreadyUsed() {
        UserEntity u1 = new UserEntity(loginId1, emailAddress1, pwHash1.getPasswordHash(), token1.getToken(), token1.getTokenExpiresAt());
        userRepository.save(u1);
        userRepository.flush();
        InternalResponse internalResponse= registerUserService.registerUser(new RegistrationRequest(loginId4,password3,emailAddress1));
        assertFalse( internalResponse.isSuccessful());
        assertEquals( MessageCode.EMAIL_ADDRESS_ALREADY_EXISTS, internalResponse.getMessageCode());
    }

    @Test
    public void loginIdAlreadyUsed() {
        UserEntity u2 = new UserEntity(loginId2, emailAddress2, pwHash2.getPasswordHash(), token2.getToken(), token2.getTokenExpiresAt());
        userRepository.save(u2);
        userRepository.flush();
        InternalResponse internalResponse= registerUserService.registerUser(new RegistrationRequest(loginId2,password3,emailAddress5));
        assertFalse( internalResponse.isSuccessful());
        assertEquals( MessageCode.LOGIN_ID_ALREADY_EXISTS, internalResponse.getMessageCode());
    }
}