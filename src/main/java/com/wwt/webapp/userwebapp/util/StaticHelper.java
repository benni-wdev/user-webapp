package com.wwt.webapp.userwebapp.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * @author benw-at-wwt
 */
public final class StaticHelper {



    private StaticHelper() {}

    public static Timestamp getNowAsUtcTimestamp() {
        OffsetDateTime utc = OffsetDateTime.now(ZoneOffset.UTC);
        return Timestamp.from(utc.toInstant());
    }

    public static Instant getNowAsUtcInstant() {
        OffsetDateTime utc = OffsetDateTime.now(ZoneOffset.UTC);
        return utc.toInstant();
    }
}
