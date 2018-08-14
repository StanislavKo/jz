package com.hsd.jz.api.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.stream.Collectors;

public class NetworkUtils {

	public static String loadUrlAsData(String url) {
		try {
			URL fileUrl = new URL(url);

			final HttpURLConnection connection = (HttpURLConnection) fileUrl.openConnection();
			connection.connect();

			int responseCode = connection.getResponseCode();
			System.out.println("loadUrlAsData() [responseCode:" + responseCode + "]");

			try (InputStream is = connection.getInputStream()) {
				return new BufferedReader(new InputStreamReader(is, "UTF-8")).lines().collect(Collectors.joining("\n"));
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String loadPostUrlAsData(String url) {
		try {
			URL fileUrl = new URL(url);

			final HttpURLConnection  connection = (HttpURLConnection) fileUrl.openConnection();
			connection.setRequestMethod("POST");
			connection.connect();

			int responseCode = connection.getResponseCode();
			System.out.println("loadUrlAsData() [responseCode:" + responseCode + "]");

			try (InputStream is = connection.getInputStream()) {
				return new BufferedReader(new InputStreamReader(is, "UTF-8")).lines().collect(Collectors.joining("\n"));
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
