package io.intino.alexandria.ui.displays.events;

import io.intino.alexandria.Resource;
import io.intino.alexandria.ui.displays.Display;

import java.util.List;

public class UploadEvent extends Event {
	private final List<Resource> values;
	private boolean cancel = false;

	public UploadEvent(Display sender, java.util.List<Resource> values) {
		super(sender);
		this.values = values;
	}

	public List<Resource> values() {
		return values;
	}

}
