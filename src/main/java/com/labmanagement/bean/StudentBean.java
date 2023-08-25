package com.labmanagement.bean;

import java.io.Serializable;

import com.opencsv.bean.CsvBindByName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@CsvBindByName(column = "FULL NAME")
	private String fullName;
	@CsvBindByName(column = "STUDENT NUMBER")
	private Long studentNumber;
	@CsvBindByName(column = "EMAIL")
	private String email;
	@CsvBindByName(column = "ROLE")
	private String role;

}
