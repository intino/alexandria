package io.intino.alexandria.ui.displays.components.selector;

import io.intino.alexandria.ui.displays.events.SelectionListener;

import java.util.List;

public interface Selector {
	<T> List<T> selection();
	List<SelectorOption> options();
	void reset();
	void add(SelectorOption option);
	void addDivider();
	Selector onSelect(SelectionListener selectionListener);
}
