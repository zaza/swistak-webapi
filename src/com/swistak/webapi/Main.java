package com.swistak.webapi;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

public class Main {
	public static void main(String[] args) throws ServiceException,
			RemoteException {

		SearchCommand search = SearchCommand.fraza("nokia").fraza_pomin("3310").cena_od(10).cena_do(20).miejscowosc("Warszawa").wojewodztwo(7);
		search.run();
		
		System.out.println(String.format("Found %d items", search.total_found.value.intValue()));
		for (Search_auction auction : search.search_auctions.value) {
			System.out.println(String.format("* %s", auction.getTitle()));
		}
	}
}
