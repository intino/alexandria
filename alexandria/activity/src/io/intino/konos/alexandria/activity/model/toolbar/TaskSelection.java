package io.intino.konos.alexandria.activity.model.toolbar;

import io.intino.konos.alexandria.activity.model.Element;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class TaskSelection extends MessageOperation {
	private Execution launcher;

	public TaskSelection execute(Execution launcher) {
		this.launcher = launcher;
		return this;
	}

	public Refresh execute(Element element, String option, List<Item> selection, ActivitySession session) {
		if (launcher == null) return Refresh.None;
		return launcher.execute(element, option, selection.stream().map(Item::object).collect(toList()), session);
	}

	public enum Refresh {
		None, Catalog, Selection;
	}

	public interface Execution {
		Refresh execute(Element element, String option, List<Object> selection, ActivitySession session);
	}

}
