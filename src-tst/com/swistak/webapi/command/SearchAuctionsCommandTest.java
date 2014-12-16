package com.swistak.webapi.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SearchAuctionsCommandTest {

	@Test
	public void nokia() {
		SearchAuctionsCommand search = SearchAuctionsCommand.fraza("nokia");
		search.call();
		assertTrue(search.getTotalAuctions() > 0);
	}

	@Test
	public void random_string() {
		SearchAuctionsCommand search = SearchAuctionsCommand.fraza("qwertyuiop");
		search.call();
		assertEquals(0, search.getTotalAuctions());
		assertTrue(search.getSearchAuctions().isEmpty());
		assertEquals(0, search._return.value.length);
		assertEquals(0, search.err.value.length);
	}
	
	@Test
	public void gazeta_motocykl() {
		SearchAuctionsCommand search = SearchAuctionsCommand.fraza("Czasopismo gazeta Motocykl").uzywany();
		search.call();
		assertTrue(search.getTotalAuctions() > 0);
	}
	
	@Test
	public void komiks_conan() {
		SearchAuctionsCommand search = SearchAuctionsCommand.fraza("komiks Conan").uzywany();
		search.call();
		assertTrue(search.getTotalAuctions() > 0);
	}
	@Test
	public void wavelength() {
		SearchAuctionsCommand search = SearchAuctionsCommand.fraza("Wavelength Empik Workbook kaseta").uzywany();
		search.call();
		assertTrue(search.getTotalAuctions() > 0);
	}
	
}
