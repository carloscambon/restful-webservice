package com.cambon.webservices.offers;

import com.cambon.webservices.offers.model.Offer;
import com.cambon.webservices.offers.utils.HttpRequestHelper;
import com.cambon.webservices.offers.utils.ResponseWrapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OfferWebserviceTest {

	private static final String TEST_OFFER =
					"{ \"title\": \"Washing machine. 1 year use.\"," +
					" \"price\": \"199.99\"," +
					" \"description\": \"In warranty. Brand xyz. Free delivery within UK\" }";
	JsonObject offerRequest = (new JsonParser()).parse(TEST_OFFER).getAsJsonObject();
	final String OFFER_WEBSERVICE_URL = "http://localhost:8080/services/offers";
	private static final String WRONG_TEST_OFFER_REQUEST =
			"{ \"title\": \"Washing machine. 1 year use.\"," +
					" \"price\": \"xxx 199.99\"," +
					" \"description\": \"In warranty. Brand xyz. Free delivery within UK\" }";
	@BeforeAll
	public void setUp() {

		OffersWebservice.startServer();
	}

	@AfterAll
	public void tearDown() {

		OffersWebservice.stopServer();
	}

	@Test
	public void createsOffer() {

		try {
			ResponseWrapper responseWrapper = HttpRequestHelper.sendHttpRequest(HttpRequestHelper.RequestMethodsSupportedType.POST, OFFER_WEBSERVICE_URL, TEST_OFFER);
			JsonObject offerResponse = extractResponseData(responseWrapper);

			Assertions.assertTrue(responseWrapper.getStatus() == HttpStatus.SC_CREATED);
			Assertions.assertTrue(offerRequest.get("description").equals(offerResponse.get("description")));
			Assertions.assertTrue(offerRequest.get("title").equals(offerResponse.get("title")));
			Assertions.assertTrue(offerResponse.get("price").getAsString().equals(offerRequest.get("price").getAsString() + " GBP"));
			Assertions.assertTrue(offerResponse.get("id") != null);
		} catch (IOException e) {
			Assertions.fail(e.getMessage());
		}
	}

	@Test
	public void updatesOffer() {

		try {
			ResponseWrapper offerCreationResponse = HttpRequestHelper.sendHttpRequest(HttpRequestHelper.RequestMethodsSupportedType.POST, OFFER_WEBSERVICE_URL, TEST_OFFER);
			JsonObject offerCreationJson = extractResponseData(offerCreationResponse);
			Assertions.assertTrue(offerCreationResponse.getStatus() == HttpStatus.SC_CREATED);

			Offer updatedOffer = new Offer();
			updatedOffer.setId(offerCreationJson.get("id").getAsString());
			updatedOffer.setDescription("Changed description");
			updatedOffer.setTitle("Changed title");
			updatedOffer.setPrice("150.55555");
			ResponseWrapper offerUpdateResponse = HttpRequestHelper.sendHttpRequest(HttpRequestHelper.RequestMethodsSupportedType.PUT,
					OFFER_WEBSERVICE_URL + "/" + offerCreationJson.get("id").getAsString(), new Gson().toJson(updatedOffer, Offer.class));

			JsonObject offerUpdateJson = extractResponseData(offerUpdateResponse);

			Assertions.assertTrue(offerUpdateResponse.getStatus() == HttpStatus.SC_OK);
			Assertions.assertTrue(offerUpdateJson.get("description").getAsString().equals("Changed description"));
			Assertions.assertTrue(offerUpdateJson.get("title").getAsString().equals("Changed title"));
			Assertions.assertTrue(offerUpdateJson.get("price").getAsString().equals( "150.56 GBP"));
			Assertions.assertTrue(offerUpdateJson.get("id").getAsString().equals(offerCreationJson.get("id").getAsString()));
		} catch (IOException e) {
			Assertions.fail(e.getMessage());
		}
	}

	@Test
	public void retrievesOffer() {

		try {

			ResponseWrapper offerCreationResponse = HttpRequestHelper.sendHttpRequest(HttpRequestHelper.RequestMethodsSupportedType.POST, OFFER_WEBSERVICE_URL, TEST_OFFER);
			JsonObject offerCreationJson = extractResponseData(offerCreationResponse);
			Assertions.assertTrue(offerCreationResponse.getStatus() == HttpStatus.SC_CREATED);

			ResponseWrapper offerRetrievalResponse = HttpRequestHelper.sendHttpRequest(HttpRequestHelper.RequestMethodsSupportedType.GET,
					OFFER_WEBSERVICE_URL + "/" + offerCreationJson.get("id").getAsString(), null);
			JsonObject offerRetrievalJson = extractResponseData(offerRetrievalResponse);

			Assertions.assertTrue(offerRetrievalResponse.getStatus() == HttpStatus.SC_OK);
			Assertions.assertTrue(offerRetrievalJson.get("description").equals(offerCreationJson.get("description")));
			Assertions.assertTrue(offerRetrievalJson.get("title").equals(offerCreationJson.get("title")));
			Assertions.assertTrue(offerRetrievalJson.get("price").getAsString().equals(offerCreationJson.get("price").getAsString()));
			Assertions.assertTrue(offerCreationJson.get("id").equals(offerRetrievalJson.get("id")));
		} catch (IOException e) {
			Assertions.fail(e.getMessage());
		}
	}

	@Test
	public void testListOffers() throws IOException {

		try {
			ResponseWrapper postResponseWrapper = HttpRequestHelper.sendHttpRequest(HttpRequestHelper.RequestMethodsSupportedType.POST, OFFER_WEBSERVICE_URL, TEST_OFFER);
			Assertions.assertTrue(postResponseWrapper.getStatus() == HttpStatus.SC_CREATED);

			ResponseWrapper getResponseWrapper = HttpRequestHelper.sendHttpRequest(HttpRequestHelper.RequestMethodsSupportedType.GET, OFFER_WEBSERVICE_URL, null);
			Assertions.assertTrue(getResponseWrapper.getStatus() == HttpStatus.SC_OK);
			JsonArray offerArray = (new JsonParser()).parse(getResponseWrapper.getBody()).getAsJsonObject().get("data").getAsJsonArray();
			Assertions.assertTrue(offerArray.size() > 0);

		} catch (IOException e) {
			Assertions.fail(e.getMessage());
		}
	}

	@Test
	public void deletesOffer() {
		try {
			ResponseWrapper offerCreationResponse = HttpRequestHelper.sendHttpRequest(HttpRequestHelper.RequestMethodsSupportedType.POST, OFFER_WEBSERVICE_URL, TEST_OFFER);
			JsonObject offerCreationJson = extractResponseData(offerCreationResponse);
			Assertions.assertTrue(offerCreationResponse.getStatus() == HttpStatus.SC_CREATED);

			ResponseWrapper offerDeletionResponse = HttpRequestHelper.sendHttpRequest(HttpRequestHelper.RequestMethodsSupportedType.DELETE,
					OFFER_WEBSERVICE_URL + "/" + offerCreationJson.get("id").getAsString(), null);
			JsonObject offerDeletionJson = extractResponseData(offerDeletionResponse);

			Assertions.assertTrue(offerDeletionResponse.getStatus() == HttpStatus.SC_OK);
			Assertions.assertTrue(offerDeletionJson.get("description").equals(offerCreationJson.get("description")));
			Assertions.assertTrue(offerDeletionJson.get("title").equals(offerCreationJson.get("title")));
			Assertions.assertTrue(offerDeletionJson.get("price").getAsString().equals(offerCreationJson.get("price").getAsString()));
			Assertions.assertTrue(offerCreationJson.get("id").equals(offerDeletionJson.get("id")));

			offerDeletionResponse = HttpRequestHelper.sendHttpRequest(HttpRequestHelper.RequestMethodsSupportedType.DELETE,
					OFFER_WEBSERVICE_URL + "/" + offerCreationJson.get("id").getAsString(), null);
			Assertions.assertTrue(offerDeletionResponse.getStatus() == HttpStatus.SC_NOT_FOUND);
		} catch (IOException e) {
			Assertions.fail(e.getMessage());
		}
	}

	@Test
	public void badRequestReturns400() {

		try {
			ResponseWrapper responseWrapper = HttpRequestHelper.sendHttpRequest(HttpRequestHelper.RequestMethodsSupportedType.POST, OFFER_WEBSERVICE_URL, WRONG_TEST_OFFER_REQUEST);
			Assertions.assertTrue(responseWrapper.getStatus() == HttpStatus.SC_BAD_REQUEST);
		} catch (IOException e) {
			Assertions.fail(e.getMessage());
		}
	}

	@Test
	public void resourceNotFoundReturns404() {

		try {
			ResponseWrapper responseWrapper = HttpRequestHelper.sendHttpRequest(HttpRequestHelper.RequestMethodsSupportedType.GET,
					OFFER_WEBSERVICE_URL + "/wrongId", null);
			Assertions.assertTrue(responseWrapper.getStatus() == HttpStatus.SC_NOT_FOUND);
		} catch (IOException e) {
			Assertions.fail(e.getMessage());
		}
	}

	private JsonObject extractResponseData(ResponseWrapper responseWrapper) {

		return (new JsonParser()).parse(responseWrapper.getBody()).getAsJsonObject().get("data").getAsJsonObject();
	}
}
