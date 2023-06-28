package com.labmanagement.service;

import javax.servlet.http.HttpServletRequest;

import com.labmanagement.bean.UserRegistration;
import com.labmanagement.response.APIResponse;

public interface IUserService {

	APIResponse<Object> createUser(UserRegistration userRegistration, HttpServletRequest request);


}
