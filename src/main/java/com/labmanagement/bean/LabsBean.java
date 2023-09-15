package com.labmanagement.bean;

import java.io.Serializable;
import java.util.Date;

import com.labmanagement.domain.Modules;

import lombok.Data;

@Data
public class LabsBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String fileName;
	private Integer week;
	private Integer totalLabsMarks;
	private String uploadedBy;
	private Date expireDate;
	private Date createDate;
	private Modules modules;
	private Integer earnedMarks;
	private String answerSheet;
	private String feedback;
}
