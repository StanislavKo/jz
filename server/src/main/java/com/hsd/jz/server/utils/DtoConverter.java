package com.hsd.jz.server.utils;

import java.util.Map;
import java.util.Optional;

import com.hsd.jz.api.db.entity.Episode;
import com.hsd.jz.api.db.entity.FavoriteEpisode;
import com.hsd.jz.server.controller.pojo.EpisodeDto;

public class DtoConverter {

	public static EpisodeDto getEpisodeDto(Episode episode, Map<String, FavoriteEpisode> userEpisodesNote) {
		String note = Optional.ofNullable(userEpisodesNote.get(episode.getUrl())).map(FavoriteEpisode::getNote).orElse("");
		return new EpisodeDto().setHash(episode.getHash()).setTitle(episode.getTitle()).setCreated(episode.getCreated())
				.setDescription(episode.getDescription()).setDescriptionHtml(episode.getDescriptionHtml()).setType(episode.getType())
				.setIframe(episode.getIframe()).setNote(note);
	}

}
