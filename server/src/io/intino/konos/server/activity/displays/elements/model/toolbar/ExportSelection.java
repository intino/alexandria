package io.intino.konos.server.activity.displays.elements.model.toolbar;

import io.intino.konos.server.activity.Resource;
import io.intino.konos.server.activity.displays.elements.model.Element;
import io.intino.konos.server.activity.displays.elements.model.Item;

import java.time.Instant;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ExportSelection extends Operation {
	private String sumusIcon = "archive";
	protected Instant from;
	protected Instant to;
	private Execution execution;

	public String sumusIcon() {
		return sumusIcon;
	}

	public ExportSelection sumusIcon(String sumusIcon) {
		this.sumusIcon = sumusIcon;
		return this;
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
