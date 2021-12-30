package com.wwt.webapp.userwebapp.rest;



import com.wwt.webapp.userwebapp.service.ActivationService;
import com.wwt.webapp.userwebapp.service.request.ActivationRequest;
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

@RestController
public class ActivationEndpoint extends BasicEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(ActivationEndpoint.class);

    @Autowired
    private ActivationService activationService;

    @PostMapping(value="/user/activate", consumes= MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InternalResponse> activateUser(@RequestBody ActivationRequest activationRequest, HttpServletRequest request) {
        evaluateAccessRisk(logger,request,activationRequest);
        return renderResponse(activationService.activateUser(activationRequest.getActivationToken()));
    }


}

