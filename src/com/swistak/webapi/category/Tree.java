package com.swistak.webapi.category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tree<T> {

	private TreeNode<T> root;

	public Tree(T rootData) {
		root = new TreeNode<T>(rootData);
	}

	public static class TreeNode<T> {
		private T data;
		private TreeNode<T> parent;
		private List<TreeNode<T>> children;

		public TreeNode(T data) {
			this.data = data;
			this.children = new ArrayList<TreeNode<T>>();
		}

		void addChild(TreeNode<T> child) {
			children.add(child);
			child.parent = this;
		}

		int getDepth() {
			int depth = 0;
			TreeNode<T> p = parent;
			while (p != null) {
				depth++;
				p = p.parent;
			}
			return depth;
		}

		public List<TreeNode<T>> getChildren() {
			return children;
		}

		public T getData() {
			return data;
		}
	}

	public TreeNode<T> getRoot() {
		return root;
	}

	public TreeNode<T> find(Matcher<T> matcher) {
		if (matcher.match(root.data))
			return root;
		return find(root.children, matcher);
	}

	private TreeNode<T> find(List<TreeNode<T>> children, Matcher<T> matcher) {
		for (TreeNode<T> child : children) {
			if (matcher.match(child.data))
				return child;
			TreeNode<T> find = find(child.children, matcher);
			if (find != null)
				return find;
		}
		return null;
	}

	int getSize() {
		return getSize(root);
	}

	private int getSize(TreeNode<T> node) {
		int result = node.getChildren().size();
		for (TreeNode<T> child : node.getChildren()) {
			result += getSize(child);
		}
		return result;
	}

	public List<TreeNode<T>> getFullPath(TreeNode<T> node) {
		List<TreeNode<T>> path = new ArrayList<TreeNode<T>>();
		path.add(node);
		TreeNode<T> p = node.parent;
		while (p != null) {
			path.add(p);
			p = p.parent;
		}
		path.remove(path.size() - 1); // remove last i.e. root
		Collections.reverse(path);
		return path;
	}

}
