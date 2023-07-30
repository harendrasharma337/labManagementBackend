package com.labmanagement.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Constants {

	AUTHORIZATION("Authorization"), SUCCESS("SUCCESS"), FAILED("FAILED");

	private final String value;

}
