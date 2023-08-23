package com.labmanagement.bean;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private String username;
	private String isActive;
	private String isDeleted;
	private String email;
	private String studentNumber;
	private Date createDate;
	private Date updateDate;
	private String role;
	private String password;



}
