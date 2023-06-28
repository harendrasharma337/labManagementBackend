package com.labmanagement.exception;

public class UserIsNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 9076819831826991503L;

	public UserIsNotFoundException(String msg) {
		super(msg);
	}
}
