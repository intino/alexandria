package io.intino.alexandria.sumus.helpers;

import java.util.List;

public class Finder<T> {
	private final List<T> items;

	public Finder(List<T> items) {
		this.items = items;
	}

	public T find(String name) {
		return items.stream()
				.filter(i -> name.equalsIgnoreCase(i.toString()))
				.findFirst()
				.orElse(null);
	}
}
