package com.adep.exception;

public class UserServiceException  extends RuntimeException{

	private static final long serialVersionUID = -1212993510217191439L;

	public UserServiceException(String message)
	{
		super(message);
	}
}