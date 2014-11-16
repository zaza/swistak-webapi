package com.swistak.webapi;

import static java.lang.String.format;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import com.swistak.webapi.command.SearchCommand;
import com.swistak.webapi.model.Province;

public class Main {
	public static void main(String[] args) throws ServiceException,
			RemoteException {

		SearchCommand search = SearchCommand.fraza("nokia").fraza_pomin("3310").cena_od(10).cena_do(20).miejscowosc("Warszawa").wojewodztwo(Province.Mazowieckie);
		search.run();
		
		if (search.total_found.value == null) {
			System.err.println(format("Nothing found."));
		} else {
			System.out.println(format("Found %d items", search.total_found.value.intValue()));
			for (Search_auction auction : search.search_auctions.value) {
				System.out.println(format("* %s", auction.getTitle()));
			}
		}
	}
}
