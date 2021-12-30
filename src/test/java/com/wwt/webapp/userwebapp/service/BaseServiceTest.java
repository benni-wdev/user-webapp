package com.wwt.webapp.userwebapp.service;

import com.wwt.webapp.userwebapp.domain.relational.UserRepository;
import com.wwt.webapp.userwebapp.domain.relational.entity.UserEntity;
import com.wwt.webapp.userwebapp.security.TestContextSetter;

import java.util.Optional;

import static org.junit.Assert.assertTrue;

/**
 * @author benw-at-wwt
 */
@SuppressWarnings("WeakerAccess")
public abstract class BaseServiceTest {

    static void assignDependenyObjects(BaseUserService baseUserService) {
        TestContextSetter.setTestContext();
    }

    protected String convertToUuid(UserRepository userRepository, String loginId) {
        Optional<UserEntity> userOpt = userRepository.getUserByLoginId(loginId);
        assertTrue(userOpt.isPresent());
        return userOpt.get().getUuid();
    }

}
