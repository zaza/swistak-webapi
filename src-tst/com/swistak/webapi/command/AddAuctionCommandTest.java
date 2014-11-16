package com.swistak.webapi.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.swistak.webapi.AbstractSwistakTest;
import com.swistak.webapi.Auction_params;
import com.swistak.webapi.Ids;
import com.swistak.webapi.model.AddAuctionStatus;
import com.swistak.webapi.model.EndAuctionsStatus;
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
	public void add_get_and_end() {
		// add
		AddAuctionCommand add = new AddAuctionCommand(getHash(), getTestAuctionParams());
		add.run();

		assertEquals(AddAuctionStatus.OK, add.getStatus());
		assertTrue(add.getIds().getId() > 0);
		assertEquals(0, add.getIds().getId_out());

		// get
		GetAuctionsCommand get = new GetAuctionsCommand(getHash()).auctions(add.getIds().getId());
		get.run();

		assertEquals(GetAuctionsStatus.OK, get.getStatus());
		assertEquals(1, get.getAuctionArray().length);
		assertEquals(getTestAuctionParams().getTitle(), get.getAuctionArray()[0].getTitle());

		// end
		EndAuctionsCommand end = new EndAuctionsCommand(getHash(), new Ids[] {add.getIds()});
		end.run();

		assertEquals(EndAuctionsStatus.OK, end.getStatus());
		assertEquals(1, end.getEndAuctions().size());
		assertEquals(add.getIds(), end.getEndAuctions().get(0));
	}
}
