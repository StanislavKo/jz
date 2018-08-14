package com.hsd.jz.server.controller.pojo;

import java.util.List;

public class TestList {

	private List<Test> data;

	public TestList(List<Test> data) {
		super();
		this.data = data;
	}

	public List<Test> getData() {
		return data;
	}

	public void setData(List<Test> data) {
		this.data = data;
	}

}
