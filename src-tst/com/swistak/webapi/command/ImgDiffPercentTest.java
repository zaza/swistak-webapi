package com.swistak.webapi.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ImgDiffPercentTest {
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	@Ignore
	public void lenna() throws IOException {
		URL url1 = new URL("http://rosettacode.org/mw/images/3/3c/Lenna50.jpg");
		URL url2 = new URL("http://rosettacode.org/mw/images/b/b6/Lenna100.jpg");
		BufferedImage img1 = ImageIO.read(url1);
		BufferedImage img2 = ImageIO.read(url2);
		ImgDiffPercent diff = new ImgDiffPercent(img1, img2);

		assertEquals(0, diff.run(), 0.0d);
	}
	
	@Test
	public void compare_same() {
		File file1 = new File("data-tst/auctions-root/auction/logo.jpg");
		File file2 = new File("data-tst/auctions-root/auction/logo.jpg");
		ImgDiffPercent diff = new ImgDiffPercent(file1, file2);

		assertEquals(0, diff.run(), 0.0d);
	}
	
	@Test
	public void dimensions_mismatch() {
		File file1 = new File("data-tst/logo_245x58.jpg");
		File file2 = new File("data-tst/logo_from_swistak.jpg");
		
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Images dimensions mismatch");
		new ImgDiffPercent(file1, file2).run();
	}
	
	@Test
	public void logo_uploaded_and_downloaded() {
		File file1 = new File("data-tst/auctions-root/auction/logo.jpg");
		File file2 = new File("data-tst/logo_from_swistak.jpg");
		ImgDiffPercent diff = new ImgDiffPercent(file1, file2);

		assertTrue(diff.run() < 1);
	}
	
	@Test
	public void very_different() {
		File file1 = new File("data-tst/logo_245x58.gif");
		File file2 = new File("data-tst/pink.gif");
		ImgDiffPercent diff = new ImgDiffPercent(file1, file2);

		assertTrue(diff.run()> 30);
	}
}
