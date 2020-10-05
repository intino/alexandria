package io.intino.alexandria;

import java.util.function.Predicate;

public interface Aggregation<X extends Aggregation, T extends Item> {
	String label();
	X add(T item);
	int count();
	Predicate<T> predicate();
}
