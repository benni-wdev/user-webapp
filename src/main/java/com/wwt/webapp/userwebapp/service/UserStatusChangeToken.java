package com.wwt.webapp.userwebapp.service;

import java.sql.Timestamp;

/**
 * @author benw-at-wwt
 */
public interface UserStatusChangeToken {

    String getToken();
    Timestamp getTokenExpiresAt();
}
