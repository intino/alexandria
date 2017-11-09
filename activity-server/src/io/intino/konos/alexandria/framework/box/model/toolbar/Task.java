package io.intino.konos.alexandria.framework.box.model.toolbar;

import io.intino.konos.alexandria.framework.box.model.Element;

public class Task extends Operation {
	private Execution launcher;

	public Task execute(Execution launcher) {
		this.launcher = launcher;
		return this;
	}

	public Refresh execute(Element element, String option, String username) {
		if (launcher == null) return Refresh.None;
		return launcher.execute(element, option, username);
	}

	public enum Refresh {
		None, Catalog;
	}

	public interface Execution {
		Refresh execute(Element element, String option, String username);
	}
}
