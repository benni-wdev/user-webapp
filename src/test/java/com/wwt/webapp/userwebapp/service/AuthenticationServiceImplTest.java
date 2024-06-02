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
import com.wwt.webapp.userwebapp.security.IdToken;
import com.wwt.webapp.userwebapp.security.PasswordHash;
import com.wwt.webapp.userwebapp.security.TestContextSetter;
import com.wwt.webapp.userwebapp.service.response.AuthenticationSuccessResponse;
import com.wwt.webapp.userwebapp.service.response.InternalResponse;
import com.wwt.webapp.userwebapp.service.response.MessageCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.Optional;

import static com.wwt.webapp.userwebapp.helper.ConfigProvider.getConfigIntValue;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class AuthenticationServiceImplTest extends BaseServiceTest {

    private static final UserStatusChangeToken token1 =  UserStatusChangeTokenImpl.newInstance();
    private static final String loginId1 = "AuthenticationServiceImplTest1";
    private static final String emailAddress1 = "AuthenticationServiceImplTest1@test.test";
    private static final String password1 = "password1";
    private static final PasswordHash pwHash1 = PasswordHash.newInstance(password1);

    private static final UserStatusChangeToken token2 =  UserStatusChangeTokenImpl.newInstance();
    private static final String loginId2 = "AuthenticationServiceImplTest2";
    private static final String emailAddress2 = "AuthenticationServiceImplTest2@test.test";
    private static final String password21 = "password21";
    private static final PasswordHash pwHash2 = PasswordHash.newInstance("password2");

    private static final UserStatusChangeToken token3 =  UserStatusChangeTokenImpl.newInstance();
    private static final String loginId3 = "AuthenticationServiceImplTest3";
    private static final String password31 = "password31";
    private static final String emailAddress3 = "AuthenticationServiceImplTest3@test.test";
    private static final PasswordHash pwHash3 = PasswordHash.newInstance("password3");

    private static final UserStatusChangeToken token4 = UserStatusChangeTokenImpl.newInstance();
    private static final String password4 = "password4";
    private static final String loginId4 = "AuthenticationServiceImplTest4";
    private static final String emailAddress4 = "AuthenticationServiceImplTest4@test.test";
    private static final PasswordHash pwHash4 = PasswordHash.newInstance(password4);

    private static final String loginId5 = "AuthenticationServiceImplTest5";
    private static final String password5 = "password5";

    private static final UserStatusChangeToken token6 = UserStatusChangeTokenImpl.newInstance();
    private static final String password6 = "password6";
    private static final String loginId6 = "AuthenticationServiceImplTest6";
    private static final String emailAddress6 = "AuthenticationServiceImplTest6@test.test";
    private static final PasswordHash pwHash6 = PasswordHash.newInstance(password6);

    private static final UserStatusChangeToken token7 =  UserStatusChangeTokenImpl.newInstance();
    private static final String loginId7 = "AuthenticationServiceImplTest7";
    private static final String emailAddress7 = "AuthenticationServiceImplTest7@test.test";
    private static final String password7 = "password7";
    private static final PasswordHash pwHash7 = PasswordHash.newInstance(password7);
    private static final RefreshToken refreshToken7 = RefreshToken.newInstance();


    private static final UserStatusChangeToken token8 =  UserStatusChangeTokenImpl.newInstance();
    private static final String loginId8 = "AuthenticationServiceImplTest8";
    private static final String emailAddress8 = "AuthenticationServiceImplTest8@test.test";
    private static final String password8 = "password8";
    private static final PasswordHash pwHash8 = PasswordHash.newInstance(password8);
    private static final RefreshToken refreshToken8 = RefreshToken.newInstance();

    private static final RefreshToken refreshToken9 = RefreshToken.newInstance();

    private static final UserStatusChangeToken token10 =  UserStatusChangeTokenImpl.newInstance();
    private static final String loginId10 = "AuthenticationServiceImplTest10";
    private static final String emailAddress10 = "AuthenticationServiceImplTest10@test.test";
    private static final String password10 = "password10";
    private static final PasswordHash pwHash10 = PasswordHash.newInstance(password10);
    private static final RefreshToken refreshToken10 = RefreshToken.newInstance();

    private static boolean initialized = false;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    ActivationService activationService;


    @BeforeEach
    public void initObjects() {
        if(!initialized) {
            TestContextSetter.setTestContext();
            assignDependenyObjects((BaseUserService)activationService);
            assignDependenyObjects((BaseUserService)authenticationService);
            initialized = true;
        }
    }

    @Test
    public void refreshAuthentication() {
        UserEntity u7 = new UserEntity(loginId7, emailAddress7, pwHash7.getPasswordHash(), token7.getToken(), token7.getTokenExpiresAt());
        u7.setRefreshToken(refreshToken7.toString());
        entityManager.persistAndFlush(u7);
        activationService.activateUser(token7.getToken());
        InternalResponse internalResponse = authenticationService.refreshAuthentication(refreshToken7.toString());
        assertTrue( internalResponse.isSuccessful());
        assertEquals( MessageCode.OPERATION_SUCCESSFUL, internalResponse.getMessageCode());
        String token = ((AuthenticationSuccessResponse)internalResponse).getToken();
        String refreshToken = ((AuthenticationSuccessResponse)internalResponse).getRefreshToken();
        IdToken idToken = IdToken.parse(token);
        Optional<UserEntity> userOpt = userRepository.getUserByLoginId(loginId7);
        assertTrue(userOpt.isPresent());
        UserEntity u = userOpt.get();
        assertEquals(loginId7,u.getLoginId());
        assertNotNull(refreshToken);
        assertEquals(u.getUuid(),idToken.getSubject());
        assertNotEquals( refreshToken,refreshToken7.toString() );
    }
    @Test
    public void refreshAuthUserNotActive() {
        UserEntity u8 = new UserEntity(loginId8, emailAddress8, pwHash8.getPasswordHash(), token8.getToken(), token8.getTokenExpiresAt());
        u8.setActivationStatus( ActivationStatus.SUSPENDED);
        u8.setRefreshToken(refreshToken8.toString());
        entityManager.persistAndFlush(u8);
        InternalResponse internalResponse = authenticationService.refreshAuthentication(refreshToken8.toString());
        assertFalse( internalResponse.isSuccessful());
        assertEquals(MessageCode.USER_NOT_ACTIVE, internalResponse.getMessageCode());
    }

    @Test
    public void refreshAuthNoUser() {
        InternalResponse internalResponse = authenticationService.refreshAuthentication(refreshToken9.toString());
        assertFalse( internalResponse.isSuccessful());
        assertEquals(MessageCode.LOGIN_OR_PASSWORD_WRONG, internalResponse.getMessageCode());
    }

    @Test
    public void authenticate() {
        UserEntity u1 = new UserEntity(loginId1, emailAddress1, pwHash1.getPasswordHash(), token1.getToken(), token1.getTokenExpiresAt());
        entityManager.persistAndFlush(u1);
        activationService.activateUser(token1.getToken());
        InternalResponse internalResponse = authenticationService.authenticate(loginId1,password1,false);
        assertTrue( internalResponse.isSuccessful());
        assertEquals( MessageCode.OPERATION_SUCCESSFUL, internalResponse.getMessageCode());
        String token = ((AuthenticationSuccessResponse)internalResponse).getToken();
        IdToken idToken = IdToken.parse(token);
        Optional<UserEntity> userOpt = userRepository.getUserByLoginId(loginId1);
        assertTrue(userOpt.isPresent());
        UserEntity u = userOpt.get();
        assertEquals(loginId1,u.getLoginId());
        assertEquals(u.getUuid(),idToken.getSubject());
    }

    @Test
    public void authenticate2() {
        UserEntity u1 = new UserEntity(loginId1, emailAddress1, pwHash1.getPasswordHash(), token1.getToken(), token1.getTokenExpiresAt());
        u1.setAdminRole( AdminRole.FULL_ADMIN);
        entityManager.persistAndFlush(u1);
        activationService.activateUser(token1.getToken());
        InternalResponse internalResponse = authenticationService.authenticate(loginId1,password1,false);
        assertTrue( internalResponse.isSuccessful());
        assertEquals(MessageCode.OPERATION_SUCCESSFUL, internalResponse.getMessageCode());
        String token = ((AuthenticationSuccessResponse)internalResponse).getToken();
        IdToken idToken = IdToken.parse(token);
        Optional<UserEntity> userOpt = userRepository.getUserByLoginId(loginId1);
        assertTrue(userOpt.isPresent());
        UserEntity u = userOpt.get();
        assertEquals(loginId1,u.getLoginId());
        assertEquals(u.getUuid(),idToken.getSubject());
        assertEquals(AdminRole.FULL_ADMIN.toString(),idToken.getAdminRole());
        assertEquals(AdminRole.FULL_ADMIN.toString(),((AuthenticationSuccessResponse)internalResponse).getAdminRole());
    }

    @Test
    public void passwordWrong() {
        UserEntity u2 = new UserEntity(loginId2, emailAddress2, pwHash2.getPasswordHash(), token2.getToken(), token2.getTokenExpiresAt());
        entityManager.persistAndFlush(u2);
        activationService.activateUser(token2.getToken());
        InternalResponse internalResponse = authenticationService.authenticate(loginId2,password21,false);
        assertFalse( internalResponse.isSuccessful());
        assertEquals(MessageCode.LOGIN_OR_PASSWORD_WRONG, internalResponse.getMessageCode());
    }

    @Test
    public void loginWrong() {
        InternalResponse internalResponse = authenticationService.authenticate(loginId5,password5,false);
        assertFalse( internalResponse.isSuccessful());
        assertEquals(MessageCode.LOGIN_OR_PASSWORD_WRONG, internalResponse.getMessageCode());
    }

    @Test
    public void wrongTries() {
        UserEntity u3 = new UserEntity(loginId3, emailAddress3, pwHash3.getPasswordHash(), token3.getToken(), token3.getTokenExpiresAt());
        entityManager.persistAndFlush(u3);
        activationService.activateUser(token3.getToken());
        InternalResponse internalResponse = authenticationService.authenticate(loginId3,password31,false);
        assertFalse( internalResponse.isSuccessful());
        assertEquals(MessageCode.LOGIN_OR_PASSWORD_WRONG, internalResponse.getMessageCode());
        internalResponse = authenticationService.authenticate(loginId3,password31,false);
        assertFalse( internalResponse.isSuccessful());
        assertEquals(MessageCode.LOGIN_OR_PASSWORD_WRONG, internalResponse.getMessageCode());
        internalResponse = authenticationService.authenticate(loginId3,password31,false);
        assertFalse( internalResponse.isSuccessful());
        assertEquals(MessageCode.LOGIN_OR_PASSWORD_WRONG, internalResponse.getMessageCode());
        internalResponse = authenticationService.authenticate(loginId3,password31,false);
        assertFalse( internalResponse.isSuccessful());
        assertEquals(MessageCode.LOGIN_OR_PASSWORD_WRONG, internalResponse.getMessageCode());
        internalResponse = authenticationService.authenticate(loginId3,password31,false);
        assertFalse( internalResponse.isSuccessful());
        assertEquals(MessageCode.TOO_MANY_FAILED_LOGINS, internalResponse.getMessageCode());
        Optional<UserEntity> userOpt = userRepository.getUserByLoginId(loginId3);
        assertTrue(userOpt.isPresent());
        UserEntity u = userOpt.get();
        assertEquals(ActivationStatus.SUSPENDED,u.getActivationStatus());
    }

    @Test
    public void userNotActive() {
        UserEntity u4 = new UserEntity(loginId4, emailAddress4, pwHash4.getPasswordHash(), token4.getToken(), token4.getTokenExpiresAt());
        entityManager.persistAndFlush(u4);
        UserEntity u6 = new UserEntity(loginId6, emailAddress6, pwHash6.getPasswordHash(), token6.getToken(), token6.getTokenExpiresAt());
        u6.setActivationStatus(ActivationStatus.SUSPENDED);
        entityManager.persistAndFlush(u6);
        InternalResponse internalResponse = authenticationService.authenticate(loginId4,password4,false);
        assertFalse( internalResponse.isSuccessful());
        assertEquals(MessageCode.USER_NOT_ACTIVE, internalResponse.getMessageCode());
        internalResponse = authenticationService.authenticate(loginId6,password6,false);
        assertFalse( internalResponse.isSuccessful());
        assertEquals(MessageCode.USER_NOT_ACTIVE, internalResponse.getMessageCode());
    }

    @Test
    public void authenticateLong() {
        UserEntity u1 = new UserEntity(loginId1, emailAddress1, pwHash1.getPasswordHash(), token1.getToken(), token1.getTokenExpiresAt());
        entityManager.persistAndFlush(u1);
        activationService.activateUser(token1.getToken());
        InternalResponse internalResponse = authenticationService.authenticate(loginId1,password1,true);
        assertTrue( internalResponse.isSuccessful());
        assertEquals(MessageCode.OPERATION_SUCCESSFUL, internalResponse.getMessageCode());
        String token = ((AuthenticationSuccessResponse)internalResponse).getToken();
        String refreshToken = ((AuthenticationSuccessResponse)internalResponse).getRefreshToken();
        IdToken idToken = IdToken.parse(token);
        Instant compare1 = Instant.now().plusSeconds(getConfigIntValue("ttl")*2L);
        Instant compare2 = Instant.now().plusSeconds(getConfigIntValue("ttlLong")*2L);
        assertTrue(idToken.getExpiresAt().isAfter(compare1)
                && idToken.getExpiresAt().isBefore(compare2));
        Optional<UserEntity> userOpt = userRepository.getUserByLoginId(loginId1);
        assertTrue(userOpt.isPresent());
        UserEntity u = userOpt.get();
        assertEquals(loginId1,u.getLoginId());
        assertNotNull(refreshToken);
        assertEquals(u.getRefreshToken(),refreshToken);
        assertEquals(u.getUuid(),idToken.getSubject());
    }

    @Test
    public void logoutTest() {
        UserEntity u10 = new UserEntity(loginId10, emailAddress10, pwHash10.getPasswordHash(), token10.getToken(), token10.getTokenExpiresAt());
        u10.setActivationStatus( ActivationStatus.ACTIVE);
        u10.setRefreshToken(refreshToken10.toString());
        entityManager.persistAndFlush(u10);
        authenticationService.authenticate(loginId10,password10,true);

        InternalResponse r2 =  authenticationService.logout(convertToUuid(userRepository,loginId10));
        Optional<UserEntity> userOpt = userRepository.getUserByLoginId(loginId10);
        assertTrue(userOpt.isPresent());
        UserEntity u = userOpt.get();
        assertNull(u.getRefreshToken());
        assertTrue( r2.isSuccessful() );
        assertEquals( MessageCode.OPERATION_SUCCESSFUL,r2.getMessageCode() );
    }

}