package com.wwt.webapp.userwebapp.rest;

import com.wwt.webapp.userwebapp.service.UserRecoveryService;
import com.wwt.webapp.userwebapp.service.request.ExecuteRecoveryRequest;
import com.wwt.webapp.userwebapp.service.request.InitiateRecoveryRequest;
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
public class UserRecoveryEndpoint extends BasicEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(UserRecoveryEndpoint.class);

    @Autowired
    private UserRecoveryService userRecoveryService;

    @PostMapping(value="/user/recovery/initiate", consumes= MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InternalResponse> initiateRecovery(@RequestBody InitiateRecoveryRequest initiateRecoveryRequest, HttpServletRequest request) {
        evaluateAccessRisk(logger,request,initiateRecoveryRequest);
        return renderResponse(userRecoveryService.initiateRecovery(initiateRecoveryRequest.getEmail()));
    }


    @PostMapping(value="/user/recovery/execute", consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InternalResponse> executeRecovery(@RequestBody ExecuteRecoveryRequest executeRecoveryRequest, HttpServletRequest request) {
        evaluateAccessRisk(logger,request, executeRecoveryRequest);
        return renderResponse(userRecoveryService.recoverUser(executeRecoveryRequest.getPasswordToken(),executeRecoveryRequest.getNewPassword()));
    }
}
