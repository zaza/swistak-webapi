package com.swistak.webapi.category;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import com.swistak.webapi.category.Tree.TreeNode;

public class CategoryTreeBuilderTest {
	@Test
	public void build_kategorie_xml() {
		CategoryTreeBuilder builder = new CategoryTreeBuilder(new File("data-tst/kategorie.xml"));
		Tree<Category> tree = builder.build();
		
		assertEquals(6, builder.getExceptions().size());
		assertNotNull(tree);
		assertEquals("2014-05-13 12:31:28", tree.getRoot().getData().getName());
		assertEquals(23751, tree.getSize());
	}
	
	@Test
	public void build_single_node_xml() {
		CategoryTreeBuilder builder = new CategoryTreeBuilder(new File("data-tst/single-node.xml"));
		Tree<Category> tree = builder.build();
		
		assertNotNull(tree);
		assertTrue(builder.getExceptions().isEmpty());
		assertEquals(1, tree.getRoot().getChildren().size());
		assertEquals(1, tree.getSize());
		
		TreeNode<Category> node = tree.getRoot().getChildren().get(0);
		
		assertNode(node, 1, "Node", 1);
	}
	
	@Test
	public void build_three_levels_xml() {
		CategoryTreeBuilder builder = new CategoryTreeBuilder(new File("data-tst/three-levels.xml"));
		Tree<Category> tree = builder.build();
		
		assertNotNull(tree);
		assertTrue(builder.getExceptions().isEmpty());
		assertEquals(1, tree.getRoot().getChildren().size());
		assertEquals(3, tree.getSize());
		
		TreeNode<Category> level1 = tree.getRoot().getChildren().get(0);
		
		assertNode(level1, 1, "Level1", 1);
		TreeNode<Category> level2 = level1.getChildren().get(0);
		assertNode(level2, 2, "Level2", 2);
		TreeNode<Category> level3 = level2.getChildren().get(0);
		assertNode(level3, 3, "Level3", 3);
	}
	
	// TODO: add more tests
	
	private static void assertNode(TreeNode<Category> node, int depth, String name, int id) {
		assertEquals(depth, node.getDepth());
		assertEquals(name, node.getData().name);
		assertEquals(id, node.getData().id);
	}
}
