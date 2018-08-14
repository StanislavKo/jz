package com.hsd.jz.server.es.pojo;

import java.util.List;

public class SearchResult {

	private List<String> episodeUrls;
	private long count;

	public SearchResult(List<String> episodeUrls, long count) {
		super();
		this.episodeUrls = episodeUrls;
		this.count = count;
	}

	public List<String> getEpisodeUrls() {
		return episodeUrls;
	}

	public void setEpisodeUrls(List<String> episodeUrls) {
		this.episodeUrls = episodeUrls;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

}
