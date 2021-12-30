/*
 *  Copyright 2021  Wehe Web Technologies - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Benjamin Wehe (contact@wehe-web-technologies.ch)
 *
 */

package com.wwt.webapp.userwebapp.service.response;

/**
 * 
 * @author beng
 *
 */
@SuppressWarnings("unused")
public interface InternalResponse {
	
	boolean isSuccessful();
	MessageCode getMessageCode();
	String getMessageText();
	
}
