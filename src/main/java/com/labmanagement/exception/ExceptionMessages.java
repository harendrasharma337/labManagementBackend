package com.labmanagement.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionMessages {

	VALIDATION_FAILED("Validation failed!"), INVALID_FILE("Sorry! Filename contains invalid path sequence ");

	private final String value;

}
