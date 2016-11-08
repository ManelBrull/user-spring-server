package com.mbrull.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.mbrull.entities.User;

public class ForgotPasswordForm {

	@NotNull
	@Size(min=1, max=User.EMAIL_MAX, message="{sizeError}")
	@Pattern(regexp=User.EMAIL_PATTERN, message="{emailRegexPattern}")
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
