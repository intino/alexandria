package io.intino.konos.alexandria.activity.model.mold.stamps.operations;

import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.mold.stamps.Operation;

public class TaskOperation extends Operation<String> {
	private Execution execution;

	public void execute(Item item, String username) {
		execution.task(item != null ? item.object() : null, username);
	}

	public TaskOperation execution(Execution execution) {
		this.execution = execution;
		return this;
	}

	public interface Execution {
		void task(Object object, String username);
	}
}
