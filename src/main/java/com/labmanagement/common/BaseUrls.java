package com.labmanagement.common;

public class BaseUrls {

	private BaseUrls() {
	}

	public static final String AUTH_BASE_URL = "/api/v1/auth";
	public static final String USER_BASE_URL = "/api/v1/user";
	public static final String IS_LOGGED_IN_BASE_URL = "api/v1/";
	
	public static final String IS_LOGGED_IN_USER = "/isLoggedIn";
	public static final String REQUEST_PARAM_USERNAME = "/{username}";
	public static final String LOGIN = "/login";
	public static final String USER_CREATE = "/create";
	public static final String FETCH_LAB_ASSISTENTS_MODULES_BY = "/lab/assistents/{id}";
	public static final String FETCH_STUDENTS_MODULES_BY = "/students/{id}";
	public static final String FETCH_LABS_MODULES_BY = "/module/{moduleId}/labs";
	public static final String FETCH_STUDENTS_BY_LABS = "/lab/{labId}/students";
	public static final String MODULES = "/modules";
	public static final String UPLOAD_LABS = "/labs/upload";
	public static final String UPDATE_LABS = "/labs/update";
	public static final String UPDATE_STUDENT_MARKS = "/lab/{labId}/student/marks/update";
	public static final String PERSIST_STUDENT_EXCEL = "/moduleId/{moduleId}/upload/students";
	public static final String UPLOAD_STUDENT_REVIEW = "/studentId/{studentId}/upload/review";
	
	// application properties 
	public static final String UPLOAD_REVIEW_DIRECTORY = "upload.review.directory";
	



}
