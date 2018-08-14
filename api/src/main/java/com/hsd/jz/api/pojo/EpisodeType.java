package com.hsd.jz.api.pojo;

import com.hsd.jz.api.consts.Consts;

public enum EpisodeType {

	LIFE("Про жизнь", Consts.EPISODE_TYPE_LIFE_URL), FOOD("Про еду", Consts.EPISODE_TYPE_FOOD_URL), MEDICINE("Про медицину",
			Consts.EPISODE_TYPE_MEDICINE_URL), HOME("Про дом", Consts.EPISODE_TYPE_HOME_URL), HEALTH("Здоровье", Consts.EPISODE_TYPE_HEALTH_URL);

	private String title;
	private String link;

	private EpisodeType(String title, String link) {
		this.title = title;
		this.link = link;
	}

	public String getTitle() {
		return title;
	}

	public String getLink() {
		return link;
	}

}
