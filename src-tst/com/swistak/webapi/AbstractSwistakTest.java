package com.swistak.webapi;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.swistak.webapi.command.GetHashCommand;
import com.swistak.webapi.model.AuctionParamsBuilder;
import com.swistak.webapi.model.ConditionProduct;
import com.swistak.webapi.model.Province;

public abstract class AbstractSwistakTest {

	private String hash;
	
	protected String getHash() {
		if (hash == null) {
			GetHashCommand getHash = new GetHashCommand(getLogin(),
					getPassword());
			getHash.run();
			hash = getHash.getHash();
		}
		return hash;
	}

	protected String getLogin() {
		return readCredentials().getProperty("login");
	}

	protected String getPassword() {
		return readCredentials().getProperty("password");
	}

	protected Auction_params getTestAuctionParams() {
		AuctionParamsBuilder builder = new AuctionParamsBuilder("[test] Aukcja testowa, wystawiowa przez SwistakAPI", 0.01f) //
		// "Pozostałe > Pozostałe > Serwis Swistak.pl"
		.category(30696) //
		.city("Warszawa") //
		.condition(ConditionProduct.uzywany) //
		.description("Przykładowa aukcja, wystawiona poprzez SwistakAPI") //
		.count(1) //
		.province(Province.Mazowieckie);
		return builder.build();
	}

	private Properties readCredentials() {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("data-tst/credentials.properties");
			prop.load(input);
			return prop;
		} catch (IOException e) {
			throw new IllegalStateException("Error reading data-tst/credentials.properties.", e);
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
