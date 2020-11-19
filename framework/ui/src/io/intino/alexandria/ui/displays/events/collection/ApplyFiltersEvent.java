package io.intino.alexandria.ui.displays.events.collection;

import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.events.Event;

import java.util.List;
import java.util.Map;

public class ApplyFiltersEvent extends Event {
	private final Map<String, List<String>> filters;

	public ApplyFiltersEvent(Display sender, Map<String, List<String>> filters) {
		super(sender);
		this.filters = filters;
	}

	public Map<String, List<String>> filters() {
		return filters;
	}

}
