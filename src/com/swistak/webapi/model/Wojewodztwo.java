package com.swistak.webapi.model;

import java.math.BigInteger;

public enum Wojewodztwo {
	dolnośląskie(1), //
	kujawsko_pomorskie(2), //
	lubelskie(3), //
	lubuskie(4), //
	łódzkie(5), //
	małopolskie(6), //
	mazowieckie(7), //
	opolskie(8), //
	podkarpackie(9), //
	podlaskie(10), //
	pomorskie(11), //
	śląskie(12), //
	świętokrzyskie(13), //
	warmińsko_mazurskie(14), //
	wielkopolskie(15), //
	zachodniopomorskie(16), //
	zagranica(99);

	private int id;

	Wojewodztwo(int id) {
		this.id = id;
	}

	public BigInteger toBigInteger() {
		return BigInteger.valueOf(id);
	}
}
