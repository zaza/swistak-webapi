package com.swistak.webapi.builder;

import static com.google.common.base.Preconditions.checkArgument;

import java.math.BigInteger;
import java.util.EnumSet;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.swistak.webapi.Auction_costs_delivery;
import com.swistak.webapi.Cost_delivery;
import com.swistak.webapi.model.DeliveryInfo;
import com.swistak.webapi.model.DeliveryInfoWithCostDelivery;

public class DeliveryInfoBuilder {

	private EnumSet<DeliveryInfo> deliveryInfos;
	
	ListMultimap<DeliveryInfo, Cost_delivery> deliveryCosts = ArrayListMultimap.create();
	
	public DeliveryInfoBuilder(DeliveryInfoWithCostDelivery delivery) {
		deliveryInfos = EnumSet.of(delivery.getDeliveryInfo());
		addCost(delivery);
	}

	public DeliveryInfoBuilder and(DeliveryInfoWithCostDelivery delivery) {
		deliveryInfos.add(delivery.getDeliveryInfo());
		addCost(delivery);
		return this;
	}

	public BigInteger buildDeliveryInfo() {
		int sum = 0;
		for (DeliveryInfo deliveryInfo : deliveryInfos) {
			sum += deliveryInfo.getId();
		}
		checkArgument(sum < DeliveryInfo.wystawiam_fakturę.getId(), "No delivery info selected.");
		return BigInteger.valueOf(sum);
	}

	public Auction_costs_delivery buildCostDelivery() {
		Auction_costs_delivery costsDelivery = createEmptyAuctionCostsDelivery();
		for (DeliveryInfo deliveryInfo : deliveryCosts.keySet()) {
			List<Cost_delivery> costsList = deliveryCosts.get(deliveryInfo);
			// TODO: issue #5: check ranges for overlaps
			Cost_delivery[] costsArray = costsList.toArray(new Cost_delivery[costsList.size()]);
			switch (deliveryInfo) {
				case list_ekonomiczny :
					costsDelivery.setCost_1(costsArray);
					break;
				case list_priorytetowy:
					costsDelivery.setCost_2(costsArray);
					break;
				case list_polecony_ekonomiczny:
					costsDelivery.setCost_3(costsArray);
					break;
				case list_polecony_priorytetowy:
					costsDelivery.setCost_4(costsArray);
					break;
				case paczka_pocztowa_ekonomiczna:
					costsDelivery.setCost_5(costsArray);
					break;
				case paczka_pocztowa_priorytetowa:
					costsDelivery.setCost_6(costsArray);				
					break;
				case przesyłka_kurierska:
					costsDelivery.setCost_7(costsArray);
					break;
				case paczka_pocztowa_pobraniowa:
					costsDelivery.setCost_8(costsArray);
					break;
				case paczka_pocztowa_pobraniowa_priorytetowa:
					costsDelivery.setCost_9(costsArray);
					break;
				case paczka_kurierska_pobraniowa:
					costsDelivery.setCost_10(costsArray);
					break;
				default :
					// ignore
			}
		}
		return costsDelivery;
	}
	
	private void addCost(DeliveryInfoWithCostDelivery delivery) {
		Cost_delivery costDelivery = new Cost_delivery(delivery.getMin(),  delivery.getMax(), delivery.getPrice());
		deliveryCosts.put(delivery.getDeliveryInfo(), costDelivery);
	}

	static Auction_costs_delivery createEmptyAuctionCostsDelivery() {
		Auction_costs_delivery costsDelivery = new Auction_costs_delivery();
		costsDelivery.setCost_1(new Cost_delivery[0]);
		costsDelivery.setCost_2(new Cost_delivery[0]);
		costsDelivery.setCost_3(new Cost_delivery[0]);
		costsDelivery.setCost_4(new Cost_delivery[0]);
		costsDelivery.setCost_5(new Cost_delivery[0]);
		costsDelivery.setCost_6(new Cost_delivery[0]);
		costsDelivery.setCost_7(new Cost_delivery[0]);
		costsDelivery.setCost_8(new Cost_delivery[0]);
		costsDelivery.setCost_9(new Cost_delivery[0]);
		costsDelivery.setCost_10(new Cost_delivery[0]);
		return costsDelivery;
	}
}
