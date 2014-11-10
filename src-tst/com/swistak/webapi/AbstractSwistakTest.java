package com.swistak.webapi;

import static java.text.MessageFormat.format;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.swistak.webapi.command.GetHashCommand;

public abstract class AbstractSwistakTest {

	protected String getHash() {
		GetHashCommand getHash = new GetHashCommand(getLogin(), getPassword());
		getHash.run();
		return getHash.getHash();
	}

	protected String getLogin() {
		return readCredentials().getProperty("login");
	}

	protected String getPassword() {
		return readCredentials().getProperty("password");
	}

	private Properties readCredentials() {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("data-tst/credentials.properties");
			prop.load(input);
			return prop;
		} catch (IOException e) {
			throw new IllegalStateException(format("Error reading {0}."), e);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}

}
