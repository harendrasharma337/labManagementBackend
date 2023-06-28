package com.labmanagement.bean;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticateUser implements Serializable {

	private static final long serialVersionUID = 1L;

	private String fullName;
	private String username;
	private String role;
	private String isActive;
	private String isDeleted;
	private Date createDate;

}
