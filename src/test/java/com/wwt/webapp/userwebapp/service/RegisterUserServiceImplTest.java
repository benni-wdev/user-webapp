package com.wwt.webapp.userwebapp.service;

import com.wwt.webapp.userwebapp.domain.ActivationStatus;
import com.wwt.webapp.userwebapp.domain.UserEntity;
import com.wwt.webapp.userwebapp.domain.UserStatusChangeToken;
import com.wwt.webapp.userwebapp.domain.UserStatusChangeTokenImpl;
import com.wwt.webapp.userwebapp.domain.request.RegistrationRequest;
import com.wwt.webapp.userwebapp.domain.response.InternalResponse;
import com.wwt.webapp.userwebapp.domain.response.MessageCode;
import com.wwt.webapp.userwebapp.security.PasswordHash;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

    @BeforeClass
    public static void createData() {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        UserEntity u1 = new UserEntity(loginId1, emailAddress1, pwHash1.getPasswordHash(), token1);
        UserEntity u2 = new UserEntity(loginId2, emailAddress2, pwHash2.getPasswordHash(), token2);
        em.persist(u1);
        em.persist(u2);
        em.getTransaction().commit();
        closeEntityManager(em);
    }

    @Test
    public void registerUser() {
        RegisterUserService rus = new RegisterUserServiceImpl();
        assignDependenyObjects((BaseService)rus);
        InternalResponse internalResponse= rus.registerUser(new RegistrationRequest(loginId3,password3,emailAddress3));
        assertTrue( internalResponse.isSuccessful());
        assertEquals( MessageCode.OPERATION_SUCCESSFUL, internalResponse.getMessageCode());
        UserEntity u = getUserByLoginId(loginId3);
        assertEquals(emailAddress3,u.getEmailAddress());
        assertEquals( ActivationStatus.ESTABLISHED,u.getActivationStatus());
        assertTrue(PasswordHash.getInstance(u.getPasswordHash()).isPasswordHashEquals(password3));
    }

    @Test
    public void emailAlreadyUsed() {
        RegisterUserService rus = new RegisterUserServiceImpl();
        assignDependenyObjects((BaseService)rus);
        InternalResponse internalResponse= rus.registerUser(new RegistrationRequest(loginId4,password3,emailAddress1));
        assertFalse( internalResponse.isSuccessful());
        assertEquals( MessageCode.EMAIL_ADDRESS_ALREADY_EXISTS, internalResponse.getMessageCode());
    }

    @Test
    public void loginIdAlreadyUsed() {
        RegisterUserService rus = new RegisterUserServiceImpl();
        assignDependenyObjects((BaseService)rus);
        InternalResponse internalResponse= rus.registerUser(new RegistrationRequest(loginId2,password3,emailAddress5));
        assertFalse( internalResponse.isSuccessful());
        assertEquals( MessageCode.LOGIN_ID_ALREADY_EXISTS, internalResponse.getMessageCode());
    }
}