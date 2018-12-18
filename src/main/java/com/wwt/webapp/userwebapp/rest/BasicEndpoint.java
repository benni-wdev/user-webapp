package com.wwt.webapp.userwebapp.rest;

import com.wwt.webapp.userwebapp.domain.request.InternalRequest;
import com.wwt.webapp.userwebapp.domain.response.InternalResponse;
import com.wwt.webapp.userwebapp.domain.response.MessageCode;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;


/**
 * @author benw-at-wwt
 */

abstract class BasicEndpoint {


    @Context
    protected HttpServletRequest httpRequest;

    void evaluateAccessRisk(Logger logger, InternalRequest internalRequest) {
        //Here we can evaluate risk based on known IP addresses (or everything what can be derived from it),
        // and for example force step up but needs additional infrastructure
        logger.log(Level.OFF, "ACCESS->" + httpRequest.getRemoteAddr() + " " + internalRequest);
    }

    Response.StatusType getHttpStatusCode(InternalResponse response) {
        if(response.getMessageCode().equals(MessageCode.LOGIN_ID_ALREADY_EXISTS) ||
           response.getMessageCode().equals(MessageCode.EMAIL_ADDRESS_ALREADY_EXISTS) ||
           response.getMessageCode().equals(MessageCode.RECOVERY_ALREADY_DONE_OR_NOT_POSSIBLE) ||
           response.getMessageCode().equals(MessageCode.NO_CHANGE_NO_UPDATE) ||
           response.getMessageCode().equals(MessageCode.EMAIL_ADDRESS_NOT_VALID) ||
           response.getMessageCode().equals(MessageCode.LOGIN_ID_NOT_VALID) ||
           response.getMessageCode().equals(MessageCode.ACTIVATION_ALREADY_DONE_OR_NOT_POSSIBLE))
            return Response.Status.BAD_REQUEST;
        else if(response.getMessageCode().equals(MessageCode.PASSWORD_RECOVERY_TOKEN_EXPIRED) ||
                response.getMessageCode().equals(MessageCode.ACTIVATION_TOKEN_EXPIRED))
            return Response.Status.UNAUTHORIZED;
        else if( response.getMessageCode().equals(MessageCode.SESSION_INVALID) ||
                response.getMessageCode().equals(MessageCode.SESSION_EXPIRED) )
            return Response.Status.SEE_OTHER;
        else if(response.getMessageCode().equals(MessageCode.FORBIDDEN))
            return Response.Status.FORBIDDEN;
        else if(response.getMessageCode().equals(MessageCode.LOGIN_OR_PASSWORD_WRONG) ||
                response.getMessageCode().equals(MessageCode.ACTIVATION_TOKEN_NOT_KNOWN) ||
                response.getMessageCode().equals(MessageCode.PASSWORD_TOKEN_NOT_KNOWN) )
            return Response.Status.NOT_FOUND;
        else if(response.getMessageCode().equals(MessageCode.CONCURRENT_MODIFICATION))
            return Response.Status.CONFLICT;
        else if(response.getMessageCode().equals(MessageCode.USER_NOT_ACTIVE) ||
                response.getMessageCode().equals(MessageCode.TOO_MANY_FAILED_LOGINS) )
            return new Response.StatusType(){
                @Override
                public Response.Status.Family getFamily() { return Response.Status.Family.CLIENT_ERROR; }

                @Override
                public String getReasonPhrase() { return "The resource that is being accessed is locked."; }

                @Override
                public int getStatusCode() { return 423; }
            };
        else {
            return Response.Status.INTERNAL_SERVER_ERROR;
        }
    }


}
