package io.intino.konos.alexandria.activity.model.mold.stamps.operations;

import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.mold.stamps.Operation;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

public class TaskOperation extends Operation<String> {
	private String confirmText;
	private Execution execution;

	public TaskOperation() {
		alexandriaIcon("hardware:developer-board");
	}

	public String confirmText() {
		return this.confirmText;
	}

	public TaskOperation confirmText(String confirmText) {
		this.confirmText = confirmText;
		return this;
	}

	public Refresh execute(Item item, ActivitySession session) {
		return execution.task(item != null ? item.object() : null, session);
	}

	public TaskOperation execution(Execution execution) {
		this.execution = execution;
		return this;
	}

	public interface Execution {
		Refresh task(Object object, ActivitySession session);
	}

	public enum Refresh {
		None, Item
	}
}
