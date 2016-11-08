package com.mbrull.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.mbrull.entities.User;

public class SignupForm {

	@NotNull
	@Size(min=1, max=User.EMAIL_MAX, message="{sizeError}")
	@Pattern(regexp=User.EMAIL_PATTERN, message="{emailRegexPattern}")
	private String email;
	
	@NotNull
	@Size(min=1, max=127, message="{sizeError}")
	private String name;
	@NotNull
	@Size(min=1, max=127, message="{sizeError}")
	private String password;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUsername() {
		return name;
	}
	public void setUsername(String username) {
		this.name = username;
	}
	
	@Override
	public String toString() {
		return "SignupForm [email=" + email + ", name=" + name + ", password="
				+ password + "]";
	}
	
}
