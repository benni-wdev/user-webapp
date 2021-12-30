/* Copyright 2018-2021 Wehe Web Technologies
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
