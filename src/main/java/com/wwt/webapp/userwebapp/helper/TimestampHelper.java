package com.wwt.webapp.userwebapp.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * @author benw-at-wwt
 */
public final class TimestampHelper {

    private final static Logger logger = LoggerFactory.getLogger( TimestampHelper.class);

    public final static long twentyFourHoursAsMillis = 24 * 60 *60 *1000;
    public final static long oneMinuteAsMillis = 60 *1000;
    public final static long fiveMinutesAsMillis = 5 * 60 *1000;
    public final static long twelveHoursAsMillis = 12 * 60 *60 *1000;
    public final static long twoHoursAsMillis = 2 * 60 *60 *1000;
    public final static long fourHoursAsMillis = 4 * 60 *60 *1000;


    private TimestampHelper() {}

    public static Timestamp getNowAsUtcTimestamp() {
        OffsetDateTime utc = OffsetDateTime.now(ZoneOffset.UTC);
        return Timestamp.from(utc.toInstant());
    }

    public static Instant getNowAsUtcInstant() {
        OffsetDateTime utc = OffsetDateTime.now(ZoneOffset.UTC);
        return utc.toInstant();
    }

    public static long getNowAsUtcEpochMilli() {
        return getNowAsUtcInstant().toEpochMilli();
    }

    public static long getDateAsUtcEpochMilli(String date) {
        return (LocalDate.parse(date).atStartOfDay(ZoneOffset.UTC)).toInstant().toEpochMilli();
    }

    public static long getNowAsUtcEpochSeconds() { return getNowAsUtcInstant().getEpochSecond(); }

    public static Timestamp getNowPlusMinsAsUtcTimestamp(int minsInFuture) {
        return getNowPlusAsUtcTimestamp(minsInFuture*60);
    }

    public static Timestamp getNowPlusAsUtcTimestamp(long secondsInFuture) {
        ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
        utc = utc.plusSeconds(secondsInFuture);
        return Timestamp.from(utc.toInstant());
    }

    public static Instant getNowPlusAsUtcInstant(int minsInFuture) {
        ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
        utc = utc.plusMinutes(minsInFuture);
        return utc.toInstant();
    }

    public static Instant convertIsoStringToTimestamp(String isoTimestamp) {
        TemporalAccessor ta = DateTimeFormatter.ISO_INSTANT.parse(isoTimestamp);
        return Instant.from(ta);
    }

    public static String convertTimeWithTimeZome(long time){
        return Instant.ofEpochMilli(time).toString();
    }

    public static boolean isNearNow(Timestamp timestampToCheck,int offsetSeconds) {
        return (timestampToCheck.before(getNowPlusAsUtcTimestamp((offsetSeconds))) && timestampToCheck.after(getNowPlusAsUtcTimestamp((-offsetSeconds))));
    }

    public static boolean isNearNow(long timestampToCheck,int offsetSeconds) {
        return (timestampToCheck > (getNowAsUtcEpochMilli()-(offsetSeconds*1000))) && (timestampToCheck < (getNowAsUtcEpochMilli()+(offsetSeconds*1000)));
    }

    public static Timestamp convertLongToTimestamp(long time) {
        return new Timestamp(time);
    }
}
