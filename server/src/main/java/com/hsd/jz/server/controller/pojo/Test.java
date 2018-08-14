package com.hsd.jz.server.controller.pojo;

public class Test {

	private int code;
	private String title;
	private String date;
	private String text;

	public Test(int code, String title, String date, String text) {
		super();
		this.code = code;
		this.title = title;
		this.date = date;
		this.text = text;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
