package io.intino.alexandria.ui.model.chat;

import io.intino.alexandria.Resource;

import java.net.URL;
import java.util.Collections;
import java.util.List;

public interface ChatDatasource {
	MessageReader messages();

	default void send(String message, ResponseReceiver receiver) {
		send(message, Collections.emptyList(), receiver);
	}

	void send(String message, List<Resource> attachments, ResponseReceiver receiver);

	URL attachmentUrl(Message message, String name);

	interface ResponseReceiver {
		ResponseReceiver create(String content);
		ResponseReceiver add(String content);
		ResponseReceiver end();
	}
}
