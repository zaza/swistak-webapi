package com.swistak.webapi;

import static java.lang.String.format;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;

import com.swistak.webapi.command.SearchAuctionsCommand;
import com.swistak.webapi.model.Province;

public class Search {

	private static final Logger LOG = Logger.getLogger(Search.class);

	public static void main(String[] args) throws ServiceException,	RemoteException {

		SearchAuctionsCommand search = SearchAuctionsCommand.fraza("nokia").fraza_pomin("3310").cena_od(10).cena_do(20).miejscowosc("Warszawa").wojewodztwo(Province.Mazowieckie);
		search.run();
		
		if (search.total_found.value == null) {
			LOG.error("Nie znaleziono aukcji spełniających zadane kryteria.");
		} else {
			LOG.info(format("Znaleziono %d aukcji:", search.total_found.value.intValue()));
			for (Search_auction auction : search.search_auctions.value) {
				LOG.info(format(format("* '%s' %s", auction.getTitle(), auction.getUrl())));
			}
		}
	}
}
