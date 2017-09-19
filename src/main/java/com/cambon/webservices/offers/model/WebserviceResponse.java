package com.cambon.webservices.offers.model;

import com.google.gson.JsonElement;

public class WebserviceResponse {

	private String status;
	private String errorMessage;
	private JsonElement data;

	public WebserviceResponse(String status, JsonElement data) {

		this.status = status;
		this.data = data;
	}

	public WebserviceResponse(String errorMessage, String status) {

		this.errorMessage = errorMessage;
		this.status = status;
	}

}
