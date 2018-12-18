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
