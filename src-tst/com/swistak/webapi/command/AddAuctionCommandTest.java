package com.swistak.webapi.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.swistak.webapi.AbstractSwistakTest;
import com.swistak.webapi.Auction_foto;
import com.swistak.webapi.Auction_params;
import com.swistak.webapi.Cost_delivery;
import com.swistak.webapi.Get_auctionArray;
import com.swistak.webapi.Ids;
import com.swistak.webapi.My_auction;
import com.swistak.webapi.builder.AbstractAuctionParamsBuilder;
import com.swistak.webapi.model.AddAuctionStatus;
import com.swistak.webapi.model.DeliveryInfo;
import com.swistak.webapi.model.DeliveryInfoWithCostDelivery;

public class AddAuctionCommandTest extends AbstractSwistakTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void add_with_no_params() {
		Auction_params params = new Auction_params();
		AddAuctionCommand add = new AddAuctionCommand(getHash(), params);

		thrown.expect(RuntimeException.class);
		thrown.expectMessage("Non nillable element 'title' is null");
		add.call();
	}
	
	@Test
	public void add_with_invalid_hash() {
		AddAuctionCommand add = new AddAuctionCommand("invalid-hash", getTestAuctionParams());
		add.call();

		assertEquals(AddAuctionStatus.ERR_AUTHORIZATION, add.getStatus());
		assertEquals(new Ids(0,0), add.getIds());
	}

	@Test
	public void add_with_fotos() throws IOException {
		// add
		Ids ids = addAuction();
		try {
		// get
		// TODO: http://www.swistak.pl/forum/29,SwistakAPI/203,brak_zdjec_w_odpowiedzi_na_get_auctions
//		GetAuctionsCommand get = new GetAuctionsCommand(getHash()).auctions(ids.getId());
//		get.run();
		My_auction auction = getMyAuctionWithId(ids.getId());
		assertNotNull(auction);

//		assertEquals(GetAuctionsStatus.OK, get.getStatus());
//		assertEquals(1, get.getAuctionArray().length);
		assertEquals(getTestAuctionParams().getTitle(), auction.getTitle());
		assertEquals(1, auction.getFotos().length);
		Auction_foto foto = auction.getFotos()[0];
		assertTrue(foto.getSrc().isEmpty());
		assertFalse(foto.getUrl().isEmpty());
		assertImagesAlmostIdentical(new File("data-tst/auctions-root/auction/logo.jpg"), new URL(foto.getUrl()));

		} finally {
			// end
			endAuction(ids);
		}
	}
	
	@Test
	public void add_with_delivery() throws IOException {
		// add
		AbstractAuctionParamsBuilder<Auction_params> builder = getTestAuctionParamsBuilder();
		builder.deliveryInfo(new DeliveryInfoWithCostDelivery(DeliveryInfo.list_ekonomiczny, 3));
		AddAuctionCommand add = new AddAuctionCommand(getHash(), builder.build());
		add.call();
		Ids ids = add.getIds();
		try {
			// get
			GetAuctionsCommand get = new GetAuctionsCommand(getHash()).auctions(ids.getId());
			List<Get_auctionArray> auctions = get.call();
			assertEquals(1, auctions.size());
			Get_auctionArray auction = auctions.get(0);

			assertEquals(getTestAuctionParams().getTitle(), auction.getTitle());
			assertEquals(DeliveryInfo.list_ekonomiczny.getId(), auction.getDelivery_info().intValue());
			Cost_delivery cost_1 = auction.getCosts_delivery().getCost_1()[0];
			assertEquals(1, cost_1.getMin().intValue()); // min
			assertEquals(1, cost_1.getMax().intValue()); // max
			assertEquals(3, cost_1.getCost(), 0.0f); // price
			assertNull(auction.getCosts_delivery().getCost_2());
			assertNull(auction.getCosts_delivery().getCost_3());
			assertNull(auction.getCosts_delivery().getCost_4());
			assertNull(auction.getCosts_delivery().getCost_5());
			assertNull(auction.getCosts_delivery().getCost_6());
			assertNull(auction.getCosts_delivery().getCost_7());
			assertNull(auction.getCosts_delivery().getCost_8());
			assertNull(auction.getCosts_delivery().getCost_9());
			assertNull(auction.getCosts_delivery().getCost_10());
		} finally {
			// end
			endAuction(ids);
		}
	}

	private static void assertImagesAlmostIdentical(File expected, URL actual) throws IOException {
		BufferedImage img1 = ImageIO.read(expected);
		BufferedImage img2 = ImageIO.read(actual);
		ImgDiffPercent diff = new ImgDiffPercent(img1, img2);
		assertTrue(diff.run() < 1);
	}

	private My_auction getMyAuctionWithId(long id) {
		GetMyAuctionsCommand getMyAuctions = new GetMyAuctionsCommand(getHash());
		getMyAuctions.call();
		
		List<My_auction> myAuctions = getMyAuctions.getMyAuctions();
		My_auction found = findWithId(myAuctions, id);
		if (found == null) {
			while(!getMyAuctions.getMyAuctions().isEmpty() && found == null) {
				getMyAuctions.incrementOffset();
				getMyAuctions.call();
				myAuctions = getMyAuctions.getMyAuctions();
				found = findWithId(myAuctions, id);
			}
		}
		return found;
	}
	
	private static My_auction findWithId(List<My_auction> myAuctions, long id) {
		for (My_auction my_auction : myAuctions) {
			if (my_auction.getId() == id)
				return my_auction;
		}
		return null;
	}
}
