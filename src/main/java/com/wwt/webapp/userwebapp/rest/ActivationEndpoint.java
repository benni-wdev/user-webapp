package com.wwt.webapp.userwebapp.rest;


import com.wwt.webapp.userwebapp.domain.request.ActivationRequest;
import com.wwt.webapp.userwebapp.domain.response.InternalResponse;
import com.wwt.webapp.userwebapp.service.ActivationService;
import com.wwt.webapp.userwebapp.service.ActivationServiceImpl;
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
public class ActivationEndpoint extends BasicEndpoint {

    private static final Logger logger = Logger.getLogger(ActivationEndpoint.class);

    private final ActivationService activationService = new ActivationServiceImpl();

    @POST
    @Path("/activate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response activateUser(ActivationRequest activationRequest) {
        evaluateAccessRisk(logger,activationRequest);
        InternalResponse response = activationService.activateUser(activationRequest.getActivationToken());
        if(response.isSuccessful()) {
            return Response.ok(response).build();
        }
        else {
            return Response.status(getHttpStatusCode(response)).entity(response).build();
        }
    }


}

