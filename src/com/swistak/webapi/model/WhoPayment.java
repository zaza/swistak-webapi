package com.swistak.webapi.model;

import java.math.BigInteger;

public enum WhoPayment {
	kupujący(1), sprzedawca(2);

	private int id;

	WhoPayment(int id) {
		this.id = id;
	}

	public BigInteger toBigInteger() {
		return BigInteger.valueOf(id);
	}
}
