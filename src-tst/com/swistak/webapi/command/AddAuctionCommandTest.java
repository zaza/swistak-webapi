package com.swistak.webapi.command;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.io.BaseEncoding;
import com.google.common.io.Files;
import com.swistak.webapi.AbstractSwistakTest;
import com.swistak.webapi.Auction_params;
import com.swistak.webapi.Ids;
import com.swistak.webapi.model.AddAuctionStatus;
import com.swistak.webapi.model.GetAuctionsStatus;

public class AddAuctionCommandTest extends AbstractSwistakTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void add_with_no_params() {
		Auction_params params = new Auction_params();
		AddAuctionCommand add = new AddAuctionCommand(getHash(), params);

		thrown.expect(RuntimeException.class);
		thrown.expectMessage("Non nillable element 'title' is null");
		add.run();
	}
	
	@Test
	public void add_with_invalid_hash() {
		AddAuctionCommand add = new AddAuctionCommand("invalid-hash", getTestAuctionParams());
		add.run();

		assertEquals(AddAuctionStatus.ERR_AUTHORIZATION, add.getStatus());
		assertEquals(new Ids(0,0), add.getIds());
	}

	@Test
	public void add_get_and_end() throws IOException {
		// add
		Ids ids = addAuction();

		// get
		GetAuctionsCommand get = new GetAuctionsCommand(getHash()).auctions(ids.getId());
		get.run();

		assertEquals(GetAuctionsStatus.OK, get.getStatus());
		assertEquals(1, get.getAuctionArray().length);
		assertEquals(getTestAuctionParams().getTitle(), get.getAuctionArray()[0].getTitle());
		// TODO: http://www.swistak.pl/forum/29,SwistakAPI/203,brak_zdjec_w_odpowiedzi_na_get_auctions
		assertEquals(1, get.getAuctionArray()[0].getFotos().length);
		assertEquals("", get.getAuctionArray()[0].getFotos()[0].getUrl());
		assertEquals(readBytes(new File("data-tst/auctions-root/auction/logo.jpg")), decode(get.getAuctionArray()[0].getFotos()[0].getSrc()));

		// end
		endAuction(ids);
	}

	private static byte[] decode(String string) {
		return BaseEncoding.base64().decode(string);
	}
	
	
	private static byte[] readBytes(File file) throws IOException {
		return Files.asByteSource(file).read();
	}
}
