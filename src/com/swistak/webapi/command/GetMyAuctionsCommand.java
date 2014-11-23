package com.swistak.webapi.command;

import static com.google.common.base.Preconditions.checkArgument;

import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.holders.BigIntegerHolder;

import com.swistak.webapi.My_auction;
import com.swistak.webapi.SwistakLocator;
import com.swistak.webapi.SwistakPortType;
import com.swistak.webapi.holders.My_auctionsHolder;

/**
 * http://www.swistak.pl/out/wsdl/wsdl.html?method=get_my_auctions
 *
 */
public class GetMyAuctionsCommand implements Callable<List<My_auction>> {

	private String hash;
	private long userId = 0;
	private long offset = 1;
	private long limit = 25;

	private BigIntegerHolder total_auctions = new BigIntegerHolder();
	private My_auctionsHolder my_auctions = new My_auctionsHolder();

	public GetMyAuctionsCommand(String hash) {
		this.hash = hash;
	}

	public GetMyAuctionsCommand userId(long userId) {
		this.userId = userId;
		return this;
	}

	@Override
	public List<My_auction> call() {
		SwistakLocator service = new SwistakLocator();
		try {
			SwistakPortType port = service.getSwistakPort();
			port.get_my_auctions(hash, BigInteger.valueOf(offset), BigInteger.valueOf(limit), userId, total_auctions, my_auctions);
			return getMyAuctions();
		} catch (ServiceException e) {
			throw new RuntimeException(e);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}
	
	public GetMyAuctionsCommand limit(long limit) {
		checkArgument(limit < 1000, "Limit must be below {0}", 1000);
		this.limit = limit;
		return this;
	}
	
	public void incrementOffset() {
		++offset;
	}
	
	public int getTotalAuctions() {
		return total_auctions.value.intValue();
	}
	
	public List<My_auction> getMyAuctions() {
		return Arrays.asList(my_auctions.value);
	}

}
