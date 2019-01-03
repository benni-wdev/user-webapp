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

import com.wwt.webapp.userwebapp.domain.request.RegistrationRequest;
import com.wwt.webapp.userwebapp.domain.response.InternalResponse;
import com.wwt.webapp.userwebapp.service.RegisterUserService;
import com.wwt.webapp.userwebapp.service.RegisterUserServiceImpl;
import org.apache.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * @author benw-at-wwt
 */
@Path("/user")
public class RegistrationEndpoint extends BasicEndpoint {

    private static final Logger logger = Logger.getLogger(RegistrationEndpoint.class);


    private final RegisterUserService registerUserService = new RegisterUserServiceImpl();

    @Path("/register")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(final RegistrationRequest registrationRequest) {
        evaluateAccessRisk(logger, registrationRequest);
        InternalResponse response = registerUserService.registerUser(registrationRequest);
        if (response.isSuccessful()) {
            return Response.ok(response).build();
        } else {
            return Response.status(getHttpStatusCode(response)).entity(response).build();
        }
    }

}
