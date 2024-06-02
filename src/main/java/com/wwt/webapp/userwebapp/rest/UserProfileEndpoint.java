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

import com.wwt.webapp.userwebapp.service.UserService;
import com.wwt.webapp.userwebapp.service.request.ArchiveRequest;
import com.wwt.webapp.userwebapp.service.request.EmailChangeRequest;
import com.wwt.webapp.userwebapp.service.request.InternalRequest;
import com.wwt.webapp.userwebapp.service.request.PasswordChangeRequest;
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
public class UserProfileEndpoint extends BasicEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileEndpoint.class);

    @Autowired
    private UserService userService;

    @GetMapping(value="/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InternalResponse> getUser(@CookieValue("id_token") Cookie cookie, HttpServletRequest request) {
        evaluateAccessRisk(logger,request, new InternalRequest() { @Override public String toString() { return "GET getUser"; }});
        if(!isAuthParametersOk(cookie)) { return ResponseEntity.status(302).build();}
        if(!isAuthenticated(cookie.getValue())) { return ResponseEntity.status(getHttpStatusCode( BasicResponse.SESSION_INVALID)).body(BasicResponse.SESSION_INVALID); }
        return renderResponse(userService.readUser(getAuthenticatedUserUuid(cookie.getValue())));
    }


    @PostMapping(value="/user/emailchange", consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InternalResponse> updateEmailAddress(@CookieValue("id_token") Cookie cookie, @RequestBody EmailChangeRequest emailChangeRequest, HttpServletRequest request) {
        evaluateAccessRisk(logger,request, emailChangeRequest);
        if(!isAuthParametersOk(cookie)) { return ResponseEntity.status(302).build();}
        if(!isAuthenticated(cookie.getValue())) { return ResponseEntity.status(getHttpStatusCode(BasicResponse.SESSION_INVALID)).body(BasicResponse.SESSION_INVALID); }
        return renderResponse(userService.changeEmail(getAuthenticatedUserUuid(cookie.getValue()),emailChangeRequest.getEmail()));
    }

    @PostMapping(value="/user/passwordchange", consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InternalResponse> changePassword(@CookieValue("id_token") Cookie cookie, @RequestBody PasswordChangeRequest pwChangeReq, HttpServletRequest request) {
        evaluateAccessRisk(logger,request, pwChangeReq);
        if(!isAuthParametersOk(cookie)) { return ResponseEntity.status(302).build();}
        if(!isAuthenticated(cookie.getValue())) { return ResponseEntity.status(getHttpStatusCode(BasicResponse.SESSION_INVALID)).body(BasicResponse.SESSION_INVALID); }
        return renderResponse(userService.changePassword(getAuthenticatedUserUuid(cookie.getValue()),pwChangeReq.getOldPassword(),pwChangeReq.getNewPassword()));
    }

    @DeleteMapping(value="/user", consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InternalResponse> archiveUser(@CookieValue("id_token") Cookie cookie, @RequestBody ArchiveRequest archiveRequest, HttpServletRequest request) {
        evaluateAccessRisk(logger,request, archiveRequest);
        if(!isAuthParametersOk(cookie)) { return ResponseEntity.status(302).build();}
        if(!isAuthenticated(cookie.getValue())) { return ResponseEntity.status(getHttpStatusCode(BasicResponse.SESSION_INVALID)).body(BasicResponse.SESSION_INVALID); }
        return renderResponse(userService.archiveUser(getAuthenticatedUserUuid(cookie.getValue()),archiveRequest.getPassword()));
    }

}
