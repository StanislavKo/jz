package com.hsd.jz.api.db.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

@DynamoDBDocument
public class AccessLogEntry {

	private String ip;
	private Long created;

	@DynamoDBAttribute(attributeName = "ip")
	public String getIp() {
		return ip;
	}

	public AccessLogEntry setIp(String ip) {
		this.ip = ip;
		return this;
	}

	@DynamoDBAttribute(attributeName = "created")
	public Long getCreated() {
		return created;
	}

	public AccessLogEntry setCreated(Long created) {
		this.created = created;
		return this;
	}

}
