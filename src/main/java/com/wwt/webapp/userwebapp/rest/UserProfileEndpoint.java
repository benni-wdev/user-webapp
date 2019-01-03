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


import com.wwt.webapp.userwebapp.domain.request.*;
import com.wwt.webapp.userwebapp.domain.response.InternalResponse;
import com.wwt.webapp.userwebapp.service.UserService;
import com.wwt.webapp.userwebapp.service.UserServiceImpl;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author benw-at-wwt
 */
@Path("/user")
public class UserProfileEndpoint extends AuthenticatedEndpoint {

    private static final Logger logger = Logger.getLogger(UserProfileEndpoint.class);

    private final UserService userService = new UserServiceImpl();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@CookieParam("id_token") Cookie cookie) {
        evaluateAccessRisk(logger, new InternalRequest() { @Override public String toString() { return "GET"; }});
        if(!isAuthParametersOk(cookie)) { return Response.status(302).build();}

        logger.info( "getUser:"+cookie.getValue() );
        AuthenticatedRequest ar = new AuthenticatedRequest();
        ar.setIdToken(cookie.getValue());
        InternalResponse response = userService.readUser(ar);
        if (response.isSuccessful()) {
            return Response.ok(response).build();
        } else {
            return Response.status(getHttpStatusCode(response)).entity(response).build();
        }
    }

    @Path("/emailchange")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateEmailAddress(@CookieParam("id_token") Cookie cookie,final EmailChangeRequest emailChangeRequest) {
        evaluateAccessRisk(logger, emailChangeRequest);
        if(!isAuthParametersOk(cookie)) { return Response.status(302).build();}

        logger.info( "updateEmailAddress:"+cookie.getValue() );
        emailChangeRequest.setIdToken(cookie.getValue());
        InternalResponse response = userService.changeEmail(emailChangeRequest);
        if (response.isSuccessful()) {
            return Response.ok(response).build();
        } else {
            return Response.status(getHttpStatusCode(response)).entity(response).build();
        }
    }

    @Path("/passwordchange")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changePassword(@CookieParam("id_token") Cookie cookie, PasswordChangeRequest pwChangeReq) {
        evaluateAccessRisk(logger, pwChangeReq);
        if(!isAuthParametersOk(cookie)) { return Response.status(302).build();}

        logger.info( "changePassword:"+cookie.getValue() );
        pwChangeReq.setIdToken(cookie.getValue());
        InternalResponse response = userService.changePassword(pwChangeReq);
        if (response.isSuccessful()) {
            return Response.ok(response).build();
        } else {
            return Response.status(getHttpStatusCode(response)).entity(response).build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response archiveUser(@CookieParam("id_token") Cookie cookie, ArchiveRequest archiveRequest) {
        evaluateAccessRisk(logger, archiveRequest);
        if(!isAuthParametersOk(cookie)) { return Response.status(302).build();}

        logger.info( "archiveUser:"+cookie.getValue() );
        archiveRequest.setIdToken(cookie.getValue());
        InternalResponse response = userService.archiveUser(archiveRequest);
        if (response.isSuccessful()) {
            return Response.ok(response).build();
        } else {
            return Response.status(getHttpStatusCode(response)).entity(response).build();
        }
    }

}
