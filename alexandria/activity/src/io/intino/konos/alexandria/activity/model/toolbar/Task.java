package io.intino.konos.alexandria.activity.model.toolbar;

import io.intino.konos.alexandria.activity.model.Element;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

public class Task extends MessageOperation {
	private Execution launcher;

	public Task execute(Execution launcher) {
		this.launcher = launcher;
		return this;
	}

	public Refresh execute(Element element, String displayId, ActivitySession session) {
		if (launcher == null) return Refresh.None;
		return launcher.execute(element, displayId, session);
	}

	public enum Refresh {
		None, Catalog
	}

	public interface Execution {
		Refresh execute(Element element, String displayId, ActivitySession session);
	}
}
