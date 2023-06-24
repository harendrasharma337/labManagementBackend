package com.labmanagement.bean;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistration implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@NotNull
	@Size(min = 3 , max = 25)
	private String fullName;
	@NotNull
	@Email(message = "Please enter valid Email")
	private String username;
	@NotNull
	@Size(min = 6 , max = 20)
	private transient String password;
	@NotNull
	@Size(min = 6 , max = 20)
	private transient String confirmPassword;
	private String role;

}
