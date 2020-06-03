package com.adep.rest.request;

import java.io.Serializable;

public class UserLoginModelRequest implements Serializable{

	private static final long serialVersionUID = 8854460265940579641L;

	private String username;
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
