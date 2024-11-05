package io.intino.alexandria.ui.displays.components.collection;

public interface CollectionItemDisplay<Type> {
	Type item();
	String section();
	void update(Type item);
}
