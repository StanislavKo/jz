package com.hsd.jz.espublisherlambda;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.hsd.jz.api.db.DBUtils;
import com.hsd.jz.api.db.entity.Episode;
import com.hsd.jz.espublisherlambda.es.ESUtils;

public class ESPublisherLambda implements RequestStreamHandler {

	@Override
	public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) {
		Iterator<Item> iterator = DBUtils.loadEpisodesNotESIndexed();
		while (iterator.hasNext()) {
			Item item = iterator.next();
			Episode episode = DBUtils.loadEpisode(item.getString("url"));
			System.out.println("item [title:" + episode.getTitle() + "]");
			if (ESUtils.index(episode.getUrl(), episode.getTitle(), episode.getCreated(), episode.getDescription())) {
				DBUtils.setESIndexed(episode.getUrl(), true);
			}
		}
	}

	public static void main(String[] args) {
		new ESPublisherLambda().handleRequest(null, null, null);
//		ESUtils.search("медик");
//		ESUtils.search("миллионы");
	}

}
