package com.wwt.webapp.userwebapp.util;

import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class UserHelperTest {

    @Test
    public void getNowAsUtcTimestamp() {
        ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
        utc = utc.plusMinutes(-1);
        ZonedDateTime utc2 = utc.plusMinutes(2);
        assertTrue((Timestamp.from(utc.toInstant())).before(StaticHelper.getNowAsUtcTimestamp()));
        assertTrue((Timestamp.from(utc2.toInstant())).after(StaticHelper.getNowAsUtcTimestamp()));
    }

    @Test
    public void getNowAsUtcInstant() {
        ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
        utc = utc.plusMinutes(-1);
        ZonedDateTime utc2 = utc.plusMinutes(2);
        assertTrue(utc.toInstant().isBefore(StaticHelper.getNowAsUtcInstant()));
        assertTrue(utc2.toInstant().isAfter(StaticHelper.getNowAsUtcInstant()));
    }
}