package com.swistak.webapi;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class GetMyAuctionsCommandTest extends AbstractSwistakTest {

	@Test
	public void my_auctions_not_empty() {
		GetMyAuctionsCommand myAuctions = new GetMyAuctionsCommand(getHash());
		
		myAuctions.run();
		
		assertTrue(myAuctions.total_auctions.value.intValue() > 0);
	}
}
