package com.swistak.webapi.category;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;

import com.google.common.net.HttpHeaders;

public class CategoryFileProvider {

	private static final Logger LOG = Logger.getLogger(CategoryFileProvider.class);
	
	private static final URI KATEGORIE_XML_URI = URI.create("http://www.swistak.pl/download/kategorie.xml");
	
	static final String KATEGORIE_FILENAME = "kategorie.xml";
	
	final private File root;

	public CategoryFileProvider(File root) {
		this.root = root;
		HttpURLConnection.setFollowRedirects(false);
	}

	public File get() throws IOException {
		File file = new File(root, KATEGORIE_FILENAME);
		if (!file.exists() || isOutDated(file)) {
			fetch(file);
		}
		return file;
	}

	private boolean isOutDated(File file) throws IOException {
		try {
			// Last-Modified date is usually few secs after the date in the xml file
			return !DateUtils.isSameDay(getDate(file), getHeadLastModifiedDate());
		} catch (ParseException e) {
			LOG.warn("Unable to determine date of the existing categories file, considering it as out-dated", e);
			return true;
		}
	}

	private Date getDate(File file) throws ParseException {
		String date = new CategoryTreeBuilder(file).buildRootOnly().getRoot().getData().getName();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.parse(date);
	}

	private Date getHeadLastModifiedDate() throws IOException {
		HttpURLConnection con = openConnection();
		con.setRequestMethod("HEAD");
		return new Date(con.getHeaderFieldDate(HttpHeaders.LAST_MODIFIED, System.currentTimeMillis()));
	}

	private void fetch(File file) throws IOException {
		HttpURLConnection con = openConnection();
		con.connect();
		FileUtils.copyInputStreamToFile((InputStream) con.getContent(), file);
	}

	private static HttpURLConnection openConnection() throws IOException {
		return (HttpURLConnection) KATEGORIE_XML_URI.toURL().openConnection();
	}

}
