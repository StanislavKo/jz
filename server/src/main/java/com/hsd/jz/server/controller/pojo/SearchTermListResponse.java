package com.hsd.jz.server.controller.pojo;

import java.util.List;

public class SearchTermListResponse extends GenericResponse {

	private List<String> searchTermList;

	public SearchTermListResponse(boolean loggedin, String error, List<String> searchTermList) {
		super(loggedin, error);
		this.searchTermList = searchTermList;
	}

	public List<String> getSearchTermList() {
		return searchTermList;
	}

	public void setSearchTermList(List<String> searchTermList) {
		this.searchTermList = searchTermList;
	}

}
