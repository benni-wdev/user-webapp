package com.wwt.webapp.userwebapp.service;

import com.wwt.webapp.userwebapp.domain.response.InternalResponse;

/**
 * @author benw-at-wwt
 */
public interface ActivationService {

    InternalResponse activateUser(String activationToken);
}
