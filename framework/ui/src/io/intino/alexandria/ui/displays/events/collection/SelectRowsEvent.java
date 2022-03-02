package io.intino.alexandria.ui.displays.events.collection;

import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.model.dynamictable.Row;
import io.intino.alexandria.ui.model.dynamictable.Section;

import java.util.List;
import java.util.Map;

public class SelectRowsEvent extends Event {
	private final Map<String, List<String>> selection;

	public SelectRowsEvent(Display sender, Map<String, List<String>> selection) {
		super(sender);
		this.selection = selection;
	}

	public Map<String, List<String>> selection() {
		return selection;
	}
}
