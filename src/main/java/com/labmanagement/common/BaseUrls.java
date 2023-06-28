package com.labmanagement.common;

public class BaseUrls {

	private BaseUrls() {
	}

	public static final String AUTH_BASE_URL = "/api/v1/auth";
	public static final String USER_BASE_URL = "/api/v1/user";
	public static final String ARTICLE_BASE_URL = "/api/v1/article";
	public static final String IS_LOGGED_IN_BASE_URL = "api/v1/";
	public static final String IS_LOGGED_IN_USER = "/isLoggedIn";
	public static final String REQUEST_PARAM_USERNAME = "/{username}";
	public static final String LOGIN = "/login";
	public static final String USER_CREATE = "/create";
	public static final String FORGOT_PASSWORD = "/forgotPassword";
	public static final String RESET_PASSWORD = "/resetPassword";
	public static final String EDIT = "/edit";
	public static final String SEARCH = "/search";

	/*** Admin URLS8 ***/
	public static final String ADMIN_BASE_URL = "/api/v1/admin";
	public static final String ALL_USER = "/getAllUsers";

}
