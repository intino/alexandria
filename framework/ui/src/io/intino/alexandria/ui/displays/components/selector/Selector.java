package io.intino.alexandria.ui.displays.components.selector;

import io.intino.alexandria.ui.displays.events.SelectListener;

import java.util.List;

public interface Selector {
	String selectedOption();
	List<SelectorOption> options();
	void add(SelectorOption option);
	Selector onSelect(SelectListener selectListener);
}
