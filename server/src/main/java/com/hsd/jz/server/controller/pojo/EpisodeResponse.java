package com.hsd.jz.server.controller.pojo;

public class EpisodeResponse extends GenericResponse {

	private EpisodeDto episode;

	public EpisodeResponse(boolean loggedin, String error, EpisodeDto episode) {
		super(loggedin, error);
		this.episode = episode;
	}

	public EpisodeDto getEpisode() {
		return episode;
	}

	public void setEpisode(EpisodeDto episode) {
		this.episode = episode;
	}

}
