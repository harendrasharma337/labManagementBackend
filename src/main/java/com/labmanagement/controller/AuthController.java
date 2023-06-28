package com.labmanagement.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.labmanagement.common.BaseUrls;
import com.labmanagement.common.Constants;
import com.labmanagement.domain.User;
import com.labmanagement.exception.UserIsNotFoundException;
import com.labmanagement.request.LoginRequest;
import com.labmanagement.response.APIResponse;
import com.labmanagement.response.JwtResponse;
import com.labmanagement.service.IAuthService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(BaseUrls.AUTH_BASE_URL)
public class AuthController {

	private IAuthService service;

	@PostMapping(BaseUrls.LOGIN)
	public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
			HttpServletRequest request) {
		log.info("AuthController :: user authenticate start : {} ", loginRequest);
		return ResponseEntity.ok(service.login(loginRequest, request));
	}

	@GetMapping(BaseUrls.REQUEST_PARAM_USERNAME)
	public ResponseEntity<APIResponse<User>> fetchByUsername(@PathVariable String username) {
		if (username == null || username.isEmpty()) {
			throw new UserIsNotFoundException("Username can not be null or empty.");
		}
		return ResponseEntity.ok(service.fetchByUsername(username));
	}

	@GetMapping(BaseUrls.IS_LOGGED_IN_USER)
	public ResponseEntity<APIResponse<User>> getUserFromJwtToken(@RequestHeader(Constants.AUTHORIZATION) String token) {
		return ResponseEntity.ok(service.getUserFromJwtToken(token));
	}

}
