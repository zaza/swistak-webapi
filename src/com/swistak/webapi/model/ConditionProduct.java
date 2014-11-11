package com.swistak.webapi.model;

import java.math.BigInteger;

public enum ConditionProduct {
	nowy(1), uzywany(2);

	private int id;

	ConditionProduct(int id) {
		this.id = id;
	}

	public BigInteger toBigInteger() {
		return BigInteger.valueOf(id);
	}
}
