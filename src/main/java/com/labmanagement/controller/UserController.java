package com.labmanagement.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.labmanagement.bean.ModulesBean;
import com.labmanagement.bean.Status;
import com.labmanagement.bean.UserBean;
import com.labmanagement.bean.UserRegistration;
import com.labmanagement.common.BaseUrls;
import com.labmanagement.response.APIResponse;
import com.labmanagement.service.AuthService;
import com.labmanagement.service.IUserService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(BaseUrls.USER_BASE_URL)
public class UserController {

	private IUserService iUserService;
	
	private AuthService authService;
	
	

	@PostMapping(BaseUrls.USER_CREATE)
	public ResponseEntity<APIResponse<Object>> createUser(@Valid @RequestBody UserRegistration userRegistration,HttpServletRequest request) {
		log.info("Inside UserController user registration start...");
		var response = iUserService.createUser(userRegistration,request);
		if (StringUtils.equals(response.getStatus(), String.valueOf(Status.SUCCESS)))
			return ResponseEntity.ok(response);
		return ResponseEntity.badRequest().body(response);
	}
	
	@GetMapping(BaseUrls.MODULES)
	public ResponseEntity<APIResponse<List<ModulesBean>>> fetchModules(){
		return ResponseEntity.ok(iUserService.fetchModules(authService.getUsername()));
	}
	
	@GetMapping(BaseUrls.FETCH_LAB_ASSISTENTS_MODULES_BY)
	public ResponseEntity<APIResponse<List<UserBean>>> fetchLabAssistentsModulesBy(@PathVariable Long id){
		return ResponseEntity.ok(iUserService.fetchLabAssistentsModulesBy(id));
	}

}
