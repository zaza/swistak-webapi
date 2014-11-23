package com.swistak.webapi.command;

import java.rmi.RemoteException;
import java.util.concurrent.Callable;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.holders.LongHolder;

import com.swistak.webapi.Ids;
import com.swistak.webapi.SwistakLocator;
import com.swistak.webapi.SwistakPortType;
import com.swistak.webapi.Update_auction_params;

/**
 * http://www.swistak.pl/out/wsdl/wsdl.html?method=update_auction
 *
 */
public class UpdateAuctionCommand implements Callable<Ids> {

	private final String hash;
	private final Update_auction_params params;

	private LongHolder id = new LongHolder();
	private LongHolder id_out = new LongHolder();

	public UpdateAuctionCommand(String hash, Update_auction_params params) {
		this.hash  = hash;
		this.params = params;
	}

	@Override
	public Ids call() {
		SwistakLocator service = new SwistakLocator();
		try {
			SwistakPortType port = service.getSwistakPort();
			port.update_auction(hash, params, id, id_out);
			return new Ids(id.value, id_out.value);
		} catch (ServiceException e) {
			throw new RuntimeException(e);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

}
