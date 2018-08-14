package com.hsd.jz.api.db.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

@DynamoDBDocument
public class SearchTerm {

	private String phrase;
	private Long created;
	private boolean visible;

	@DynamoDBAttribute(attributeName = "phrase")
	public String getPhrase() {
		return phrase;
	}

	public SearchTerm setPhrase(String phrase) {
		this.phrase = phrase;
		return this;
	}

	@DynamoDBAttribute(attributeName = "created")
	public Long getCreated() {
		return created;
	}

	public SearchTerm setCreated(Long created) {
		this.created = created;
		return this;
	}

	@DynamoDBAttribute(attributeName = "visible")
	public boolean isVisible() {
		return visible;
	}

	public SearchTerm setVisible(boolean visible) {
		this.visible = visible;
		return this;
	}

}
