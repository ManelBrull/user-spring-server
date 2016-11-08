package com.mbrull.services;

import org.springframework.validation.BindingResult;

import com.mbrull.dto.ForgotPasswordForm;
import com.mbrull.dto.ResetPasswordForm;
import com.mbrull.dto.SignupForm;
import com.mbrull.dto.UserEditForm;
import com.mbrull.entities.User;

public interface UserService {

	public void signup(SignupForm signupForm);

	public void verify(String verificationCode);
	
	public void reSendVerificationEmail();

	public void forgotPassword(ForgotPasswordForm forgotPasswordForm);

	public void resetPassword(String forgotPasswordCode, ResetPasswordForm resetPasswordForm, BindingResult result);

	public User findOne(long userId);

	public void update(long userId, UserEditForm userEditForm);
	
}
