package com.sjsu.faceit.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateSignUpData {
	
	//Regular Expressions
	private static final String NAME_REGEX     = "(\\p{Lower}+)";
	private static final String USERNAME_REGEX = "[A-Za-z0-9]+";
	private static final String PASSWORD_REGEX = "[A-Za-z0-9~!@#$%^&*()]+";
	private static final String EMAIL_REGEX    = "^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
	private static final String STATE_REGEX    = "AL|AK|AR|AZ|CA|CO|CT|DC|DE|FL|GA|HI|IA|ID|IL|IN|KS|KY|LA|MA|MD|ME|MI|MN|MO|MS|MT|NC|ND|NE|NH|NJ|NM|NV|NY|OH|OK|OR|PA|RI|SC|SD|TN|TX|UT|VA|VT|WA|WI|WV|WY";
	private static final String ZIP_REGEX      = "\\d{5}(-\\d{4})?";

	//Compiled Patterns for regular expressions
	private static final Pattern COMPILED_EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
	private static final Pattern COMPILED_STATE_PATTERN = Pattern.compile(STATE_REGEX, Pattern.CASE_INSENSITIVE);
	
	//Accepts upper-case and lower-case letters
	public static boolean validateFirstName(String name){
	    return name.toLowerCase().matches(NAME_REGEX);
	}
	
	//Accepts upper-case and lower-case letters
	public static boolean validateLastName(String name){
	    return validateFirstName(name);
	}
	
	//Accepts upper-case, lower-case and digits
	public static boolean validateUserName(String userName){
		return userName.matches(USERNAME_REGEX);
	}
	
	//Accepts all upper-case, lower-case, digits and special chars from set ~!@#$%^&*()
	public static boolean validatePassword(String password){
		return password.matches(PASSWORD_REGEX);
	}
	
	//validates email
	public static boolean validateEmail(String email){
	    CharSequence inputString = email;  
	    Matcher matcher = COMPILED_EMAIL_PATTERN.matcher(inputString);  
	    
	    return matcher.matches();
	}
	
	//Accepts two letter state initials in any case
	public static boolean validateState(String state){
		CharSequence inputString = state;  
	    Matcher matcher = COMPILED_STATE_PATTERN.matcher(inputString);  
	    
	    return matcher.matches();
	}
	
	//Accepts pattern 12345 or 12345-6789
	public static boolean validateZip(String zip){
		return zip.matches(ZIP_REGEX);
	}
	
}
