package com.hsd.jz.server.es;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hsd.jz.api.consts.Consts;
import com.hsd.jz.server.es.pojo.SearchResult;

public class ESUtils {

	private static final String USERNAME = System.getenv("JZ_ES_USERNAME");
	private static final String PASSWORD = System.getenv("JZ_ES_PASSWORD");

	private static final Logger logger = LoggerFactory.getLogger(ESUtils.class);

//	private static RestHighLevelClient client;

	private static synchronized RestHighLevelClient getClient() {
//		if (client == null) {
			final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(USERNAME, PASSWORD));

			RestClientBuilder builder = RestClient.builder(new HttpHost(Consts.ES_HOST, Consts.ES_PORT, "http"))
					.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
						@Override
						public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
							return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
						}
					});

			return new RestHighLevelClient(builder);
//		}
//		
//		return client;
	}

	public static SearchResult search(String text, int offset, int limit) {
		RestHighLevelClient client = getClient();
		SearchRequest searchRequest = new SearchRequest(Consts.ES_INDEX_EPISODE);
		searchRequest.types("doc");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//		sourceBuilder.query(QueryBuilders.queryStringQuery(text));
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		boolQuery.should().add(QueryBuilders.matchQuery("created", text).boost(3)); // fuzzyQuery matchQuery
		for (String textOne : text.split(" ")) {
			boolQuery.should().add(QueryBuilders.fuzzyQuery("title", textOne).boost(3));
			boolQuery.should().add(QueryBuilders.fuzzyQuery("description", textOne).boost(1));
		}
		sourceBuilder.query(boolQuery);
		sourceBuilder.from(offset);
		sourceBuilder.size(limit);
		sourceBuilder.fetchSource(new String[] {"url", "title", "created", "description"}, new String[] {});
		searchRequest.source(sourceBuilder);
		
		List<String> episodeUrls = new LinkedList();
		long count = 0;
		
		try {
			SearchResponse searchResponse = client.search(searchRequest);
			SearchHits hits = searchResponse.getHits();
			SearchHit[] searchHits = hits.getHits();
			count = hits.getTotalHits();
			for (SearchHit hit : searchHits) {
				String type = hit.getType();
				String id = hit.getId();
				float score = hit.getScore();
				logger.debug("hit, [score:" + score + "] [type:" + type + "] [id:" + id + "] [title=" + hit.getSourceAsMap().get("title") + "]");
				logger.debug("hit, [url=" + hit.getSourceAsMap().get("url") + "]");
				episodeUrls.add(hit.getSourceAsMap().get("url").toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				client.close();
			} catch (IOException e) {
			}
		}
		logger.info("episodeUrls.size={}", episodeUrls.size());
		return new SearchResult(episodeUrls, count);
	}

}
