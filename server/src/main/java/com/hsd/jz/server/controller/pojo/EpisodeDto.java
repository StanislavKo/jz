package com.hsd.jz.server.controller.pojo;

public class EpisodeDto {

	private String hash;
	private String type;
	private String title;
	private String created;
	private String iframe;
	private String description;
	private String descriptionHtml;
	private String note;

	public String getHash() {
		return hash;
	}

	public EpisodeDto setHash(String hash) {
		this.hash = hash;
		return this;
	}

	public String getType() {
		return type;
	}

	public EpisodeDto setType(String type) {
		this.type = type;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public EpisodeDto setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getCreated() {
		return created;
	}

	public EpisodeDto setCreated(String created) {
		this.created = created;
		return this;
	}

	public String getIframe() {
		return iframe;
	}

	public EpisodeDto setIframe(String iframe) {
		this.iframe = iframe;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public EpisodeDto setDescription(String description) {
		this.description = description;
		return this;
	}

	public String getDescriptionHtml() {
		return descriptionHtml;
	}

	public EpisodeDto setDescriptionHtml(String descriptionHtml) {
		this.descriptionHtml = descriptionHtml;
		return this;
	}

	public String getNote() {
		return note;
	}

	public EpisodeDto setNote(String note) {
		this.note = note;
		return this;
	}

}
