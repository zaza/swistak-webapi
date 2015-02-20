package com.swistak.webapi.model;

import java.math.BigInteger;

import com.google.common.collect.Range;

/**
 * http://www.swistak.pl/out/wsdl/wsdl.html?type=cost_delivery
 */
public class DeliveryInfoWithCostDelivery {

	private DeliveryInfo deliveryInfo;
	private Range<Integer> range;
	private float price;

	public DeliveryInfoWithCostDelivery(DeliveryInfo deliveryInfo, Range<Integer> range, float price) {
		this.deliveryInfo = deliveryInfo;
		this.range = range;
		this.price = price;
	}
	
	/**
	 * Delivery cost for a single item.
	 * 
	 * @param deliveryInfo
	 * @param price
	 */
	public DeliveryInfoWithCostDelivery(DeliveryInfo deliveryInfo, float price) {
		this.deliveryInfo = deliveryInfo;
		this.range = Range.atMost(1);
		this.price = price;
	}
	
	/**
	 * Free delivery.
	 * 
	 * @param deliveryInfo
	 */
	public DeliveryInfoWithCostDelivery(DeliveryInfo deliveryInfo) {
		this.deliveryInfo = deliveryInfo;
		this.range = Range.all();
		this.price = 0;
	}
	
	public DeliveryInfo getDeliveryInfo() {
		return deliveryInfo;
	}
	
	public Range<Integer> getRange() {
		return range;
	}

	public BigInteger getMin() {
		return getRange().hasLowerBound() ? BigInteger.valueOf(getRange().lowerEndpoint()) : BigInteger.ONE;
	}

	public BigInteger getMax() {
		return getRange().hasUpperBound() ? BigInteger.valueOf(getRange().upperEndpoint()) : BigInteger.valueOf(Integer.MAX_VALUE);
	}
	
	public float getPrice() {
		return price;
	}

}
