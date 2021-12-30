package com.wwt.webapp.userwebapp.service;

import com.wwt.webapp.userwebapp.domain.ActivationStatus;
import com.wwt.webapp.userwebapp.domain.relational.UserRepository;
import com.wwt.webapp.userwebapp.domain.relational.entity.UserEntity;
import com.wwt.webapp.userwebapp.security.PasswordHash;
import com.wwt.webapp.userwebapp.service.response.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("unused")
@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class UserServiceImplTest extends BaseServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImplTest.class);

    private static final UserStatusChangeToken token1 =  UserStatusChangeTokenImpl.newInstance();
    private static final String loginId1 = "UserServiceImplTest1";
    private static final String emailAddress1 = "UserServiceImplTest1@test.test";
    private static final String password1 = "password1";
    private static final PasswordHash pwHash1 = PasswordHash.newInstance(password1);

    private static final UserStatusChangeToken token2 =  UserStatusChangeTokenImpl.newInstance();
    private static final String loginId2 = "UserServiceImplTest2";
    private static final String emailAddress2 = "UserServiceImplTest2@test.test";
    private static final String password2 = "password2";
    private static final String password21 = "password21";
    private static final PasswordHash pwHash2 = PasswordHash.newInstance(password2);
    private static final UserStatusChangeToken pwToken2 =  UserStatusChangeTokenImpl.newInstance();

    private static final UserStatusChangeToken token3 =  UserStatusChangeTokenImpl.newInstance();
    private static final String loginId3 = "UserServiceImplTest3";
    private static final String emailAddress3 = "UserServiceImplTest3@test.test";
    private static final String emailAddress31 = "UserServiceImplTest31@test.test";
    private static final String password3 = "password3";
    private static final PasswordHash pwHash3 = PasswordHash.newInstance(password3);

    private static final UserStatusChangeToken token4 =  UserStatusChangeTokenImpl.newInstance();
    private static final String loginId4 = "UserServiceImplTest4";
    private static final String emailAddress4 = "UserServiceImplTest4@test.test";
    private static final String password4 = "password4";
    private static final PasswordHash pwHash4 = PasswordHash.newInstance(password4);

    private static final UserStatusChangeToken token5 =  UserStatusChangeTokenImpl.newInstance();
    private static final String loginId5 = "UserServiceImplTest5";
    private static final String emailAddress5 = "UserServiceImplTest5@test.test";
    private static final String password5 = "password5";
    private static final PasswordHash pwHash5 = PasswordHash.newInstance(password5);

    private static final UserStatusChangeToken token6 =  UserStatusChangeTokenImpl.newInstance();
    private static final String loginId6 = "UserServiceImplTest6";
    private static final String emailAddress6 = "UserServiceImplTest6@test.test";
    private static final String emailAddress61 = "UserServiceImplTest61@test.test";
    private static final String password6 = "password6";
    private static final String password61 = "password61";
    private static final PasswordHash pwHash6 = PasswordHash.newInstance(password6);

    private static final UserStatusChangeToken token7 =  UserStatusChangeTokenImpl.newInstance();
    private static final String loginId7 = "UserServiceImplTest7";
    private static final String emailAddress7 = "UserServiceImplTest7@test.test";
    private static final String emailAddres71 = "UserServiceImplTest71@test.test";
    private static final String password7 = "password7";
    private static final String password71 = "password71";
    private static final PasswordHash pwHash7 = PasswordHash.newInstance(password7);

    //not used
    private static final UserStatusChangeToken token8 =  UserStatusChangeTokenImpl.newInstance();
    private static final String loginId8 = "UserServiceImplTest8";
    private static final String emailAddress8 = "UserServiceImplTest8@test.test";
    private static final String password8 = "password8";
    private static final PasswordHash pwHash8 = PasswordHash.newInstance(password7);
    private static final RefreshToken refreshToken8 = RefreshToken.newInstance();

    private static boolean initialized = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    @BeforeEach
    public void createData() {
        if(!initialized) {

            UserEntity u8 = new UserEntity(loginId8, emailAddress8, pwHash8.getPasswordHash(), token8.getToken(),token8.getTokenExpiresAt());
            u8.setActivationStatus(ActivationStatus.ACTIVE);
            u8.setRefreshToken(refreshToken8.toString());
            userRepository.save(u8);
            userRepository.flush();
            assignDependenyObjects((BaseUserService)userService);
            initialized = true;
        }
    }


    @Test
    public void readUser() {
        UserEntity u1 = new UserEntity(loginId1, emailAddress1, pwHash1.getPasswordHash(), token1.getToken(),token1.getTokenExpiresAt());
        u1.setActivationStatus(ActivationStatus.ACTIVE);
        userRepository.save(u1);
        userRepository.flush();
        UserResponse user = (UserResponse)userService.readUser(convertToUuid(userRepository,loginId1));
        assertTrue( user.isSuccessful());
        assertEquals( MessageCode.OPERATION_SUCCESSFUL, user.getMessageCode());
        assertEquals(emailAddress1,user.getEmailAddress());
        assertEquals(loginId1,user.getLoginId());
        assertEquals(2,user.getVersion());
    }


    @Test
    public void changePassword() {
        UserEntity u2 = new UserEntity(loginId2, emailAddress2, pwHash2.getPasswordHash(), token2.getToken(),token2.getTokenExpiresAt());
        u2.setActivationStatus( ActivationStatus.ACTIVE);
        u2.setPasswordRecoveryToken(pwToken2.getToken(),pwToken2.getTokenExpiresAt());
        userRepository.save(u2);
        userRepository.flush();
        InternalResponse internalResponse = userService.changePassword(convertToUuid(userRepository,loginId2),password2,password21);
        assertTrue( internalResponse.isSuccessful());
        assertEquals( MessageCode.OPERATION_SUCCESSFUL, internalResponse.getMessageCode());
        Optional<UserEntity> userOpt = userRepository.getUserByLoginId(loginId2);
        assertTrue(userOpt.isPresent());
        UserEntity u = userOpt.get();
        assertEquals(emailAddress2,u.getEmailAddress());
        assertEquals(ActivationStatus.ACTIVE,u.getActivationStatus());
        assertTrue(PasswordHash.getInstance(u.getPasswordHash()).isPasswordHashEquals(password21));
    }

    @Test
    public void changePasswordFail() {
        UserEntity u7 = new UserEntity(loginId7, emailAddress7, pwHash7.getPasswordHash(), token7.getToken(),token7.getTokenExpiresAt());
        u7.setActivationStatus(ActivationStatus.ACTIVE);
        userRepository.save(u7);
        userRepository.flush();

        InternalResponse internalResponse = userService.changePassword(convertToUuid(userRepository,loginId7),password71,password7);
        assertFalse( internalResponse.isSuccessful());
        assertEquals( MessageCode.LOGIN_OR_PASSWORD_WRONG, internalResponse.getMessageCode());
        Optional<UserEntity> userOpt = userRepository.getUserByLoginId(loginId7);
        assertTrue(userOpt.isPresent());
        UserEntity u = userOpt.get();
        assertEquals(emailAddress7,u.getEmailAddress());
        assertEquals(ActivationStatus.ACTIVE,u.getActivationStatus());
        assertTrue(PasswordHash.getInstance(u.getPasswordHash()).isPasswordHashEquals(password7));
    }

    @Test
    public void emailUniqueFail() {
        UserEntity u7 = new UserEntity(loginId7, emailAddress7, pwHash7.getPasswordHash(), token7.getToken(),token7.getTokenExpiresAt());
        u7.setActivationStatus(ActivationStatus.ACTIVE);
        userRepository.save(u7);
        UserEntity u1 = new UserEntity(loginId1, emailAddress1, pwHash1.getPasswordHash(), token1.getToken(),token1.getTokenExpiresAt());
        u1.setActivationStatus(ActivationStatus.ACTIVE);
        userRepository.save(u1);
        userRepository.flush();

        InternalResponse internalResponse = userService.changeEmail(convertToUuid(userRepository,loginId7),emailAddress1);
        assertFalse( internalResponse.isSuccessful());
        assertEquals( MessageCode.EMAIL_ADDRESS_ALREADY_EXISTS, internalResponse.getMessageCode());
        Optional<UserEntity> userOpt = userRepository.getUserByLoginId(loginId7);
        assertTrue(userOpt.isPresent());
        UserEntity u = userOpt.get();
        assertEquals(emailAddress7,u.getEmailAddress());
        assertEquals(ActivationStatus.ACTIVE,u.getActivationStatus());
        assertTrue(PasswordHash.getInstance(u.getPasswordHash()).isPasswordHashEquals(password7));
    }

    @Test
    public void changeEmail() {
        UserEntity u3 = new UserEntity(loginId3, emailAddress3, pwHash3.getPasswordHash(), token3.getToken(),token3.getTokenExpiresAt());
        u3.setActivationStatus(ActivationStatus.ACTIVE);
        userRepository.save(u3);
        userRepository.flush();
        InternalResponse internalResponse = userService.changeEmail(convertToUuid(userRepository,loginId3),emailAddress31);
        assertTrue( internalResponse.isSuccessful());
        assertEquals( MessageCode.OPERATION_SUCCESSFUL, internalResponse.getMessageCode());
        Optional<UserEntity> userOpt = userRepository.getUserByLoginId(loginId3);
        assertTrue(userOpt.isPresent());
        UserEntity u = userOpt.get();
        assertEquals(emailAddress31,u.getEmailAddress());
        assertEquals(ActivationStatus.ESTABLISHED,u.getActivationStatus());
        assertTrue(PasswordHash.getInstance(u.getPasswordHash()).isPasswordHashEquals(password3));
    }

    @Test
    public void archiveUser() {
        UserEntity u4 = new UserEntity(loginId4, emailAddress4, pwHash4.getPasswordHash(), token4.getToken(),token4.getTokenExpiresAt());
        u4.setActivationStatus(ActivationStatus.ACTIVE);
        userRepository.save(u4);
        userRepository.flush();

        InternalResponse internalResponse = userService.archiveUser(convertToUuid(userRepository,loginId4),password4);
        assertTrue( internalResponse.isSuccessful());
        assertEquals( MessageCode.OPERATION_SUCCESSFUL, internalResponse.getMessageCode());
        Optional<UserEntity> userOpt = userRepository.getUserByLoginId(loginId4);
        assertTrue(userOpt.isPresent());
        UserEntity u = userOpt.get();
        assertEquals(ActivationStatus.ARCHIVED,u.getActivationStatus());
    }

    @Test
    public void readUserWrongInput() {
        UserEntity u1 = new UserEntity(loginId1, emailAddress1, pwHash1.getPasswordHash(), token1.getToken(),token1.getTokenExpiresAt());
        u1.setActivationStatus(ActivationStatus.ACTIVE);
        userRepository.save(u1);
        userRepository.flush();

        InternalResponse internalResponse = userService.readUser("dkasdksakdök");
        assertFalse( internalResponse.isSuccessful());
        assertEquals( MessageCode.LOGIN_OR_PASSWORD_WRONG, internalResponse.getMessageCode());

        internalResponse = userService.readUser(null);
        assertFalse( internalResponse.isSuccessful());
        assertEquals( MessageCode.LOGIN_OR_PASSWORD_WRONG, internalResponse.getMessageCode());
    }

    @Test
    public void readAllUsersTest() {
        UserEntity u1 = new UserEntity(loginId1, emailAddress1, pwHash1.getPasswordHash(), token1.getToken(),token1.getTokenExpiresAt());
        UserEntity u4 = new UserEntity(loginId4, emailAddress4, pwHash4.getPasswordHash(), token4.getToken(),token4.getTokenExpiresAt());
        userRepository.save(u1);
        userRepository.save(u4);
        userRepository.flush();

        InternalResponse internalResponse = userService.readAllUsers();
        assertTrue( internalResponse.isSuccessful());
        assertEquals( MessageCode.OPERATION_SUCCESSFUL, internalResponse.getMessageCode());
        List<UserResponseItem> list = ((UserResponseList) internalResponse).getUsers();
        boolean found1 = false;
        boolean found2 = false;
        for(UserResponseItem i:list) {
            logger.info(i.toString());
            if(i.getLoginId().equals(loginId1)) {
                found1 = true;
                assertEquals(emailAddress1,i.getEmailAddress());
            }
            if(i.getLoginId().equals(loginId4)) {
                found2 = true;
                assertEquals(emailAddress4,i.getEmailAddress());
            }
        }
        assertTrue(found1&&found2);
    }
}