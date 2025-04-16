package com.jms.model;

public class UserSignUpEvent {
	private String userName;
	private String email;
	
	public UserSignUpEvent(String userName, String email) {
		this.userName = userName;
		this.email = email;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public String getEmail() {
		return email;
	}
	
	@Override
	public String toString() {
		return "UserSignUpEvent: (username: " + userName + "), (email: " + email + ")";
	}
}
