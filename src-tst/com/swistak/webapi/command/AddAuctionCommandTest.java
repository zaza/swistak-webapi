package com.swistak.webapi.command;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.swistak.webapi.AbstractSwistakTest;
import com.swistak.webapi.Auction_params;
import com.swistak.webapi.Ids;
import com.swistak.webapi.model.AddAuctionStatus;

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
		Ids ids = addAuction();
		// get
		getAuctionAndAssertTitle(ids.getId(), getTestAuctionParams().getTitle());
		// end
		endAuction(ids);
	}
}
