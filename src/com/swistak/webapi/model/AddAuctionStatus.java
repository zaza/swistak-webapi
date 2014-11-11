package com.swistak.webapi.model;

/**
 * http://www.swistak.pl/out/wsdl/wsdl.html?method=add_auction
 */
public enum AddAuctionStatus {
	OK,
	/** Niepoprawny hash lub sesja utraciła ważność. */
	ERR_AUTHORIZATION,
	/** Konto użytkownika posiada ograniczenia na sprzedaż. */
	ERR_USER_STATUS,
	/** Użytkownik nie zaakceptował aktualnej wersji regulaminu. */
	ERR_NOT_ACCEPTED_RULES,
	/** Niepoprawny numer id_out. */
	ERR_INVALID_ID_OUT,
	/** Brak tytułu lub tytuł przekracza 50 znaków. */
	ERR_INVALID_TITLE,
	/** Brak ceny lub cena większa niż 100000000.00. */
	ERR_INVALID_PRICE,
	/** Brak określonej ilości sztuk towaru lub ilość sztuk większa niż 9999999. */
	ERR_INVALID_ITEM_COUNT,
	/** Brak opisu przedmiotu lub opis krótszy niż 20 znaków. */
	ERR_INVALID_DESCRIPTION,
	/** Słowa pomocnicze dłuższe niż 64 znaki. */
	ERR_INVALID_TAGS,
	/** Kategoria nie istnieje lub posiada podkategorie. */
	ERR_INVALID_CATEGORY,
	/** Nazwa lokalizacji posiada powyżej 50 znaków. */
	ERR_INVALID_CITY,
	/** Użytkowik posiada trwającą aukcję o taki samym id_out. */
	ERR_DUPLICATE_ID_OUT,
	/**
	 * Błędnie określone koszty przesyłki. Błędny zakres ilości sztuk lub brak
	 * kosztu dostawy.
	 */
	ERR_INVALID_DELIVERY_COST_1,
	/**
	 * Błędnie określone koszty przesyłki. Błędny zakres ilości sztuk lub brak
	 * kosztu dostawy.
	 */
	ERR_INVALID_DELIVERY_COST_2,
	/**
	 * Błędnie określone koszty przesyłki. Błędny zakres ilości sztuk lub brak
	 * kosztu dostawy.
	 */
	ERR_INVALID_DELIVERY_COST_3,
	/**
	 * Błędnie określone koszty przesyłki. Błędny zakres ilości sztuk lub brak
	 * kosztu dostawy.
	 */
	ERR_INVALID_DELIVERY_COST_4,
	/**
	 * Błędnie określone koszty przesyłki. Błędny zakres ilości sztuk lub brak
	 * kosztu dostawy.
	 */
	ERR_INVALID_DELIVERY_COST_5,
	/**
	 * Błędnie określone koszty przesyłki. Błędny zakres ilości sztuk lub brak
	 * kosztu dostawy.
	 */
	ERR_INVALID_DELIVERY_COST_6,
	/**
	 * Błędnie określone koszty przesyłki. Błędny zakres ilości sztuk lub brak
	 * kosztu dostawy.
	 */
	ERR_INVALID_DELIVERY_COST_7,
	/**
	 * Błędnie określone koszty przesyłki. Błędny zakres ilości sztuk lub brak
	 * kosztu dostawy.
	 */
	ERR_INVALID_DELIVERY_COST_8,
	/**
	 * Błędnie określone koszty przesyłki. Błędny zakres ilości sztuk lub brak
	 * kosztu dostawy.
	 */
	ERR_INVALID_DELIVERY_COST_9,
	/**
	 * Błędnie określone koszty przesyłki. Błędny zakres ilości sztuk lub brak
	 * kosztu dostawy.
	 */
	ERR_INVALID_DELIVERY_COST_10,
	/**
	 * Nie określono żadnej formy przesyłki. Należy określić koszty przesyłki
	 * dla wszystkich sztuk towaru przynjmniej dla jednej formy przesyłki.
	 */
	ERR_INVALID_DELIVERY_COST,
	/** Wewnętrzny błąd WebAPI Swistak.pl. */
	ERR_INTERNAL_SERVER_ERROR;
}
