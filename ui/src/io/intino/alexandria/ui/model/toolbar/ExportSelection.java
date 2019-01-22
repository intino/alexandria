package io.intino.alexandria.ui.model.toolbar;

import io.intino.alexandria.ui.Resource;
import io.intino.alexandria.ui.model.Element;
import io.intino.alexandria.ui.model.Item;
import io.intino.alexandria.ui.services.push.UISession;

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
		alexandriaIcon("archive");
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


	public Resource execute(Element element, Instant from, Instant to, List<Item> selection, String displayId, UISession session) {
		List<Object> selectionObjects = selection.stream().map(Item::object).collect(toList());
		return execution != null ? execution.export(element, from, to, selectionObjects, displayId, session) : null;
	}

	public ExportSelection execute(Execution execution) {
		this.execution = execution;
		return this;
	}

	public interface Execution {
		Resource export(Element element, Instant from, Instant to, List<Object> selection, String displayId, UISession session);
	}
}
