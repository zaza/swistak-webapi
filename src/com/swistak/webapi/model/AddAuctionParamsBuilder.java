package com.swistak.webapi.model;

import java.math.BigInteger;

import com.swistak.webapi.Auction_foto;
import com.swistak.webapi.Auction_params;

public class AddAuctionParamsBuilder extends AbstractAuctionParamsBuilder<Auction_params> {

	public AddAuctionParamsBuilder(String title, float price) {
		this.title = title;
		this.price = price;
		
		this.type = AuctionType.kup_teraz;
		this.deliveryInfo = new DeliveryInfo[]{DeliveryInfo.odbiór_osobisty};
		this.count = 1;
		this.unit = AuctionUnit.sztuki;
		this.whoPayment = WhoPayment.kupujący;
		this.tags = title;
	}

	@Override
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
		params.setDelivery_info(createDeliveryInfoBuilder().build());
		params.setDescription(description);
		params.setFotos(new Auction_foto[0]); // TODO
		//params.setId_out(id_out); // TODO
		params.setItem_count(BigInteger.valueOf(count));
		params.setParameters(paramaters);
		params.setPrice(price);
		params.setProvince(province.toBigInteger());
		params.setTags(tags);
		params.setTitle(title);
		params.setType(type.toBigInteger());
		params.setUnit(unit.toBigInteger());
		params.setWho_payment(whoPayment.toBigInteger());

		return params;
	}
}
