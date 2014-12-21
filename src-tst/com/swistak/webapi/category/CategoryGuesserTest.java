package com.swistak.webapi.category;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.swistak.webapi.AbstractSwistakTest;
import com.swistak.webapi.My_auction;
import com.swistak.webapi.Search_auction;
import com.swistak.webapi.category.Category;
import com.swistak.webapi.category.CategoryGuesser;
import com.swistak.webapi.category.CategoryIdMatcher;
import com.swistak.webapi.category.CategoryTreeBuilder;
import com.swistak.webapi.category.Tree;
import com.swistak.webapi.category.Tree.TreeNode;
import com.swistak.webapi.command.GetMyAuctionsCommand;
import com.swistak.webapi.command.SearchAuctionsCommand;

public class CategoryGuesserTest extends AbstractSwistakTest {

	private static Tree<Category> tree;

	@Test
	public void random_title() {
		assertTrue(CategoryGuesser.withCategoryTree(tree).guess("qwertyuiop").isUnknown());
	}

	@Test
	public void guess_category_for_auctions_by_login() {
		SearchAuctionsCommand search = SearchAuctionsCommand.fraza("").login(getLogin());
		List<Search_auction> auctions = search.call();

		for (Search_auction my_auction : auctions) {
			Category category = CategoryGuesser.withCategoryTree(tree)
					.guess(my_auction.getTitle());
			assertEquals(my_auction.getKat_id(), category.getId());
		}
	}

	@Test
	public void guess_category_for_my_auctions() {
		GetMyAuctionsCommand myAuctions = new GetMyAuctionsCommand(getHash()).limit(100);
		List<My_auction> auctions = myAuctions.call();

		for (My_auction auction : auctions) {
			int catSwistakId = auction.getCategory_id().intValue();
			String catSwistakPath = getCategoryFullPath(catSwistakId);
			Category catGuess = CategoryGuesser.withCategoryTree(tree)
					.guess(auction.getTitle());
			if (!catGuess.isUnknown()) {
				String catGuessPath = getCategoryFullPath(catGuess.getId());
				assertEquals(
						format("Expected category for '%s' is '%s', got '%s'",
								auction.getTitle(), catSwistakPath,
								catGuessPath), catSwistakId, catGuess.getId());
			}
		}
	}
	
	@Test
	public void guess_opony() {
		Category category = CategoryGuesser.withCategoryTree(tree)
				.guess("opony letnie");
		String fullPath = getCategoryFullPath(category.getId());
		
		assertTrue(fullPath.startsWith("Motoryzacja : Ogumienie : Opony samochodowe : Letnie :"));
	}

	private String getCategoryFullPath(long id) {
		TreeNode<Category> category = tree.find(new CategoryIdMatcher(id));
		List<TreeNode<Category>> fullPath = tree.getFullPath(category);
		StringBuilder sb = new StringBuilder();
		for (Iterator<TreeNode<Category>> it = fullPath.iterator(); it.hasNext();) {
			sb.append(it.next().getData().getName());
			if (it.hasNext())
				sb.append(" : ");
		}
		return sb.toString();
	}

	@BeforeClass
	public static void buildCategoryTree() {
		CategoryTreeBuilder builder = new CategoryTreeBuilder(new File(
				"data-tst/auctions-root/kategorie.xml"));
		tree = builder.build();
	}
}
