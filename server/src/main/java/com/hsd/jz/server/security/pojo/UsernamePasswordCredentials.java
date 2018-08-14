package com.hsd.jz.server.security.pojo;

public class UsernamePasswordCredentials {

	private String username;
	private String password;

	public UsernamePasswordCredentials() {
		super();
	}

	public UsernamePasswordCredentials(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
