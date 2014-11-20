package com.swistak.webapi;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import com.swistak.webapi.category.Category;
import com.swistak.webapi.category.CategoryIdMatcher;
import com.swistak.webapi.category.CategoryTreeBuilder;
import com.swistak.webapi.category.Tree;
import com.swistak.webapi.category.Tree.TreeNode;
import com.swistak.webapi.command.GetMyAuctionsCommand;
import com.swistak.webapi.command.SearchAuctionsCommand;

public class CategoryGuesserTest extends AbstractSwistakTest {

	private static final Logger LOG = Logger.getLogger(CategoryGuesserTest.class);

	private Tree<Category> tree;

	@Test
	public void random_title() {
		assertEquals(Category.UNKNOWN,
				CategoryGuesser.withCategoryTree(getTree()).guess("qwertyuiop"));
	}

	@Test
	public void guess_category_for_auctions_by_login() {
		SearchAuctionsCommand search = SearchAuctionsCommand.fraza("").login(getLogin());
		search.run();

		for (Search_auction my_auction : search.search_auctions.value) {
			Category category = CategoryGuesser.withCategoryTree(getTree())
					.guess(my_auction.getTitle());
			assertEquals(my_auction.getKat_id(), category.getId());
		}
	}

	@Test
	@Ignore
	public void guess_category_for_my_auctions() {
		GetMyAuctionsCommand myAuctions = new GetMyAuctionsCommand(getHash());
		myAuctions.run();
		List<My_auction> auctions = myAuctions.getMyAuctions();

		// TODO: founds first 25
		LOG.debug(format("Found %d", auctions.size()));
		for (My_auction my_auction : auctions) {
			int catSwistakId = my_auction.getCategory_id().intValue();
			String catSwistakPath = getCategoryFullPath(catSwistakId);
			Category catGuess = CategoryGuesser.withCategoryTree(getTree())
					.guess(my_auction.getTitle());
			String catGuessPath = getCategoryFullPath(catGuess.getId());
			// TODO: replace with assert
			if (catSwistakId != catGuess.getId()) {
				LOG.debug(format("%s: %d (%s) ?=? %d (%s)",
					my_auction.getTitle(), catSwistakId, catSwistakPath,
					catGuess.getId(), catGuessPath));
			}
		}
	}
	
	@Test
	public void guess_opony() {
		Category category = CategoryGuesser.withCategoryTree(getTree())
				.guess("opony letnie");
		String fullPath = getCategoryFullPath(category.getId());
		
		assertTrue(fullPath.startsWith("Motoryzacja > Ogumienie > Opony samochodowe > Letnie >"));
	}

	private String getCategoryFullPath(long id) {
		TreeNode<Category> category = getTree().find(new CategoryIdMatcher(id));
		List<TreeNode<Category>> fullPath = getTree().getFullPath(category);
		StringBuilder sb = new StringBuilder();
		for (Iterator<TreeNode<Category>> it = fullPath.iterator(); it.hasNext();) {
			sb.append(it.next().getData().getName());
			if (it.hasNext())
				sb.append(" > ");
		}
		return sb.toString();
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
