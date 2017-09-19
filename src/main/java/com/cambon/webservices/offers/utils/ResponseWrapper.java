package com.cambon.webservices.offers.utils;

public class ResponseWrapper {

	private String body;
	private Integer status;

	public ResponseWrapper(String body, Integer status) {

		this.body = body;
		this.status = status;
	}

	public String getBody() {

		return body;
	}

	public Integer getStatus() {

		return status;
	}
}
