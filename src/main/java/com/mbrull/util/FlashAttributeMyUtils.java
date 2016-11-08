package com.mbrull.util;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class FlashAttributeMyUtils {

	private FlashAttributeMyUtils() {
	}
	
	public static void flashSuccess(RedirectAttributes redirectAttributes, 
			String messageKey) {
		redirectAttributes.addFlashAttribute("flashKind", "success");
		redirectAttributes.addFlashAttribute("flashMessage", MyUtil.getMessage(messageKey));
	}
	
}
