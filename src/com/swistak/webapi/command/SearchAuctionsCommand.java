package com.swistak.webapi.command;

import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.holders.BigIntegerHolder;

import com.swistak.webapi.Search_auction;
import com.swistak.webapi.SwistakLocator;
import com.swistak.webapi.SwistakPortType;
import com.swistak.webapi.holders.Search_auctionsHolder;
import com.swistak.webapi.holders.SwistakMessageHolder;
import com.swistak.webapi.model.Province;

/**
 * http://www.swistak.pl/out/wsdl/wsdl.html?method=search_auctions
 */
public class SearchAuctionsCommand implements Callable<List<Search_auction>> {

	private final String fraza;
	private String fraza_pomin = "";
	private float cena_od = 0;
	private float cena_do = 0;
	private String login = "";
	private String miejscowosc = "";
	private BigInteger wojewodztwo = BigInteger.ZERO;
	private BigInteger rodzaj = BigInteger.ONE;
	private BigInteger odbior_osobisty = BigInteger.ZERO;
	private BigInteger dostawa_gratis = BigInteger.ZERO;
	private BigInteger typ_aukcji = BigInteger.ZERO;
	private BigInteger sort = BigInteger.ZERO;
	private BigInteger kat_id = BigInteger.ZERO;
	private BigInteger strona = BigInteger.ONE;
	private BigInteger opis = BigInteger.ZERO;
	private BigInteger stan_towaru = BigInteger.ZERO;
	private BigInteger limit = BigInteger.valueOf(100);

	private BigIntegerHolder total_found;
	private Search_auctionsHolder search_auctions;
	SwistakMessageHolder _return;
	SwistakMessageHolder err;

	private SearchAuctionsCommand(String fraza) {
		this.fraza = fraza;
	}

	public static SearchAuctionsCommand fraza(String fraza) {
		return new SearchAuctionsCommand(fraza);
	}

	@Override
	public List<Search_auction> call() {
		total_found = new BigIntegerHolder();
		search_auctions = new Search_auctionsHolder();
		_return = new SwistakMessageHolder();
		err = new SwistakMessageHolder();

		SwistakLocator service = new SwistakLocator();
		try {
			SwistakPortType port = service.getSwistakPort();
			port.search_auctions(fraza, fraza_pomin, cena_od, cena_do, login,
					miejscowosc, wojewodztwo, rodzaj, odbior_osobisty,
					dostawa_gratis, typ_aukcji, sort, kat_id, strona, opis,
					stan_towaru, limit, total_found, search_auctions, _return,
					err);
			return getSearchAuctions();
		} catch (ServiceException e) {
			throw new RuntimeException(e);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	public SearchAuctionsCommand fraza_pomin(String fraza_pomin) {
		this.fraza_pomin = fraza_pomin;
		return this;
	}

	public SearchAuctionsCommand cena_od(float cena_od) {
		this.cena_od = cena_od;
		return this;
	}

	public SearchAuctionsCommand cena_do(float cena_do) {
		this.cena_do = cena_do;
		return this;
	}

	public SearchAuctionsCommand miejscowosc(String miejscowosc) {
		this.miejscowosc = miejscowosc;
		return this;
	}

	public SearchAuctionsCommand wojewodztwo(Province wojewodztwo) {
		this.wojewodztwo = wojewodztwo.toBigInteger();
		return this;
	}

	public SearchAuctionsCommand login(String login) {
		this.login = login;
		return this;
	}
	
	public SearchAuctionsCommand po_tytulach() {
		this.opis = BigInteger.ZERO;
		return this;
	}
	
	public SearchAuctionsCommand po_tytulach_i_opisach() {
		this.opis = BigInteger.ONE;
		return this;
	}
	
	public SearchAuctionsCommand nowy() {
		this.stan_towaru = BigInteger.ZERO;
		return this;
	}
	
	public SearchAuctionsCommand uzywany() {
		this.stan_towaru = BigInteger.ONE;
		return this;
	}
	
	public int getTotalAuctions() {
		return total_found.value == null ? 0: total_found.value.intValue();
	}

	public List<Search_auction> getSearchAuctions() {
		return search_auctions.value == null ? Collections.<Search_auction>emptyList() : Arrays.asList(search_auctions.value);
	}
}
