package com.labmanagement.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.labmanagement.service.UserDetailService;
import com.labmanagement.utility.JwtUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {

	private static final String BEARER = "Bearer ";

	private static final String AUTHORIZATION = "Authorization";

	private static final String EXPIRED = "expired";

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private UserDetailService userDetailService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			final String jwt = parseJwt(request);
			if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
				final String username = jwtUtils.getUserNameFromJwtToken(jwt);
				final UserDetails userDetails = userDetailService.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, "", userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception e) {
			request.setAttribute(EXPIRED, e.getMessage());
			log.error("Cannot set user authentication: {}", e.getMessage());
		}
		filterChain.doFilter(request, response);
	}

	private String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader(AUTHORIZATION);
		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
			return headerAuth.substring(7, headerAuth.length());
		}
		return null;
	}
}