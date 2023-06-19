package com.labmanagement.service;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.labmanagement.bean.Status;
import com.labmanagement.common.Messages;
import com.labmanagement.domain.User;
import com.labmanagement.exception.UserIsNotFoundException;
import com.labmanagement.exception.UserNotAutharizedException;
import com.labmanagement.repository.UserRepository;
import com.labmanagement.request.LoginRequest;
import com.labmanagement.response.APIResponse;
import com.labmanagement.response.JwtResponse;
import com.labmanagement.utility.JwtUtils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class AuthService implements IAuthService {

	private JwtUtils jwtUtils;

	private UserRepository userRepository;

	private AuthenticationManager authenticationManager;


	@Override
	public JwtResponse login(@Valid LoginRequest loginRequest, HttpServletRequest request) {
		log.info("AuthService :: User authentication start...");
		JwtResponse response = null;
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			response = jwtUtils.generateJwtToken(authentication, loginRequest);
			log.info("User is authenticated.....");
		} catch (Exception e) {
			throw new UserNotAutharizedException("Invalid username or password...!");
		}
		return response;
	}

	@Override
	public APIResponse<User> fetchByUsername(String username) {
		Optional<User> user = userRepository.findByUsername(username);
		if (!user.isPresent()) {
			throw new UserIsNotFoundException(Messages.USER_IS_NOT_FOUND_WITH_USERNAME + username);
		}
		return APIResponse.<User>builder().status(Status.SUCCESS.toString()).message(Messages.DATA_FETCHED_SUCCESSFULLY)
				.results(user.get()).build();
	}

	@Override
	public APIResponse<User> getUserFromJwtToken(String token) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			log.info("User authentication username: {}", ((UserDetails) principal).getUsername());
			Optional<User> user = userRepository.findByUsername(((UserDetails) principal).getUsername());
			if (user.isPresent()) {
				return APIResponse.<User>builder().status(Status.SUCCESS.toString())
						.message(Messages.DATA_FETCHED_SUCCESSFULLY).results(user.get()).build();
			}
		}
		throw new UserIsNotFoundException(Messages.USERNAME_IS_NOT_FOUND);
	}

}
