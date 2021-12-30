/* Copyright 2018-2021 Wehe Web Technologies
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

import com.wwt.webapp.userwebapp.domain.AdminRole;
import com.wwt.webapp.userwebapp.helper.TimestampHelper;
import com.wwt.webapp.userwebapp.security.IdToken;
import com.wwt.webapp.userwebapp.service.request.InternalRequest;
import com.wwt.webapp.userwebapp.service.response.BasicResponse;
import com.wwt.webapp.userwebapp.service.response.InternalResponse;
import com.wwt.webapp.userwebapp.service.response.MessageCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.util.Calendar;

import static com.wwt.webapp.userwebapp.helper.ConfigProvider.getConfigIntValue;
import static com.wwt.webapp.userwebapp.helper.ConfigProvider.getConfigValue;

@RequestMapping("/rest")
public abstract class BasicEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(BasicEndpoint.class);
    private static final String cookieName = "id_token";

    @ExceptionHandler(value= { MissingRequestCookieException.class })
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public ResponseEntity<InternalResponse> handleMissingRequestCookieException(MissingRequestCookieException exception) {
        return renderResponse( BasicResponse.SESSION_INVALID);
    }



    protected int getHttpStatusCode(InternalResponse response) {
        if(response.getMessageCode().equals( MessageCode.LOGIN_ID_ALREADY_EXISTS) ||
                response.getMessageCode().equals( MessageCode.EMAIL_ADDRESS_ALREADY_EXISTS) ||
                response.getMessageCode().equals( MessageCode.RECOVERY_ALREADY_DONE_OR_NOT_POSSIBLE) ||
                response.getMessageCode().equals( MessageCode.NO_CHANGE_NO_UPDATE) ||
                response.getMessageCode().equals( MessageCode.EMAIL_ADDRESS_NOT_VALID) ||
                response.getMessageCode().equals( MessageCode.LOGIN_ID_NOT_VALID) ||
                response.getMessageCode().equals( MessageCode.INPUT_NOT_VALID) ||
                response.getMessageCode().equals( MessageCode.ACTIVATION_ALREADY_DONE_OR_NOT_POSSIBLE))
            return Response.Status.BAD_REQUEST.getStatusCode();
        else if(response.getMessageCode().equals( MessageCode.PASSWORD_RECOVERY_TOKEN_EXPIRED) ||
                response.getMessageCode().equals( MessageCode.ACTIVATION_TOKEN_EXPIRED))
            return Response.Status.UNAUTHORIZED.getStatusCode();
        else if( response.getMessageCode().equals( MessageCode.SESSION_INVALID) ||
                response.getMessageCode().equals( MessageCode.SESSION_EXPIRED) )
            return Response.Status.SEE_OTHER.getStatusCode();
        else if(response.getMessageCode().equals( MessageCode.FORBIDDEN) )
            return Response.Status.FORBIDDEN.getStatusCode();
        else if(response.getMessageCode().equals( MessageCode.LOGIN_OR_PASSWORD_WRONG) ||
                response.getMessageCode().equals( MessageCode.ACTIVATION_TOKEN_NOT_KNOWN) ||
                response.getMessageCode().equals( MessageCode.PASSWORD_TOKEN_NOT_KNOWN) ||
                response.getMessageCode().equals( MessageCode.ITEM_NOT_KNOWN) )
            return Response.Status.NOT_FOUND.getStatusCode();
        else if(response.getMessageCode().equals( MessageCode.CONCURRENT_MODIFICATION))
            return Response.Status.CONFLICT.getStatusCode();
        else if(response.getMessageCode().equals( MessageCode.USER_NOT_ACTIVE) ||
                response.getMessageCode().equals( MessageCode.TOO_MANY_FAILED_LOGINS) )
            return 423;
        else {
            return Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        }
    }

    protected ResponseEntity<InternalResponse> renderResponse(InternalResponse response) {
        if (response.isSuccessful()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(getHttpStatusCode(response)).body(response);
        }
    }

    protected void evaluateAccessRisk(Logger logger, HttpServletRequest httpRequest, InternalRequest internalRequest) {
        //Here we can evaluate risk based on known IP addresses (or everything what can be derived from it),
        // and for example force step up but needs additional infrastructure
        logger.info("Request received:"+internalRequest.toString());
        logger.error( "ACCESS->" + httpRequest.getRemoteAddr() + " " + internalRequest);
    }

    protected boolean isAuthParametersOk(Cookie cookie) {
        if(cookie == null || cookie.getValue().equals("")) {
            logger.info( "cookie not shaped well:"+cookie );
            return false;
        }
        else {
            // logger.info( "cookie shaped well:" + cookie );
            return true;
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    protected boolean isAuthenticated(String idToken) {
        if(idToken == null) {
            logger.warn("token not present");
            return false;
        }
        else {
            try {
                IdToken tokenInfo = IdToken.parse(idToken);
                //logger.info(tokenInfo.toString());
                if (tokenInfo.getIssuer().equals(getConfigValue("appName"))) {
                    if (tokenInfo.getExpiresAt().isAfter( TimestampHelper.getNowAsUtcInstant())) {
                        logger.info("Token valid");
                        return true;
                    } else {
                        logger.info("Token expired");
                        return false;
                    }
                } else {
                    logger.error("Token invalid!!!");
                    return false;
                }
            } catch (ExpiredJwtException e1) {
                logger.info("Token expired "+idToken);
                return false;
            } catch (SignatureException | MalformedJwtException | IllegalArgumentException e2) {
                logger.error("Token invalid!!! "+idToken);
                return false;
            }
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    protected boolean isAuthorized(String idToken, AdminRole requiredRole) {
        IdToken tokenInfo = IdToken.parse(idToken);
        AdminRole userRole;
        try {
            userRole = AdminRole.valueOf( tokenInfo.getAdminRole() );
        }
        catch(IllegalArgumentException e) {
            logger.error( "Admin Role Conversion",e );
            userRole = AdminRole.NO_ROLE;
        }
        logger.info( "user has role " + userRole + " required is " + requiredRole );
        if (requiredRole.equals( userRole )) return true;
        if (requiredRole.equals( AdminRole.NO_ROLE )) return true;
        return userRole.equals( AdminRole.FULL_ADMIN );
    }


    protected String getAuthenticatedUserUuid(String idToken) {
        IdToken tokenInfo = IdToken.parse(idToken);
        return tokenInfo.getSubject();
    }



    public static String constructCookie(String value, boolean isLongSession, boolean isInvalidation) {
        NewCookie cookie;
        if(isInvalidation) {
            cookie = new NewCookie(cookieName, value, "/", null,  "",
                    0, true,true);
        }
        else if(!isLongSession) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.SECOND, getConfigIntValue("ttl"));
            cookie = new NewCookie(cookieName, value, "/", null,1, "",
                    -1,cal.getTime(), true,true);
        }
        else {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.SECOND, getConfigIntValue("ttlLong"));
            cookie = new NewCookie(cookieName, value, "/", null,1, "",
                    getConfigIntValue("ttlLong"),cal.getTime(), true,true);
        }
        return(cookie+";SameSite=Lax");
    }
}
