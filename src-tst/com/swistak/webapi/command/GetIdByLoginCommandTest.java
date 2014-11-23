package com.swistak.webapi.command;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.swistak.webapi.AbstractSwistakTest;
import com.swistak.webapi.model.GetIdByLoginStatus;

public class GetIdByLoginCommandTest extends AbstractSwistakTest {

	@Test
	public void user_not_found() {
		GetIdByLoginCommand getId = new GetIdByLoginCommand("");

		assertEquals(0, getId.call().intValue());
		assertEquals(GetIdByLoginStatus.ERR_USER_NOT_FOUND, getId.getStatus());
	}

	@Test
	public void user_zazarniak() {
		GetIdByLoginCommand getId = new GetIdByLoginCommand(getLogin());

		assertEquals(588415, getId.call().intValue());
	}
}
