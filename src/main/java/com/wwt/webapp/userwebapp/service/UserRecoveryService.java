package com.wwt.webapp.userwebapp.service;

import com.wwt.webapp.userwebapp.domain.request.ExecuteRecoveryRequest;
import com.wwt.webapp.userwebapp.domain.response.InternalResponse;

/**
 * @author benw-at-wwt
 */
public interface UserRecoveryService {

    InternalResponse initiateRecovery(String email);
    InternalResponse recoverUser(ExecuteRecoveryRequest executeRecoveryRequest);
}
