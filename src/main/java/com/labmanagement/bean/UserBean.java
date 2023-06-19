package com.labmanagement.bean;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private String username;
	private String email;
	private String isActive;
	private String isDeleted;
	private Date createDate;
	private Date updateDate;

}
