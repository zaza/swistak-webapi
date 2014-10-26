package com.swistak.webapi;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

/**
 * http://www.swistak.pl/out/wsdl/wsdl.html?method=get_province
 */
class GetProvinceCommand implements Runnable {

	Province[] provinces;

	@Override
	public void run() {

		SwistakLocator service = new SwistakLocator();
		try {
			SwistakPortType port = service.getSwistakPort();
			provinces = port.get_province();
		} catch (ServiceException e) {
			throw new RuntimeException(e);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}
}
