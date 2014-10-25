package com.swistak.webapi;

/**
 * http://www.swistak.pl/out/wsdl/wsdl.html?method=get_hash
 */
enum GetHashStatus {
	OK, 
	/**
	 * Błędny login lub hasło.
	 */
	ERR_USER_PASSWD, 
	/**
	 * Użytkownik zablokowany.
	 */
	ERR_USER_BLOCKED,
	/**
	 * Tymczasowa blokada konta po trzech błędnych próbach zalogowania.
	 */
	ERR_USER_BLOCKED_ONE_HOUR
}
