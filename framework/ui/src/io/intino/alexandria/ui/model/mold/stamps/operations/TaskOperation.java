package io.intino.alexandria.ui.model.mold.stamps.operations;

import io.intino.alexandria.ui.displays.AlexandriaDisplay;
import io.intino.alexandria.ui.model.mold.StampResult;
import io.intino.alexandria.ui.model.mold.stamps.Operation;
import io.intino.alexandria.ui.model.Item;
import io.intino.alexandria.ui.services.push.UISession;

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

	public StampResult execute(Item item, AlexandriaDisplay self, UISession session) {
		return execution.task(item != null ? item.object() : null, self.id(), session);
	}

	public TaskOperation execution(Execution execution) {
		this.execution = execution;
		return this;
	}

	public interface Execution {
		StampResult task(Object object, String selfId, UISession session);
	}

}
