package com.swistak.webapi;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.swistak.webapi.category.Category;
import com.swistak.webapi.category.CategoryIdMatcher;
import com.swistak.webapi.category.Tree;
import com.swistak.webapi.command.SearchAuctionsCommand;

public class CategoryGuesser {

	private Tree<Category> tree;

	private CategoryGuesser(Tree<Category> tree) {
		this.tree = tree;
	}

	public static CategoryGuesser withCategoryTree(Tree<Category> tree) {
		return new CategoryGuesser(tree);
	}
	
	public Category guess(String title) {
		
		String tmpTitle = title;
		int lastSpaceIndex = tmpTitle.lastIndexOf(' ');
		SearchAuctionsCommand search = SearchAuctionsCommand.fraza(tmpTitle);
		search.call();
		
		while (lastSpaceIndex != -1 && search.getTotalAuctions() == 0){
			tmpTitle = tmpTitle.substring(0, lastSpaceIndex);
			lastSpaceIndex = tmpTitle.lastIndexOf(' ');
			search = SearchAuctionsCommand.fraza(tmpTitle);
			search.call();
		}
		
		if (search.getSearchAuctions().isEmpty())
			return Category.UNKNOWN;
		
		Multimap<Long, Search_auction> auctionsByCategory = groupByCategory(search.getSearchAuctions());
		long categoryId = findCategoryWithLargestCollection(auctionsByCategory);
		if (categoryId == 0)
			return Category.UNKNOWN;
		return tree.find(new CategoryIdMatcher(categoryId)).getData();
	}


	private Multimap<Long, Search_auction> groupByCategory(List<Search_auction> auctions) {
		Multimap<Long, Search_auction> result = ArrayListMultimap.create();
		for (Search_auction auction :auctions) {
			result.put(auction.getKat_id(), auction);
		}
		return result;
	}
	
	private long findCategoryWithLargestCollection(Multimap<Long, Search_auction> auctionsByCategory) {
		long result = 0;
		int largest = 0;
		Set<Long> keySet = auctionsByCategory.keySet();
		for (Object key : keySet) {
			Long category = (Long) key;
			Collection<Search_auction> col = auctionsByCategory.get(category);
			if (col.size() > largest) {
				result = category;
				largest = col.size();
			}
		}
		return result;
	}
}
