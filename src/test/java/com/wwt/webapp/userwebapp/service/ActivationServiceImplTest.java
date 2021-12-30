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
public class ActivationServiceImplTest extends BaseServiceTest {

    private static final UserStatusChangeToken token2 =  UserStatusChangeTokenImpl.newInstance();

    private static final UserStatusChangeToken token3 =  UserStatusChangeTokenImpl.newInstance();
    private static final String loginId3 = "ActivationServiceImplTest3";
    private static final String emailAddress3 = "ActivationServiceImplTest3@test.test";
    private static final PasswordHash pwHash3 = PasswordHash.newInstance("password3");

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
    private static final String loginId4 = "ActivationServiceImplTest4";
    private static final String emailAddress4 = "ActivationServiceImplTest4@test.test";
    private static final PasswordHash pwHash4 = PasswordHash.newInstance("password4");

    private static boolean initialized = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivationService activationService;

    @BeforeEach
    public void createData() {
        if(!initialized) {
            assignDependenyObjects((BaseUserService)activationService);
            initialized = true;
        }
    }

    @Test
    public void activateUser() {
        UserStatusChangeToken token1 =  UserStatusChangeTokenImpl.newInstance();
        String loginId1 = "ActivationServiceImplTest1";
        String emailAddress1 = "ActivationServiceImplTest1@test.test";
        PasswordHash pwHash1 = PasswordHash.newInstance("password1");
        UserEntity u1 = new UserEntity(loginId1, emailAddress1, pwHash1.getPasswordHash(), token1.getToken(), token1.getTokenExpiresAt());
        userRepository.save(u1);
        userRepository.flush();
        Optional<UserEntity> userOpt = userRepository.getUserByActivationToken(token1.getToken());
        assertTrue(userOpt.isPresent());
        UserEntity u = userOpt.get();
        assertEquals( ActivationStatus.ESTABLISHED,u.getActivationStatus());
        InternalResponse response = activationService.activateUser(token1.getToken());
        userOpt = userRepository.getUserByActivationToken(token1.getToken());
        assertTrue(userOpt.isPresent());
        u = userOpt.get();
        assertEquals(loginId1,u.getLoginId());
        assertEquals(ActivationStatus.ACTIVE,u.getActivationStatus());
        assertTrue( response.isSuccessful());
        assertEquals( MessageCode.OPERATION_SUCCESSFUL, response.getMessageCode());
    }

    @Test
    public void tokenNotKnown() {
        InternalResponse response = activationService.activateUser(token2.getToken());
        assertFalse( response.isSuccessful());
        assertEquals(MessageCode.ACTIVATION_TOKEN_NOT_KNOWN, response.getMessageCode());
    }

    @SuppressWarnings("UnusedAssignment")
    @Test
    public void tokenAlreadyUsed() {
        UserEntity u3 = new UserEntity(loginId3, emailAddress3, pwHash3.getPasswordHash(), token3.getToken(), token3.getTokenExpiresAt());
        userRepository.save(u3);
        userRepository.flush();
        InternalResponse response = activationService.activateUser(token3.getToken());
        response = activationService.activateUser(token3.getToken());
        assertFalse( response.isSuccessful());
        assertEquals(MessageCode.ACTIVATION_ALREADY_DONE_OR_NOT_POSSIBLE, response.getMessageCode());
    }

    @Test
    public void tokenAExpired() {
        UserEntity u4 = new UserEntity(loginId4, emailAddress4, pwHash4.getPasswordHash(), token4.getToken(), token4.getTokenExpiresAt());
        userRepository.save(u4);
        userRepository.flush();
        InternalResponse response = activationService.activateUser(token4.getToken());
        assertFalse( response.isSuccessful());
        assertEquals(MessageCode.ACTIVATION_TOKEN_EXPIRED, response.getMessageCode());
    }
}