package com.cambon.webservices.offers;

import com.cambon.webservices.offers.model.Offer;
import com.cambon.webservices.offers.model.StatusResponseType;
import com.cambon.webservices.offers.model.WebserviceResponse;
import com.cambon.webservices.offers.persistence.EntityMemoryPersistor;
import com.google.gson.Gson;
import org.apache.http.HttpStatus;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import spark.Response;

import java.util.UUID;

import static spark.Spark.*;

public class OffersWebservice {

	private static final EntityMemoryPersistor<Offer> offerPersistor = new EntityMemoryPersistor();
	private static final int PORT;
	private static final String DEFAULT_SERVER_PORT = "8080";
	private static final Logger logger = Logger.getLogger(OffersWebservice.class);

	static {
		PORT = Integer.parseInt(System.getProperty("server.port", DEFAULT_SERVER_PORT));
	}

	public static void main(String[] args) {

		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.INFO);
		OffersWebservice.startServer();
	}

	public static void startServer() {

		logger.info(String.format("*************** Starting server on port %s ***************", PORT));

		port(PORT);

		post("/services/offers", (request, response) -> {
			try {
				setJsonResponseType(response);
				Offer offer = new Gson().fromJson(request.body(), Offer.class);
				offer.setId(UUID.randomUUID().toString());
				offer.normalizePrice();
				offerPersistor.create(offer);
				response.status(HttpStatus.SC_CREATED);
				return new Gson().toJson(new WebserviceResponse(StatusResponseType.SUCCESS.getStatus(), new Gson().toJsonTree(offer)));
			} catch (Exception e) {
				response.status(HttpStatus.SC_BAD_REQUEST);
				return new Gson().toJson(new WebserviceResponse("Please refer to api documentation at: www.offerWebservice/docs", StatusResponseType.ERROR.getStatus()));
			}

		});

		put("/services/offers/:id", (request, response) -> {
			try {
				setJsonResponseType(response);
				Offer offer = new Gson().fromJson(request.body(), Offer.class);
				offer.normalizePrice();
				offer.setId(request.params(":id"));
				Offer updatedOffer = offerPersistor.update(offer);
				if (updatedOffer == null) {
					response.status(HttpStatus.SC_NOT_FOUND);
					return new Gson().toJson(new WebserviceResponse(
							new Gson().toJson("Resource not found"), StatusResponseType.ERROR.getStatus()));
				} else {
					response.status(HttpStatus.SC_OK);
					return new Gson().toJson(new WebserviceResponse(StatusResponseType.SUCCESS.getStatus(), new Gson().toJsonTree(updatedOffer)));
				}
			} catch (Exception e) {
				response.status(HttpStatus.SC_BAD_REQUEST);
				return new Gson().toJson(new WebserviceResponse("Please refer to api documentation at: www.offerWebservice/docs", StatusResponseType.ERROR.getStatus()));
			}
		});

		get("/services/offers", (request, response) -> {
			setJsonResponseType(response);
			response.status(HttpStatus.SC_OK);
			return new Gson().toJson(new WebserviceResponse(StatusResponseType.SUCCESS.getStatus(), new Gson().toJsonTree(offerPersistor.list())));
		});

		get("/services/offers/:id", (request, response) -> {
			String id = request.params(":id");
			Offer offer = offerPersistor.get(id);
			return getWebserviceResponse(response, offer);
		});

		delete("/services/offers/:id", (request, response) -> {
			try {
				String id = request.params(":id");
				Offer offer = offerPersistor.delete(id);
				return getWebserviceResponse(response, offer);
			} catch (Exception e) {
				response.status(HttpStatus.SC_BAD_REQUEST);
				return new Gson().toJson(new WebserviceResponse("Please refer to api documentation at: www.offerWebservice/docs", StatusResponseType.ERROR.getStatus()));
			}
		});

		awaitInitialization();

		logger.info(String.format("*************** Server started on port %s ***************", PORT));

	}

	private static Object getWebserviceResponse(Response response, Offer offer) {

		setJsonResponseType(response);
		if (offer != null) {
			response.status(HttpStatus.SC_OK);
			return new Gson().toJson(new WebserviceResponse(StatusResponseType.SUCCESS.getStatus(), new Gson().toJsonTree(offer)));
		} else {
			response.status(HttpStatus.SC_NOT_FOUND);
			return new Gson().toJson(new WebserviceResponse(StatusResponseType.ERROR.getStatus(), new Gson().toJsonTree(offer)));
		}
	}

	private static void setJsonResponseType(Response response) {

		response.type("application/json");
	}

	public static void stopServer() {

		logger.info("***************Stopping the server***************");
		stop();
	}
}
