package io.intino.konos.alexandria.activity.model.toolbar;

import io.intino.konos.alexandria.activity.model.Element;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

public class MessageOperation extends Operation {
	private MessageLoader messageLoader;

	public String message(Element element, String displayId, ActivitySession session) {
		return messageLoader != null ? messageLoader.load(element, displayId, session) : null;
	}

	public Operation messageLoader(MessageLoader loader) {
		this.messageLoader = loader;
		return this;
	}

	public interface MessageLoader {
		String load(Element element, String displayId, ActivitySession session);
	}
}
