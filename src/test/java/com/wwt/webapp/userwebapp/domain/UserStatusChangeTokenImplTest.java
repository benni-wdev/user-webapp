package com.wwt.webapp.userwebapp.domain;

import com.wwt.webapp.userwebapp.util.ConfigProvider;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.Assert.*;

public class UserStatusChangeTokenImplTest {

    @Test
    public void getToken() {
        UserStatusChangeToken token = UserStatusChangeTokenImpl.newInstance();
        assertNotEquals(token.getToken(),UserStatusChangeTokenImpl.newInstance().getToken());
        UserStatusChangeToken token2 = UserStatusChangeTokenImpl.getInstance(token.getToken(),token.getTokenExpiresAt());
        assertEquals(token.getToken(),token2.getToken());
        assertEquals(token.getTokenExpiresAt(),token2.getTokenExpiresAt());
    }

    @Test
    public void getTokenExpiresAt() {
        UserStatusChangeToken token = UserStatusChangeTokenImpl.newInstance();
        assertTrue(token.getTokenExpiresAt().after(getTimestampInFuture(0)));
        assertTrue(token.getTokenExpiresAt().after(getTimestampInFuture(
                Integer.parseInt( ConfigProvider.getConfigValue("minutesUntilTokenExpiry"))-1)));
        assertFalse(token.getTokenExpiresAt().after(getTimestampInFuture(
                Integer.parseInt(ConfigProvider.getConfigValue("minutesUntilTokenExpiry"))+1)));
    }


    private Timestamp getTimestampInFuture(int minutes) {
        ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
        utc = utc.plusMinutes(minutes);
        return Timestamp.from(utc.toInstant());
    }
}