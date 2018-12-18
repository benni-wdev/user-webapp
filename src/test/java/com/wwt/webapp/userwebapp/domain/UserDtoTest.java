package com.wwt.webapp.userwebapp.domain;

import com.wwt.webapp.userwebapp.domain.response.UserResponse;
import com.wwt.webapp.userwebapp.util.StaticHelper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.UUID;

public class UserDtoTest {

    @Test
    public void convert() {
        UserDto userDto = new UserDto(UUID.randomUUID().toString());
        UserResponse userResponse = userDto.convert();
        assertEquals(userDto.getUuid(),userResponse.getUuid());

        userDto = new UserDto(UUID.randomUUID().toString(), StaticHelper.getNowAsUtcTimestamp(),1,"test1",
                "test@test.test", StaticHelper.getNowAsUtcTimestamp(), StaticHelper.getNowAsUtcTimestamp(),
                ActivationStatus.ACTIVE, StaticHelper.getNowAsUtcTimestamp(), StaticHelper.getNowAsUtcTimestamp());
        userResponse = userDto.convert();
        assertEquals(userDto.getUuid(),userResponse.getUuid());
        assertEquals(userDto.getEmailAddress(),userResponse.getEmailAddress());
        assertEquals(userDto.getLoginId(),userResponse.getLoginId());
        assertEquals(userDto.getCreatedAt(),userResponse.getCreatedAt());
        assertEquals(userDto.getEmailChangedAt(),userResponse.getEmailChangedAt());
        assertEquals(userDto.getLastLoggedInAt(),userResponse.getLastLoggedInAt());
        assertEquals(userDto.getPasswordChangedAt(),userResponse.getPasswordChangedAt());
        assertEquals(userDto.getVersion(),userResponse.getVersion());
    }
}