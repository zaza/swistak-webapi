package com.swistak.webapi.model;

/**
 * http://www.swistak.pl/out/wsdl/wsdl.html?method=get_auctions
 */
public enum GetAuctionsStatus {
	OK,
	/**
	 * Nieprawidłowa wartość pola ids lub jej brak.
	 */
	ERR_INVALID_IDS,
	/**
	 * Tablica zawiera więcej niż 100 elementów.
	 */
	ERR_TOO_MANY_IDS;
}
