package com.swistak.webapi.scanner;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.File;
import java.io.FileFilter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;

public class Scanner {

	private File root;

	public Scanner(File root) {
		checkArgument(root.exists(), "%s does not exist.", root);
		checkArgument(root.isDirectory(), "%s is not a directory.", root);
		this.root = root;
	}

	public Set<AuctionFolder> scan() {
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
			AuctionFolder auctionFolder = new AuctionFolder(folder);
			if (auctionFolder.hasDescription() && auctionFolder.hasParameters())
				auctionFolders.add(auctionFolder);
		}
		return auctionFolders;
	}

}
