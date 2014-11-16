package com.swistak.webapi.model;

import static com.google.common.base.Preconditions.checkArgument;

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

	public BigInteger build() {
		checkArgument(sum < DeliveryInfo.wystawiam_fakturę.getId(), "No delivery info selected.");
		return BigInteger.valueOf(sum);
	}
}
