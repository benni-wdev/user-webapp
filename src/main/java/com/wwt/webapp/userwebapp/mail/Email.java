package com.wwt.webapp.userwebapp.mail;

import java.util.Map;

/**
 * @author benw-at-wwt
 */
@SuppressWarnings("WeakerAccess")
public interface Email {

    String TITLE = "title";
    String NAME = "name";
    String BODY1 = "body1";
    String BODY2 = "body2";
    String BODY3 = "body3";
    String PREHEADER = "preheader";

    String getSubject();
    Map<String,String> getModel();
    String getEmailAddress();

}
