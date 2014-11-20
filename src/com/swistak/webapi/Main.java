package com.swistak.webapi;

import static java.lang.String.format;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;

import com.swistak.webapi.command.SearchAuctionsCommand;
import com.swistak.webapi.model.Province;

public class Main {

	private static final Logger LOG = Logger.getLogger(Main.class);

	public static void main(String[] args) throws ServiceException,
			RemoteException {

		SearchAuctionsCommand search = SearchAuctionsCommand.fraza("nokia").fraza_pomin("3310").cena_od(10).cena_do(20).miejscowosc("Warszawa").wojewodztwo(Province.Mazowieckie);
		search.run();
		
		if (search.total_found.value == null) {
			LOG.error("Nothing found.");
		} else {
			LOG.info(format("Found %d items", search.total_found.value.intValue()));
			for (Search_auction auction : search.search_auctions.value) {
				LOG.info(format(format("* %s", auction.getTitle())));
			}
		}
	}
}
