package com.wwt.webapp.userwebapp.service;

import com.wwt.webapp.userwebapp.BaseTest;
import com.wwt.webapp.userwebapp.domain.UserEntity;
import org.junit.AfterClass;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author benw-at-wwt
 */
@SuppressWarnings("WeakerAccess")
public abstract class BaseServiceTest extends BaseTest {

    static void assignDependenyObjects(BaseService baseService) {
        baseService.mailProcessor = (emailType, emailAddress, token) -> true;
    }

    UserEntity getUserByLoginId(String loginId) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        List<UserEntity> userEntities = em.createQuery("select u from UserEntity u where u.loginId = :loginId1",UserEntity.class)
                .setParameter("loginId1",loginId)
                .getResultList();
        assertEquals(1,userEntities.size());
        em.getTransaction().commit();
        closeEntityManager(em);
        return userEntities.get(0);
    }

    @AfterClass
    public static void destroyData() {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.createQuery("delete from UserEntity u").executeUpdate();
        em.getTransaction().commit();
        closeEntityManager(em);
    }
}
