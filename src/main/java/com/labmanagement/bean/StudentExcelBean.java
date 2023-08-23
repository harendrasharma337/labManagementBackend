package com.labmanagement.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StudentExcelBean {

	private String fullName;
	private String studentNumber;
	private String email;
	private String role;

}
