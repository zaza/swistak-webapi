package com.swistak.webapi.command;

import java.rmi.RemoteException;
import java.util.Arrays;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.holders.LongHolder;

import org.apache.axis.AxisFault;

import com.swistak.webapi.Auction_params;
import com.swistak.webapi.SwistakLocator;
import com.swistak.webapi.SwistakPortType;
import com.swistak.webapi.model.AddAuctionStatus;

/**
 * http://www.swistak.pl/out/wsdl/wsdl.html?method=add_auction
 */
public class AddAuctionCommand implements Runnable {

	private final String hash;
	private final Auction_params params;

	public LongHolder id = new LongHolder();
	public LongHolder id_out = new LongHolder();
	private AddAuctionStatus status;
	
	public AddAuctionCommand(String hash, Auction_params params) {
		this.hash  = hash;
		this.params = params;
	}
	
	@Override
	public void run() {
		SwistakLocator service = new SwistakLocator();
		try {
			SwistakPortType port = service.getSwistakPort();
			port.add_auction(hash, params, id, id_out);
			status = AddAuctionStatus.OK;
		} catch (ServiceException e) {
			throw new RuntimeException(e);
		} catch (AxisFault e) {
			String faultCode = e.getFaultCode().getLocalPart();
			if (Arrays.asList(AddAuctionStatus.values()).contains(faultCode)) {
				status = AddAuctionStatus.valueOf(faultCode);
				return;
			}
			throw new RuntimeException(e);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}
	
	public AddAuctionStatus getStatus() {
		return status;
	}

}
