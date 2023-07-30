package com.labmanagement.service;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.labmanagement.common.Messages;
import com.labmanagement.domain.User;
import com.labmanagement.exception.UserIsNotFoundException;
import com.labmanagement.exception.UserNotAutharizedException;
import com.labmanagement.repository.UserRepository;

@Service
public class UserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UserIsNotFoundException {
		Optional<User> user = userRepository.findByUsername(username);
		if (!user.isPresent()) {
			throw new UserNotAutharizedException(Messages.UNAUTHORIZED_USER.getValue());
		}
		if (StringUtils.equals("N", user.get().getIsActive())) {
			throw new UserNotAutharizedException(Messages.USER_IS_NOT_ACTIVE.getValue());
		}
		return user.get();
	}
}
