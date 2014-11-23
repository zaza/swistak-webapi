package com.swistak.webapi.command;

import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import javax.xml.rpc.ServiceException;

import org.apache.axis.AxisFault;

import com.swistak.webapi.Ids;
import com.swistak.webapi.SwistakLocator;
import com.swistak.webapi.SwistakPortType;
import com.swistak.webapi.model.EndAuctionsStatus;

/**
 * http://www.swistak.pl/out/wsdl/wsdl.html?method=end_auctions
 *
 */
public class EndAuctionsCommand implements Callable<List<Ids>> {

	private final String hash;
	private final BigInteger[] id;
	private final BigInteger[] id_out;

	private List<Ids> end_auctions = new ArrayList<Ids>();
	private EndAuctionsStatus status;

	public EndAuctionsCommand(String hash, Ids[] ids) {
		this.hash  = hash;
		this.id = toIds(ids);
		this.id_out = toIdsOut(ids);
	}

	@Override
	public List<Ids> call() {
		SwistakLocator service = new SwistakLocator();
		try {
			SwistakPortType port = service.getSwistakPort();
			end_auctions = Arrays.asList(port.end_auctions(hash, id, id_out));
			status = EndAuctionsStatus.OK;
			return getEndAuctions();
		} catch (ServiceException e) {
			throw new RuntimeException(e);
		} catch (AxisFault e) {
			String faultCode = e.getFaultCode().getLocalPart();
			status = EndAuctionsStatus.valueOf(faultCode);
			return getEndAuctions();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Ids> getEndAuctions() {
		return end_auctions;
	}

	public EndAuctionsStatus getStatus() {
		return status;
	}

	private static BigInteger[] toIds(Ids[] ids) {
		BigInteger[] result = new BigInteger[ids.length];
		for (int i = 0; i < ids.length; i++) {
			result[i] = BigInteger.valueOf(ids[i].getId());
		}
		return result;
	}

	private static BigInteger[] toIdsOut(Ids[] ids) {
		BigInteger[] result = new BigInteger[ids.length];
		for (int i = 0; i < ids.length; i++) {
			result[i] = BigInteger.valueOf(ids[i].getId_out());
		}
		return result;
	}

}
