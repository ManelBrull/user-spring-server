package com.mbrull.util;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class MyUtil {

    private static MessageSource messageSource;
    
	@Autowired
    public MyUtil(MessageSource messageSource) {
	    MyUtil.messageSource = messageSource;
    }
    
    public static String getMessage(String messageKey, Object... args) {
        return messageSource.getMessage(messageKey, args, Locale.getDefault());
    }
	
	public static void validate(boolean valid, String msgContent, Object... args) {
		if(!valid) {
			throw new RuntimeException(MessageUtils.getMessage(msgContent, args));
		}
	}
	
}
