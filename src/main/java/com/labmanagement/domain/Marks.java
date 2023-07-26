package com.labmanagement.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Marks implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", nullable = false, unique = true)
	private Long id;
	@Column(name = "TOTALMARKS")
	private Integer totalMarks;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "MODULE_ID")
	private Modules modules;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "LABS_ID")
	private Labs labs;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "USER_ID")
	private User user;
	@Column(name = "ANSWER_SHEET")
	private String answerSheet;
	@Column(name = "FEEDBACK")
	private String feedback;
}
