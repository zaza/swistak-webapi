package com.swistak.webapi.model;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DeliveryInfoBuilderTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void odbior_osobisty() {
		DeliveryInfoBuilder builder = new DeliveryInfoBuilder(
				DeliveryInfo.odbiór_osobisty);

		assertEquals(DeliveryInfo.odbiór_osobisty.getId(), builder.build()
				.intValue());
	}

	@Test
	public void listy_polecone() {
		DeliveryInfoBuilder builder = new DeliveryInfoBuilder(
				DeliveryInfo.list_polecony_ekonomiczny)
				.and(DeliveryInfo.list_polecony_priorytetowy);

		assertEquals(DeliveryInfo.list_polecony_ekonomiczny.getId()
				+ DeliveryInfo.list_polecony_priorytetowy.getId(), builder
				.build().intValue());
	}

	@Test
	public void only_wystawiam_fakture() {
		DeliveryInfoBuilder builder = new DeliveryInfoBuilder(
				DeliveryInfo.wystawiam_fakturę);

		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("No delivery info selected.");
		builder.build();
	}
	
	@Test
	public void only_szczegoly_w_opisie() {
		DeliveryInfoBuilder builder = new DeliveryInfoBuilder(
				DeliveryInfo.szczegóły_w_opisie);

		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("No delivery info selected.");
		builder.build();
	}
	
	@Test
	public void only_wysylka_za_granice() {
		DeliveryInfoBuilder builder = new DeliveryInfoBuilder(
				DeliveryInfo.zgadzam_się_na_wysyłkę_za_granicę);

		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("No delivery info selected.");
		builder.build();
	}
}
