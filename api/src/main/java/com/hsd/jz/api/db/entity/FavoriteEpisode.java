package com.hsd.jz.api.db.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

@DynamoDBDocument
public class FavoriteEpisode {

	private String url;
	private String note;
	private long created;

	@DynamoDBAttribute(attributeName = "url")
	public String getUrl() {
		return url;
	}

	public FavoriteEpisode setUrl(String url) {
		this.url = url;
		return this;
	}

	@DynamoDBAttribute(attributeName = "note")
	public String getNote() {
		return note;
	}

	public FavoriteEpisode setNote(String note) {
		this.note = note;
		return this;
	}

	@DynamoDBAttribute(attributeName = "created")
	public long getCreated() {
		return created;
	}

	public FavoriteEpisode setCreated(long created) {
		this.created = created;
		return this;
	}

}
