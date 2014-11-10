package com.swistak.webapi;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import com.swistak.webapi.category.Category;
import com.swistak.webapi.category.CategoryIdMatcher;
import com.swistak.webapi.category.CategoryTreeBuilder;
import com.swistak.webapi.category.Tree;
import com.swistak.webapi.command.GetMyAuctionsCommand;
import com.swistak.webapi.command.SearchCommand;

public class CategoryGuesserTest extends AbstractSwistakTest {

	private Tree<Category> tree;

	@Test
	public void random_title() {
		assertEquals(Category.UNKNOWN,
				CategoryGuesser.withCategoryTree(getTree()).guess("qwertyuiop"));
	}

	@Test
	public void guess_category_for_auctions_by_login() {
		SearchCommand search = SearchCommand.fraza("").login(getLogin());
		search.run();

		for (Search_auction my_auction : search.search_auctions.value) {
			Category category = CategoryGuesser.withCategoryTree(getTree())
					.guess(my_auction.getTitle());
			assertEquals(my_auction.getKat_id(), category.getId());
		}
	}

	@Test
	public void guess_cateagory_for_my_auctions() {
		GetMyAuctionsCommand myAuctions = new GetMyAuctionsCommand(getHash());
		myAuctions.run();
		My_auction[] auctions = myAuctions.my_auctions.value;

		// TODO: founds first 25
		System.out.println(format("Found %d", auctions.length));
		for (My_auction my_auction : auctions) {
			int catSwistakId = my_auction.getCategory_id().intValue();
			String catSwistakName = getCategoryName(catSwistakId);
			Category catGuess = CategoryGuesser.withCategoryTree(getTree())
					.guess(my_auction.getTitle());
			// TODO: replace with assert
			System.out.println(format("%s: %d (%s) ?=? %d (%s)",
					my_auction.getTitle(), catSwistakId, catSwistakName,
					catGuess.getId(), catGuess.getName()));
		}
	}

	private String getCategoryName(long id) {
		return getTree().find(new CategoryIdMatcher(id)).getData().getName();
	}

	private Tree<Category> getTree() {
		if (tree == null) {
			CategoryTreeBuilder builder = new CategoryTreeBuilder(new File(
					"data-tst/kategorie.xml"));
			tree = builder.build();
		}
		return tree;
	}
}
