package com.swistak.webapi.builder;

import java.math.BigInteger;

import com.swistak.webapi.Auction_foto;
import com.swistak.webapi.Auction_params;
import com.swistak.webapi.model.AuctionType;
import com.swistak.webapi.model.AuctionUnit;
import com.swistak.webapi.model.DeliveryInfo;
import com.swistak.webapi.model.DeliveryInfoWithCostDelivery;
import com.swistak.webapi.model.WhoPayment;

/**
 * http://www.swistak.pl/out/wsdl/wsdl.html?type=auction_params
 */
public class AddAuctionParamsBuilder extends AbstractAuctionParamsBuilder<Auction_params> {

	public AddAuctionParamsBuilder(String title, float price) {
		// TODO remove '-' from title, excluded from searches
		this.title = title;
		this.price = price;
		
		this.type = AuctionType.kup_teraz;
		this.deliveryInfo = new DeliveryInfoWithCostDelivery[]{new DeliveryInfoWithCostDelivery(DeliveryInfo.odbiór_osobisty)};
		this.fotos = new Auction_foto[0];
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
		checkMaxCount(deliveryInfo, count);
		// TODO: check category in category tree

		DeliveryInfoBuilder builder = createDeliveryInfoBuilder();
		
		Auction_params params = new Auction_params();
		params.setCategory_id(BigInteger.valueOf(category));
		params.setCity(city);
		params.setCondition_product(condition.toBigInteger());
		params.setCosts_delivery(builder.buildCostDelivery());
		params.setDelivery_info(builder.buildDeliveryInfo());
		params.setDescription(description);
		params.setFotos(fotos);
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
