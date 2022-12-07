package io.intino.alexandria.terminal;

import io.intino.alexandria.event.Event;

import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface Connector {

	String clientId();

	void sendEvent(String path, Event event);

	void sendEvent(String path, Event event, int expirationInSeconds);

	void sendEvents(String path, List<Event> events);

	void sendEvents(String path, List<Event> events, int expirationInSeconds);

	void sendMessage(String path, String message);

	void attachListener(String path, Consumer<Event> onEventReceived);

	void attachListener(String path, String subscriberId, Consumer<Event> onEventReceived);

	void attachListener(String path, String subscriberId, Consumer<Event> onEventReceived, Predicate<Instant> filter);

	void attachListener(String path, MessageConsumer consumer);

	void attachListener(String path, String subscriberId, MessageConsumer consumer);

	void detachListeners(Consumer<Event> consumer);

	void detachListeners(MessageConsumer consumer);

	void detachListeners(String path);

	void createSubscription(String path, String subscriberId);

	void destroySubscription(String subscriberId);

	void requestResponse(String path, String message, Consumer<String> onResponse);

	void requestResponse(String path, String message, String responsePath);

	interface MessageConsumer {
		void accept(String message, String callback);
	}
}