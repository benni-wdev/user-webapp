/*
 *  Copyright 2021  Wehe Web Technologies - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Benjamin Wehe (contact@wehe-web-technologies.ch)
 *
 */
package com.wwt.webapp.userwebapp.service.response;

/**
 * @author benw-at-wwt
 */
public class BasicSuccessCreationResponse extends BasicSuccessResponse {

    private final String uuid;

    public BasicSuccessCreationResponse(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }
}
