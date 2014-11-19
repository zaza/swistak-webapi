package com.swistak.webapi.model;

import java.math.BigInteger;

import com.swistak.webapi.Auction_foto;
import com.swistak.webapi.Update_auction_params;

public class UpdateAuctionParamsBuilder extends AbstractAuctionParamsBuilder<Update_auction_params> {

	private long id;

	public UpdateAuctionParamsBuilder(long id) {
		this.id = id;
		
		this.description = "";
		this.city = "";
		this.tags = "";
	}
	
	public AbstractAuctionParamsBuilder<Update_auction_params> title(String title) {
		this.title = title;
		return this;
	}
	
	public AbstractAuctionParamsBuilder<Update_auction_params> price(float price) {
		this.price = price;
		return this;
	}

	@Override
	public Update_auction_params build() {
		Update_auction_params params = new Update_auction_params();
		params.setId(id);
		if (category != -1)
			params.setCategory_id(BigInteger.valueOf(category));
		params.setCity(city);
		if (condition != null)
			params.setCondition_product(condition.toBigInteger());
		if (costs_delivery != null)
			params.setCosts_delivery(costs_delivery);
		if (deliveryInfo != null)
			params.setDelivery_info(createDeliveryInfoBuilder().build());
		params.setDescription(description);
		params.setFotos(new Auction_foto[0]);
		if (count != -1)
			params.setItem_count(BigInteger.valueOf(count));
		if (paramaters != null)
			params.setParameters(paramaters);
		if (price != -1)
			params.setPrice(price);
		if (province != null)
			params.setProvince(province.toBigInteger());
		params.setTags(tags);
		if (title != null)
			params.setTitle(title);
		if (type != null)
		params.setType(type.toBigInteger());
		if (whoPayment != null)
			params.setWho_payment(whoPayment.toBigInteger());

		return params;
	}

}
