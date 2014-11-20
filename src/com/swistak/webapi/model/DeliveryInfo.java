package com.swistak.webapi.model;


public enum DeliveryInfo {
	list_ekonomiczny(2), //
	list_priorytetowy(4), //
	list_polecony_ekonomiczny(8), //
	list_polecony_priorytetowy(16), //
	paczka_pocztowa_ekonomiczna(32), //
	paczka_pocztowa_priorytetowa(64), //
	przesyłka_kurierska(128), //
	paczka_pocztowa_pobraniowa(256), //
	paczka_pocztowa_pobraniowa_priorytetowa(512), //
	paczka_kurierska_pobraniowa(1024), //
	list_elektroniczny_email(2048), //
	odbiór_osobisty(4096), //
	wystawiam_fakturę(8192), //
	szczegóły_w_opisie(16384), //
	zgadzam_się_na_wysyłkę_za_granicę(32768);

	private int id;

	DeliveryInfo(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
