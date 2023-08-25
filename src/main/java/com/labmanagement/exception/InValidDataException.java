package com.labmanagement.exception;

public class InValidDataException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InValidDataException(String msg) {
		super(msg);
	}

}