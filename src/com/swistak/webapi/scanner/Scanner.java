package com.swistak.webapi.scanner;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.common.collect.Sets;
import com.swistak.webapi.category.Category;
import com.swistak.webapi.category.CategoryFileProvider;
import com.swistak.webapi.category.CategoryGuesser;
import com.swistak.webapi.category.CategoryTreeBuilder;
import com.swistak.webapi.category.Tree;

public class Scanner {

	private static final Logger LOG = Logger.getLogger(Scanner.class);
	
	private File root;

	public Scanner(File root) {
		checkNotNull(root, "Root directory must be provided.");
		checkArgument(root.exists(), "%s does not exist.", root);
		checkArgument(root.isDirectory(), "%s is not a directory.", root);
		checkArgument(new File(root, "kategorie.xml").exists(), "kategorie.xml file does not exist.");
		this.root = root;
	}

	public Set<AuctionFolder> scan() {
		File file;
		try {
			file = new CategoryFileProvider(root).get();
		} catch (IOException e) {
			LOG.error("Failed to obtain categories file", e);
			return Collections.emptySet();
		}
		CategoryTreeBuilder builder = new CategoryTreeBuilder(file);
		Tree<Category> tree = builder.build();
		File[] folders = root.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File file) {
				return file.isDirectory();
			}
		});
		if (folders.length == 0)
			return Collections.emptySet();

		Set<AuctionFolder> auctionFolders = Sets.newHashSetWithExpectedSize(folders.length);
		for (File folder : folders) {
			AuctionFolder auctionFolder = new AuctionFolder(folder, tree);
			if (auctionFolder.hasDescription() && auctionFolder.hasParameters()) {
				if (!auctionFolder.hasCategory()) {
					Category guess = CategoryGuesser.withCategoryTree(tree).guess(auctionFolder.getTitle());
					auctionFolder.setCategory(guess.getId());
				}
				File[] photos = folder.listFiles(new FilenameFilter() {
					
					@Override
					public boolean accept(File dir, String name) {
						return name.endsWith(".jpg");
					}
				});
				auctionFolder.setPhotos(photos);
				auctionFolders.add(auctionFolder);
			}
		}
		return auctionFolders;
	}

}
