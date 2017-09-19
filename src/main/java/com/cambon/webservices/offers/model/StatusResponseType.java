package com.cambon.webservices.offers.model;

public enum StatusResponseType {

	SUCCESS("Success"), ERROR("Error");

	final private String status;

	StatusResponseType(String status) {

		this.status = status;
	}

	public String getStatus() {

		return status;
	}

}
