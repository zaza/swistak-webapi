package com.swistak.webapi.model;

import java.math.BigInteger;

public enum AuctionType {
	kup_teraz(1), kup_teraz_lub_zaproponuj_cenę(2);

	private int id;

	AuctionType(int id) {
		this.id = id;
	}

	public BigInteger toBigInteger() {
		return BigInteger.valueOf(id);
	}
}
