package io.intino.konos.alexandria.activity.model.toolbar;

import io.intino.konos.alexandria.activity.model.Element;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class TaskSelection extends Operation {
	private String confirmText;
	private Execution launcher;

	public String confirmText() {
		return this.confirmText;
	}

	public TaskSelection confirmText(String confirmText) {
		this.confirmText = confirmText;
		return this;
	}

	public TaskSelection execute(Execution launcher) {
		this.launcher = launcher;
		return this;
	}

	public ToolbarSelectionResult execute(Element element, String option, List<Item> selection, ActivitySession session) {
		if (launcher == null) return ToolbarSelectionResult.none();
		return launcher.execute(element, option, selection.stream().map(Item::object).collect(toList()), session);
	}

	public interface Execution {
		ToolbarSelectionResult execute(Element element, String option, List<Object> selection, ActivitySession session);
	}

}
