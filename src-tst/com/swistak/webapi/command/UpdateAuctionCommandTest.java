package com.swistak.webapi.command;

import org.junit.Test;

import com.swistak.webapi.AbstractSwistakTest;
import com.swistak.webapi.Ids;
import com.swistak.webapi.builder.UpdateAuctionParamsBuilder;

public class UpdateAuctionCommandTest extends AbstractSwistakTest {

	@Test
	public void add_update_get_and_end() {
		// add
		Ids ids = addAuction();

		// update
		UpdateAuctionParamsBuilder paramsBuilder = new UpdateAuctionParamsBuilder(ids.getId());
		paramsBuilder.title("Aukcja testowa zauktualizowana przez WebAPI");
		UpdateAuctionCommand update = new UpdateAuctionCommand(getHash(), paramsBuilder.build());
		update.run();
		
		// get
		getAuctionAndAssertTitle(ids.getId(), "Aukcja testowa zauktualizowana przez WebAPI");

		// end
		endAuction(ids);
	}
}
