package com.labmanagement.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Modules implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", nullable = false, unique = true)
	private Long id;
	@Column(name = "MODULE_NAME")
	private String moduleName;
	@Column(name = "MODULE_CODE")
	private String modelCode;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATEDATE", nullable = false)
	private Date createDate;
	
	@JsonIgnore
	@OneToMany(mappedBy = "modules", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<ModuleRelation> modulesRelation = new HashSet<>();
	
}
