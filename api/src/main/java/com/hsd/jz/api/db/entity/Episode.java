package com.hsd.jz.api.db.entity;

import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "JZ_EPISODE")
public class Episode {

	private String url;
	private String hash;
	private String type;
	private String title;
	private String created;
	private String iframe;
	private String description;
	private String descriptionHtml;
	private boolean isEsIndexed;

	@DynamoDBHashKey(attributeName = "url")
	public String getUrl() {
		return url;
	}

	public Episode setUrl(String url) {
		this.url = url;
		return this;
	}

	@DynamoDBAttribute(attributeName = "hash")
	public String getHash() {
		return hash;
	}

	public Episode setHash(String hash) {
		this.hash = hash;
		return this;
	}

	@DynamoDBAttribute(attributeName = "type")
	public String getType() {
		return type;
	}

	public Episode setType(String type) {
		this.type = type;
		return this;
	}

	@DynamoDBAttribute(attributeName = "title")
	public String getTitle() {
		return title;
	}

	public Episode setTitle(String title) {
		this.title = title;
		return this;
	}

	@DynamoDBAttribute(attributeName = "created")
	public String getCreated() {
		return created;
	}

	public Episode setCreated(String created) {
		this.created = created;
		return this;
	}

	@DynamoDBAttribute(attributeName = "iframe")
	public String getIframe() {
		return iframe;
	}

	public Episode setIframe(String iframe) {
		this.iframe = iframe;
		return this;
	}

	@DynamoDBAttribute(attributeName = "description")
	public String getDescription() {
		return description;
	}

	public Episode setDescription(String description) {
		this.description = description;
		return this;
	}

	@DynamoDBAttribute(attributeName = "descriptionHtml")
	public String getDescriptionHtml() {
		return descriptionHtml;
	}

	public void setDescriptionHtml(String descriptionHtml) {
		this.descriptionHtml = descriptionHtml;
	}

	@DynamoDBAttribute(attributeName = "isEsIndexed")
	public boolean isEsIndexed() {
		return isEsIndexed;
	}

	public Episode setEsIndexed(boolean isEsIndexed) {
		this.isEsIndexed = isEsIndexed;
		return this;
	}

}
