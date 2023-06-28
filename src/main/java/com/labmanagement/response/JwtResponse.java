package com.labmanagement.response;

import java.io.Serializable;

import com.labmanagement.bean.AuthenticateUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String accessToken;
	private String tokenType;
	private Long expireIn;
	private AuthenticateUser authenticateUser;

}
