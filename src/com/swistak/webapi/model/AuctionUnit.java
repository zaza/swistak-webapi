package com.swistak.webapi.model;

import java.math.BigInteger;

public enum AuctionUnit {
	sztuki(1), komplety(2), pary(3);

	private int id;

	AuctionUnit(int id) {
		this.id = id;
	}

	public BigInteger toBigInteger() {
		return BigInteger.valueOf(id);
	}
}
