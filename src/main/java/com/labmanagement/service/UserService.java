package com.labmanagement.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.labmanagement.bean.Status;
import com.labmanagement.bean.UserRegistration;
import com.labmanagement.common.Messages;
import com.labmanagement.domain.Role;
import com.labmanagement.domain.RoleType;
import com.labmanagement.domain.User;
import com.labmanagement.domain.UserRole;
import com.labmanagement.repository.RoleRepository;
import com.labmanagement.repository.UserRepository;
import com.labmanagement.response.APIResponse;
import com.labmanagement.response.ErrorResponse;
import com.labmanagement.utility.CommonUtility;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class UserService implements IUserService {
	
	private UserRepository userRepository;

	private ModelMapper modelMapper;

	private RoleRepository roleRepository;
	
	private CommonUtility commonUtility;


	@Override
	public APIResponse<Object> createUser(UserRegistration userRegistration, HttpServletRequest request) {
		log.info("Inside UserService user registeration start...");
		List<ErrorResponse> errorResponse = validateUser(userRegistration);
		if (errorResponse.isEmpty()) {
			saveUser(userRegistration);
			log.info("Inside UserService user registered sucessfully...");
			return APIResponse.<Object>builder().status(String.valueOf(Status.SUCCESS))
					.message(Messages.USER_REGISTER_SUCCESSFULLY).build();
		}
		return APIResponse.<Object>builder().status(String.valueOf(Status.FAILED)).errors(errorResponse)
				.message(Messages.USER_VALIDATION_FAILED).build();
	}

	private User saveUser(UserRegistration userRegistration) {
		log.info("Inside UserService saving user...");
		var user = modalMapper(userRegistration);
		Role role = new Role();
		setRoles(userRegistration, role);
		Set<UserRole> userRole = new HashSet<>();
		userRole.add(new UserRole(user, role));
		userRole.forEach(roles -> roleRepository.save(roles.getRole()));
		user.getUserRoles().addAll(userRole);
		return userRepository.save(user);
	}

	private void setRoles(UserRegistration userRegistration, Role role) {
		if (userRegistration.getRole().equals("ADMIN")) {
			role.setRoleId(1L);
			role.setName(RoleType.ADMIN.toString());
		} else if (userRegistration.getRole().equals("STUDENT")) {
			role.setRoleId(2L);
			role.setName(RoleType.STUDENT.toString());
		} else if (userRegistration.getRole().equals("PROFESSOR")) {
			role.setRoleId(3L);
			role.setName(RoleType.PROFESSOR.toString());
		} else if (userRegistration.getRole().equals("LAB_ASSISTANT")) {
			role.setRoleId(4L);
			role.setName(RoleType.LAB_ASSISTANT.toString());
		}
	}

	private User modalMapper(UserRegistration userRegistration) {
		var user = modelMapper.map(userRegistration, User.class);
		user.setCreateDate(new Date());
		user.setIsActive("N");
		user.setIsDeleted("N");
		return user;
	}
	
	private List<ErrorResponse> validateUser(UserRegistration userRegistration) {
		log.info("Inside UserService validating user...");
		List<ErrorResponse> list = new ArrayList<>();
		commonUtility.validateName(userRegistration, list);
		commonUtility.validatePassword(userRegistration, list);
		commonUtility.validateEmail(userRegistration, list);
		return list;
	}
}
