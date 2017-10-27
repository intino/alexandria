package io.intino.konos.server.activity.displays.molds.model.stamps.operations;

import io.intino.konos.server.activity.Resource;
import io.intino.konos.server.activity.displays.elements.model.Item;
import io.intino.konos.server.activity.displays.molds.model.stamps.Operation;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ExportOperation extends Operation<String> {
	private String title;
	private List<String> options = new java.util.ArrayList<>();
	private Instant from = Instant.now(Clock.systemUTC());
	private Instant to = Instant.now(Clock.systemUTC()).plus(1, ChronoUnit.DAYS);;
	private Execution execution;

	public String title() {
		return this.title;
	}

	public ExportOperation title(String title) {
		this.title = title;
		return this;
	}

	public List<String> options() {
		return this.options;
	}

	public ExportOperation options(List<String> options) {
		this.options = options;
		return this;
	}

	public Instant from() {
		return this.from;
	}

	public Instant to() {
		return this.to;
	}

	public Resource execute(Item item, Instant from, Instant to, String option, String username) {
		return item != null && execution != null ? execution.export(item.object(), from, to, option, username) : null;
	}

	public ExportOperation execution(Execution execution) {
		this.execution = execution;
		return this;
	}

	public interface Execution {
		Resource export(Object object, Instant from, Instant to, String option, String username);
	}
}
