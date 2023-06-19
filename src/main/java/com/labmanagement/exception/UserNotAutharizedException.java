package com.labmanagement.exception;

public class UserNotAutharizedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UserNotAutharizedException(String msg) {
		super(msg);
	}
}
