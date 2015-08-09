package com.swistak.webapi.category;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class CategoryFileProviderTest {

	private static final File TEST_CATEGORIES_FILE = new File("data-tst/auctions-root/kategorie.xml");
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Test
	public void do_nothing_if_exists_and_up_to_date() throws IOException {
		File tempCategoriesFile = new File(folder.getRoot(), CategoryFileProvider.KATEGORIE_FILENAME);
		CategoryFileProvider provider = new CategoryFileProvider(folder.getRoot());
		FileUtils.copyFile(TEST_CATEGORIES_FILE, tempCategoriesFile);
		long checksum = FileUtils.checksumCRC32(tempCategoriesFile);

		File file = provider.get();

		assertTrue(file.exists());
		assertEquals(checksum, FileUtils.checksumCRC32(file)); // not updated
	}

	@Test
	public void fetch_if_not_exists() throws IOException {
		CategoryFileProvider provider = new CategoryFileProvider(folder.getRoot());

		File file = provider.get();

		assertTrue(file.exists()); // downloaded
	}

	@Test
	public void fetch_if_out_dated() throws IOException {
		File tempCategoriesFile = new File(folder.getRoot(), CategoryFileProvider.KATEGORIE_FILENAME);
		CategoryFileProvider provider = new CategoryFileProvider(folder.getRoot());
		FileUtils.write(tempCategoriesFile,
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?><kategorie date=\"2014-05-12 12:31:28\"></kategorie>");
		long checksum = FileUtils.checksumCRC32(tempCategoriesFile);

		File file = provider.get();

		assertTrue(file.exists());
		assertThat(FileUtils.checksumCRC32(file), not(equalTo(checksum))); // updated
	}
	
	@Test
	public void fetch_if_failed_to_parse_date() throws IOException {
		File tempCategoriesFile = new File(folder.getRoot(), CategoryFileProvider.KATEGORIE_FILENAME);
		CategoryFileProvider provider = new CategoryFileProvider(folder.getRoot());
		FileUtils.write(tempCategoriesFile,
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?><kategorie date=\"not-a-date\"></kategorie>");
		long checksum = FileUtils.checksumCRC32(tempCategoriesFile);

		File file = provider.get();

		assertTrue(file.exists());
		assertThat(FileUtils.checksumCRC32(file), not(equalTo(checksum))); // updated
	}

	@Test
	public void do_nothing_if_not_category_file() throws IOException {
		File root = new File("data-tst/auctions-root/non-auction");
		File categoriesFile = new File(root, CategoryFileProvider.KATEGORIE_FILENAME);
		CategoryFileProvider provider = new CategoryFileProvider(root);
		long checksum = FileUtils.checksumCRC32(categoriesFile);

		File file = provider.get();

		assertTrue(file.exists());
		assertEquals(checksum, FileUtils.checksumCRC32(file)); // not updated
	}
}
