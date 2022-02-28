package io.intino.alexandria.sumus;

import io.intino.alexandria.sumus.helpers.Finder;

import java.util.List;

public interface Dimension {
	String name();
	Attribute.Type type();
	boolean hasNA();
	default int levels() { return 1; }
	default List<Slice> slices(int level) { return slices(); }
	List<Slice> slices();
	default Slice slice(String name) { return new Finder<>(slices()).find(name); }
	default boolean isOrdinal() { return type().isOrdinal(); }

}
