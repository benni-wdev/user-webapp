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
import com.wwt.webapp.userwebapp.service.UserService;
import com.wwt.webapp.userwebapp.service.request.InternalRequest;
import com.wwt.webapp.userwebapp.service.request.UserChangeRequest;
import com.wwt.webapp.userwebapp.service.response.BasicResponse;
import com.wwt.webapp.userwebapp.service.response.InternalResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class AdminUserEndpoint extends BasicEndpoint {

    private static final Logger logger = LoggerFactory.getLogger( AdminUserEndpoint.class);

    @Autowired
    private UserService userService;

    @GetMapping(value="/admin/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InternalResponse> getUserById(@CookieValue("id_token") Cookie cookie, @PathVariable("id") String id, HttpServletRequest request) {
        evaluateAccessRisk(logger,request, new InternalRequest() { @Override public String toString() { return "GET"; }});
        if(!isAuthParametersOk(cookie)) { return ResponseEntity.status(302).build();}
        if(!isAuthenticated(cookie.getValue())) {
            return ResponseEntity.status(getHttpStatusCode( BasicResponse.SESSION_INVALID)).body(BasicResponse.SESSION_INVALID);
        }
        logger.info( "getUserById: {}",cookie.getValue() );
        if(!isAuthorized(cookie.getValue(), AdminRole.READ_ONLY_ADMIN)) {
            return ResponseEntity.status(getHttpStatusCode(BasicResponse.FORBIDDEN)).body(BasicResponse.FORBIDDEN);
        }
        return renderResponse(userService.readUser(id));
    }

    @PostMapping(value="/admin/user/{id}", consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InternalResponse> updateUser(@CookieValue("id_token") Cookie cookie,
                                                       @PathVariable("id") String id,
                                                       @RequestBody UserChangeRequest userChangeRequest, HttpServletRequest request) {
        evaluateAccessRisk(logger,request, userChangeRequest);
        if(!isAuthParametersOk(cookie)) { return ResponseEntity.status(302).build();}
        if(!isAuthenticated(cookie.getValue())) {
            return ResponseEntity.status(getHttpStatusCode(BasicResponse.SESSION_INVALID)).body(BasicResponse.SESSION_INVALID);
        }
        logger.info( "updateUser:{}",cookie.getValue() );
        if(!isAuthorized(cookie.getValue(), AdminRole.FULL_ADMIN)) {
            return ResponseEntity.status(getHttpStatusCode(BasicResponse.FORBIDDEN)).body(BasicResponse.FORBIDDEN);
        }
        return renderResponse(userService.updateUser(id,userChangeRequest.getEmailAddress(),
                userChangeRequest.getAdminRole(),userChangeRequest.getActivationStatus()));
    }

    @GetMapping(value="/admin/user/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InternalResponse> getAllUsers(@CookieValue("id_token") Cookie cookie, HttpServletRequest request) {
        evaluateAccessRisk(logger,request, new InternalRequest() { @Override public String toString() { return "GET"; }});
        if(!isAuthParametersOk(cookie)) { return ResponseEntity.status(302).build();}
        if(!isAuthenticated(cookie.getValue())) {
            return ResponseEntity.status(getHttpStatusCode(BasicResponse.SESSION_INVALID)).body(BasicResponse.SESSION_INVALID);
        }
        logger.info( "getAllUsers:{}",cookie.getValue() );
        if(!isAuthorized(cookie.getValue(), AdminRole.READ_ONLY_ADMIN)) {
            return ResponseEntity.status(getHttpStatusCode(BasicResponse.FORBIDDEN)).body(BasicResponse.FORBIDDEN);
        }
        return renderResponse(userService.readAllUsers());
    }




}
