package com.wwt.webapp.userwebapp.rest;


import com.wwt.webapp.userwebapp.domain.request.AuthenticatedRequest;
import com.wwt.webapp.userwebapp.domain.request.AuthenticationRequest;
import com.wwt.webapp.userwebapp.domain.request.InternalRequest;
import com.wwt.webapp.userwebapp.domain.response.AuthenticationSuccessResponse;
import com.wwt.webapp.userwebapp.domain.response.InternalResponse;
import com.wwt.webapp.userwebapp.domain.response.MessageCode;
import com.wwt.webapp.userwebapp.service.AuthenticationService;
import com.wwt.webapp.userwebapp.service.AuthenticationServiceImpl;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * @author benw-at-wwt
 */
@Path("/user")
public class AuthenticationEndpoint extends AuthenticatedEndpoint {

    private static final Logger logger = Logger.getLogger(AuthenticationEndpoint.class);


    private final AuthenticationService authenticationService = new AuthenticationServiceImpl();

    @Path("/authenticate")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticateUser(final AuthenticationRequest authenticationRequest) {
        evaluateAccessRisk(logger, authenticationRequest);
        InternalResponse response = authenticationService.authenticate(authenticationRequest.getLoginId(),
                authenticationRequest.getPassword(),authenticationRequest.isLongSession());
        if (response.isSuccessful()) {
            String cookie = constructCookie(((AuthenticationSuccessResponse) response).getToken(),authenticationRequest.isLongSession(),false);
            logger.info("authenticateUser:"+cookie);
            return Response.ok(response).header("Set-Cookie",cookie).build();
        } else {
            return Response.status(getHttpStatusCode(response)).entity(response).build();
        }


    }

    @Path("/logout")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout(@CookieParam("id_token") Cookie cookie) {
        evaluateAccessRisk(logger, new InternalRequest() { @Override public String toString() { return "LOGOUT"; }});
        if(!isAuthParametersOk(cookie)) { return Response.status(302).build();}

        logger.info( "logout:"+cookie.getValue() );
        String invalidationCookie = constructCookie("",false,true);

        return Response.ok( new InternalResponse() {
            @Override
            public boolean isSuccessful() {
                return true;
            }

            @Override
            public MessageCode getMessageCode() {
                return MessageCode.OPERATION_SUCCESSFUL;
            }

            @Override
            public String getMessageText() {
                return MessageCode.OPERATION_SUCCESSFUL.getMessage();
            }
        }
        ).header("Set-Cookie",invalidationCookie).build();

    }

}
