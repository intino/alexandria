package io.intino.alexandria.terminal;

import io.intino.alexandria.event.Event;

import java.util.function.Consumer;

public interface Connector {
	void sendEvent(String path, Event event);

	void sendMessage(String path, String message);

	void attachListener(String path, Consumer<Event> onEventReceived);

	void attachListener(String path, String subscriberId, Consumer<Event> onEventReceived);

	void attachListener(String path, MessageConsumer consumer);

	void detachListeners(Consumer<Event> consumer);

	void detachListeners(MessageConsumer consumer);

	void detachListeners(String path);

	void requestResponse(String path, String message, Consumer<String> onResponse);

	void requestResponse(String path, String message, String responsePath);

	interface MessageConsumer {
		void accept(String message, String callback);
	}
}