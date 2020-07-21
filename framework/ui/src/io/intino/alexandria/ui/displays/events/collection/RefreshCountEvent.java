package io.intino.alexandria.ui.displays.events.collection;

import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.events.Event;

import java.util.List;

public class RefreshCountEvent extends Event {
	private final long count;

	public RefreshCountEvent(Display sender, long count) {
		super(sender);
		this.count = count;
	}

	public long count() {
		return count;
	}

}
