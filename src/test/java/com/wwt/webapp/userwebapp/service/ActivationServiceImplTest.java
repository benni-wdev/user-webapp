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
import com.wwt.webapp.userwebapp.domain.response.InternalResponse;
import com.wwt.webapp.userwebapp.domain.response.MessageCode;
import com.wwt.webapp.userwebapp.security.PasswordHash;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

public class ActivationServiceImplTest extends BaseServiceTest {

    private static final UserStatusChangeToken token1 =  UserStatusChangeTokenImpl.newInstance();
    private static final String loginId1 = "ActivationServiceImplTest1";
    private static final String emailAddress1 = "ActivationServiceImplTest1@test.test";
    private static final PasswordHash pwHash1 = PasswordHash.newInstance("password1");

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

    @BeforeClass
    public static void createData() {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        UserEntity u1 = new UserEntity(loginId1, emailAddress1, pwHash1.getPasswordHash(), token1);
        UserEntity u3 = new UserEntity(loginId3, emailAddress3, pwHash3.getPasswordHash(), token3);
        UserEntity u4 = new UserEntity(loginId4, emailAddress4, pwHash4.getPasswordHash(), token4);
        em.persist(u1);
        em.persist(u3);
        em.persist(u4);
        em.getTransaction().commit();
        em.close();
    }

    @Test
    public void activateUser() {
        UserEntity u = getUserByToken(token1.getToken());
        assertEquals( ActivationStatus.ESTABLISHED,u.getActivationStatus());
        ActivationService as = new ActivationServiceImpl();
        assignDependenyObjects((BaseService)as);
        InternalResponse response = as.activateUser(token1.getToken());
        u = getUserByToken(token1.getToken());
        assertEquals(loginId1,u.getLoginId());
        assertEquals(ActivationStatus.ACTIVE,u.getActivationStatus());
        assertTrue( response.isSuccessful());
        assertEquals( MessageCode.OPERATION_SUCCESSFUL, response.getMessageCode());
    }

    @Test
    public void tokenNotKnown() {
        ActivationService as = new ActivationServiceImpl();
        assignDependenyObjects((BaseService)as);
        InternalResponse response = as.activateUser(token2.getToken());
        assertFalse( response.isSuccessful());
        assertEquals(MessageCode.ACTIVATION_TOKEN_NOT_KNOWN, response.getMessageCode());
    }

    @SuppressWarnings("UnusedAssignment")
    @Test
    public void tokenAlreadyUsed() {
        ActivationService as = new ActivationServiceImpl();
        assignDependenyObjects((BaseService)as);
        InternalResponse response = as.activateUser(token3.getToken());
        assignDependenyObjects((BaseService)as);
        response = as.activateUser(token3.getToken());
        assertFalse( response.isSuccessful());
        assertEquals(MessageCode.ACTIVATION_ALREADY_DONE_OR_NOT_POSSIBLE, response.getMessageCode());
    }

    @Test
    public void tokenAExpired() {
        ActivationService as = new ActivationServiceImpl();
        assignDependenyObjects((BaseService)as);
        InternalResponse response = as.activateUser(token4.getToken());
        assertFalse( response.isSuccessful());
        assertEquals(MessageCode.ACTIVATION_TOKEN_EXPIRED, response.getMessageCode());
    }


    private UserEntity getUserByToken(String token) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        List<UserEntity> userEntities = em.createQuery("select u from UserEntity u where u.activationToken = :activationToken",UserEntity.class)
                .setParameter("activationToken",token)
                .getResultList();
        assertEquals(1,userEntities.size());
        em.getTransaction().commit();
        closeEntityManager(em);
        return userEntities.get(0);
    }

}