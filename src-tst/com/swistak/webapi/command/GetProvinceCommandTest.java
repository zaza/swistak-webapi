package com.swistak.webapi.command;

import static java.lang.String.format;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.swistak.webapi.model.Province;

public class GetProvinceCommandTest {

	@Test
	public void get_province_matches_Wojewodztwo_enums() {
		GetProvinceCommand getProvince = new GetProvinceCommand();
		getProvince.run();

		List<com.swistak.webapi.Province> provinces = getProvince.getProvince();
		for (com.swistak.webapi.Province province : provinces) {
			Province.valueOf(province.getProvince().replace('-', '_').replace(' ', '$'));
		}
		for (Province wojewodztwo : Province.values()) {
			com.swistak.webapi.Province province = new com.swistak.webapi.Province(wojewodztwo.toBigInteger(), wojewodztwo.name().replace('_', '-').replace('$', ' '));
			assertTrue(format("No province found for %s", wojewodztwo.name()), provinces.contains(province));
		}
	}
}
