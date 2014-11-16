package com.swistak.webapi.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.swistak.webapi.AbstractSwistakTest;
import com.swistak.webapi.model.GetAuctionsStatus;

public class GetAuctionsCommandTest extends AbstractSwistakTest {

	@Test
	public void no_ids() {
		GetAuctionsCommand getAuctions = new GetAuctionsCommand(getHash());
		getAuctions.run();

		assertEquals(GetAuctionsStatus.ERR_INVALID_IDS, getAuctions.getStatus());
		assertNull(getAuctions.getAuctionArray());
	}

	@Test
	public void invalid_id() {
		GetAuctionsCommand getAuctions = new GetAuctionsCommand(getHash()).auctions(-1);
		getAuctions.run();

		assertEquals(GetAuctionsStatus.ERR_INVALID_IDS, getAuctions.getStatus());
		assertNull(getAuctions.getAuctionArray());
	}

}
