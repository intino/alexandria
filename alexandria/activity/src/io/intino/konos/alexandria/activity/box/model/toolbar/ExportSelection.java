package io.intino.konos.alexandria.activity.box.model.toolbar;

import io.intino.konos.alexandria.activity.box.Resource;
import io.intino.konos.alexandria.activity.box.model.Element;
import io.intino.konos.alexandria.activity.box.model.Item;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ExportSelection extends Operation {
	private Instant from = Instant.now(Clock.systemUTC());
	private Instant to = Instant.now(Clock.systemUTC()).plus(1, ChronoUnit.DAYS);;
	private Execution execution;

	public ExportSelection() {
		sumusIcon("archive");
	}

	public Instant from() {
		return from;
	}

	public ExportSelection from(Instant from) {
		this.from = from;
		return this;
	}

	public Instant to() {
		return to;
	}

	public ExportSelection to(Instant to) {
		this.to = to;
		return this;
	}


	public Resource execute(Element element, Instant from, Instant to, List<Item> selection, String username) {
		List<Object> selectionObjects = selection.stream().map(Item::object).collect(toList());
		return execution != null ? execution.export(element, from, to, selectionObjects, username) : null;
	}

	public ExportSelection execute(Execution execution) {
		this.execution = execution;
		return this;
	}

	public interface Execution {
		Resource export(Element element, Instant from, Instant to, List<Object> selection, String username);
	}
}
