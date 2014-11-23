package com.swistak.webapi.command;

import java.rmi.RemoteException;
import java.util.concurrent.Callable;

import javax.xml.rpc.ServiceException;

import org.apache.axis.AxisFault;

import com.swistak.webapi.SwistakLocator;
import com.swistak.webapi.SwistakPortType;
import com.swistak.webapi.model.GetIdByLoginStatus;

/**
 * http://www.swistak.pl/out/wsdl/wsdl.html?method=get_id_by_login
 *
 */
public class GetIdByLoginCommand implements Callable<Integer> {

	private final String login;
	private GetIdByLoginStatus status;

	GetIdByLoginCommand(String login) {
		this.login = login;
	}

	@Override
	public Integer call() {
		SwistakLocator service = new SwistakLocator();
		try {
			SwistakPortType port = service.getSwistakPort();
			return port.get_id_by_login(login).intValue();
		} catch (ServiceException e) {
			throw new RuntimeException(e);
		} catch (AxisFault e) {
			String faultCode = e.getFaultCode().getLocalPart();
			status = GetIdByLoginStatus.valueOf(faultCode);
			return 0;
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}
	
	public GetIdByLoginStatus getStatus() {
		return status;
	}

}
