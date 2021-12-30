package com.wwt.webapp.userwebapp.service;


import com.wwt.webapp.userwebapp.service.response.InternalResponse;

/**
 * @author benw-at-wwt
 */
public interface AuthenticationService {


    InternalResponse authenticate(String loginId, String password, boolean isLongSession);

    InternalResponse refreshAuthentication(String refreshToken);

    InternalResponse logout(String uuid);
}
