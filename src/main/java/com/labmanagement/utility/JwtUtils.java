package com.labmanagement.utility;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.labmanagement.bean.AuthenticateUser;
import com.labmanagement.config.ReadJWTConfig;
import com.labmanagement.domain.User;
import com.labmanagement.request.LoginRequest;
import com.labmanagement.response.JwtResponse;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class JwtUtils {

	private static final String BEARER = "Bearer";

	private static final Long SEVEN_DAYS_LOGIN_DURATION = 604800000L;

	private ReadJWTConfig jwtConfig;

	private ModelMapper modelMapper;

	public JwtResponse generateJwtToken(Authentication authentication, LoginRequest request) {
		final User userPrincipal = (User) authentication.getPrincipal();
		Long expireIn = request.isRememberMe() ? SEVEN_DAYS_LOGIN_DURATION : Long.parseLong(jwtConfig.getValidity());
		log.info("Inside JwtUtils generateJwtToken expireIn {}", expireIn);
		if (!ObjectUtils.isEmpty(userPrincipal)) {
			final AuthenticateUser authUser = getUserDetails(userPrincipal);
			return JwtResponse.builder()
					.accessToken(Jwts.builder().setSubject(userPrincipal.getUsername()).setIssuedAt(new Date())
							.setExpiration(new Date((new Date()).getTime() + expireIn))
							.signWith(SignatureAlgorithm.HS512, jwtConfig.getSecretKey()).compact())
					.tokenType(BEARER).authenticateUser(authUser).build();
		}
		return null;
	}

	public AuthenticateUser getUserDetails(final User user) {
		Set<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toSet());
		AuthenticateUser authenticateUser = modelMapper.map(user, AuthenticateUser.class);
		roles.stream().forEach(authenticateUser::setRole);
		return authenticateUser;
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtConfig.getSecretKey()).parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtConfig.getSecretKey()).parseClaimsJws(authToken).getBody();
			return true;
		} catch (SignatureException e) {
			log.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			log.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			log.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			log.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			log.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}
}
