package com.swistak.webapi;

import java.math.BigInteger;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

public class GetIdByLoginCommand implements Runnable {

	private final String login;
	private BigInteger id;

	GetIdByLoginCommand(String login) {
		this.login = login;
	}

	@Override
	public void run() {
		SwistakLocator service = new SwistakLocator();
		try {
			SwistakPortType port = service.getSwistakPort();
			id = port.get_id_by_login(login);
		} catch (ServiceException e) {
			throw new RuntimeException(e);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	public int getId() {
		return id == null ? 0 : id.intValue();
	}

}
