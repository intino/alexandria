package io.intino.alexandria.ui.displays.events.operation;

import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.events.Event;

import java.time.Instant;

public class ExportEvent extends Event {
	private final Instant from;
	private final Instant to;
	private final String option;

	public ExportEvent(Display sender, Instant from, Instant to, String option) {
		super(sender);
		this.from = from;
		this.to = to;
		this.option = option;
	}

	public Instant from() { return from; }

	public Instant to() { return to; }

	public String option() { return option; }
}
