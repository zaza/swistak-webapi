package com.swistak.webapi.command;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.rpc.ServiceException;

import com.swistak.webapi.Province;
import com.swistak.webapi.SwistakLocator;
import com.swistak.webapi.SwistakPortType;

/**
 * http://www.swistak.pl/out/wsdl/wsdl.html?method=get_province
 *
 */
public class GetProvinceCommand implements Runnable {

	private List<Province> province = new ArrayList<Province>();

	@Override
	public void run() {
		SwistakLocator service = new SwistakLocator();
		try {
			SwistakPortType port = service.getSwistakPort();
			province = Arrays.asList(port.get_province());
		} catch (ServiceException e) {
			throw new RuntimeException(e);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Province> getProvince() {
		return province;
	}
}
