package com.swistak.webapi.model;

public enum EndAuctionsStatus {
	OK,
	/**
	 * Niepoprawny hash lub sesja utraciła ważność.
	 */
	ERR_AUTHORIZATION;
}
