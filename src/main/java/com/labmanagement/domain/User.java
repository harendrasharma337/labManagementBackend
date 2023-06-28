package com.labmanagement.domain;

import java.util.Collection;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.labmanagement.config.Authority;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_details")
public class User implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "id")
	@Column(name = "ID", nullable = false, unique = true)
	private Long id;
	@Column(name = "FULLNAME")
	private String fullName;
	@Column(name = "EMAIL", nullable = false, unique = true)
	private String username;
	@JsonIgnore
	@Column(name = "PASSWORD")
	private String password;
	@Column(name = "ISACTIVE")
	private String isActive;
	@Column(name = "ISDELETED")
	private String isDeleted;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATEDATE", nullable = false)
	private Date createDate;
	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATEDATE", nullable = true)
	private Date updateDate;

	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<UserRole> userRoles = new HashSet<>();

	@JsonIgnore
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<>();
		userRoles.forEach(ur -> authorities.add(new Authority(ur.getRole().getName())));
		return authorities;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isEnabled() {
		return true;
	}

}
