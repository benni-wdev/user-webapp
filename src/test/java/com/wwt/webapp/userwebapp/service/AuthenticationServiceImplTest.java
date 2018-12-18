package com.wwt.webapp.userwebapp.service;

import com.wwt.webapp.userwebapp.domain.request.AuthenticatedRequest;
import com.wwt.webapp.userwebapp.domain.response.AuthenticationSuccessResponse;
import com.wwt.webapp.userwebapp.domain.response.InternalResponse;
import com.wwt.webapp.userwebapp.domain.response.MessageCode;
import com.wwt.webapp.userwebapp.security.IdToken;
import com.wwt.webapp.userwebapp.security.PasswordHash;
import com.wwt.webapp.userwebapp.domain.*;
import com.wwt.webapp.userwebapp.util.ConfigProvider;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import java.time.Instant;

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


    private static final UserStatusChangeToken token8 =  UserStatusChangeTokenImpl.newInstance();
    private static final String loginId8 = "AuthenticationServiceImplTest8";
    private static final String emailAddress8 = "AuthenticationServiceImplTest8@test.test";
    private static final String password8 = "password8";
    private static final PasswordHash pwHash8 = PasswordHash.newInstance(password8);

    private static final UserStatusChangeToken token10 =  UserStatusChangeTokenImpl.newInstance();
    private static final String loginId10 = "AuthenticationServiceImplTest10";
    private static final String emailAddress10 = "AuthenticationServiceImplTest10@test.test";
    private static final String password10 = "password10";
    private static final PasswordHash pwHash10 = PasswordHash.newInstance(password10);

    @BeforeClass
    @SuppressWarnings({"unused", "UnusedAssignment"})
    public static void createData() {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        UserEntity u1 = new UserEntity(loginId1, emailAddress1, pwHash1.getPasswordHash(), token1);
        UserEntity u2 = new UserEntity(loginId2, emailAddress2, pwHash2.getPasswordHash(), token2);
        UserEntity u3 = new UserEntity(loginId3, emailAddress3, pwHash3.getPasswordHash(), token3);
        UserEntity u4 = new UserEntity(loginId4, emailAddress4, pwHash4.getPasswordHash(), token4);
        UserEntity u6 = new UserEntity(loginId6, emailAddress6, pwHash6.getPasswordHash(), token6);
        UserEntity u7 = new UserEntity(loginId7, emailAddress7, pwHash7.getPasswordHash(), token7);
        UserEntity u8 = new UserEntity(loginId8, emailAddress8, pwHash8.getPasswordHash(), token8);
        UserEntity u10 = new UserEntity(loginId10, emailAddress10, pwHash10.getPasswordHash(), token10);
        u6.setActivationStatus( ActivationStatus.SUSPENDED );
        u8.setActivationStatus( ActivationStatus.SUSPENDED );
        u10.setActivationStatus( ActivationStatus.ACTIVE );
        em.persist(u1);
        em.persist(u2);
        em.persist(u3);
        em.persist(u4);
        em.persist(u6);
        em.persist(u7);
        em.persist(u8);
        em.persist(u10);
        em.getTransaction().commit();
        closeEntityManager(em);

        ActivationService as = new ActivationServiceImpl();
        assignDependenyObjects((BaseService)as);
        InternalResponse response = as.activateUser(token1.getToken());
        assignDependenyObjects((BaseService)as);
        response = as.activateUser(token2.getToken());
        assignDependenyObjects((BaseService)as);
        response = as.activateUser(token3.getToken());
        assignDependenyObjects((BaseService)as);
        response = as.activateUser(token7.getToken());
    }

    @Test
    public void authenticate() {
        AuthenticationService as = new AuthenticationServiceImpl();
        assignDependenyObjects((BaseService)as);
        InternalResponse internalResponse = as.authenticate(loginId1,password1,false);
        assertTrue( internalResponse.isSuccessful());
        assertEquals(MessageCode.OPERATION_SUCCESSFUL, internalResponse.getMessageCode());
        String token = ((AuthenticationSuccessResponse)internalResponse).getToken();
        IdToken idToken = IdToken.parse(token);
        UserEntity u = getUserByLoginId(loginId1);
        assertEquals(loginId1,u.getLoginId());
        assertEquals(u.getUuid(),idToken.getSubject());
    }

    @Test
    public void passwordWrong() {
        AuthenticationService as = new AuthenticationServiceImpl();
        assignDependenyObjects((BaseService)as);
        InternalResponse internalResponse = as.authenticate(loginId2,password21,false);
        assertFalse( internalResponse.isSuccessful());
        assertEquals(MessageCode.LOGIN_OR_PASSWORD_WRONG, internalResponse.getMessageCode());
    }

    @Test
    public void loginWrong() {
        AuthenticationService as = new AuthenticationServiceImpl();
        assignDependenyObjects((BaseService)as);
        InternalResponse internalResponse = as.authenticate(loginId5,password5,false);
        assertFalse( internalResponse.isSuccessful());
        assertEquals(MessageCode.LOGIN_OR_PASSWORD_WRONG, internalResponse.getMessageCode());
    }

    @Test
    public void wrongTries() {
        AuthenticationService as = new AuthenticationServiceImpl();
        assignDependenyObjects((BaseService)as);
        InternalResponse internalResponse = as.authenticate(loginId3,password31,false);
        assertFalse( internalResponse.isSuccessful());
        assertEquals(MessageCode.LOGIN_OR_PASSWORD_WRONG, internalResponse.getMessageCode());
        assignDependenyObjects((BaseService)as);
        internalResponse = as.authenticate(loginId3,password31,false);
        assertFalse( internalResponse.isSuccessful());
        assertEquals(MessageCode.LOGIN_OR_PASSWORD_WRONG, internalResponse.getMessageCode());
        assignDependenyObjects((BaseService)as);
        internalResponse = as.authenticate(loginId3,password31,false);
        assertFalse( internalResponse.isSuccessful());
        assertEquals(MessageCode.LOGIN_OR_PASSWORD_WRONG, internalResponse.getMessageCode());
        assignDependenyObjects((BaseService)as);
        internalResponse = as.authenticate(loginId3,password31,false);
        assertFalse( internalResponse.isSuccessful());
        assertEquals(MessageCode.LOGIN_OR_PASSWORD_WRONG, internalResponse.getMessageCode());
        assignDependenyObjects((BaseService)as);
        internalResponse = as.authenticate(loginId3,password31,false);
        assertFalse( internalResponse.isSuccessful());
        assertEquals(MessageCode.TOO_MANY_FAILED_LOGINS, internalResponse.getMessageCode());
        UserEntity u = getUserByLoginId(loginId3);
        assertEquals(ActivationStatus.SUSPENDED,u.getActivationStatus());
    }

    @Test
    public void userNotActive() {
        AuthenticationService as = new AuthenticationServiceImpl();
        assignDependenyObjects((BaseService)as);
        InternalResponse internalResponse = as.authenticate(loginId4,password4,false);
        assertFalse( internalResponse.isSuccessful());
        assertEquals(MessageCode.USER_NOT_ACTIVE, internalResponse.getMessageCode());
        assignDependenyObjects((BaseService)as);
        internalResponse = as.authenticate(loginId6,password6,false);
        assertFalse( internalResponse.isSuccessful());
        assertEquals(MessageCode.USER_NOT_ACTIVE, internalResponse.getMessageCode());
    }


    @Test
    public void authenticateLong() {
        AuthenticationService as = new AuthenticationServiceImpl();
        assignDependenyObjects((BaseService)as);
        InternalResponse internalResponse = as.authenticate(loginId1,password1,true);
        assertTrue( internalResponse.isSuccessful());
        assertEquals(MessageCode.OPERATION_SUCCESSFUL, internalResponse.getMessageCode());
        String token = ((AuthenticationSuccessResponse)internalResponse).getToken();
        IdToken idToken = IdToken.parse(token);
        Instant compare1 = Instant.now().plusSeconds( ConfigProvider.getConfigIntValue("ttl")*2);
        Instant compare2 = Instant.now().plusSeconds( ConfigProvider.getConfigIntValue("ttlLong")*2);
        assertTrue(idToken.getExpiresAt().isAfter(compare1)
                && idToken.getExpiresAt().isBefore(compare2));
        UserEntity u = getUserByLoginId(loginId1);
        assertEquals(loginId1,u.getLoginId());
        assertEquals(u.getUuid(),idToken.getSubject());
    }


}