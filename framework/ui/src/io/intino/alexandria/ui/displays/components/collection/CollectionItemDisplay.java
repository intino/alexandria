package io.intino.alexandria.ui.displays.components.collection;

public interface CollectionItemDisplay<Type> {
	Type item();
	void update(Type item);
}
