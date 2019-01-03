/* Copyright 2018-2019 Wehe Web Technologies
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