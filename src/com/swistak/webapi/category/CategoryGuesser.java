package com.swistak.webapi.category;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.swistak.webapi.Search_auction;
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
		Collection<Search_auction> auctions = searchNewAndUsed(tmpTitle);
		
		while (lastSpaceIndex != -1 && auctions.isEmpty()){
			tmpTitle = tmpTitle.substring(0, lastSpaceIndex);
			lastSpaceIndex = tmpTitle.lastIndexOf(' ');
			auctions = searchNewAndUsed(tmpTitle);
		}
		
		if (auctions.isEmpty())
			return Category.UNKNOWN;
		
		Multimap<Long, Search_auction> auctionsByCategory = groupByCategory(auctions);
		long categoryId = findCategoryWithLargestCollection(auctionsByCategory);
		if (categoryId == 0)
			return Category.UNKNOWN;
		return tree.find(new CategoryIdMatcher(categoryId)).getData();
	}

	private Collection<Search_auction> searchNewAndUsed(String title) {
		// TODO: move to the search command
		Collection<Search_auction> result = new ArrayList<Search_auction>();
		SearchAuctionsCommand search = SearchAuctionsCommand.fraza(title).nowy();
		search.call();
		result.addAll(search.getSearchAuctions());
		search = SearchAuctionsCommand.fraza(title).uzywany();
		search.call();
		result.addAll(search.getSearchAuctions());		
		return result;
	}

	private Multimap<Long, Search_auction> groupByCategory(Collection<Search_auction> auctions) {
		Multimap<Long, Search_auction> result = ArrayListMultimap.create();
		for (Search_auction auction : auctions) {
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
