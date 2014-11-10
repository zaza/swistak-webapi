package com.swistak.webapi.category;

public class Category {

	public static final Category UNKNOWN = new Category(-1, "<unknown>");

	int id;

	String name;

	public Category(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}