package com.swistak.webapi.builder;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.io.BaseEncoding;
import com.google.common.io.Files;
import com.swistak.webapi.Auction_foto;

public class AuctionFotosBuilder {

	private static final Logger LOG = Logger.getLogger(AuctionFotosBuilder.class);
	
	private List<File> fotos = Lists.newArrayList();

	public AuctionFotosBuilder foto(File foto) {
		checkArgument(isJpg(foto), "Only JPG images are accepted.");
		checkArgument(isMin160x160(foto), "Images must be at least 160x160.");
		fotos.add(foto);
		return this;
	}

	private static boolean isJpg(File foto) {
		return foto.getName().toLowerCase().endsWith(".jpg");
	}

	private static boolean isMin160x160(File foto) {
		try {
			BufferedImage image = ImageIO.read(foto);
			return image.getHeight() >= 160 && image.getWidth() >= 160;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Auction_foto[] build() {
		List<Auction_foto> result = Lists.newArrayListWithCapacity(fotos.size());
		for (File fotoFile : fotos) {
			Auction_foto auctionFoto = new Auction_foto();
			try {
				byte[] bytes = Files.asByteSource(fotoFile).read();
				String bytesEncoded = BaseEncoding.base64().encode(bytes);
				auctionFoto.setSrc(bytesEncoded);
				auctionFoto.setUrl("");
				result.add(auctionFoto);
			} catch (IOException e) {
				LOG.error(format("Failed to read file %s, skipping.", fotoFile), e);
			}
		}
		return result.toArray(new Auction_foto[result.size()]);
	}
}
