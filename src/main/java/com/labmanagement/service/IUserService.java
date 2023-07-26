package com.labmanagement.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.labmanagement.bean.LabsBean;
import com.labmanagement.bean.ModulesBean;
import com.labmanagement.bean.Students;
import com.labmanagement.bean.UserBean;
import com.labmanagement.bean.UserRegistration;
import com.labmanagement.response.APIResponse;

public interface IUserService {

	APIResponse<Object> createUser(UserRegistration userRegistration, HttpServletRequest request);

	APIResponse<List<ModulesBean>> fetchModules(String username);

	APIResponse<List<UserBean>> fetchLabAssistentsModulesBy(Long id);

	APIResponse<List<Students>> fetchStudentsModulesBy(Long id);

	APIResponse<List<LabsBean>> fetchLabsModulesBy(Long moduleId);

}
