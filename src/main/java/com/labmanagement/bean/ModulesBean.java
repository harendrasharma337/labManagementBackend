package com.labmanagement.bean;

import java.io.Serializable;

import lombok.Data;


@Data
public class ModulesBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String moduleName;
	private String moduleCode;
}
