package com.wwt.webapp.userwebapp.service;

import com.wwt.webapp.userwebapp.domain.request.ArchiveRequest;
import com.wwt.webapp.userwebapp.domain.request.AuthenticatedRequest;
import com.wwt.webapp.userwebapp.domain.request.EmailChangeRequest;
import com.wwt.webapp.userwebapp.domain.request.PasswordChangeRequest;
import com.wwt.webapp.userwebapp.domain.response.InternalResponse;


/**
 * @author benw-at-wwt
 */
public interface UserService {


    InternalResponse readUser(AuthenticatedRequest request);
    InternalResponse changePassword(PasswordChangeRequest passwordChangeReq);
    InternalResponse changeEmail(EmailChangeRequest emailChangeReq);
    InternalResponse archiveUser(ArchiveRequest archiveRequestRequest);

}
