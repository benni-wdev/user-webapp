package com.wwt.webapp.userwebapp.domain.response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author benw-at-wwt
 *
 */
@XmlRootElement
@SuppressWarnings("unused")
public interface InternalResponse {
	
	@XmlElement boolean isSuccessful();
	@XmlElement MessageCode getMessageCode();
	@XmlElement String getMessageText();
	
}
