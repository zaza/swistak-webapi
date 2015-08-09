package com.swistak.webapi.scanner;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
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
		new Scanner(new File("data-tst/auctions-root/kategorie.xml"));
	}

	@Test
	public void no_subfolders() {
		assertTrue(new Scanner(new File("data-tst/auctions-root/non-auction")).scan().isEmpty());
	}
	
	@Test
	public void missing_kategorie_xml() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("kategorie.xml file does not exist");
		new Scanner(new File("data-tst"));
	}

	@Test
	public void auctions_root() {
		Set<AuctionFolder> folders = new Scanner(new File("data-tst/auctions-root")).scan();
		
		assertEquals(2, folders.size());
	}

	@Test
	public void auction() {
		AuctionParams auction = findWithTitle("Aukcja testowa wystawiona przez WebAPI");

		assertEquals("Aukcja testowa wystawiona przez WebAPI.\nAukcja testowa wystawiona przez WebAPI.\nAukcja testowa wystawiona przez WebAPI.", auction.getDescription());
		assertEquals("Aukcja testowa wystawiona przez WebAPI", auction.getTitle());
		assertEquals(0.01f, auction.getPrice(), 0.0f);
		assertEquals(AuctionUnit.sztuki, auction.getUnit());
		assertEquals(1, auction.getCount());
		assertEquals("Warszawa", auction.getCity());
		assertEquals(Province.Mazowieckie, auction.getProvince());
		assertEquals(ConditionProduct.uzywany, auction.getCondition());
		assertEquals(30696, auction.getCategory());
	}
	
	@Test
	public void auction_no_category() {
		AuctionParams auction = findWithTitle("Buty Nike");

		assertEquals("Buty Nike. Nie Adidasy ani File.", auction.getDescription());
		assertEquals("Buty Nike", auction.getTitle());
		assertEquals(0.01f, auction.getPrice(), 0.0f);
		assertEquals(AuctionUnit.sztuki, auction.getUnit());
		assertEquals(1, auction.getCount());
		assertEquals("Warszawa", auction.getCity());
		assertEquals(Province.Mazowieckie, auction.getProvince());
		assertEquals(ConditionProduct.nowy, auction.getCondition());
		// buty sportowe Nike w obuwiu damskim lub męskim
		assertThat(auction.getCategory(), anyOf(equalTo(4530), equalTo(4536)));
	}
	
	private static AuctionParams findWithTitle(final String title) {
		return Iterables.find(
				new Scanner(new File("data-tst/auctions-root")).scan(),
				new Predicate<AuctionParams>() {

					@Override
					public boolean apply(AuctionParams input) {
						return input.getTitle().equals(title);
					}
				});
	}
}
