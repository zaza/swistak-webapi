package com.swistak.webapi.category;

public class CategoryIdMatcher implements Matcher<Category> {

	private long id;

	public CategoryIdMatcher(long id) {
		this.id = id;
	}

	@Override
	public boolean match(Category node) {
		return id == node.id;
	}

}
