package com.hsd.jz.server.controller.pojo;

import java.util.HashMap;
import java.util.Map;

public class EpisodeListResponse extends GenericResponse {

	private EpisodeListDto episodeList;

	public EpisodeListResponse(boolean loggedin, String error, EpisodeListDto episodeList) {
		super(loggedin, error);
		this.episodeList = episodeList;
	}

	public EpisodeListDto getEpisodeList() {
		return episodeList;
	}

	public void setEpisodeList(EpisodeListDto episodeList) {
		this.episodeList = episodeList;
	}

}
