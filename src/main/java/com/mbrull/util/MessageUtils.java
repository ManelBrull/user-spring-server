package com.mbrull.util;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class MessageUtils {

	private static MessageSource messageSource;
	
	@Autowired
	public MessageUtils(MessageSource messageSource) {
		MessageUtils.messageSource = messageSource;
	}

	public static String getMessage(String messageKey, Object... args) {
		return messageSource.getMessage(messageKey, args, Locale.getDefault());
	}
	
}
