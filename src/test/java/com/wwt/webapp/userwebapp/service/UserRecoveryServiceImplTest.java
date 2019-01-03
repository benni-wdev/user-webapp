/* Copyright 2018-2019 Wehe Web Technologies
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
import com.wwt.webapp.userwebapp.domain.UserEntity;
import com.wwt.webapp.userwebapp.domain.UserStatusChangeToken;
import com.wwt.webapp.userwebapp.domain.UserStatusChangeTokenImpl;
import com.wwt.webapp.userwebapp.domain.request.ExecuteRecoveryRequest;
import com.wwt.webapp.userwebapp.domain.response.InternalResponse;
import com.wwt.webapp.userwebapp.domain.response.MessageCode;
import com.wwt.webapp.userwebapp.security.PasswordHash;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;

import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserRecoveryServiceImplTest extends BaseServiceTest{

    private final static Logger logger = Logger.getLogger(UserRecoveryServiceImplTest.class);

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
    private static final UserStatusChangeToken token41 =  UserStatusChangeTokenImpl.newInstance();
    private static final String loginId4 = "UserRecoveryServiceImplTest4";
    private static final String emailAddress4 = "UserRecoveryServiceImplTest4@test.test";
    private static final PasswordHash pwHash4 = PasswordHash.newInstance("password4");


    @BeforeClass
    public static void createData() {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        UserEntity u1 = new UserEntity(loginId1, emailAddress1, pwHash1.getPasswordHash(), token1);
        UserEntity u2 = new UserEntity(loginId2, emailAddress2, pwHash2.getPasswordHash(), token2);
        UserEntity u4 = new UserEntity(loginId4, emailAddress4, pwHash4.getPasswordHash(), token41);
        u1.setActivationStatus( ActivationStatus.ACTIVE);
        u2.setActivationStatus(ActivationStatus.SUSPENDED);
        u2.setPasswordRecoveryToken(pwToken2);
        u4.setActivationStatus(ActivationStatus.SUSPENDED);
        u4.setPasswordRecoveryToken(token4);
        em.persist(u1);
        em.persist(u2);
        em.persist(u4);
        em.getTransaction().commit();
        closeEntityManager(em);
    }

    @Test
    public void lockUserRecovery() {
        UserRecoveryService urs = new UserRecoveryServiceImpl();
        assignDependenyObjects((BaseService)urs);
        InternalResponse internalResponse = urs.initiateRecovery(emailAddress1);
        assertTrue( internalResponse.isSuccessful());
        assertEquals( MessageCode.OPERATION_SUCCESSFUL, internalResponse.getMessageCode());
        UserEntity u = getUserByLoginId(loginId1);
        assertEquals(emailAddress1,u.getEmailAddress());
        assertEquals(ActivationStatus.SUSPENDED,u.getActivationStatus());
        assignDependenyObjects((BaseService)urs);
         internalResponse = urs.initiateRecovery(emailAddress1);
        assertTrue( internalResponse.isSuccessful());
        assertEquals( MessageCode.OPERATION_SUCCESSFUL, internalResponse.getMessageCode());
        u = getUserByLoginId(loginId1);
        assertEquals(emailAddress1,u.getEmailAddress());
        assertEquals(ActivationStatus.SUSPENDED,u.getActivationStatus());
    }

    @Test
    public void recoverUser() {
        UserRecoveryService urs = new UserRecoveryServiceImpl();
        assignDependenyObjects((BaseService)urs);
        InternalResponse internalResponse = urs.recoverUser(new ExecuteRecoveryRequest(pwToken2.getToken(),password21));
        logger.fatal(internalResponse);
        assertTrue(internalResponse.isSuccessful());
        assertEquals(MessageCode.OPERATION_SUCCESSFUL, internalResponse.getMessageCode());
        UserEntity u = getUserByLoginId(loginId2);
        assertEquals(emailAddress2,u.getEmailAddress());
        assertEquals(ActivationStatus.ACTIVE,u.getActivationStatus());
        assertTrue(PasswordHash.getInstance(u.getPasswordHash()).isPasswordHashEquals(password21));
        assignDependenyObjects((BaseService)urs);
        internalResponse = urs.recoverUser(new ExecuteRecoveryRequest(pwToken2.getToken(),password22));
        assertFalse( internalResponse.isSuccessful());
        assertEquals( MessageCode.RECOVERY_ALREADY_DONE_OR_NOT_POSSIBLE, internalResponse.getMessageCode());
        u = getUserByLoginId(loginId2);
        assertEquals(emailAddress2,u.getEmailAddress());
        assertEquals(ActivationStatus.ACTIVE,u.getActivationStatus());
        assertTrue(PasswordHash.getInstance(u.getPasswordHash()).isPasswordHashEquals(password21));
    }

    @Test
    public void recoverUserNotKnown() {
        UserRecoveryService urs = new UserRecoveryServiceImpl();
        assignDependenyObjects((BaseService)urs);
        InternalResponse internalResponse = urs.recoverUser(new ExecuteRecoveryRequest(token3.getToken(),password21));
        assertFalse( internalResponse.isSuccessful());
        assertEquals( MessageCode.PASSWORD_TOKEN_NOT_KNOWN, internalResponse.getMessageCode());
    }

    @Test
    public void tokenExpired() {
        UserRecoveryService urs = new UserRecoveryServiceImpl();
        assignDependenyObjects((BaseService)urs);
        InternalResponse internalResponse = urs.recoverUser(new ExecuteRecoveryRequest(token4.getToken(),password21));
        assertFalse( internalResponse.isSuccessful());
        assertEquals( MessageCode.PASSWORD_RECOVERY_TOKEN_EXPIRED, internalResponse.getMessageCode());
    }
}