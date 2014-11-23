package com.swistak.webapi.command;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import javax.xml.rpc.ServiceException;

import com.swistak.webapi.Province;
import com.swistak.webapi.SwistakLocator;
import com.swistak.webapi.SwistakPortType;

/**
 * http://www.swistak.pl/out/wsdl/wsdl.html?method=get_province
 *
 */
public class GetProvinceCommand implements Callable<List<Province>> {

	@Override
	public List<Province> call() {
		SwistakLocator service = new SwistakLocator();
		try {
			SwistakPortType port = service.getSwistakPort();
			return Arrays.asList(port.get_province());
		} catch (ServiceException e) {
			throw new RuntimeException(e);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

}
