package com.swistak.webapi.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.swistak.webapi.AbstractSwistakTest;
import com.swistak.webapi.Ids;
import com.swistak.webapi.model.EndAuctionsStatus;

public class EndAuctionsCommandTest extends AbstractSwistakTest {

	@Test
	public void bad_hash() {
		EndAuctionsCommand endAuctions = new EndAuctionsCommand("bad-hash", new Ids[] {});
		endAuctions.call();

		assertEquals(EndAuctionsStatus.ERR_AUTHORIZATION, endAuctions.getStatus());
		assertTrue(endAuctions.getEndAuctions().isEmpty());
	}

	@Test
	public void end_with_no_ids() {
		EndAuctionsCommand endAuctions = new EndAuctionsCommand(getHash(), new Ids[] {});
		endAuctions.call();

		assertEquals(EndAuctionsStatus.OK, endAuctions.getStatus());
		assertTrue(endAuctions.getEndAuctions().isEmpty());
	}

	@Test
	public void end_with_invalid_id() {
		EndAuctionsCommand endAuctions = new EndAuctionsCommand(getHash(), new Ids[] {new Ids(-1, -1)});
		endAuctions.call();

		assertEquals(EndAuctionsStatus.OK, endAuctions.getStatus());
		assertTrue(endAuctions.getEndAuctions().isEmpty());
	}
}
