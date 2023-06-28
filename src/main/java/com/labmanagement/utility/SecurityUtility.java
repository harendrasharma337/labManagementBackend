package com.labmanagement.utility;

import java.security.SecureRandom;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtility {

	private SecurityUtility() {
	}
	
	private static final String SALT = "authentic@info&*!123";// Salt should be protected carefully

	@Bean
	public static BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12, new SecureRandom(SALT.getBytes()));
	}
	
}
