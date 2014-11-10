package com.swistak.webapi;

import java.util.Collection;
import java.util.Set;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;

import com.swistak.webapi.Search_auction;
import com.swistak.webapi.category.Category;
import com.swistak.webapi.category.CategoryIdMatcher;
import com.swistak.webapi.category.Tree;
import com.swistak.webapi.command.SearchCommand;

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
		SearchCommand search;
		int lastSpaceIndex = tmpTitle.lastIndexOf(' ');
		search = SearchCommand.fraza(tmpTitle);
		search.run();
		
		while (lastSpaceIndex != -1 && search.total_found.value == null){
			tmpTitle = tmpTitle.substring(0, lastSpaceIndex);
			lastSpaceIndex = tmpTitle.lastIndexOf(' ');
			search = SearchCommand.fraza(tmpTitle);
			search.run();
		}
		
		if (search.search_auctions.value == null)
			return Category.UNKNOWN;
		
		MultiMap auctionsByCategory = groupByCategory(search.search_auctions.value);
		long categoryId = findCategoryWithLargestCollection(auctionsByCategory);
		if (categoryId == 0)
			return Category.UNKNOWN;
		return tree.find(new CategoryIdMatcher(categoryId)).getData();
	}


	private MultiMap groupByCategory(Search_auction[] auctions) {
		MultiMap result = new MultiValueMap();
		for (Search_auction auction :auctions) {
			result.put(auction.getKat_id(), auction);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private long findCategoryWithLargestCollection(MultiMap auctionsByCategory) {
		long result = 0;
		int largest = 0;
		Set<Long> keySet = auctionsByCategory.keySet();
		for (Object key : keySet) {
			Long category = (Long) key;
			Collection<Search_auction> col = (Collection<Search_auction>) auctionsByCategory.get(key);
			if (col.size() > largest) {
				result = category;
				largest = col.size();
			}
		}
		return result;
	}
}
