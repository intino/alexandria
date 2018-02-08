package io.intino.konos.alexandria.activity.model.mold.stamps.operations;

import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.mold.stamps.Operation;
import io.intino.konos.alexandria.activity.services.push.User;

public class TaskOperation extends Operation<String> {
	private Execution execution;

	public void execute(Item item, User user) {
		execution.task(item != null ? item.object() : null, user);
	}

	public TaskOperation execution(Execution execution) {
		this.execution = execution;
		return this;
	}

	public interface Execution {
		void task(Object object, User user);
	}
}
