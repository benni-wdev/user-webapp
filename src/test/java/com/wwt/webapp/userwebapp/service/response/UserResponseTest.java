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