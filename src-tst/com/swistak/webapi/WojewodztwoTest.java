package com.swistak.webapi;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class WojewodztwoTest {
	@Test
	public void match_with_get_province() {
		GetProvinceCommand cmd = new GetProvinceCommand();
		cmd.run();
		Province[] provinces = cmd.provinces;

		assertEquals(17, provinces.length);
	}
}
