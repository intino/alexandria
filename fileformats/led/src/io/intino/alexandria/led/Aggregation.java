package io.intino.alexandria.led;

import java.util.function.Predicate;

public interface Aggregation<SELF extends Aggregation<SELF, T>, T extends Schema> {

	String label();

	SELF add(T schema);

	int count();

	Predicate<T> predicate();
}
