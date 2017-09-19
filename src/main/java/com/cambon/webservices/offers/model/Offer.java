package com.cambon.webservices.offers.model;

import org.javamoney.moneta.format.CurrencyStyle;

import javax.money.MonetaryAmount;
import javax.money.format.AmountFormatQueryBuilder;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;
import java.util.Locale;

public class Offer extends Entity {

	private static final String DEFAULT_CURRENCY = "GBP";
	private final static
	MonetaryAmountFormat MONETARY_AMOUNT_PARSER = MonetaryFormats.getAmountFormat(Locale.UK);
	private static final MonetaryAmountFormat MONETARY_AMOUNT_FORMAT = MonetaryFormats.getAmountFormat(
			AmountFormatQueryBuilder.of(Locale.UK)
					.set(CurrencyStyle.CODE)
					.set("pattern", "0.00 ¤")
					.build());
	private String title;
	private String price;
	private String description;

	public void setPrice(String price) {

		this.price = price;
	}

	public void setTitle(String title) {

		this.title = title;
	}

	public void setDescription(String description) {

		this.description = description;
	}

	public void normalizePrice() {

		MonetaryAmount monetaryAmount = MONETARY_AMOUNT_PARSER.parse(String.format("%s %s", DEFAULT_CURRENCY, price));
		setPrice(MONETARY_AMOUNT_FORMAT.format(monetaryAmount));
	}

}

