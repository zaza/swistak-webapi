package com.swistak.webapi.command;
import static com.google.common.base.Preconditions.checkArgument;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

/**
 * A fixed version of
 * http://rosettacode.org/wiki/Percentage_difference_between_images#Java
 *
 */
public class ImgDiffPercent {
	
	private static final Logger LOG = Logger.getLogger(ImgDiffPercent.class);
	
	private final BufferedImage img1;
	private final BufferedImage img2;

	ImgDiffPercent(File file1, File file2) {
		this(readImage(file1), readImage(file2));
	}
	
	ImgDiffPercent(BufferedImage img1, BufferedImage img2) {
		this.img1 = img1;
		this.img2 = img2;
	}

	double run() {
		int width1 = img1.getWidth(null);
		int width2 = img2.getWidth(null);
		int height1 = img1.getHeight(null);
		int height2 = img2.getHeight(null);
		checkArgument(width1 == width2 && height1 == height2, "Images dimensions mismatch");
		long diff = 0;
		for (int i = 0; i < width1; i++) {
			for (int j = 0; j < height1; j++) {
				int rgb1 = img1.getRGB(i, j);
				int rgb2 = img2.getRGB(i, j);
				int r1 = (rgb1 >> 16) & 0xff;
				int g1 = (rgb1 >> 8) & 0xff;
				int b1 = (rgb1) & 0xff;
				int r2 = (rgb2 >> 16) & 0xff;
				int g2 = (rgb2 >> 8) & 0xff;
				int b2 = (rgb2) & 0xff;
				diff += Math.abs(r1 - r2);
				diff += Math.abs(g1 - g2);
				diff += Math.abs(b1 - b2);
			}
		}
		double n = width1 * height1 * 3;
		double p = diff / n / 255.0;
		double result =  p * 100.0;
		LOG.debug("diff percent: " + result);
		return result;
	}
	
	private static BufferedImage readImage(File file) {
		try {
			return ImageIO.read(file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}