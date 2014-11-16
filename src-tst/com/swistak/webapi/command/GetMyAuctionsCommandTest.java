package com.swistak.webapi.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.swistak.webapi.AbstractSwistakTest;

public class GetMyAuctionsCommandTest extends AbstractSwistakTest {

	@Test
	public void my_auctions_not_empty() {
		GetMyAuctionsCommand myAuctions = new GetMyAuctionsCommand(getHash());
		
		myAuctions.run();
		
		assertTrue(myAuctions.getTotalAuctions() > 0);
		assertFalse(myAuctions.getMyAuctions().isEmpty());
	}

	@Test
	public void my_auctions_by_login_not_empty() {
		GetIdByLoginCommand getId = new GetIdByLoginCommand("miklosznet");
		getId.run();
		
		GetMyAuctionsCommand myAuctions = new GetMyAuctionsCommand(getHash()).userId(getId.getId()).limit(10);
		myAuctions.run();
		
		assertTrue(myAuctions.getTotalAuctions() > 0);
		assertFalse(myAuctions.getMyAuctions().isEmpty());
	}

	@Test
	public void my_auctions_by_login_empty() {
		GetIdByLoginCommand getId = new GetIdByLoginCommand("2sloma");
		getId.run();
		
		GetMyAuctionsCommand myAuctions = new GetMyAuctionsCommand(getHash()).userId(getId.getId());
		myAuctions.run();
		
		assertEquals(0, myAuctions.getTotalAuctions());
		assertTrue(myAuctions.getMyAuctions().isEmpty());
	}
	
}
