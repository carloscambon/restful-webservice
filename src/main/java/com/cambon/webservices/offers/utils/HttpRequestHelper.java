package com.cambon.webservices.offers.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

public class HttpRequestHelper {

	public static ResponseWrapper sendHttpRequest(RequestMethodsSupportedType method, String url, String payload) throws MalformedURLException, IOException {

		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setRequestMethod(method.name());

		if ("POST".equals(method.name()) || "PUT".equals(method.name())) {
			connection.setDoOutput(true);
			DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(payload);
			outputStream.flush();
			outputStream.close();
		}

		int status = connection.getResponseCode();
		String responseBody = "";
		try {
			responseBody = new BufferedReader(new InputStreamReader(connection.getInputStream())).lines().collect(Collectors.joining("\n"));
		} catch (Exception ignore) {

		}

		return new ResponseWrapper(responseBody, status);
	}

	public enum RequestMethodsSupportedType {
		GET, POST, PUT, DELETE;
	}
}
