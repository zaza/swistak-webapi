package com.swistak.webapi.command;

import static com.google.common.base.Preconditions.checkArgument;

import java.math.BigInteger;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.apache.axis.AxisFault;

import com.swistak.webapi.Get_auctionArray;
import com.swistak.webapi.SwistakLocator;
import com.swistak.webapi.SwistakPortType;
import com.swistak.webapi.model.GetAuctionsStatus;

/**
 * http://www.swistak.pl/out/wsdl/wsdl.html?method=get_auctions
 *
 */
public class GetAuctionsCommand implements Runnable {

	private final String hash;
	private long[] auctionIds = new long[0];
	private Get_auctionArray[] get_auctionArray;
	private GetAuctionsStatus status;

	public GetAuctionsCommand(String hash) {
		this.hash = hash;
	}

	public GetAuctionsCommand auctions(long... auctionIds) {
		checkArgument(auctionIds.length < 100, "Maximum number of auctions to retrieve is {0}, got {1}", 100, auctionIds.length);
		this.auctionIds = auctionIds;
		return this;
	}

	@Override
	public void run() {
		SwistakLocator service = new SwistakLocator();
		try {
			SwistakPortType port = service.getSwistakPort();
			get_auctionArray = port.get_auctions(hash, toIds(auctionIds));
			status = GetAuctionsStatus.OK;
		} catch (ServiceException e) {
			throw new RuntimeException(e);
		} catch (AxisFault e) {
			String faultCode = e.getFaultCode().getLocalPart();
			status = GetAuctionsStatus.valueOf(faultCode);
			return;
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	public Get_auctionArray[] getAuctionArray() {
		return get_auctionArray;
	}

	private static BigInteger[] toIds(long[] ids) {
		BigInteger[] result = new BigInteger[ids.length];
		for (int i = 0; i < ids.length; i++) {
			result[i] = BigInteger.valueOf(ids[i]);
		}
		return result;
	}

	public GetAuctionsStatus getStatus() {
		return status;
	}

}
