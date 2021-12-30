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
package com.wwt.webapp.userwebapp.service.response;

import com.wwt.webapp.userwebapp.domain.relational.entity.UserEntity;
import com.wwt.webapp.userwebapp.helper.TimestampHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserResponseTest {

    @Test
    public void convert() {
        UserEntity userEntity = new UserEntity("test1","test@test.test","test1","test2", TimestampHelper.getNowAsUtcTimestamp());
        UserResponse userResponse = UserResponse.convertToUserResponse(userEntity);
        assertEquals(userEntity.getUuid(),userResponse.getUuid());
        assertEquals(userEntity.getUuid(),userResponse.getUuid());
        assertEquals(userEntity.getEmailAddress(),userResponse.getEmailAddress());
        assertEquals(userEntity.getLoginId(),userResponse.getLoginId());
        assertEquals(userEntity.getCreatedAt(),userResponse.getCreatedAt());
        assertEquals(userEntity.getEmailChangedAt(),userResponse.getEmailChangedAt());
        assertEquals(userEntity.getLastLoggedInAt(),userResponse.getLastLoggedInAt());
        assertEquals(userEntity.getPasswordChangedAt(),userResponse.getPasswordChangedAt());
        assertEquals(userEntity.getVersion(),userResponse.getVersion());
    }
}