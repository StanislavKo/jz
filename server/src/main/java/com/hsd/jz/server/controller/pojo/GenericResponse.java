package com.hsd.jz.server.controller.pojo;

public class GenericResponse {

	private boolean loggedin;
	private String error;

	public GenericResponse(boolean loggedin, String error) {
		super();
		this.loggedin = loggedin;
		this.error = error;
	}

	public boolean isLoggedin() {
		return loggedin;
	}

	public void setLoggedin(boolean loggedin) {
		this.loggedin = loggedin;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
