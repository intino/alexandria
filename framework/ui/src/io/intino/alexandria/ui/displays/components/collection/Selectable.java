package io.intino.alexandria.ui.displays.components.collection;

import io.intino.alexandria.ui.displays.events.SelectionListener;

public interface Selectable<DN, B> {
	void onSelect(SelectionListener listener);
}
