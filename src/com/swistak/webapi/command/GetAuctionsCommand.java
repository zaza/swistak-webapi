package com.swistak.webapi.command;

import static java.text.MessageFormat.format;

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
		this.auctionIds = checkMaxLength(auctionIds, 100);
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

	private static long[] checkMaxLength(long[] array, int length) {
		if (array.length > length)
			throw new IllegalArgumentException(format("'{0}' is too long. Maximum length for the array is {1}.", array, length));
		return array;
	}

	public GetAuctionsStatus getStatus() {
		return status;
	}

}
