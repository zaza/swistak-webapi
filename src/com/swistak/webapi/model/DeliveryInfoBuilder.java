package com.swistak.webapi.model;

import java.math.BigInteger;

public class DeliveryInfoBuilder {

	private int sum;

	public DeliveryInfoBuilder(DeliveryInfo delivery) {
		sum = delivery.getId();
	}

	public DeliveryInfoBuilder and(DeliveryInfo delivery) {
		sum += delivery.getId();
		return this;
	}

	public BigInteger toBigInteger() {
		return BigInteger.valueOf(sum);
	}
}
