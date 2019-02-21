package io.intino.alexandria.ui.displays.components.selector;

import io.intino.alexandria.ui.displays.Component;

public interface SelectorContainer {
	void select(String option);
	void add(Component container);
	void bindTo(Selector selector);
}
