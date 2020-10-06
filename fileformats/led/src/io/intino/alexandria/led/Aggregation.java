package io.intino.alexandria.led;

import java.util.function.Predicate;

public interface Aggregation<X extends Aggregation, T extends Schema> {
	String label();
	X add(T item);
	int count();
	Predicate<T> predicate();
}
