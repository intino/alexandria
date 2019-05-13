package io.intino.alexandria.ui.displays.components.selector;

import io.intino.alexandria.ui.displays.Component;

public interface Selection {
	void add(Component container);
	void bindTo(Selector selector, String option);
}
