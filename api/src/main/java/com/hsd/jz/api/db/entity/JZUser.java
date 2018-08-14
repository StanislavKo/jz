package com.hsd.jz.api.db.entity;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "JZ_USER")
public class JZUser {

	private String username;
	private String username2;
	private String passwordSalt;
	private String passwordHash;
	private String vkAccessToken;
	private String vkRefreshToken;
	private String okAccessToken;
	private String okRefreshToken;
	private String firebaseUid;
	private Long created;
	private boolean enabled;
	private String ip;
	private List<SearchTerm> searchTerms = new LinkedList();
	private List<AccessLogEntry> accessLog = new LinkedList();
	private List<FavoriteEpisode> favoriteEpisodes = new LinkedList();

	@DynamoDBHashKey(attributeName = "username")
	public String getUsername() {
		return username;
	}

	public JZUser setUsername(String username) {
		this.username = username;
		return this;
	}

	@DynamoDBAttribute(attributeName = "username2")
	public String getUsername2() {
		return username2;
	}

	public JZUser setUsername2(String username2) {
		this.username2 = username2;
		return this;
	}

	@DynamoDBAttribute(attributeName = "passwordSalt")
	public String getPasswordSalt() {
		return passwordSalt;
	}

	public JZUser setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
		return this;
	}

	@DynamoDBAttribute(attributeName = "passwordHash")
	public String getPasswordHash() {
		return passwordHash;
	}

	public JZUser setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
		return this;
	}

	@DynamoDBAttribute(attributeName = "vkAccessToken")
	public String getVkAccessToken() {
		return vkAccessToken;
	}

	public JZUser setVkAccessToken(String vkAccessToken) {
		this.vkAccessToken = vkAccessToken;
		return this;
	}

	@DynamoDBAttribute(attributeName = "vkRefreshToken")
	public String getVkRefreshToken() {
		return vkRefreshToken;
	}

	public JZUser setVkRefreshToken(String vkRefreshToken) {
		this.vkRefreshToken = vkRefreshToken;
		return this;
	}

	@DynamoDBAttribute(attributeName = "okAccessToken")
	public String getOkAccessToken() {
		return okAccessToken;
	}

	public JZUser setOkAccessToken(String okAccessToken) {
		this.okAccessToken = okAccessToken;
		return this;
	}

	@DynamoDBAttribute(attributeName = "okRefreshToken")
	public String getOkRefreshToken() {
		return okRefreshToken;
	}

	public JZUser setOkRefreshToken(String okRefreshToken) {
		this.okRefreshToken = okRefreshToken;
		return this;
	}

	@DynamoDBAttribute(attributeName = "firebaseUid")
	public String getFirebaseUid() {
		return firebaseUid;
	}

	public JZUser setFirebaseUid(String firebaseUid) {
		this.firebaseUid = firebaseUid;
		return this;
	}

	@DynamoDBAttribute(attributeName = "created")
	public Long getCreated() {
		return created;
	}

	public JZUser setCreated(Long created) {
		this.created = created;
		return this;
	}

	@DynamoDBAttribute(attributeName = "enabled")
	public boolean isEnabled() {
		return enabled;
	}

	public JZUser setEnabled(boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	@DynamoDBAttribute(attributeName = "ip")
	public String getIp() {
		return ip;
	}

	public JZUser setIp(String ip) {
		this.ip = ip;
		return this;
	}

	@DynamoDBAttribute(attributeName = "searchTerms")
	public List<SearchTerm> getSearchTerms() {
		return searchTerms;
	}

	public JZUser setSearchTerms(List<SearchTerm> searchTerms) {
		this.searchTerms = searchTerms;
		return this;
	}

	@DynamoDBAttribute(attributeName = "accessLog")
	public List<AccessLogEntry> getAccessLog() {
		return accessLog;
	}

	public JZUser setAccessLog(List<AccessLogEntry> accessLog) {
		this.accessLog = accessLog;
		return this;
	}

	@DynamoDBAttribute(attributeName = "favoriteEpisodes")
	public List<FavoriteEpisode> getFavoriteEpisodes() {
		return favoriteEpisodes;
	}

	public void setFavoriteEpisodes(List<FavoriteEpisode> favoriteEpisodes) {
		this.favoriteEpisodes = favoriteEpisodes;
	}

}
