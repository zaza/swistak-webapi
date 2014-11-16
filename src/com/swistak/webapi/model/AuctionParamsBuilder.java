package com.swistak.webapi.model;

import static java.text.MessageFormat.format;

import java.math.BigInteger;

import com.swistak.webapi.Auction_costs_delivery;
import com.swistak.webapi.Auction_foto;
import com.swistak.webapi.Auction_parameter;
import com.swistak.webapi.Auction_params;
import com.swistak.webapi.Cost_delivery;

public class AuctionParamsBuilder {

	private static final long NOT_SET = -1;

	private final String title;
	private final float price;
	private AuctionType type = AuctionType.kup_teraz;
	private int category;
	private String city;
	private ConditionProduct condition;
	private DeliveryInfo[] deliveryInfo = new DeliveryInfo[]{DeliveryInfo.odbiór_osobisty};
	private String description;
	private int count = 1;
	private Auction_costs_delivery costs_delivery = noCostsDelivery();
	private AuctionUnit unit = AuctionUnit.sztuki;
	private WhoPayment whoPayment = WhoPayment.kupujący;
	private Province province;
	private String tags;
	private long id_out = NOT_SET;

	public AuctionParamsBuilder(String title, float price) {
		this.title = title;
		this.price = price;
		this.tags = title;
	}
	
	public AuctionParamsBuilder type(AuctionType type) {
		this.type = type;
		return this;
	}
	
	public AuctionParamsBuilder category(int category) {
		this.category = category;
		return this;
	}

	public AuctionParamsBuilder city(String city) {
		this.city = city;
		return this;
	}

	public AuctionParamsBuilder condition(ConditionProduct condition) {
		this.condition = condition;
		return this;
	}

	public AuctionParamsBuilder deliveryInfo(DeliveryInfo... deliveryInfo) {
		this.deliveryInfo = deliveryInfo;
		return this;
	}

	public AuctionParamsBuilder description(String description) {
		this.description = description;
		return this;
	}

	public AuctionParamsBuilder count(int count) {
		this.count = count;
		return this;
	}

	public AuctionParamsBuilder province(Province province) {
		this.province = province;
		return this;
	}

	public AuctionParamsBuilder tags(String tags) {
		this.tags = tags;
		return this;
	}
	
	public Auction_params build() {
		checkMaxLength(title, 50);
		checkMinValue(price, 0.01f);
		checkMaxValue(price, 100000000);
		checkMaxValue(count, 9999999);
		checkMinLength(description, 20);
		checkMinValue(count, 1);
		checkNotEmpty(tags);
		checkMaxLength(tags, 64);
		checkNotEmpty(city);
		checkMaxLength(city, 50);
		checkNotEmpty(deliveryInfo);
		// TODO: check cost delivery
		// TODO: check category in category tree

		Auction_params params = new Auction_params();
		params.setCategory_id(BigInteger.valueOf(category));
		params.setCity(city);
		params.setCondition_product(condition.toBigInteger());
		params.setCosts_delivery(costs_delivery);
		params.setDelivery_info(createBuilder(deliveryInfo).build());
		params.setDescription(description);
		params.setFotos(new Auction_foto[0]); // TODO
		if (id_out != NOT_SET)
			params.setId_out(id_out);
		params.setItem_count(BigInteger.valueOf(count));
		params.setParameters(new Auction_parameter[0]); // TODO
		params.setPrice(price);
		params.setProvince(province.toBigInteger());
		params.setTags(tags);
		params.setTitle(title);
		params.setType(type.toBigInteger());
		params.setUnit(unit.toBigInteger());
		params.setWho_payment(whoPayment.toBigInteger());

		return params;
	}

	private static Auction_costs_delivery noCostsDelivery() {
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

	private DeliveryInfoBuilder createBuilder(DeliveryInfo[] deliveryInfo2) {
		DeliveryInfoBuilder builder = new DeliveryInfoBuilder(deliveryInfo[0]);
		if (deliveryInfo.length > 1) {
			for (int i = 1; i < deliveryInfo.length; i++) {
				builder.and(deliveryInfo[i]);
			}
		}
		return builder;
	}

	private static void checkMinLength(String string, int length) {
		if (string.length() < length)
			throw new IllegalArgumentException(format("'{0}' is too short. Minimum length for the param is {1}.", string, length));
	}

	private static void checkMaxLength(String string, int length) {
		if (string.length() > length)
			throw new IllegalArgumentException(format("'{0}' is too long. Maximum length for the param is {1}.", string, length));
	}

	private static void checkMinValue(Number number, Number value) {
		if (number.doubleValue() < value.doubleValue())
			throw new IllegalArgumentException(format("{0} is too low. Minimum value for the param is {1}.", number, value));
	}

	private static void checkMaxValue(Number number, Number value) {
		if (number.doubleValue() > value.doubleValue())
			throw new IllegalArgumentException(format("{0} is too high. Maximum value for the param is {1}.", number, value));
	}

	private static void checkNotEmpty(String string) {
		if (string == null || string.isEmpty())
				throw new IllegalArgumentException("Expected a non-empty string.");
	}

	private static void checkNotEmpty(Object[] array) {
		if (array == null || array.length == 0)
			throw new IllegalArgumentException("Expected a non-empty array.");
	}
}
