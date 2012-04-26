package com.sjsu.faceit.bean;

public class SignUpData {
	
	//variables to hold user sign up data
	private final String firstName;
	private final String lastName;
	private final String password;
	private final String email;
	private final String state;
	private final String zip;
	private final String securityQuestion;
	private final String securityAnswer;
	private final String imageKey;
	private final String imageData1;
	private final String imageData2;
	private final String imageData3;
	private final String imageData4;
	private final String imageData5;
	
	
	//Make the variables concrete by using constructor to initialize the variables
	public SignUpData(String firstName, String lastName,
			String password, String email, String state, String zip,
			String securityQuestion, String securityAnswer, 
			String imageKey, String imageData1, String imageData2, String imageData3, String imageData4, String imageData5) {
		
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.email = email;
		this.state = state;
		this.zip = zip;
		this.securityQuestion = securityQuestion;
		this.securityAnswer = securityAnswer;
		this.imageKey = imageKey;
		this.imageData1 = imageData1;
		this.imageData2 = imageData2;
		this.imageData3 = imageData3;
		this.imageData4 = imageData4;
		this.imageData5 = imageData5;
	}
	
	//Getters to get the values of each variable
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}

	public String getPassword() {
		return password;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getState() {
		return state;
	}
	
	public String getZip() {
		return zip;
	}
	
	public String getSecurityQuestion() {
		return securityQuestion;
	}

	public String getSecurityAnswer() {
		return securityAnswer;
	}

	public String getImageKey() {
		return imageKey;
	}

	public String getImageData1() {
		return imageData1;
	}

	public String getImageData2() {
		return imageData2;
	}

	public String getImageData3() {
		return imageData3;
	}

	public String getImageData4() {
		return imageData4;
	}

	public String getImageData5() {
		return imageData5;
	}
	
	
}
