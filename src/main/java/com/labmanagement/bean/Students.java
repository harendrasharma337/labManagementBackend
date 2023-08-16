package com.labmanagement.bean;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Students implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private String username;
	private String isActive;
	private Integer totalMarks; 
	private String answerSheet;
	private Long marksId;
}
