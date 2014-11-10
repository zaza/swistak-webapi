package com.swistak.webapi.category;

import static java.lang.String.format;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.swistak.webapi.category.Tree.TreeNode;

public class CategoryTreeBuilder {

	private List<Exception> exceptions = new ArrayList<Exception>();

	private File file;

	public CategoryTreeBuilder(File file) {
		this.file = file;
	}

	public Tree<Category> build() {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("kategoria");

			Tree<Category> tree = new Tree<Category>(new Category(0,
					"2014-05-13 12:31:28"));

			traverse(nList, 0, tree);

			return tree;
		} catch (Exception e) {
			exceptions.add(e);
		}
		return null;
	}

	private void traverse(NodeList nodes, int level, Tree<Category> tree) {
		boolean found = false;
		for (int temp = 0; temp < nodes.getLength(); temp++) {
			Node node = nodes.item(temp);

			if (node.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) node;
				int poziom = Integer.parseInt(eElement
						.getElementsByTagName("poziom").item(0)
						.getTextContent());
				if (poziom > level) {
					found = true;
					continue;
				}
				if (poziom < level)
					continue;

				int parentId = Integer.parseInt(eElement
						.getElementsByTagName("parent").item(0)
						.getTextContent());
				TreeNode<Category> parent = tree.find(new CategoryIdMatcher(
						parentId));

				int id = Integer.parseInt(eElement.getElementsByTagName("id")
						.item(0).getTextContent());
				String name = eElement.getElementsByTagName("name").item(0)
						.getTextContent();
				TreeNode<Category> childTreeNode = new TreeNode<Category>(
						new Category(id, name));
				if (parent != null) {
					parent.addChild(childTreeNode);
				} else {
					exceptions.add(new IllegalStateException(format(
							"No parent found for %d", id)));
				}
			}
		}
		if (found)
			traverse(nodes, ++level, tree);
	}

	List<Exception> getExceptions() {
		return exceptions;
	}
}
