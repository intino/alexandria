package io.intino.alexandria.sumus;

import java.util.List;

public interface Fact {
	int id();
	List<Attribute> attributes();
	Object value(String attribute);
	default Object value(Attribute attribute) { return value(attribute.name); }
}
