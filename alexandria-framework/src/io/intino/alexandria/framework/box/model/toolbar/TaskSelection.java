package io.intino.alexandria.framework.box.model.toolbar;

import io.intino.alexandria.framework.box.model.Element;
import io.intino.alexandria.framework.box.model.Item;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class TaskSelection<T> extends Operation {
	private Execution launcher;

	public TaskSelection execute(Execution launcher) {
		this.launcher = launcher;
		return this;
	}

	public Refresh execute(Element element, String option, List<Item> selection, String username) {
		if (launcher == null) return Refresh.None;
		return launcher.execute(element, option, selection.stream().map(Item::object).collect(toList()), username);
	}

	public enum Refresh {
		None, Catalog, Selection;
	}

	public interface Execution {
		Refresh execute(Element element, String option, List<Object> selection, String username);
	}

}
