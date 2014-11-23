package com.swistak.webapi.command;

import java.rmi.RemoteException;
import java.util.concurrent.Callable;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.holders.LongHolder;

import org.apache.axis.AxisFault;

import com.google.common.base.Enums;
import com.google.common.base.Optional;
import com.swistak.webapi.Auction_params;
import com.swistak.webapi.Ids;
import com.swistak.webapi.SwistakLocator;
import com.swistak.webapi.SwistakPortType;
import com.swistak.webapi.model.AddAuctionStatus;

/**
 * http://www.swistak.pl/out/wsdl/wsdl.html?method=add_auction
 */
public class AddAuctionCommand implements Callable<Ids> {

	private final String hash;
	private final Auction_params params;

	private LongHolder id = new LongHolder();
	private LongHolder id_out = new LongHolder();
	private AddAuctionStatus status;

	public AddAuctionCommand(String hash, Auction_params params) {
		this.hash  = hash;
		this.params = params;
	}

	@Override
	public Ids call() {
		SwistakLocator service = new SwistakLocator();
		try {
			SwistakPortType port = service.getSwistakPort();
			port.add_auction(hash, params, id, id_out);
			status = AddAuctionStatus.OK;
			return getIds();
		} catch (ServiceException e) {
			throw new RuntimeException(e);
		} catch (AxisFault e) {
			String faultCode = e.getFaultCode().getLocalPart();
			Optional<AddAuctionStatus> optional = Enums.getIfPresent(AddAuctionStatus.class, faultCode);
			if (optional.isPresent()) {
				status = optional.get();
				return getIds();
			}
			throw new RuntimeException(e);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	public AddAuctionStatus getStatus() {
		return status;
	}

	public Ids getIds() {
		return new Ids(id.value, id_out.value);
	}

}
