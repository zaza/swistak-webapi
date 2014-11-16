package com.swistak.webapi.command;

import static java.lang.String.format;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.swistak.webapi.Province;
import com.swistak.webapi.model.Wojewodztwo;

public class GetProvinceCommandTest {

	@Test
	public void get_province_matches_Wojewodztwo_enums() {
		GetProvinceCommand getProvince = new GetProvinceCommand();
		getProvince.run();

		List<Province> provinces = getProvince.getProvince();
		for (Province province : provinces) {
			Wojewodztwo.valueOf(province.getProvince().replace('-', '_').replace(' ', '$'));
		}
		for (Wojewodztwo wojewodztwo : Wojewodztwo.values()) {
			Province province = new Province(wojewodztwo.toBigInteger(), wojewodztwo.name().replace('_', '-').replace('$', ' '));
			assertTrue(format("No province found for %s", wojewodztwo.name()), provinces.contains(province));
		}
	}
}
