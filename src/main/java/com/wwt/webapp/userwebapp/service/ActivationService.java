package com.wwt.webapp.userwebapp.service;


import com.wwt.webapp.userwebapp.service.response.InternalResponse;

/**
 * @author benw-at-wwt
 */
public interface ActivationService {

    InternalResponse activateUser(String activationToken);
}
