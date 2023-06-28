package com.labmanagement.bean;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPassword implements Serializable {

	private static final long serialVersionUID = 1L;

	private String resetToken;
	private String email;
	@NotNull
	@Size(min = 6 , max = 20)
	private transient String newPassword;
	@NotNull
	@Size(min = 6 , max = 20)
	private transient String confirmPassword;
	
}
