package com.swistak.webapi.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SearchCommandTest {

	@Test
	public void nokia() {
		SearchAuctionsCommand search = SearchAuctionsCommand.fraza("nokia");
		search.run();
		assertTrue(search.total_found.value.intValue() > 0);
	}

	@Test
	public void random_string() {
		SearchAuctionsCommand search = SearchAuctionsCommand.fraza("qwertyuiop");
		search.run();
		assertNull(search.total_found.value);
		assertNull(search.search_auctions.value);
		assertEquals(0, search._return.value.length);
		assertEquals(0, search.err.value.length);
	}
}
