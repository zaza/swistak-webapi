package com.swistak.webapi.scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

public class AuctionFolderTest {

	@Rule
	public TemporaryFolder temp = new TemporaryFolder();

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void no_id_in_new_auction() throws IOException {
		AuctionFolder auction = new AuctionFolder(new File("data-tst/auctions-root/auction"), null);

		assertFalse(auction.hasId());

		thrown.expect(IllegalStateException.class);
		auction.getId();
	}

	@Test
	public void setId_and_save() throws IOException {
		temp.newFile("parametry.properties");
		AuctionFolder auction1 = new AuctionFolder(temp.getRoot(), null);

		auction1.setId(1);
		auction1.save();

		AuctionFolder auction2 = new AuctionFolder(temp.getRoot(), null);
		assertEquals(1, auction2.getId());
	}
}
