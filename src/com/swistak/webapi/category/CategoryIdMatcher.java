package com.swistak.webapi.category;

class CategoryIdMatcher implements Matcher<Category> {

	private long id;

	CategoryIdMatcher(long id) {
		this.id = id;
	}

	@Override
	public boolean match(Category node) {
		return id == node.id;
	}

}
