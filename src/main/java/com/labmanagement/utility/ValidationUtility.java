package com.labmanagement.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtility {

	private ValidationUtility() {
	}

	public static boolean isMobileValid(String mobile) {
		Pattern p = Pattern.compile("^\\d{10}$");
		Matcher m = p.matcher(mobile);
		return m.matches();
	}
	
	public static boolean isValidName(String name) {
		Pattern p = Pattern.compile("^[\\p{L} .'-]+$");
		Matcher m = p.matcher(name);
		return m.matches();
	}

}
