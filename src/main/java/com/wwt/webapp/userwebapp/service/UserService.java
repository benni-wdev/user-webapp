package com.wwt.webapp.userwebapp.service;


import com.wwt.webapp.userwebapp.domain.ActivationStatus;
import com.wwt.webapp.userwebapp.domain.AdminRole;
import com.wwt.webapp.userwebapp.service.response.InternalResponse;

/**
 * @author benw-at-wwt
 */
public interface UserService {


    InternalResponse readUser(String userUuid);
    InternalResponse changePassword(String userUuid, String oldPassword, String newPassword);
    InternalResponse changeEmail(String userUuid, String newEmailAddress);
    InternalResponse archiveUser(String userUuid, String password);
    InternalResponse readAllUsers();
    InternalResponse updateUser(String userUuid, String email, AdminRole adminRole, ActivationStatus activationStatus);

}
