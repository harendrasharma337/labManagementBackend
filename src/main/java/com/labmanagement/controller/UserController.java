package com.labmanagement.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.labmanagement.bean.LabsBean;
import com.labmanagement.bean.ModulesBean;
import com.labmanagement.bean.Students;
import com.labmanagement.bean.UserBean;
import com.labmanagement.bean.UserRegistration;
import com.labmanagement.common.BaseUrls;
import com.labmanagement.common.Constants;
import com.labmanagement.common.Messages;
import com.labmanagement.domain.RoleType;
import com.labmanagement.domain.User;
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
	public ResponseEntity<APIResponse<Object>> createUser(@Valid @RequestBody UserRegistration userRegistration,
			HttpServletRequest request) {
		log.info("Inside UserController user registration start...");
		var response = iUserService.createUser(userRegistration, request);
		if (StringUtils.equals(response.getStatus(), Constants.SUCCESS.getValue()))
			return ResponseEntity.ok(response);
		return ResponseEntity.badRequest().body(response);
	}

	@GetMapping(BaseUrls.MODULES)
	public ResponseEntity<APIResponse<List<ModulesBean>>> fetchModules() {
		return ResponseEntity.ok(iUserService.fetchModules(authService.getUsername()));
	}

	@GetMapping(BaseUrls.FETCH_LAB_ASSISTENTS_MODULES_BY)
	public ResponseEntity<APIResponse<List<UserBean>>> fetchLabAssistentsModulesBy(@PathVariable Long id) {
		if (hasRole(RoleType.PROFESSOR))
			return ResponseEntity.ok(iUserService.fetchLabAssistentsModulesBy(id));
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(APIResponse.<List<UserBean>>builder()
				.status(Constants.FAILED.getValue()).message(Messages.ACCESS_DENIED.getValue()).build());
	}

	@GetMapping(BaseUrls.FETCH_STUDENTS_MODULES_BY)
	public ResponseEntity<APIResponse<List<Object>>> fetchStudentsModulesBy(@PathVariable Long id) {
		if (hasRole(RoleType.PROFESSOR))
			return ResponseEntity.ok(iUserService.fetchStudentsModulesBy(id));
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(APIResponse.<List<Object>>builder()
				.status(Constants.FAILED.getValue()).message(Messages.ACCESS_DENIED.getValue()).build());
	}

	@GetMapping(BaseUrls.FETCH_LABS_MODULES_BY)
	public ResponseEntity<APIResponse<List<LabsBean>>> fetchLabsModulesBy(@PathVariable Long moduleId) {
		if (hasRole(RoleType.PROFESSOR) || hasRole(RoleType.LAB_ASSISTANT)) {
			return ResponseEntity.ok(iUserService.fetchLabsModulesBy(moduleId));
		} else  if (hasRole(RoleType.STUDENT)){
			return ResponseEntity.ok(iUserService.fetchLabsModulesBy(moduleId));
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(APIResponse.<List<LabsBean>>builder()
					.status(Constants.FAILED.getValue()).message(Messages.ACCESS_DENIED.getValue()).build());
		}
		
		
	}

	@GetMapping(BaseUrls.FETCH_STUDENTS_BY_LABS)
	public ResponseEntity<APIResponse<List<Students>>> fetchStudentsByLabs(@PathVariable Long labId) {
		if (hasRole(RoleType.PROFESSOR))
			return ResponseEntity.ok(iUserService.fetchStudentsByLabs(labId));
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(APIResponse.<List<Students>>builder()
				.status(Constants.FAILED.getValue()).message(Messages.ACCESS_DENIED.getValue()).build());
	}

	@PostMapping(BaseUrls.UPLOAD_LABS)
	public ResponseEntity<APIResponse<String>> uploadLab(@RequestParam MultipartFile file,
			@RequestParam String expireDate, @RequestParam Integer totalMarks, @RequestParam Long moduleId) {
		log.info("UserController :: Lab upload start...");
		User user = isLoggedInUser();
		if (!ObjectUtils.isEmpty(user))
			return ResponseEntity.ok(iUserService.uploadLab(file, expireDate, totalMarks, moduleId, user));
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.<String>builder()
				.status(Constants.FAILED.getValue()).message(Messages.UNAUTHORIZED_USER.getValue()).build());

	}

	@PatchMapping(BaseUrls.UPDATE_LABS)
	public ResponseEntity<APIResponse<String>> updateLab(@RequestParam MultipartFile file, @RequestParam Long id,
			@RequestParam String expireDate, @RequestParam Integer totalLabsMarks) {
		log.info("UserController :: Lab update start...");
		User user = isLoggedInUser();
		if (!ObjectUtils.isEmpty(user)) {
			LabsBean lab = new LabsBean();
			try {
				lab.setId(id);
				lab.setExpireDate(new SimpleDateFormat("yyyy-MM-dd").parse(expireDate));
				lab.setTotalLabsMarks(totalLabsMarks);
				return ResponseEntity.ok(iUserService.updateLab(lab, file));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.<String>builder()
				.status(Constants.FAILED.getValue()).message(Messages.UNAUTHORIZED_USER.getValue()).build());

	}

	@PatchMapping(BaseUrls.UPDATE_STUDENT_MARKS)
	public ResponseEntity<APIResponse<String>> updateStudentMarks(@RequestBody Students students,
			@PathVariable Long labId) {
		log.info("UserController :: Student Marks update start...");
		User user = isLoggedInUser();
		if (!ObjectUtils.isEmpty(user))
			return ResponseEntity.ok(iUserService.updateStudentMarks(students, labId));
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.<String>builder()
				.status(Constants.FAILED.getValue()).message(Messages.UNAUTHORIZED_USER.getValue()).build());

	}

	@PostMapping(BaseUrls.PERSIST_STUDENT_EXCEL)
	public ResponseEntity<APIResponse<String>> uploadStudents(
			@RequestParam MultipartFile uploadfile, @PathVariable Long moduleId) {
		if (hasRole(RoleType.PROFESSOR))
			return ResponseEntity.ok(iUserService.uploadStudents(moduleId, uploadfile));
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(APIResponse.<String>builder()
				.status(Constants.FAILED.getValue()).message(Messages.ACCESS_DENIED.getValue()).build());
	}

	@PostMapping(BaseUrls.UPLOAD_STUDENT_REVIEW)
	public ResponseEntity<APIResponse<String>> uploadStudentReview(@RequestParam MultipartFile uploadfile,
			@PathVariable Long studentId, @PathVariable Long labId) {
		if (hasRole(RoleType.PROFESSOR))
			return ResponseEntity.ok(iUserService.uploadStudentReview(studentId, uploadfile, labId));
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(APIResponse.<String>builder()
				.status(Constants.FAILED.getValue()).message(Messages.ACCESS_DENIED.getValue()).build());
	}

	@PostMapping(BaseUrls.UPLOAD_ANSWER_SHEET)
	public ResponseEntity<APIResponse<String>> uploadAnswerSheet(@RequestParam MultipartFile uploadfile,
			@PathVariable Long answerSheet, @PathVariable Long labId) {
		if (hasRole(RoleType.STUDENT))
			return ResponseEntity.ok(iUserService.uploadAnswerSheet(answerSheet, uploadfile, labId));
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(APIResponse.<String>builder()
				.status(Constants.FAILED.getValue()).message(Messages.ACCESS_DENIED.getValue()).build());
	}
	

	private boolean hasRole(RoleType roleType) {
		return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.anyMatch((authority -> authority.getAuthority().equals(roleType.toString())));
	}

	private User isLoggedInUser() {
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

}
