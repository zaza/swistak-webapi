package com.swistak.webapi.command;

import java.math.BigInteger;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.holders.BigIntegerHolder;

import com.swistak.webapi.SwistakLocator;
import com.swistak.webapi.SwistakPortType;
import com.swistak.webapi.holders.My_auctionsHolder;

/**
 * http://www.swistak.pl/out/wsdl/wsdl.html?method=get_my_auctions
 *
 */
public class GetMyAuctionsCommand implements Runnable {

	private String hash;
	private long userId = 0;
	private BigInteger offset = BigInteger.ONE;
	private BigInteger limit = BigInteger.valueOf(1000);

	BigIntegerHolder total_auctions= new BigIntegerHolder();
	public My_auctionsHolder my_auctions = new My_auctionsHolder();
	
	public GetMyAuctionsCommand(String hash) {
		this.hash = hash;
	}
	
	public GetMyAuctionsCommand userId(long userId) {
		this.userId = userId;
		return this;
	}
	
	@Override
	public void run() {
		SwistakLocator service = new SwistakLocator();
		try {
			SwistakPortType port = service.getSwistakPort();
			port.get_my_auctions(hash, offset, limit, userId, total_auctions, my_auctions);
		} catch (ServiceException e) {
			throw new RuntimeException(e);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

}
