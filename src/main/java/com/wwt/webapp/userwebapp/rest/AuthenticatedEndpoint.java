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
package com.wwt.webapp.userwebapp.rest;

import com.wwt.webapp.userwebapp.util.ConfigProvider;
import org.apache.log4j.Logger;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;

/**
 * @author benw-at-wwt
 */
abstract class AuthenticatedEndpoint  extends BasicEndpoint {

    private static final Logger logger = Logger.getLogger(AuthenticatedEndpoint.class);
    private static final String cookieName = "id_token";


    String constructCookie(String value, boolean isLongSession, boolean isInvalidation) {
        NewCookie cookie;
        if(isInvalidation) {
            cookie = new NewCookie(cookieName, value, "/", "",  "",
                    0, true, true);
        }
        else if(!isLongSession) {
            cookie = new NewCookie(cookieName, value, "/", "",  "",
                    -1, true, true);
        }
        else {
            cookie = new NewCookie(cookieName, value, "/", "", "", ConfigProvider.getConfigIntValue("ttl"),  true, true);
        }
        return(cookie.toString()+";SameSite=Strict");
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean isAuthParametersOk(Cookie cookie) {
        if(cookie == null || cookie.getValue().equals("")) {
            logger.info( "cookie not shaped well:" + cookie );
            return false;
        }
        else {
            logger.info( "cookie shaped well:" + cookie );
            return true;
        }

    }
}
