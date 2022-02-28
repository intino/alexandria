package io.intino.alexandria.sumus;

import java.util.List;
import java.util.function.Predicate;

public interface Classifier {
	List<String> categories();
	Predicate<Object> predicateOf(String category);
}
