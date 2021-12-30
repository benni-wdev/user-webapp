/*
 *  Copyright 2020  Wehe Web Technologies - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Benjamin Wehe (contact@wehe-web-technologies.ch)
 *
 */

package com.wwt.webapp.userwebapp.mail;

public interface SharedMailSender {

    void sendMail(Email email);
}
