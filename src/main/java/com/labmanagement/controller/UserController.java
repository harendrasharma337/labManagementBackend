package com.labmanagement.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.labmanagement.bean.Status;
import com.labmanagement.bean.UserRegistration;
import com.labmanagement.common.BaseUrls;
import com.labmanagement.response.APIResponse;
import com.labmanagement.service.IUserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(BaseUrls.USER_BASE_URL)
public class UserController {

	@Autowired
	private IUserService iUserService;

	@PostMapping(BaseUrls.USER_CREATE)
	public ResponseEntity<APIResponse<Object>> createUser(@Valid @RequestBody UserRegistration userRegistration,HttpServletRequest request) {
		log.info("Inside UserController user registration start...");
		var response = iUserService.createUser(userRegistration,request);
		if (StringUtils.equals(response.getStatus(), String.valueOf(Status.SUCCESS)))
			return ResponseEntity.ok(response);
		return ResponseEntity.badRequest().body(response);
	}

}
