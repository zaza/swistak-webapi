package com.swistak.webapi.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AuctionFotosBuilderTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void no_fotos() {
		AuctionFotosBuilder builder = new AuctionFotosBuilder();

		assertEquals(0, builder.build().length);
	}

	@Test
	public void gif() {
		AuctionFotosBuilder builder = new AuctionFotosBuilder();

		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Only JPG images are accepted.");
		builder.foto(new File("data-tst/logo_245x58.gif"));
	}

	@Test
	public void too_small() {
		AuctionFotosBuilder builder = new AuctionFotosBuilder();

		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Images must be at least 160x160.");
		builder.foto(new File("data-tst/logo_245x58.jpg"));
	}
	
	@Test
	public void ok() {
		AuctionFotosBuilder builder = new AuctionFotosBuilder().foto(new File("data-tst/auctions-root/auction/logo.jpg"));

		assertEquals(1, builder.build().length);
		assertFalse(builder.build()[0].getSrc().isEmpty());
		assertTrue(builder.build()[0].getUrl().isEmpty());
	}
}
