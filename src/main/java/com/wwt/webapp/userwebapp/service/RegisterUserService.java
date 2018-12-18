package com.wwt.webapp.userwebapp.service;

import com.wwt.webapp.userwebapp.domain.request.RegistrationRequest;
import com.wwt.webapp.userwebapp.domain.response.InternalResponse;

/**
 * @author benw-at-wwt
 */
public interface RegisterUserService {

    InternalResponse registerUser(RegistrationRequest registrationRequest);
}
