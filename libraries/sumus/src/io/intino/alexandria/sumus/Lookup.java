package io.intino.alexandria.sumus;

import java.util.List;
import java.util.function.Predicate;

public interface Lookup {
	String name();
	Attribute.Type type();
	boolean hasNA();
	List<Object> uniques();
	Object min();
	Object max();
	Index index(Predicate<Object> predicate);
}
