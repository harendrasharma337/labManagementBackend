package com.labmanagement.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Messages {

	USERNAME_EXIST("UserName Already Exist..!"), EMAIL_EXIST("Email Already Exist..!"),
	USER_VALIDATION_FAILED("User is not valid..!"), NAME_IS_NOT_VALID("Please enter valid name..!"),
	PASSWORD_IS_NOT_SAME("Password should be same..!"),
	USER_IS_NOT_FOUND_WITH_USERNAME("User is not found with username : "),
	USERNAME_IS_NOT_FOUND("Username is not found"), DATA_FETCHED_SUCCESSFULLY("Data fetched successfully..!"),
	DATA_NOT_FOUND("Data not found..!"), ACCESS_DENIED("Access denied..!"), UNAUTHORIZED_USER("UnAuthorized user..!"),
	USER_IS_NOT_ACTIVE("User is not active...!"), USER_REGISTER_SUCCESSFULLY("User Registered Successfully..!");

	public final String value;

}
