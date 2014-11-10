package com.swistak.webapi.model;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import com.swistak.webapi.Province;
import com.swistak.webapi.SwistakLocator;
import com.swistak.webapi.SwistakPortType;

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
