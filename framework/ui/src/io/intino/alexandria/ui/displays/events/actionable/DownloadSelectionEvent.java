package io.intino.alexandria.ui.displays.events.actionable;

import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.events.SelectionEvent;

import java.util.List;

public class DownloadSelectionEvent extends SelectionEvent {
	private final String option;

	public DownloadSelectionEvent(Display sender, List<String> items, String option) {
		super(sender, items);
		this.option = option;
	}

	public String option() { return option; }
}
