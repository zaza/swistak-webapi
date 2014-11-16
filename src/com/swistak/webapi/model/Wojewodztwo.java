package com.swistak.webapi.model;

import java.math.BigInteger;

public enum Wojewodztwo {
	Dolnośląskie(1), //
	Kujawsko_pomorskie(2), //
	Lubelskie(3), //
	Lubuskie(4), //
	Łódzkie(5), //
	Małopolskie(6), //
	Mazowieckie(7), //
	Opolskie(8), //
	Podkarpackie(9), //
	Podlaskie(10), //
	Pomorskie(11), //
	Śląskie(12), //
	Świętokrzyskie(13), //
	Warmińsko_mazurskie(14), //
	Wielkopolskie(15), //
	Zachodniopomorskie(16), //
	_$Zagranica(99);

	private int id;

	Wojewodztwo(int id) {
		this.id = id;
	}

	public BigInteger toBigInteger() {
		return BigInteger.valueOf(id);
	}
}
