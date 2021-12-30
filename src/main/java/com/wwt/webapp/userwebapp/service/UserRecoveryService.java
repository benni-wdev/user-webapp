package com.wwt.webapp.userwebapp.service;


import com.wwt.webapp.userwebapp.service.response.InternalResponse;

/**
 * @author benw-at-wwt
 */
public interface UserRecoveryService {

    @SuppressWarnings("SameReturnValue")
    InternalResponse initiateRecovery(String email);
    InternalResponse recoverUser(String passwordToken, String newPassword);
}
