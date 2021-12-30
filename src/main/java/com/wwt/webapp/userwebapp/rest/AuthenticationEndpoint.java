package com.wwt.webapp.userwebapp.rest;


import com.wwt.webapp.userwebapp.service.AuthenticationService;
import com.wwt.webapp.userwebapp.service.request.AuthenticationRequest;
import com.wwt.webapp.userwebapp.service.request.InternalRequest;
import com.wwt.webapp.userwebapp.service.request.RefreshAuthenticationRequest;
import com.wwt.webapp.userwebapp.service.response.AuthenticationSuccessResponse;
import com.wwt.webapp.userwebapp.service.response.BasicResponse;
import com.wwt.webapp.userwebapp.service.response.InternalResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


/**
 * @author benw-at-wwt
 */
@RestController
public class AuthenticationEndpoint extends BasicEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationEndpoint.class);

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping(value="/user/authenticate", consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InternalResponse> authenticateUser(@RequestBody AuthenticationRequest authenticationRequest, HttpServletRequest request) {
        evaluateAccessRisk(logger,request, authenticationRequest);
        InternalResponse response = authenticationService.authenticate(authenticationRequest.getLoginId(),
                authenticationRequest.getPassword(),authenticationRequest.isLongSession());
        if (response.isSuccessful()) {
            String cookie = constructCookie(((AuthenticationSuccessResponse) response).getToken(),authenticationRequest.isLongSession(),false);
            logger.info("authenticateUser:"+cookie);
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookie).body(response);
        } else {
            return ResponseEntity.status(getHttpStatusCode(response)).body(response);
        }


    }

    @PostMapping(value="/user/refreshauth", consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InternalResponse> refreshAuthentication(@RequestBody RefreshAuthenticationRequest refreshAuthenticationRequest, HttpServletRequest request) {
        evaluateAccessRisk(logger,request, refreshAuthenticationRequest);
        logger.info( "refreshAuthentication:"+refreshAuthenticationRequest.getRefreshToken() );
        InternalResponse response = authenticationService.refreshAuthentication(refreshAuthenticationRequest.getRefreshToken());
        if (response.isSuccessful()) {
            String cookie = constructCookie(((AuthenticationSuccessResponse) response).getToken(),true,false);
            logger.info("refreshAuthentication:"+cookie);
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookie).body(response);
        } else {
            return ResponseEntity.status(getHttpStatusCode(response)).body(response);
        }

    }

    @PostMapping(value="/user/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InternalResponse> logout(@CookieValue("id_token") Cookie cookie, HttpServletRequest request) {
        evaluateAccessRisk(logger,request, new InternalRequest() { @Override public String toString() { return "LOGOUT"; }});
        if(!isAuthParametersOk(cookie)) { return ResponseEntity.status(302).build();}
        if(!isAuthenticated(cookie.getValue())) { return ResponseEntity.status(getHttpStatusCode( BasicResponse.SESSION_INVALID)).body(BasicResponse.SESSION_INVALID); }
        InternalResponse response = authenticationService.logout(getAuthenticatedUserUuid(cookie.getValue()));
        if (response.isSuccessful()) {
            String deletionCookie = constructCookie("",false,true);
            logger.info("logout:"+deletionCookie);
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,deletionCookie).body(response);
        } else {
            return ResponseEntity.status(getHttpStatusCode(response)).body(response);
        }


    }

}
