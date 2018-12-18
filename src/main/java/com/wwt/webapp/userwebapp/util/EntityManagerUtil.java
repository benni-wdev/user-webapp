package com.wwt.webapp.userwebapp.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


/**
 * @author benw-at-wwt
 *
 */

public class EntityManagerUtil {

    private static final EntityManagerFactory entityManagerFactory;

    static {
        entityManagerFactory = Persistence.createEntityManagerFactory("APP_DS");

    }

    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
}
