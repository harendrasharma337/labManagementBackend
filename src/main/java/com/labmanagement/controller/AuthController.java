package com.labmanagement.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.labmanagement.common.BaseUrls;
import com.labmanagement.request.LoginRequest;
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

}
