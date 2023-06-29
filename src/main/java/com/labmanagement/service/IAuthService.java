package com.labmanagement.service;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.labmanagement.domain.User;
import com.labmanagement.request.LoginRequest;
import com.labmanagement.response.APIResponse;
import com.labmanagement.response.JwtResponse;

public interface IAuthService {

	JwtResponse login(@Valid LoginRequest loginRequest, HttpServletRequest request);

	APIResponse<User> fetchByUsername(String username);

	APIResponse<User> getUserFromJwtToken(String token);
	
	String getUsername();

}
