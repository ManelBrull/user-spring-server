package com.mbrull.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.mbrull.dto.UserDetailsImpl;
import com.mbrull.entities.User;

public class UserUtil {

	private UserUtil() {}

	public static User getSessionUser() {
		UserDetailsImpl auth = getAuth();
		return auth == null ? null : auth.getUser();
	}
	
	public static UserDetailsImpl getAuth() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(auth != null) {
			Object principal = auth.getPrincipal();
			if(principal instanceof UserDetailsImpl) {
				return (UserDetailsImpl) principal;
			}
		}
		
		return null;
	}
	
	
	
}
