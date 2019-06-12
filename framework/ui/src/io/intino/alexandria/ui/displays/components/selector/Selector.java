package io.intino.alexandria.ui.displays.components.selector;

import io.intino.alexandria.ui.displays.events.SelectionListener;

import java.util.List;

public interface Selector {
	List<String> selection();
	List<SelectorOption> options();
	void reset();
	void add(SelectorOption option);
	Selector onSelect(SelectionListener selectionListener);
}
