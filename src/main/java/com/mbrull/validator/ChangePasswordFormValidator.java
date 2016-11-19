package com.mbrull.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.mbrull.dto.ChangePasswordForm;

@Component
public class ChangePasswordFormValidator extends LocalValidatorFactoryBean {
	
	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.isAssignableFrom(ChangePasswordForm.class);
	}

	@Override
	public void validate(Object obj, Errors errors, final Object... validationHints) {
		
		super.validate(obj, errors, validationHints);
		
		if(errors.hasErrors()) {
		    return;
		}

		ChangePasswordForm changePasswordForm = (ChangePasswordForm) obj;
        if (!changePasswordForm.getNewPassword().equals(changePasswordForm.getNewRetypePassword())){
            errors.reject("passwordsDoNotMatch");           
        }
 
	}

}
