package com.wwt.webapp.userwebapp;


import com.wwt.webapp.userwebapp.util.EntityManagerUtil;

import javax.persistence.EntityManager;


/**
 * @author benw-at-wwt
 */
public abstract class BaseTest {

    protected static EntityManager getEntityManager() {
        return EntityManagerUtil.getEntityManager();
    }

    protected static void closeEntityManager(EntityManager em) {
        if (em != null) {
            em.close();
        }
    }


}

