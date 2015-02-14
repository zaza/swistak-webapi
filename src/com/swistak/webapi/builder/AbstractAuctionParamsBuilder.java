package com.swistak.webapi.builder;

import static java.lang.String.format;

import com.swistak.webapi.Auction_costs_delivery;
import com.swistak.webapi.Auction_foto;
import com.swistak.webapi.Auction_parameter;
import com.swistak.webapi.Cost_delivery;
import com.swistak.webapi.model.AuctionType;
import com.swistak.webapi.model.AuctionUnit;
import com.swistak.webapi.model.ConditionProduct;
import com.swistak.webapi.model.DeliveryInfo;
import com.swistak.webapi.model.Province;
import com.swistak.webapi.model.WhoPayment;

public abstract class AbstractAuctionParamsBuilder<T> /* TODO: implements AuctionParams */{

	protected String title;
	protected float price = -1;
	protected AuctionType type;
	protected int category = -1;
	protected String city;
	protected ConditionProduct condition;
	protected DeliveryInfo[] deliveryInfo;
	protected String description;
	protected Auction_foto[] fotos;
	protected int count = -1;
	protected Auction_parameter[] paramaters = new Auction_parameter[0];
	protected Auction_costs_delivery costs_delivery = noCostsDelivery();
	protected AuctionUnit unit;
	protected WhoPayment whoPayment;
	protected Province province;
	protected String tags;

	public AbstractAuctionParamsBuilder<T> type(AuctionType type) {
		this.type = type;
		return this;
	}

	public AbstractAuctionParamsBuilder<T> category(int category) {
		this.category = category;
		return this;
	}

	public AbstractAuctionParamsBuilder<T> city(String city) {
		this.city = city;
		return this;
	}

	public AbstractAuctionParamsBuilder<T> condition(ConditionProduct condition) {
		this.condition = condition;
		return this;
	}

	public AbstractAuctionParamsBuilder<T> deliveryInfo(DeliveryInfo... deliveryInfo) {
		this.deliveryInfo = deliveryInfo;
		return this;
	}

	public AbstractAuctionParamsBuilder<T> description(String description) {
		this.description = description;
		return this;
	}
	
	public AbstractAuctionParamsBuilder<T> fotos(Auction_foto[] fotos) {
		this.fotos = fotos;
		return this;
	}

	public AbstractAuctionParamsBuilder<T> count(int count) {
		this.count = count;
		return this;
	}

	// TODO: parameters

	public AbstractAuctionParamsBuilder<T> province(Province province) {
		this.province = province;
		return this;
	}

	public AbstractAuctionParamsBuilder<T> tags(String tags) {
		this.tags = tags;
		return this;
	}
	
	abstract public T build();

	protected static Auction_costs_delivery noCostsDelivery() {
		Auction_costs_delivery costs_delivery = new Auction_costs_delivery();
		costs_delivery.setCost_1(new Cost_delivery[0]);
		costs_delivery.setCost_2(new Cost_delivery[0]);
		costs_delivery.setCost_3(new Cost_delivery[0]);
		costs_delivery.setCost_4(new Cost_delivery[0]);
		costs_delivery.setCost_5(new Cost_delivery[0]);
		costs_delivery.setCost_6(new Cost_delivery[0]);
		costs_delivery.setCost_7(new Cost_delivery[0]);
		costs_delivery.setCost_8(new Cost_delivery[0]);
		costs_delivery.setCost_9(new Cost_delivery[0]);
		costs_delivery.setCost_10(new Cost_delivery[0]);
		return costs_delivery;
	}
	
	protected DeliveryInfoBuilder createDeliveryInfoBuilder() {
		DeliveryInfoBuilder builder = new DeliveryInfoBuilder(deliveryInfo[0]);
		if (deliveryInfo.length > 1) {
			for (int i = 1; i < deliveryInfo.length; i++) {
				builder.and(deliveryInfo[i]);
			}
		}
		return builder;
	}

	protected static void checkMinLength(String string, int length) {
		if (string.length() < length)
			throw new IllegalArgumentException(format("'%s' is too short. Minimum length for the param is %d.", string, length));
	}

	protected static void checkMaxLength(String string, int length) {
		if (string.length() > length)
			throw new IllegalArgumentException(format("'%s' is too long. Maximum length for the param is %d.", string, length));
	}

	protected static void checkMinValue(Number number, Number value) {
		if (number.doubleValue() < value.doubleValue())
			throw new IllegalArgumentException(format("%.2f is too low. Minimum value for the param is %.2f.", number, value));
	}

	protected static void checkMaxValue(Number number, Number value) {
		if (number.doubleValue() > value.doubleValue())
			throw new IllegalArgumentException(format("%.2f is too high. Maximum value for the param is %.2f.", number, value));
	}

	protected static void checkNotEmpty(String string) {
		if (string == null || string.isEmpty())
				throw new IllegalArgumentException("Expected a non-empty string.");
	}

	protected static void checkNotEmpty(Object[] array) {
		if (array == null || array.length == 0)
			throw new IllegalArgumentException("Expected a non-empty array.");
	}
}
