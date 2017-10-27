package io.intino.konos.server.activity.displays.molds.model.stamps.operations;

import io.intino.konos.server.activity.displays.elements.model.Item;
import io.intino.konos.server.activity.displays.molds.model.stamps.Operation;

public class TaskOperation extends Operation<String> {
	private Execution execution;

	public void execute(Item item, String username) {
		if (item == null || execution == null) return;
		execution.task(item.object(), username);
	}

	public TaskOperation execution(Execution execution) {
		this.execution = execution;
		return this;
	}

	public interface Execution {
		void task(Object object, String username);
	}
}
