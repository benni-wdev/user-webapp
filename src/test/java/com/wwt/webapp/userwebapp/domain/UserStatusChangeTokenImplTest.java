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