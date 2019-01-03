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

import com.wwt.webapp.userwebapp.domain.request.ExecuteRecoveryRequest;
import com.wwt.webapp.userwebapp.domain.request.InitiateRecoveryRequest;
import com.wwt.webapp.userwebapp.domain.response.InternalResponse;
import com.wwt.webapp.userwebapp.service.UserRecoveryService;
import com.wwt.webapp.userwebapp.service.UserRecoveryServiceImpl;
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
public class UserRecoveryEndpoint extends BasicEndpoint {

    private static final Logger logger = Logger.getLogger(UserRecoveryEndpoint.class);


    private final UserRecoveryService userRecoveryService = new UserRecoveryServiceImpl();

    @POST
    @Path("/recovery/initiate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response initiateRecovery(InitiateRecoveryRequest initiateRecoveryRequest) {
        evaluateAccessRisk(logger,initiateRecoveryRequest);
        InternalResponse response = userRecoveryService.initiateRecovery(initiateRecoveryRequest.getEmail());
        if(response.isSuccessful()) {
            return Response.ok(response).build();
        }
        else {
            return Response.status(getHttpStatusCode(response)).entity(response).build();
        }

    }


    @POST
    @Path("/recovery/execute")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response executeRecovery(ExecuteRecoveryRequest executeRecoveryRequest) {
        evaluateAccessRisk(logger, executeRecoveryRequest);
        InternalResponse response = userRecoveryService.recoverUser(executeRecoveryRequest);
        if(response.isSuccessful()) {
            return Response.ok(response).build();
        }
        else {
            return Response.status(getHttpStatusCode(response)).entity(response).build();
        }
    }
}
