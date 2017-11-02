package io.intino.alexandria.framework.box.model.toolbar;

import io.intino.alexandria.foundation.activity.Resource;
import io.intino.alexandria.framework.box.model.Element;

import java.time.Instant;

public class Export extends Operation {
	protected Instant from;
	protected Instant to;
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
