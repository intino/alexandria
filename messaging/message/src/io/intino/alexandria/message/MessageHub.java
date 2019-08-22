package io.intino.alexandria.message;

import java.util.function.Consumer;

public interface MessageHub {
	void sendMessage(String channel, Message message);

	void attachListener(String channel, Consumer<Message> onMessageReceived);

	void attachListener(String channel, String subscriberId, Consumer<Message> onMessageReceived);

	void detachListeners(String channel);

	void attachRequestListener(String channel, RequestConsumer onMessageReceived);

	void requestResponse(String channel, String message, Consumer<String> onResponse);

	interface RequestConsumer {
		String accept(String request);
	}
}