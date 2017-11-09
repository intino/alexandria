package io.intino.konos.alexandria.activity.model.toolbar;

import io.intino.konos.alexandria.activity.Resource;
import io.intino.konos.alexandria.activity.model.Element;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Export extends Operation {
	private Instant from = Instant.now(Clock.systemUTC());
	private Instant to = Instant.now(Clock.systemUTC()).plus(1, ChronoUnit.DAYS);;
	private Execution execution;

	public Export() {
		sumusIcon("archive");
	}

	public Instant from() {
		return from;
	}

	public Export from(Instant from) {
		this.from = from;
		return this;
	}

	public Instant to() {
		return to;
	}

	public Export to(Instant to) {
		this.to = to;
		return this;
	}

	public Resource execute(Element element, Instant from, Instant to, String username) {
		return execution != null ? execution.export(element, from, to, username) : null;
	}

	public Export execute(Execution execution) {
		this.execution = execution;
		return this;
	}

	public interface Execution {
		Resource export(Element element, Instant from, Instant to, String username);
	}
}
