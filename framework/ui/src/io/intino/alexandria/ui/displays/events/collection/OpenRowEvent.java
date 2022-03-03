package io.intino.alexandria.ui.displays.events.collection;

import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.model.dynamictable.Section;

public class OpenRowEvent extends Event {
	private final Section section;
	private final String row;

	public OpenRowEvent(Display sender, Section section, String row) {
		super(sender);
		this.section = section;
		this.row = row;
	}

	public Section section() {
		return section;
	}

	public String row() {
		return row;
	}

}
