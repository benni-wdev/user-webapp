package com.wwt.webapp.userwebapp.service;


import com.wwt.webapp.userwebapp.domain.ActivationStatus;
import com.wwt.webapp.userwebapp.domain.relational.UserRepository;
import com.wwt.webapp.userwebapp.domain.relational.entity.UserEntity;
import com.wwt.webapp.userwebapp.security.PasswordHash;
import com.wwt.webapp.userwebapp.service.response.InternalResponse;
import com.wwt.webapp.userwebapp.service.response.MessageCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class UserRecoveryServiceImplTest extends BaseServiceTest {

    private final static Logger logger = LoggerFactory.getLogger(UserRecoveryServiceImplTest.class);

    private static final UserStatusChangeToken token1 =  UserStatusChangeTokenImpl.newInstance();
    private static final String loginId1 = "UserRecoveryServiceImplTest1";
    private static final String emailAddress1 = "UserRecoveryServiceImplTest1@test.test";
    private static final PasswordHash pwHash1 = PasswordHash.newInstance("password1");

    private static final UserStatusChangeToken token2 =  UserStatusChangeTokenImpl.newInstance();
    private static final String loginId2 = "UserRecoveryServiceImplTest2";
    private static final String emailAddress2 = "UserRecoveryServiceImplTest2@test.test";
    private static final PasswordHash pwHash2 = PasswordHash.newInstance("password2");
    private static final UserStatusChangeToken pwToken2 =  UserStatusChangeTokenImpl.newInstance();
    private static final String password21 = "password21";
    private static final String password22 = "password22";

    private static final UserStatusChangeToken token3 =  UserStatusChangeTokenImpl.newInstance();

    private static final UserStatusChangeToken token4 =  new UserStatusChangeToken() {
        @Override
        public String getToken() {
            return "e8f0458f-82b1-4e9d-90fe-f7129ff7d489";
        }

        @Override
        public Timestamp getTokenExpiresAt() {
            ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
            return Timestamp.from(utc.toInstant());
        }
    };
    private static final String loginId4 = "UserRecoveryServiceImplTest4";
    private static final String emailAddress4 = "UserRecoveryServiceImplTest4@test.test";
    private static final PasswordHash pwHash4 = PasswordHash.newInstance("password4");

    private static boolean initialized = false;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserRecoveryService userRecoveryService;

    @BeforeEach
    public void createData() {
        if(!initialized) {
            assignDependenyObjects((BaseUserService)userRecoveryService);
            initialized = true;
        }
    }

    @Test
    public void lockUserRecovery() {
        UserEntity u1 = new UserEntity(loginId1, emailAddress1, pwHash1.getPasswordHash(), token1.getToken(),token1.getTokenExpiresAt());
        u1.setActivationStatus( ActivationStatus.ACTIVE);
        userRepository.save(u1);
        userRepository.flush();
        InternalResponse internalResponse = userRecoveryService.initiateRecovery(emailAddress1);
        assertTrue( internalResponse.isSuccessful());
        assertEquals( MessageCode.OPERATION_SUCCESSFUL, internalResponse.getMessageCode());
        Optional<UserEntity> userOpt = userRepository.getUserByLoginId(loginId1);
        assertTrue(userOpt.isPresent());
        UserEntity u = userOpt.get();
        assertEquals(emailAddress1,u.getEmailAddress());
        assertEquals(ActivationStatus.SUSPENDED,u.getActivationStatus());
        internalResponse = userRecoveryService.initiateRecovery(emailAddress1);
        assertTrue( internalResponse.isSuccessful());
        assertEquals( MessageCode.OPERATION_SUCCESSFUL, internalResponse.getMessageCode());
        userOpt = userRepository.getUserByLoginId(loginId1);
        assertTrue(userOpt.isPresent());
        u = userOpt.get();
        assertEquals(emailAddress1,u.getEmailAddress());
        assertEquals(ActivationStatus.SUSPENDED,u.getActivationStatus());
    }

    @Test
    public void recoverUser() {
        UserEntity u2 = new UserEntity(loginId2, emailAddress2, pwHash2.getPasswordHash(), token2.getToken(),token2.getTokenExpiresAt());
        u2.setActivationStatus(ActivationStatus.SUSPENDED);
        u2.setPasswordRecoveryToken(pwToken2.getToken(),pwToken2.getTokenExpiresAt());
        userRepository.save(u2);
        userRepository.flush();
        InternalResponse internalResponse = userRecoveryService.recoverUser(pwToken2.getToken(),password21);
        logger.warn(internalResponse.toString());
        assertTrue(internalResponse.isSuccessful());
        assertEquals(MessageCode.OPERATION_SUCCESSFUL, internalResponse.getMessageCode());
        Optional<UserEntity> userOpt = userRepository.getUserByLoginId(loginId2);
        assertTrue(userOpt.isPresent());
        UserEntity u = userOpt.get();
        assertEquals(emailAddress2,u.getEmailAddress());
        assertEquals(ActivationStatus.ACTIVE,u.getActivationStatus());
        assertTrue(PasswordHash.getInstance(u.getPasswordHash()).isPasswordHashEquals(password21));
        internalResponse = userRecoveryService.recoverUser(pwToken2.getToken(),password22);
        assertFalse( internalResponse.isSuccessful());
        assertEquals( MessageCode.RECOVERY_ALREADY_DONE_OR_NOT_POSSIBLE, internalResponse.getMessageCode());
        userOpt = userRepository.getUserByLoginId(loginId2);
        assertTrue(userOpt.isPresent());
        u = userOpt.get();
        assertEquals(emailAddress2,u.getEmailAddress());
        assertEquals(ActivationStatus.ACTIVE,u.getActivationStatus());
        assertTrue(PasswordHash.getInstance(u.getPasswordHash()).isPasswordHashEquals(password21));
    }

    @Test
    public void recoverUserNotKnown() {
        InternalResponse internalResponse = userRecoveryService.recoverUser(token3.getToken(),password21);
        assertFalse( internalResponse.isSuccessful());
        assertEquals( MessageCode.PASSWORD_TOKEN_NOT_KNOWN, internalResponse.getMessageCode());
    }

    @Test
    public void tokenExpired() {
        UserEntity u4 = new UserEntity(loginId4, emailAddress4, pwHash4.getPasswordHash(), token4.getToken(),token4.getTokenExpiresAt());
        u4.setActivationStatus(ActivationStatus.SUSPENDED);
        u4.setPasswordRecoveryToken(token4.getToken(),token4.getTokenExpiresAt());
        userRepository.save(u4);
        userRepository.flush();
        InternalResponse internalResponse = userRecoveryService.recoverUser(token4.getToken(),password21);
        assertFalse( internalResponse.isSuccessful());
        assertEquals( MessageCode.PASSWORD_RECOVERY_TOKEN_EXPIRED, internalResponse.getMessageCode());
    }
}