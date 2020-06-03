package com.adep.exception;

public class OrderNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 8293535916877260672L;

	public OrderNotFoundException(String message)
	{
		super(message);
	}
}