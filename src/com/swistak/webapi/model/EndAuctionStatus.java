package com.swistak.webapi.model;

public enum EndAuctionStatus {
	OK,
	/**
	 * Niepoprawny hash lub sesja utraciła ważność.
	 */
	ERR_AUTHORIZATION;
}
