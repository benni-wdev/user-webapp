package com.wwt.webapp.userwebapp.service;

import com.wwt.webapp.userwebapp.domain.response.AuthenticationSuccessResponse;
import com.wwt.webapp.userwebapp.domain.response.InternalResponse;
import com.wwt.webapp.userwebapp.domain.response.MessageCode;
import com.wwt.webapp.userwebapp.domain.response.UserResponse;
import com.wwt.webapp.userwebapp.security.PasswordHash;
import com.wwt.webapp.userwebapp.domain.*;
import com.wwt.webapp.userwebapp.domain.request.ArchiveRequest;
import com.wwt.webapp.userwebapp.domain.request.AuthenticatedRequest;
import com.wwt.webapp.userwebapp.domain.request.EmailChangeRequest;
import com.wwt.webapp.userwebapp.domain.request.PasswordChangeRequest;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("unused")
public class UserServiceImplTest extends BaseServiceTest {

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
    private static String emailAddres71 = "UserServiceImplTest71@test.test";
    private static final String password7 = "password7";
    private static final String password71 = "password71";
    private static final PasswordHash pwHash7 = PasswordHash.newInstance(password7);

    //not used
    private static final UserStatusChangeToken token8 =  UserStatusChangeTokenImpl.newInstance();
    private static final String loginId8 = "UserServiceImplTest8";
    private static final String emailAddress8 = "UserServiceImplTest8@test.test";
    private static final String password8 = "password8";
    private static final PasswordHash pwHash8 = PasswordHash.newInstance(password7);



    @BeforeClass
    public static void createData() {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        UserEntity u1 = new UserEntity(loginId1, emailAddress1, pwHash1.getPasswordHash(), token1);
        UserEntity u2 = new UserEntity(loginId2, emailAddress2, pwHash2.getPasswordHash(), token2);
        UserEntity u3 = new UserEntity(loginId3, emailAddress3, pwHash3.getPasswordHash(), token3);
        UserEntity u4 = new UserEntity(loginId4, emailAddress4, pwHash4.getPasswordHash(), token4);
        UserEntity u5 = new UserEntity(loginId5, emailAddress5, pwHash5.getPasswordHash(), token5);
        UserEntity u6 = new UserEntity(loginId6, emailAddress6, pwHash6.getPasswordHash(), token6);
        UserEntity u7 = new UserEntity(loginId7, emailAddress7, pwHash7.getPasswordHash(), token7);
        UserEntity u8 = new UserEntity(loginId8, emailAddress8, pwHash8.getPasswordHash(), token8);
        u1.setActivationStatus( ActivationStatus.ACTIVE);
        u2.setActivationStatus(ActivationStatus.ACTIVE);
        u2.setPasswordRecoveryToken(pwToken2);
        u3.setActivationStatus(ActivationStatus.ACTIVE);
        u4.setActivationStatus(ActivationStatus.ACTIVE);
        u5.setActivationStatus(ActivationStatus.ACTIVE);
        u6.setActivationStatus(ActivationStatus.ACTIVE);
        u7.setActivationStatus(ActivationStatus.ACTIVE);
        u8.setActivationStatus(ActivationStatus.ACTIVE);
        em.persist(u1);
        em.persist(u2);
        em.persist(u3);
        em.persist(u4);
        em.persist(u5);
        em.persist(u6);
        em.persist(u7);
        em.persist(u8);
        em.getTransaction().commit();
        closeEntityManager(em);
    }


    @Test
    public void readUser() {
        UserService us = new UserServiceImpl();
        assignDependenyObjects((BaseService)us);
        AuthenticationSuccessResponse auth = getToken(loginId1,password1);
        AuthenticatedRequest ar = new AuthenticatedRequest();
        ar.setIdToken(auth.getToken());
        UserResponse user = (UserResponse)us.readUser(ar);
        assertTrue( user.isSuccessful());
        assertEquals( MessageCode.OPERATION_SUCCESSFUL, user.getMessageCode());
        assertEquals(emailAddress1,user.getEmailAddress());
        assertEquals(loginId1,user.getLoginId());
        assertEquals(4,user.getVersion());
    }


    @Test
    public void changePassword() {
        UserService us = new UserServiceImpl();
        assignDependenyObjects((BaseService)us);
        AuthenticationSuccessResponse auth = getToken(loginId2,password2);
        AuthenticatedRequest ar1 = new AuthenticatedRequest();
        ar1.setIdToken( auth.getToken() );
        UserResponse user = (UserResponse)us.readUser(ar1);
        PasswordChangeRequest ar2 = new PasswordChangeRequest(password2,password21);
        ar2.setIdToken(auth.getToken());
        assertTrue( user.isSuccessful());

        assignDependenyObjects((BaseService)us);
        InternalResponse internalResponse = us.changePassword(ar2);
        assertTrue( internalResponse.isSuccessful());
        assertEquals( MessageCode.OPERATION_SUCCESSFUL, internalResponse.getMessageCode());
        UserEntity u = getUserByLoginId(loginId2);
        assertEquals(emailAddress2,u.getEmailAddress());
        assertEquals(ActivationStatus.ACTIVE,u.getActivationStatus());
        assertTrue(PasswordHash.getInstance(u.getPasswordHash()).isPasswordHashEquals(password21));
    }

    @Test
    public void changePasswordFail() {
        UserService us = new UserServiceImpl();
        assignDependenyObjects((BaseService)us);
        AuthenticationSuccessResponse auth = getToken(loginId7,password7);
        AuthenticatedRequest ar1 = new AuthenticatedRequest();
        ar1.setIdToken( auth.getToken() );
        UserResponse user = (UserResponse)us.readUser(ar1);
        PasswordChangeRequest ar2 = new PasswordChangeRequest(password71,password7);
        ar2.setIdToken( auth.getToken() );
        assertTrue( user.isSuccessful());

        assignDependenyObjects((BaseService)us);
        InternalResponse internalResponse = us.changePassword(ar2);
        assertFalse( internalResponse.isSuccessful());
        assertEquals( MessageCode.LOGIN_OR_PASSWORD_WRONG, internalResponse.getMessageCode());
        UserEntity u = getUserByLoginId(loginId7);
        assertEquals(emailAddress7,u.getEmailAddress());
        assertEquals(ActivationStatus.ACTIVE,u.getActivationStatus());
        assertTrue(PasswordHash.getInstance(u.getPasswordHash()).isPasswordHashEquals(password7));
    }

    @Test
    public void emailUniqueFail() {
        UserService us = new UserServiceImpl();
        assignDependenyObjects((BaseService)us);
        AuthenticationSuccessResponse auth = getToken(loginId7,password7);
        AuthenticatedRequest ar1 = new AuthenticatedRequest();
        ar1.setIdToken( auth.getToken() );
        UserResponse user = (UserResponse)us.readUser(ar1);
        EmailChangeRequest ar2 = new EmailChangeRequest(emailAddress1 );
        ar2.setIdToken( auth.getToken() );
        assertTrue( user.isSuccessful());

        assignDependenyObjects((BaseService)us);
        InternalResponse internalResponse = us.changeEmail(ar2);
        assertFalse( internalResponse.isSuccessful());
        assertEquals( MessageCode.EMAIL_ADDRESS_ALREADY_EXISTS, internalResponse.getMessageCode());
        UserEntity u = getUserByLoginId(loginId7);
        assertEquals(emailAddress7,u.getEmailAddress());
        assertEquals(ActivationStatus.ACTIVE,u.getActivationStatus());
        assertTrue(PasswordHash.getInstance(u.getPasswordHash()).isPasswordHashEquals(password7));
    }

    @Test
    public void changeEmail() {
        UserService us = new UserServiceImpl();
        assignDependenyObjects((BaseService)us);
        AuthenticationSuccessResponse auth = getToken(loginId3,password3);
        EmailChangeRequest ar2 = new EmailChangeRequest(emailAddress31 );
        ar2.setIdToken(auth.getToken());
        assignDependenyObjects((BaseService)us);
        InternalResponse internalResponse = us.changeEmail(ar2);

        assertTrue( internalResponse.isSuccessful());
        assertEquals( MessageCode.OPERATION_SUCCESSFUL, internalResponse.getMessageCode());
        UserEntity u = getUserByLoginId(loginId3);
        assertEquals(emailAddress31,u.getEmailAddress());
        assertEquals(ActivationStatus.ESTABLISHED,u.getActivationStatus());
        assertTrue(PasswordHash.getInstance(u.getPasswordHash()).isPasswordHashEquals(password3));
    }

    @Test
    public void archiveUser() {
        UserService us = new UserServiceImpl();
        assignDependenyObjects((BaseService)us);
        AuthenticationSuccessResponse auth = getToken(loginId4,password4);
        ArchiveRequest ar2 = new ArchiveRequest(password4 );
        ar2.setIdToken(auth.getToken());
        assignDependenyObjects((BaseService)us);
        InternalResponse internalResponse = us.archiveUser(ar2);
        assertTrue( internalResponse.isSuccessful());
        assertEquals( MessageCode.OPERATION_SUCCESSFUL, internalResponse.getMessageCode());
        UserEntity u = getUserByLoginId(loginId4);
        assertEquals(ActivationStatus.ARCHIVED,u.getActivationStatus());
    }

    @Test
    public void authenticationFailed() {
        UserService us = new UserServiceImpl();
        AuthenticatedRequest ar = new AuthenticatedRequest();
        ar.setIdToken("dkasdksakdök");
        assignDependenyObjects((BaseService)us);
        InternalResponse internalResponse = us.readUser(ar);
        assertFalse( internalResponse.isSuccessful());
        assertEquals( MessageCode.SESSION_INVALID, internalResponse.getMessageCode());
        ar.setIdToken(null);
        assignDependenyObjects((BaseService)us);
        internalResponse = us.readUser(ar);
        assertFalse( internalResponse.isSuccessful());
        assertEquals( MessageCode.SESSION_EXPIRED, internalResponse.getMessageCode());
        AuthenticationSuccessResponse auth = getToken(loginId1,password1);
        ar.setIdToken("a"+auth.getToken());
        assignDependenyObjects((BaseService)us);
        internalResponse = us.readUser(ar);
        assertFalse( internalResponse.isSuccessful());
        assertEquals( MessageCode.SESSION_INVALID, internalResponse.getMessageCode());
    }


    @Ignore
    public void authorizationFailed() {
        UserService us = new UserServiceImpl();
        assignDependenyObjects((BaseService)us);
        AuthenticationSuccessResponse auth = getToken(loginId5,password5);
        AuthenticatedRequest ar1 = new AuthenticatedRequest();
        ar1.setIdToken( auth.getToken() );
        UserResponse user = (UserResponse)us.readUser(ar1);
        assertTrue( user.isSuccessful());
        EmailChangeRequest ar2 = new EmailChangeRequest(emailAddress61 );
        ar2.setIdToken( auth.getToken() );
        assignDependenyObjects((BaseService)us);
        InternalResponse internalResponse = us.changeEmail(ar2);
        assertFalse( internalResponse.isSuccessful());
        assertEquals( MessageCode.FORBIDDEN, internalResponse.getMessageCode());
        PasswordChangeRequest ar3 = new PasswordChangeRequest(password6,password61);
        ar3.setIdToken( auth.getToken() );
        assignDependenyObjects((BaseService)us);
        internalResponse = us.changeEmail(ar2);
        assertFalse( internalResponse.isSuccessful());
        assertEquals( MessageCode.FORBIDDEN, internalResponse.getMessageCode());
        ArchiveRequest ar4 = new ArchiveRequest(password6 );
        ar4.setIdToken( auth.getToken() );
        assignDependenyObjects((BaseService)us);
        internalResponse = us.archiveUser(ar4);
        assertFalse( internalResponse.isSuccessful());
        assertEquals( MessageCode.FORBIDDEN, internalResponse.getMessageCode());

    }

    @Ignore
    public void concurrentUpdateCheck() {
        UserService us = new UserServiceImpl();
        assignDependenyObjects((BaseService)us);
        AuthenticationSuccessResponse auth = getToken(loginId6,password6);
        AuthenticatedRequest ar1 = new AuthenticatedRequest();
        ar1.setIdToken( auth.getToken() );
        UserResponse user = (UserResponse)us.readUser(ar1);
        assertTrue( user.isSuccessful());
        EmailChangeRequest ar2 = new EmailChangeRequest(emailAddress61 );
        ar2.setIdToken( auth.getToken() );
        assignDependenyObjects((BaseService)us);
        InternalResponse internalResponse = us.changeEmail(ar2);
        assertFalse( internalResponse.isSuccessful());
        assertEquals( MessageCode.CONCURRENT_MODIFICATION, internalResponse.getMessageCode());
        PasswordChangeRequest ar3 = new PasswordChangeRequest(password6,password61);
        ar3.setIdToken( auth.getToken() );
        assignDependenyObjects((BaseService)us);
        internalResponse = us.changeEmail(ar2);
        assertFalse( internalResponse.isSuccessful());
        assertEquals( MessageCode.CONCURRENT_MODIFICATION, internalResponse.getMessageCode());
        ArchiveRequest ar4 = new ArchiveRequest(password6 );
        ar4.setIdToken( auth.getToken() );
        assignDependenyObjects((BaseService)us);
        internalResponse = us.archiveUser(ar4);
        assertFalse( internalResponse.isSuccessful());
        assertEquals( MessageCode.CONCURRENT_MODIFICATION, internalResponse.getMessageCode());

    }

    private AuthenticationSuccessResponse getToken(String loginId, String password) {
        AuthenticationService as = new AuthenticationServiceImpl();
        assignDependenyObjects((BaseService)as);
        return (AuthenticationSuccessResponse) as.authenticate(loginId,password,false);
    }

}