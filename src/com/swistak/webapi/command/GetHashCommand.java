package com.swistak.webapi.command;

import static java.lang.String.format;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.rpc.ServiceException;

import org.apache.axis.AxisFault;

import com.swistak.webapi.SwistakLocator;
import com.swistak.webapi.SwistakPortType;
import com.swistak.webapi.model.GetHashStatus;

/**
 * http://www.swistak.pl/out/wsdl/wsdl.html?method=get_hash
 */
public class GetHashCommand implements Runnable {

	private final String login;
	private final String pass;

	private String hash;
	private GetHashStatus status;

	public GetHashCommand(String login, String pass) {
		this.login = login;
		this.pass = md5(pass);
	}

	static String md5(String pass) {
		try {
			byte[] bytes = pass.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] hash = md.digest(bytes);
			StringBuilder sb = new StringBuilder(2 * hash.length);
			for (byte b : hash) {
				sb.append(format("%02x", b & 0xff));
			}
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(
					"Error occured while digesting the password", e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(
					"Error occured while digesting the password", e);
		}
	}

	@Override
	public void run() {
		SwistakLocator service = new SwistakLocator();
		try {
			SwistakPortType port = service.getSwistakPort();
			hash = port.get_hash(login, pass);
			status = GetHashStatus.OK;
		} catch (ServiceException e) {
			throw new RuntimeException(e);
		} catch (AxisFault e) {
			String faultCode = e.getFaultCode().getLocalPart();
			status = GetHashStatus.valueOf(faultCode);
			return;
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String getHash() {
		return hash;
	}
	
	public GetHashStatus getStatus() {
		return status;
	}

}
