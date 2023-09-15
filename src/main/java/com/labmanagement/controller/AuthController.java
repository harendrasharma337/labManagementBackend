package com.labmanagement.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.labmanagement.bean.UserRegistration;
import com.labmanagement.common.BaseUrls;
import com.labmanagement.common.Constants;
import com.labmanagement.request.LoginRequest;
import com.labmanagement.response.APIResponse;
import com.labmanagement.response.JwtResponse;
import com.labmanagement.service.IAuthService;
import com.labmanagement.service.IUserService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(BaseUrls.AUTH_BASE_URL)
public class AuthController {

	private IAuthService service;
	
	private IUserService iUserService;

	@PostMapping(BaseUrls.LOGIN)
	public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
			HttpServletRequest request) {
		log.info("AuthController :: user authenticate start : {} ", loginRequest);
		return ResponseEntity.ok(service.login(loginRequest, request));
	}
	
	@PostMapping(BaseUrls.USER_CREATE)
	public ResponseEntity<APIResponse<Object>> createUser(@Valid @RequestBody UserRegistration userRegistration,
			HttpServletRequest request) {
		log.info("Inside UserController user registration start...");
		var response = iUserService.createUser(userRegistration, request);
		if (StringUtils.equals(response.getStatus(), Constants.SUCCESS.getValue()))
			return ResponseEntity.ok(response);
		return ResponseEntity.badRequest().body(response);
	}

}
