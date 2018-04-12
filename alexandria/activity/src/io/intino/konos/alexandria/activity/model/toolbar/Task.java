package io.intino.konos.alexandria.activity.model.toolbar;

import io.intino.konos.alexandria.activity.model.Element;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

public class Task extends Operation {
	private Execution launcher;

	public Task execute(Execution launcher) {
		this.launcher = launcher;
		return this;
	}

	public ToolbarResult execute(Element element, String displayId, ActivitySession session) {
		if (launcher == null) return ToolbarResult.none();
		return launcher.execute(element, displayId, session);
	}

	public interface Execution {
		ToolbarResult execute(Element element, String displayId, ActivitySession session);
	}
}
