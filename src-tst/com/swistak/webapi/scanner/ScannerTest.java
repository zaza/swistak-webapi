package com.swistak.webapi.scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.swistak.webapi.model.AuctionParams;
import com.swistak.webapi.model.AuctionUnit;
import com.swistak.webapi.model.ConditionProduct;
import com.swistak.webapi.model.Province;

public class ScannerTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void root_folder_does_not_exist() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("does not exist");
		new Scanner(new File(""));
	}

	@Test
	public void root_is_not_a_directory() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("is not a directory");
		new Scanner(new File("data-tst/kategorie.xml"));
	}

	@Test
	public void no_subfolders() {
		assertTrue(new Scanner(new File("data-tst/auctions-root/auction")).scan().isEmpty());
	}

	@Test
	public void auctions_root() {
		Set<AuctionFolder> folders = new Scanner(new File("data-tst/auctions-root")).scan();
		
		assertEquals(1, folders.size());
	}

	@Test
	public void auction() {
		AuctionParams auction = new Scanner(new File("data-tst/auctions-root")).scan().iterator().next();

		assertEquals("Aukcja testowa wystawiona przez WebAPI.\nAukcja testowa wystawiona przez WebAPI.\nAukcja testowa wystawiona przez WebAPI.", auction.getDescription());
		assertEquals("Aukcja testowa wystawiona przez WebAPI", auction.getTitle());
		assertEquals(0.01f, auction.getPrice(), 0.0f);
		assertEquals(AuctionUnit.sztuki, auction.getUnit());
		assertEquals(1, auction.getCount());
		assertEquals("Warszawa", auction.getCity());
		assertEquals(Province.Mazowieckie, auction.getProvince());
		assertEquals(ConditionProduct.uzywany, auction.getCondition());
		//TODO: assertEquals(30696, auction.getCategory());
	}
}
