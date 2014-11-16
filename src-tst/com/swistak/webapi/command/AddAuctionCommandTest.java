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
	public void add_get_and_end() {
		// add
		AddAuctionCommand add = new AddAuctionCommand(getHash(), getTestAuctionParams());
		add.run();

		assertEquals(AddAuctionStatus.OK, add.getStatus());
		assertTrue(add.id.value > 0);
		assertEquals(0, add.id_out.value);

		// get
		GetAuctionsCommand get = new GetAuctionsCommand(getHash()).auctions(add.id.value);
		get.run();

		assertEquals(GetAuctionsStatus.OK, get.getStatus());
		assertEquals(1, get.getAuctionArray().length);
		assertEquals(getTestAuctionParams().getTitle(), get.getAuctionArray()[0].getTitle());

		// end
		Ids ids = new Ids(add.id.value, add.id_out.value);
		EndAuctionsCommand end = new EndAuctionsCommand(getHash(), new Ids[] {ids});
		end.run();

		assertEquals(EndAuctionsStatus.OK, end.getStatus());
		assertEquals(1, end.getEndAuctions().size());
		assertEquals(ids, end.getEndAuctions().get(0));
	}
}
