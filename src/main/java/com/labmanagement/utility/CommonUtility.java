package com.labmanagement.utility;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.labmanagement.bean.ResetPassword;
import com.labmanagement.bean.UserRegistration;
import com.labmanagement.common.Messages;
import com.labmanagement.repository.UserRepository;
import com.labmanagement.response.ErrorResponse;

@Component
public class CommonUtility {

	@Autowired
	private UserRepository userRepository;

	public void validateName(UserRegistration userRegistration, List<ErrorResponse> list) {
		if (!ValidationUtility.isValidName(userRegistration.getFullName())) {
			list.add(ErrorResponse.builder().field("name").errorMessage(Messages.NAME_IS_NOT_VALID).build());
		}
	}

	public void validatePassword(UserRegistration userRegistration, List<ErrorResponse> list) {
		if (userRegistration.getPassword().equals(userRegistration.getConfirmPassword())) {
			userRegistration.setPassword(SecurityUtility.passwordEncoder().encode(userRegistration.getPassword()));
		} else {
			list.add(ErrorResponse.builder().field("password").errorMessage(Messages.PASSWORD_IS_NOT_SAME).build());
		}
	}

	public boolean validatePassword(ResetPassword resetPassword) {
		if (resetPassword.getNewPassword().equals(resetPassword.getConfirmPassword())) {
			resetPassword.setNewPassword(SecurityUtility.passwordEncoder().encode(resetPassword.getConfirmPassword()));
			return true;
		}
		return false;
	}

	public void validateEmail(UserRegistration userRegistration, List<ErrorResponse> list) {
		var isExistByEmail = userRepository.findByUsername(userRegistration.getUsername());
		if (isExistByEmail.isPresent()) {
			list.add(ErrorResponse.builder().field("email").errorMessage(Messages.EMAIL_EXIST).build());
		}
	}

}
