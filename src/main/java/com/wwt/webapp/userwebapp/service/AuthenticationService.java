package com.wwt.webapp.userwebapp.service;

import com.wwt.webapp.userwebapp.domain.request.AuthenticatedRequest;
import com.wwt.webapp.userwebapp.domain.response.InternalResponse;

/**
 * @author benw-at-wwt
 */
public interface AuthenticationService {


    InternalResponse authenticate(String loginId, String password,boolean isLongSession);

}
