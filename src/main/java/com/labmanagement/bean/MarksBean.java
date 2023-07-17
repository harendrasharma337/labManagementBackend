package com.labmanagement.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class MarksBean implements Serializable{

	private static final long serialVersionUID = 3375870248833382889L;
	private Long id;
	private Integer totalMarks;
}
