package com.labmanagement.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Labs implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", nullable = false, unique = true)
	private Long id;
	@Column(name = "FILENAME")
	private String fileName;
	@Column(name = "WEEK")
	private Integer week;
	@Column(name = "UPLOADEDBY")
	private String uploadedBy;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATEDATE", nullable = false)
	private Date createDate;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXPIREDATE", nullable = false)
	private Date expireDate;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "MODULE_ID")
	private Modules modules;
}
