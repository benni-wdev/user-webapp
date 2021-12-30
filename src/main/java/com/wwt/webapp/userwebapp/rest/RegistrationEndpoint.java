package com.wwt.webapp.userwebapp.rest;

import com.wwt.webapp.userwebapp.service.RegisterUserService;
import com.wwt.webapp.userwebapp.service.request.RegistrationRequest;
import com.wwt.webapp.userwebapp.service.response.InternalResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * @author beng
 */
@RestController
public class RegistrationEndpoint extends BasicEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationEndpoint.class);

    @Autowired
    private RegisterUserService registerUserService;

    @PostMapping(value="/user/register", consumes= MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InternalResponse> registerUser(@RequestBody RegistrationRequest registrationRequest, HttpServletRequest request) {
        evaluateAccessRisk(logger,request, registrationRequest);
        return renderResponse(registerUserService.registerUser(registrationRequest));
    }

}
