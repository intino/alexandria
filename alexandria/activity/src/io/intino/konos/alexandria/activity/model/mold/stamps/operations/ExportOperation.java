package io.intino.konos.alexandria.activity.model.mold.stamps.operations;

import io.intino.konos.alexandria.activity.Resource;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.mold.stamps.Operation;
import io.intino.konos.alexandria.activity.services.push.User;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ExportOperation extends Operation<String> {
	private List<String> options = new java.util.ArrayList<>();
	private Instant from = Instant.now(Clock.systemUTC());
	private Instant to = Instant.now(Clock.systemUTC()).plus(1, ChronoUnit.DAYS);
	private Execution execution;

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

	public ExportOperation from(Instant from) {
		this.from = from;
		return this;
	}

	public Instant to() {
		return this.to;
	}

	public ExportOperation to(Instant to) {
		this.to = to;
		return this;
	}

	public Resource execute(Item item, Instant from, Instant to, String option, User user) {
		return item != null && execution != null ? execution.export(item.object(), from, to, option, user) : null;
	}

	public ExportOperation execution(Execution execution) {
		this.execution = execution;
		return this;
	}

	public interface Execution {
		Resource export(Object object, Instant from, Instant to, String option, User user);
	}
}
