package com.swistak.webapi.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.swistak.webapi.AbstractSwistakTest;
import com.swistak.webapi.Ids;
import com.swistak.webapi.model.EndAuctionStatus;

public class EndAuctionsCommandTest extends AbstractSwistakTest {

	@Test
	public void bad_hash() {
		EndAuctionsCommand endAuctions = new EndAuctionsCommand("bad-hash", new Ids[] {});
		endAuctions.run();

		assertEquals(EndAuctionStatus.ERR_AUTHORIZATION, endAuctions.status);
		assertTrue(endAuctions.getEndAuctions().isEmpty());
	}

	@Test
	public void end_with_no_ids() {
		EndAuctionsCommand endAuctions = new EndAuctionsCommand(getHash(), new Ids[] {});
		endAuctions.run();

		assertEquals(EndAuctionStatus.OK, endAuctions.status);
		assertTrue(endAuctions.getEndAuctions().isEmpty());
	}

	@Test
	public void end_with_invalid_id() {
		EndAuctionsCommand endAuctions = new EndAuctionsCommand(getHash(), new Ids[] {new Ids(-1, -1)});
		endAuctions.run();

		assertEquals(EndAuctionStatus.OK, endAuctions.status);
		assertTrue(endAuctions.getEndAuctions().isEmpty());
	}
}
