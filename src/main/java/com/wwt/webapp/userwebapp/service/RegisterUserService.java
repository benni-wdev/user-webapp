package com.wwt.webapp.userwebapp.service;

import com.wwt.webapp.userwebapp.service.request.RegistrationRequest;
import com.wwt.webapp.userwebapp.service.response.InternalResponse;

/**
 * @author benw-at-wwt
 */
public interface RegisterUserService {

    InternalResponse registerUser(RegistrationRequest registrationRequest);
}
