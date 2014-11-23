package com.swistak.webapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.swistak.webapi.builder.AbstractAuctionParamsBuilder;
import com.swistak.webapi.builder.AddAuctionParamsBuilder;
import com.swistak.webapi.builder.AuctionFotosBuilder;
import com.swistak.webapi.command.AddAuctionCommand;
import com.swistak.webapi.command.EndAuctionsCommand;
import com.swistak.webapi.command.GetAuctionsCommand;
import com.swistak.webapi.command.GetHashCommand;
import com.swistak.webapi.model.AddAuctionStatus;
import com.swistak.webapi.model.ConditionProduct;
import com.swistak.webapi.model.EndAuctionsStatus;
import com.swistak.webapi.model.GetAuctionsStatus;
import com.swistak.webapi.model.Province;

public abstract class AbstractSwistakTest {

	private String hash;
	
	protected String getHash() {
		if (hash == null) {
			GetHashCommand getHash = new GetHashCommand(getLogin(),
					getPassword());
			hash = getHash.call();
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
		AbstractAuctionParamsBuilder<Auction_params> builder = new AddAuctionParamsBuilder("Aukcja testowa wystawiona przez WebAPI", 0.01f) //
		// "Pozostałe > Pozostałe > Serwis Swistak.pl"
		.category(30696) //
		.city("Warszawa") //
		.condition(ConditionProduct.uzywany) //
		.description("Aukcja testowa wystawiona przez WebAPI.<br />Aukcja testowa wystawiona przez WebAPI.<br />Aukcja testowa wystawiona przez WebAPI.<br />") //
		.count(1) //
		.province(Province.Mazowieckie)
		.fotos(new AuctionFotosBuilder().foto(new File("data-tst/auctions-root/auction/logo.jpg")).build());
		return builder.build();
	}
	
	protected Ids addAuction() {
		AddAuctionCommand add = new AddAuctionCommand(getHash(), getTestAuctionParams());
		add.call();

		assertEquals(AddAuctionStatus.OK, add.getStatus());
		assertTrue(add.getIds().getId() > 0);
		assertEquals(0, add.getIds().getId_out());
		return add.getIds();
	}
	
	protected void getAuctionAndAssertTitle(long id, String title) {
		GetAuctionsCommand get = new GetAuctionsCommand(getHash()).auctions(id);
		get.call();

		assertEquals(GetAuctionsStatus.OK, get.getStatus());
		assertEquals(1, get.getAuctionArray().size());
		assertEquals(title, get.getAuctionArray().get(0).getTitle());
	}
	
	protected void endAuction(Ids ids) {
		EndAuctionsCommand end = new EndAuctionsCommand(getHash(), new Ids[] {ids});
		end.call();

		assertEquals(EndAuctionsStatus.OK, end.getStatus());
		assertEquals(1, end.getEndAuctions().size());
		assertEquals(ids, end.getEndAuctions().get(0));
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
