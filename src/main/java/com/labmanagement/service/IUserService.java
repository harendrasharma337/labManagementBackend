package com.labmanagement.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.labmanagement.bean.LabsBean;
import com.labmanagement.bean.ModulesBean;
import com.labmanagement.bean.Students;
import com.labmanagement.bean.UserBean;
import com.labmanagement.bean.UserRegistration;
import com.labmanagement.domain.User;
import com.labmanagement.response.APIResponse;

public interface IUserService {

	APIResponse<Object> createUser(UserRegistration userRegistration, HttpServletRequest request);

	APIResponse<List<ModulesBean>> fetchModules(String username);

	APIResponse<List<UserBean>> fetchLabAssistentsModulesBy(Long id);

	APIResponse<List<Object>> fetchStudentsModulesBy(Long id);

	APIResponse<List<LabsBean>> fetchLabsModulesBy(Long moduleId);

	APIResponse<List<Students>> fetchStudentsByLabs(Long labId);

	APIResponse<String> uploadLab(MultipartFile file, String expireDate, Integer totalMarks, Long moduleId, User user);

	APIResponse<String> updateLab(LabsBean lab, MultipartFile file);

	APIResponse<String> updateStudentMarks(Students students, Long labId);

	APIResponse<String> uploadStudents(Long moduleId, MultipartFile file);

	APIResponse<String> uploadStudentReview(Long studentId, MultipartFile uploadfile);

}
