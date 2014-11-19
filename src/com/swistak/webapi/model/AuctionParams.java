package com.swistak.webapi.model;

import com.swistak.webapi.Auction_costs_delivery;
import com.swistak.webapi.Auction_parameter;

public interface AuctionParams {

	public String getTitle();
	public float getPrice();
	public AuctionType getType();
	public int getCategory();
	public String getCity();
	public ConditionProduct getCondition();
	public DeliveryInfo[] getDeliveryInfo();
	public String getDescription();
	public int getCount();
	public Auction_parameter[] getParamaters();
	public Auction_costs_delivery getCostsDelivery();
	public AuctionUnit getUnit();
	public WhoPayment getWhoPayment();
	public Province getProvince();
	public String getTags();
}
