package com.hsd.jz.espublisherlambda.es;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.JSONObject;

import com.hsd.jz.api.consts.Consts;
import com.hsd.jz.api.notification.SESUtils;

public class ESUtils {

	private static final String USERNAME = System.getenv("JZ_ES_USERNAME");
	private static final String PASSWORD = System.getenv("JZ_ES_PASSWORD");

//	private static RestHighLevelClient client;

	private static synchronized RestHighLevelClient getClient() {
//		if (client == null) {
			final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(USERNAME, PASSWORD));

			RestClientBuilder builder = RestClient.builder(new HttpHost(Consts.ES_HOST, Consts.ES_PORT))
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

	public static boolean index(String url, String title, String created, String description) {
		RestHighLevelClient client = getClient();
		IndexRequest request = new IndexRequest(Consts.ES_INDEX_EPISODE, "doc");
		JSONObject jsonOffer = new JSONObject();
		jsonOffer.put("url", url);
		jsonOffer.put("title", title);
		jsonOffer.put("created", created);
		jsonOffer.put("description", description);
		String jsonString = jsonOffer.toString();
		request.source(jsonString, XContentType.JSON);
		try {
			client.index(request);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			SESUtils.send("New episode is not ES indexed [url:" + url + "]");
			return false;
		} finally {
			try {
				client.close();
			} catch (IOException e) {
			}
		}
	}

//	public static void search(String text) {
//		RestHighLevelClient client = getClient();
//		SearchRequest searchRequest = new SearchRequest(Consts.ES_INDEX_EPISODE);
//		searchRequest.types("doc");
//		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
////		sourceBuilder.query(QueryBuilders.queryStringQuery(text));
//		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
//		boolQuery.should().add(QueryBuilders.matchQuery("title", text).boost(3));
//		boolQuery.should().add(QueryBuilders.matchQuery("created", text).boost(3));
//		boolQuery.should().add(QueryBuilders.matchQuery("description", text).boost(1));
//		sourceBuilder.query(boolQuery);
//		sourceBuilder.from(0);
//		sourceBuilder.size(5);
//		sourceBuilder.fetchSource(new String[] {"url", "title", "created", "description"}, new String[] {});
//		searchRequest.source(sourceBuilder);
//		try {
//			SearchResponse searchResponse = client.search(searchRequest);
//			SearchHits hits = searchResponse.getHits();
//			SearchHit[] searchHits = hits.getHits();
//			for (SearchHit hit : searchHits) {
//				String type = hit.getType();
//				String id = hit.getId();
//				float score = hit.getScore();
//				System.out.println("hit, [score:" + score + "] [type:" + type + "] [id:" + id + "] [title=" + hit.getSourceAsMap().get("title") + "]");
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				client.close();
//			} catch (IOException e) {
//			}
//		}
//	}

}
