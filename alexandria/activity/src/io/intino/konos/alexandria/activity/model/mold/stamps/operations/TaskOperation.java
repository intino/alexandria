package io.intino.konos.alexandria.activity.model.mold.stamps.operations;

import io.intino.konos.alexandria.activity.displays.AlexandriaDisplay;
import io.intino.konos.alexandria.activity.model.Element;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.mold.stamps.Operation;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

public class TaskOperation extends Operation<String> {
	private String confirmText;
	private Execution execution;
	private MessageLoader messageLoader;

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

	public Refresh execute(Item item, AlexandriaDisplay self, ActivitySession session) {
		return execution.task(item != null ? item.object() : null, self.id(), session);
	}

	public TaskOperation execution(Execution execution) {
		this.execution = execution;
		return this;
	}

	public interface Execution {
		Refresh task(Object object, String selfId, ActivitySession session);
	}

	public String message(Item item, AlexandriaDisplay self, ActivitySession session) {
		return messageLoader != null ? messageLoader.load(item != null ? item.object() : null, self.id(), session) : null;
	}

	public enum Refresh {
		None, Item, Element
	}

	public interface MessageLoader {
		String load(Element element, String displayId, ActivitySession session);
	}

	public TaskOperation messageLoader(MessageLoader loader) {
		this.messageLoader = loader;
		return this;
	}

}
