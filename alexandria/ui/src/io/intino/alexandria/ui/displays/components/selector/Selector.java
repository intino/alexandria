package io.intino.alexandria.ui.displays.components.selector;

import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.events.SelectListener;

import java.util.List;

public interface Selector {
	String selectedOption();
	List<Component> options();
	void add(Component option);
	Selector onSelect(SelectListener selectListener);
}
