package io.intino.konos.server.activity.displays.elements.model.toolbar;

import io.intino.konos.server.activity.displays.elements.model.Element;
import io.intino.konos.server.activity.Resource;

import java.time.Instant;

public class Export extends Operation {
	private String sumusIcon = "archive";
	protected Instant from;
	protected Instant to;
	private Execution execution;

	public String sumusIcon() {
		return sumusIcon;
	}

	public Export sumusIcon(String sumusIcon) {
		this.sumusIcon = sumusIcon;
		return this;
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
