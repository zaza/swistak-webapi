package com.swistak.webapi.builder;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.collect.Range;
import com.swistak.webapi.Cost_delivery;
import com.swistak.webapi.model.DeliveryInfo;
import com.swistak.webapi.model.DeliveryInfoWithCostDelivery;

public class DeliveryInfoBuilderTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void odbior_osobisty() {
		DeliveryInfoBuilder builder = newBuilder(DeliveryInfo.odbiór_osobisty);

		assertEquals(DeliveryInfo.odbiór_osobisty.getId(), builder.buildDeliveryInfo()
				.intValue());
		assertEquals(0, builder.buildCostDelivery().getCost_1().length);
		assertEquals(0, builder.buildCostDelivery().getCost_2().length);
		assertEquals(0, builder.buildCostDelivery().getCost_3().length);
		assertEquals(0, builder.buildCostDelivery().getCost_4().length);
		assertEquals(0, builder.buildCostDelivery().getCost_5().length);
		assertEquals(0, builder.buildCostDelivery().getCost_6().length);
		assertEquals(0, builder.buildCostDelivery().getCost_7().length);
		assertEquals(0, builder.buildCostDelivery().getCost_8().length);
		assertEquals(0, builder.buildCostDelivery().getCost_9().length);
		assertEquals(0, builder.buildCostDelivery().getCost_10().length);
	}

	@Test
	public void listy_polecone() {
		DeliveryInfoBuilder builder = newBuilder(
				DeliveryInfo.list_polecony_ekonomiczny,
				DeliveryInfo.list_polecony_priorytetowy);

		assertEquals(DeliveryInfo.list_polecony_ekonomiczny.getId()
				+ DeliveryInfo.list_polecony_priorytetowy.getId(), builder
				.buildDeliveryInfo().intValue());
	}
	
	@Test
	public void no_lower_bound_defaults_to_1() {
		DeliveryInfoBuilder builder = new DeliveryInfoBuilder(
				new DeliveryInfoWithCostDelivery(
						DeliveryInfo.list_ekonomiczny,
						Range.atMost(3), 0.0f));

		Cost_delivery[] cost_1 = builder.buildCostDelivery().getCost_1();

		assertEquals(1, cost_1.length);
		assertEquals(1, cost_1[0].getMin().intValue());
		assertEquals(3, cost_1[0].getMax().intValue());
		assertEquals(0.0f, cost_1[0].getCost(), 0.0f);
	}
	
	@Test
	public void no_upper_bound_defaults_to_max_int() {
		DeliveryInfoBuilder builder = new DeliveryInfoBuilder(
				new DeliveryInfoWithCostDelivery(
						DeliveryInfo.list_ekonomiczny,
						Range.atLeast(3), 0.0f));

		Cost_delivery[] cost_1 = builder.buildCostDelivery().getCost_1();

		assertEquals(1, cost_1.length);
		assertEquals(3, cost_1[0].getMin().intValue());
		assertEquals(Integer.MAX_VALUE, cost_1[0].getMax().intValue());
		assertEquals(0.0f, cost_1[0].getCost(), 0.0f);
	}

	@Test
	public void only_wystawiam_fakture() {
		DeliveryInfoBuilder builder = newBuilder(DeliveryInfo.wystawiam_fakturę);

		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("No delivery info selected.");
		builder.buildDeliveryInfo();
	}

	@Test
	public void only_szczegoly_w_opisie() {
		DeliveryInfoBuilder builder = new DeliveryInfoBuilder(
				new DeliveryInfoWithCostDelivery(
						DeliveryInfo.szczegóły_w_opisie));

		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("No delivery info selected.");
		builder.buildDeliveryInfo();
	}

	@Test
	public void only_wysylka_za_granice() {
		DeliveryInfoBuilder builder = newBuilder(DeliveryInfo.zgadzam_się_na_wysyłkę_za_granicę);

		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("No delivery info selected.");
		builder.buildDeliveryInfo();
	}

	@Test
	public void paczka_pocztowa_with_price() {
		DeliveryInfoBuilder builder = new DeliveryInfoBuilder(
				new DeliveryInfoWithCostDelivery(
						DeliveryInfo.paczka_pocztowa_ekonomiczna,
						Range.atMost(1), 9.5f));

		Cost_delivery cost_5 = builder.buildCostDelivery().getCost_5()[0];
		
		assertEquals(DeliveryInfo.paczka_pocztowa_ekonomiczna.getId(), builder
				.buildDeliveryInfo().intValue());
		assertEquals(0, builder.buildCostDelivery().getCost_1().length);
		assertEquals(0, builder.buildCostDelivery().getCost_2().length);
		assertEquals(0, builder.buildCostDelivery().getCost_3().length);
		assertEquals(0, builder.buildCostDelivery().getCost_4().length);
		assertEquals(1, cost_5.getMin().intValue());
		assertEquals(1, cost_5.getMax().intValue());
		assertEquals(9.5f, cost_5.getCost(), 0.0);
		assertEquals(0, builder.buildCostDelivery().getCost_6().length);
		assertEquals(0, builder.buildCostDelivery().getCost_7().length);
		assertEquals(0, builder.buildCostDelivery().getCost_8().length);
		assertEquals(0, builder.buildCostDelivery().getCost_9().length);
		assertEquals(0, builder.buildCostDelivery().getCost_10().length);
	}
	
	@Test
	public void letters() {
		// 1-3: 1.75, 4-10: 3.7; 11+ 6.3 
		DeliveryInfoBuilder builder = new DeliveryInfoBuilder(
				new DeliveryInfoWithCostDelivery(DeliveryInfo.list_ekonomiczny,
						Range.atMost(3), 1.75f)).and(
				new DeliveryInfoWithCostDelivery(DeliveryInfo.list_ekonomiczny,
						Range.closed(4, 10), 3.7f)).and(
				new DeliveryInfoWithCostDelivery(DeliveryInfo.list_ekonomiczny,
						Range.atLeast(11), 6.3f));

		Cost_delivery[] cost_1 = builder.buildCostDelivery().getCost_1();
		
		assertEquals(DeliveryInfo.list_ekonomiczny.getId(), builder
				.buildDeliveryInfo().intValue());
		assertEquals(3, cost_1.length);
		assertEquals(1, cost_1[0].getMin().intValue());
		assertEquals(3, cost_1[0].getMax().intValue());
		assertEquals(1.75f, cost_1[0].getCost(), 0.0f);
		assertEquals(4, cost_1[1].getMin().intValue());
		assertEquals(10, cost_1[1].getMax().intValue());
		assertEquals(3.7f, cost_1[1].getCost(), 0.0f);
		assertEquals(11, cost_1[2].getMin().intValue());
		assertEquals(Integer.MAX_VALUE, cost_1[2].getMax().intValue());
		assertEquals(6.3f, cost_1[2].getCost(), 0.0f);
		assertEquals(0, builder.buildCostDelivery().getCost_2().length);
		assertEquals(0, builder.buildCostDelivery().getCost_3().length);
		assertEquals(0, builder.buildCostDelivery().getCost_4().length);
		assertEquals(0, builder.buildCostDelivery().getCost_5().length);
		assertEquals(0, builder.buildCostDelivery().getCost_6().length);
		assertEquals(0, builder.buildCostDelivery().getCost_7().length);
		assertEquals(0, builder.buildCostDelivery().getCost_8().length);
		assertEquals(0, builder.buildCostDelivery().getCost_9().length);
		assertEquals(0, builder.buildCostDelivery().getCost_10().length);
	}
	
	private static DeliveryInfoBuilder newBuilder(DeliveryInfo... deliveryInfo) {
		DeliveryInfoBuilder builder = new DeliveryInfoBuilder(
				new DeliveryInfoWithCostDelivery(deliveryInfo[0]));
		for (int i = 1; i < deliveryInfo.length; i++) {
			builder.and(new DeliveryInfoWithCostDelivery(deliveryInfo[i]));
		}
		return builder;
	}
}
