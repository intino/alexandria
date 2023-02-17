package io.intino.alexandria.event;

import io.intino.alexandria.event.message.MessageEvent;

import java.util.function.Consumer;

public interface EventHub {
	void sendEvent(String channel, MessageEvent event);

	void attachListener(String channel, Consumer<MessageEvent> onEventReceived);

	void attachListener(String channel, String subscriberId, Consumer<MessageEvent> onEventReceived);

	void detachListeners(String channel);

	void detachListeners(Consumer<MessageEvent> consumer);

	void attachRequestListener(String channel, RequestConsumer onEventReceived);

	void requestResponse(String channel, String event, Consumer<String> onResponse);

	interface RequestConsumer {
		String accept(String request);
	}
}