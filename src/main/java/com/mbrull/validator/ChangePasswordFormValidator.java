package com.mbrull.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.mbrull.dto.ChangePasswordForm;
import com.mbrull.dto.ForgotPasswordForm;
import com.mbrull.dto.ResetPasswordForm;
import com.mbrull.entities.User;
import com.mbrull.repositories.UserRepository;
import com.mbrull.util.UserUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class ChangePasswordFormValidator extends LocalValidatorFactoryBean {
	
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	public ChangePasswordFormValidator(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

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
        
        long loggedInUserId = UserUtil.getSessionUser().getId();
        User user = userRepository.findOne(loggedInUserId);
        
        if(!passwordEncoder.matches(changePasswordForm.getOldPassword(), user.getPassword())){
            errors.rejectValue("oldPassword", "currentPasswordDoNotMatch");
        }
 
	}

}
