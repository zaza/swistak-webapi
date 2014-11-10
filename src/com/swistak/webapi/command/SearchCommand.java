package com.swistak.webapi.command;

import java.math.BigInteger;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.holders.BigIntegerHolder;

import com.swistak.webapi.SwistakLocator;
import com.swistak.webapi.SwistakPortType;
import com.swistak.webapi.holders.Search_auctionsHolder;
import com.swistak.webapi.holders.SwistakMessageHolder;
import com.swistak.webapi.model.Wojewodztwo;

/**
 * http://www.swistak.pl/out/wsdl/wsdl.html?method=search_auctions
 */
public class SearchCommand implements Runnable {

	final private String fraza;
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
	private BigInteger sort = BigInteger.ONE;
	private BigInteger kat_id = BigInteger.ZERO;
	private BigInteger strona = BigInteger.ONE;
	private BigInteger opis = BigInteger.ZERO;
	private BigInteger stan_towaru = BigInteger.ZERO;
	private BigInteger limit = BigInteger.valueOf(100);

	public BigIntegerHolder total_found;
	public Search_auctionsHolder search_auctions;
	SwistakMessageHolder _return;
	SwistakMessageHolder err;

	private SearchCommand(String fraza) {
		this.fraza = fraza;
	}

	public static SearchCommand fraza(String fraza) {
		return new SearchCommand(fraza);
	}

	@Override
	public void run() {
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
		} catch (ServiceException e) {
			throw new RuntimeException(e);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	public SearchCommand fraza_pomin(String fraza_pomin) {
		this.fraza_pomin = fraza_pomin;
		return this;
	}

	public SearchCommand cena_od(float cena_od) {
		this.cena_od = cena_od;
		return this;
	}

	public SearchCommand cena_do(float cena_do) {
		this.cena_do = cena_do;
		return this;
	}

	public SearchCommand miejscowosc(String miejscowosc) {
		this.miejscowosc = miejscowosc;
		return this;
	}

	public SearchCommand wojewodztwo(Wojewodztwo wojewodztwo) {
		this.wojewodztwo = wojewodztwo.toBigInteger();
		return this;
	}

	public SearchCommand login(String login) {
		this.login = login;
		return this;
	}
}
