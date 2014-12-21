package com.swistak.webapi.scanner;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.File;
import java.io.FileFilter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;
import com.swistak.webapi.CategoryGuesser;
import com.swistak.webapi.category.Category;
import com.swistak.webapi.category.CategoryTreeBuilder;
import com.swistak.webapi.category.Tree;

public class Scanner {

	private File root;

	public Scanner(File root) {
		checkArgument(root.exists(), "%s does not exist.", root);
		checkArgument(root.isDirectory(), "%s is not a directory.", root);
		checkArgument(new File(root, "kategorie.xml").exists(), "kategorie.xml file does not exist.");
		this.root = root;
	}

	public Set<AuctionFolder> scan() {
		CategoryTreeBuilder builder = new CategoryTreeBuilder(new File(root, "kategorie.xml"));
		Tree<Category> tree = builder.build();
		File[] folders = root.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File file) {
				return file.isDirectory();
			}
		});
		if (folders.length == 0)
			return Collections.emptySet();

		HashSet<AuctionFolder> auctionFolders = Sets.newHashSetWithExpectedSize(folders.length);
		for (File folder : folders) {
			AuctionFolder auctionFolder = new AuctionFolder(folder, tree);
			if (auctionFolder.hasDescription() && auctionFolder.hasParameters()) {
				if (!auctionFolder.hasCategory()) {
					Category guess = CategoryGuesser.withCategoryTree(tree).guess(auctionFolder.getTitle());
					auctionFolder.setCategory(guess.getId());
				}
				auctionFolders.add(auctionFolder);
			}
		}
		return auctionFolders;
	}

}
