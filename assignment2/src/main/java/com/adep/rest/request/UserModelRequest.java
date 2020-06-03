package com.adep.rest.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserModelRequest {

	@Email(message = "Username needs to be an email")
	@NotBlank(message = "Username is required")
	private String username;
	@NotBlank(message = "Please enter your fullName")
	private String fullName;
	@NotBlank(message = "Password is required")
	private String password;

	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
