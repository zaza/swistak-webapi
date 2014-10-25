package com.swistak.webapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GetHashCommandTest {

	@Test
	public void md5() {
		assertEquals("1b587fdf977c0769c9502bdbb81eb982",
				GetHashCommand.md5("moje_tajne_haslo"));
	}

	@Test
	public void not_run() {
		GetHashCommand getHash = new GetHashCommand("whatever", "does-not-matter");

		assertNull(getHash.status);
		assertNull(getHash.hash);
	}

	@Test
	public void bad_password() {
		GetHashCommand getHash = new GetHashCommand("bad-user", "bad-password");
		getHash.run();

		assertEquals(GetHashStatus.ERR_USER_PASSWD, getHash.status);
		assertNull(getHash.hash);
	}
	
	@Test
	public void user_blocked_for_an_hour() {
		GetHashCommand getHash = new GetHashCommand("user-to-be-blocked", "bad-password");
		for (int i = 0; i < 4; i++) {
			getHash.run();
		}

		assertEquals(GetHashStatus.ERR_USER_BLOCKED_ONE_HOUR, getHash.status);
		assertNull(getHash.hash);
	}
}
