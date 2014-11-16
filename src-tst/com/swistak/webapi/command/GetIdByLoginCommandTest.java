package com.swistak.webapi.command;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GetIdByLoginCommandTest {

	@Test
	public void user_not_found() {
		GetIdByLoginCommand getId = new GetIdByLoginCommand("");

		assertEquals(0, getId.getId());
	}

	@Test
	public void user_zazarniak() {
		GetIdByLoginCommand getId = new GetIdByLoginCommand("zazarniak");
		getId.run();

		assertEquals(588415, getId.getId());
	}
}
