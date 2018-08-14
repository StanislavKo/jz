package com.hsd.jz.api.db;

import java.util.Date;
import java.util.Iterator;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.KeyAttribute;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.hsd.jz.api.consts.Consts;
import com.hsd.jz.api.db.entity.Episode;
import com.hsd.jz.api.db.entity.JZUser;
import com.hsd.jz.api.pojo.EpisodeType;
import com.hsd.jz.api.utils.PrimitiveUtils;

public class DBUtils {

	private static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
	private static DynamoDB dynamoDB = new DynamoDB(client);

	public static boolean saveEpisode(EpisodeType type, String url, String title, Date created, String iframe, String description, String descriptionHtml) {
		DynamoDBMapper mapper = new DynamoDBMapper(client);

		Episode episode = new Episode();
		episode.setUrl(url);
		episode.setHash(PrimitiveUtils.getMd5(url));
		episode.setType(type.name());
		episode.setTitle(title);
		episode.setCreated(Consts.DB_CREATED_FORMAT.format(created));
		episode.setIframe(iframe);
		episode.setDescription(description);
		episode.setDescriptionHtml(descriptionHtml);
		episode.setEsIndexed(false);

		try {
			Episode result = mapper.load(episode);
			if (result != null) {
				return false;
			} else {
				mapper.save(episode);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void saveEpisode(Episode episode) {
		DynamoDBMapper mapper = new DynamoDBMapper(client);
		
		try {
			mapper.save(episode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Episode loadEpisode(String url) {
		DynamoDBMapper mapper = new DynamoDBMapper(client);

		Episode episode = new Episode();
		episode.setUrl(url);

		try {
			episode = mapper.load(episode);
			if (episode == null) {
				System.out.println("loadEpisode() null for url=" + url);
			}
			return episode;
		} catch (Exception e) {
		e.printStackTrace();
			return null;
		}
	}

	public static Iterator<Item> loadEpisodesNotESIndexed() {
		DynamoDBMapper mapper = new DynamoDBMapper(client);

		Table table = dynamoDB.getTable(Consts.DB_TABLE_EPISODE);
		Index index = table.getIndex(Consts.DB_INDEX_ES_INDEXED);

		ItemCollection<QueryOutcome> items = null;

//		QuerySpec querySpec = new QuerySpec();
//		querySpec.withKeyConditionExpression("isEsIndexed = 0");
//		items = index.query(querySpec);
		items = index.query(new KeyAttribute("isEsIndexed", 0));

		Iterator<Item> iterator = items.iterator();
		return iterator;

//		while (iterator.hasNext()) {
//			Item item = iterator.next();
//		}
	}

	public static void setESIndexed(String url, Boolean esIndexed) {
		Episode episode = loadEpisode(url);
		episode.setEsIndexed(esIndexed);
		saveEpisode(episode);
	}

	public static void saveUser(JZUser user) {
		DynamoDBMapper mapper = new DynamoDBMapper(client);
		
		try {
			mapper.save(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static JZUser loadUser(String username) {
		DynamoDBMapper mapper = new DynamoDBMapper(client);

		JZUser user = new JZUser();
		user.setUsername(username);

		try {
			return mapper.load(user);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static JZUser loadUserByFirebaseUid(String uid) {
		DynamoDBMapper mapper = new DynamoDBMapper(client);

		Table table = dynamoDB.getTable(Consts.DB_TABLE_USER);
		Index index = table.getIndex(Consts.DB_INDEX_USER_FIREBASE_UID);

		ItemCollection<QueryOutcome> items = null;

		items = index.query(new KeyAttribute("firebaseUid", uid));

		Iterator<Item> iterator = items.iterator();
		if (!iterator.hasNext()) {
			return null;
		}
		Item item = iterator.next();
		String username = item.getString("username");
		return loadUser(username);
	}

	public static Episode loadEpisodeByHash(String hash) {
		DynamoDBMapper mapper = new DynamoDBMapper(client);

		Table table = dynamoDB.getTable(Consts.DB_TABLE_EPISODE);
		Index index = table.getIndex(Consts.DB_INDEX_EPISODE_HASH);

		ItemCollection<QueryOutcome> items = null;

		items = index.query(new KeyAttribute("hash", hash));

		Iterator<Item> iterator = items.iterator();
		if (!iterator.hasNext()) {
			return null;
		}
		Item item = iterator.next();
		String url = item.getString("url");
		return loadEpisode(url);
	}

}
