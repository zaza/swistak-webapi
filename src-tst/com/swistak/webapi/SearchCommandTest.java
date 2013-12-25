package com.swistak.webapi;

import static org.junit.Assert.*;

import org.junit.Test;

import com.swistak.webapi.SearchCommand;

public class SearchCommandTest {

	@Test
	public void nokia() {
		SearchCommand search = SearchCommand.fraza("nokia");
		search.run();
		assertTrue(search.total_found.value.intValue() > 0);
	}

	@Test
	public void nie_ma_i_nie_bedzie() {
		SearchCommand search = SearchCommand.fraza("nie ma i nie będzie");
		search.run();
		assertNull(search.total_found.value);
		assertNull(search.search_auctions.value);
		assertEquals(0, search._return.value.length);
		assertEquals(0, search.err.value.length);
	}
}
