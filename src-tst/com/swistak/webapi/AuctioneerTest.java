package com.swistak.webapi;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentCaptor;

import com.google.common.io.Files;

public class AuctioneerTest extends AbstractSwistakTest {

	@Rule
	public TemporaryFolder temp = new TemporaryFolder();

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void no_hash() {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("Neither hash nor credentials have been provided.");
		Auctioneer.create().start();
	}

	@Test
	public void no_folder() {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("Root directory must be provided.");
		Auctioneer.create().hash(getHash()).start();
	}

	@Test
	public void folder_with_no_auctions() throws IOException {
		Files.copy(new File("data-tst/auctions-root/kategorie.xml"), new File(temp.getRoot(), "kategorie.xml"));
		Logger mockedLog = mock(Logger.class);
		Auctioneer.LOG = mockedLog;

		Auctioneer.create().hash(getHash()).scan(temp.getRoot()).start();

		verify(mockedLog).warn("Nie znaleziono folderów z aukcjami");
	}
	
	@Test
	@Ignore
	public void folder_with_auction_to_add() throws IOException {
		Files.copy(new File("data-tst/auctions-root/kategorie.xml"), new File(temp.getRoot(), "kategorie.xml"));
		FileUtils.copyDirectory(new File("data-tst/auctions-root/auction-no-category"), new File(temp.getRoot(), "auction"));
		Logger mockedLog = mock(Logger.class);
		Auctioneer.LOG = mockedLog;
		Auctioneer auctioneer = Auctioneer.create().hash(getHash()).scan(temp.getRoot());
		
		// scan => add
		auctioneer.start();

		ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
		verify(mockedLog).info(argument.capture());
		String value = argument.getValue();
		assertTrue(value.startsWith("Dodano aukcje o numerze "));
		// FIXME
		long id = Long.parseLong(value.substring(value.lastIndexOf(' ') + 1));

		// scan again => try to update, but fail
		auctioneer.start();
		
		verify(mockedLog).error("Aktualizowanie aukcji nie jest jeszcze dostępne. Aukcja "+id+" pominięta.");
		
		endAuction(new Ids(id, 0));
	}

}
