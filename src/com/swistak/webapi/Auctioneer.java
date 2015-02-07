package com.swistak.webapi;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

import java.io.File;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.common.annotations.VisibleForTesting;
import com.swistak.webapi.builder.AddAuctionParamsBuilder;
import com.swistak.webapi.builder.AuctionFotosBuilder;
import com.swistak.webapi.command.AddAuctionCommand;
import com.swistak.webapi.command.GetHashCommand;
import com.swistak.webapi.scanner.AuctionFolder;
import com.swistak.webapi.scanner.Scanner;

public class Auctioneer {

	@VisibleForTesting
	static Logger LOG = Logger.getLogger(Auctioneer.class);

	private File folder;

	private String hash;

	public static Auctioneer create() {
		return new Auctioneer();
	}

	public Auctioneer scan(File folder) {
		this.folder = folder;
		return this;
	}

	public Auctioneer hash(String hash) {
		this.hash = hash;
		return this;
	}

	public Auctioneer credentials(String login, String pass) {
		this.hash = new GetHashCommand(login, pass).call();
		return this;
	}

	public void start() {
		checkNotNull(hash, "Neither hash nor credentials have been provided.");
		Set<AuctionFolder> auctions = new Scanner(folder).scan();
		if (auctions.isEmpty()) {
			LOG.warn("Nie znaleziono folderów z aukcjami");
		}
		for (AuctionFolder auction : auctions) {
			if (auction.hasId()) {
				updateIfNeeded(auction);
			} else {
				add(auction);
			}
		}
	}

	private void updateIfNeeded(AuctionFolder auction) {
		LOG.error(format("Aktualizowanie aukcji nie jest jeszcze dostępne. Aukcja %d pominięta.", auction.getId()));
	}

	private void add(AuctionFolder auction) {
		try {
			AddAuctionParamsBuilder paramsBuilder = new AddAuctionParamsBuilder(auction.getTitle(), auction.getPrice());

			// params
			paramsBuilder.description(auction.getDescription());
			paramsBuilder.category(auction.getCategory());
			paramsBuilder.city(auction.getCity());
			paramsBuilder.condition(auction.getCondition());
			paramsBuilder.province(auction.getProvince());
			paramsBuilder.count(auction.getCount());

			// fotos
			AuctionFotosBuilder fotosBuilder = new AuctionFotosBuilder();
			for (File photo : auction.getPhotos()) {
				fotosBuilder.foto(photo);
			}
			paramsBuilder.fotos(fotosBuilder.build());

			// add
			Ids ids = new AddAuctionCommand(hash, paramsBuilder.build()).call();
			storeIds(auction, ids);
			LOG.info("Dodano aukcje o numerze " + ids.getId());
		} catch (Exception e) {
			LOG.error("Nieudało się dodać aukcji " + auction.getTitle());
		}
	}

	private void storeIds(AuctionFolder auction, Ids ids) {
		auction.setId(ids.getId());
		auction.save();
	}

}
