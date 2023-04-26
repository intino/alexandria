package io.intino.alexandria.terminal;

import io.intino.alexandria.event.Event;
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class StubConnector implements Connector {
	private final Map<String, List<Consumer<Event>>> eventConsumers;
	private final Map<String, List<MessageConsumer>> messageConsumers;
	private EventOutBox eventOutBox;
	private MessageOutBox messageOutBox;

	public StubConnector(File outBoxDirectory) {
		eventConsumers = new HashMap<>();
		messageConsumers = new HashMap<>();
		if (outBoxDirectory != null) {
			this.eventOutBox = new EventOutBox(new File(outBoxDirectory, "events"));
			this.messageOutBox = new MessageOutBox(new File(outBoxDirectory, "requests"));
		}
	}

	@Override
	public String clientId() {
		return "mock-client";
	}

	public void start() {
	}

	@Override
	public synchronized void sendEvent(String path, Event event) {
		ArrayList<Consumer<Event>> consumers = new ArrayList<>(eventConsumers.getOrDefault(path, Collections.emptyList()));
		for (Consumer<Event> c : consumers) c.accept(event);
		if (eventOutBox != null) eventOutBox.push(path, event);
	}

	public synchronized void sendEvents(String path, List<Event> events) {
		ArrayList<Consumer<Event>> consumers = new ArrayList<>(eventConsumers.getOrDefault(path, Collections.emptyList()));
		consumers.forEach(events::forEach);
		if (eventOutBox != null) events.forEach(e -> eventOutBox.push(path, e));
	}

	public synchronized void sendEvents(String path, List<Event> events, int expirationInSeconds) {
		sendEvents(path, events);
	}

	@Override
	public synchronized void sendEvent(String path, Event event, int expirationInSeconds) {
		sendEvent(path, event);
	}

	@Override
	public void attachListener(String path, Consumer<Event> onEventReceived) {
		registerEventConsumer(path, onEventReceived);
	}

	@Override
	public void sendQueueMessage(String path, String message) {
		if (messageOutBox != null) messageOutBox.push(path, message);
	}

	@Override
	public void sendTopicMessage(String path, String message) {
		if (messageOutBox != null) messageOutBox.push(path, message);
	}

	@Override
	public void attachListener(String path, String subscriberId, Consumer<Event> onEventReceived) {
		registerEventConsumer(path, onEventReceived);
	}

	@Override
	public void attachListener(String path, String subscriberId, Consumer<Event> onEventReceived, Predicate<Instant> filter) {
		registerEventConsumer(path, onEventReceived);
	}

	@Override
	public void attachListener(String path, MessageConsumer onMessageReceived) {
		registerMessageConsumer(path, onMessageReceived);

	}

	@Override
	public void attachListener(String path, String subscriberId, MessageConsumer onMessageReceived) {
		registerMessageConsumer(path, onMessageReceived);
	}


	@Override
	public void detachListeners(Consumer<Event> consumer) {
		eventConsumers.values().forEach(list -> list.remove(consumer));
	}

	@Override
	public void detachListeners(MessageConsumer consumer) {
		messageConsumers.values().forEach(list -> list.remove(consumer));
	}

	@Override
	public void createSubscription(String path, String subscriberId) {
	}

	public void destroySubscription(String subscriberId) {
	}

	@Override
	public void detachListeners(String path) {
		this.eventConsumers.get(path).clear();
		this.messageConsumers.get(path).clear();
	}

	@Override
	public void requestResponse(String path, javax.jms.Message message, Consumer<javax.jms.Message> onResponse) {

	}

	@Override
	public javax.jms.Message requestResponse(String path, javax.jms.Message message) {
		return null;
	}

	@Override
	public javax.jms.Message requestResponse(String path, javax.jms.Message message, long timeout, TimeUnit timeUnit) {
		try {
			Thread.sleep(TimeUnit.MILLISECONDS.convert(timeout,timeUnit));
		} catch (InterruptedException e) {
			Logger.error(e);
		}
		return null;
	}

	@Override
	public void requestResponse(String path, javax.jms.Message message, String responsePath) {
	}

	@Override
	public long defaultTimeoutAmount() {
		return 0;
	}

	@Override
	public TimeUnit defaultTimeoutUnit() {
		return TimeUnit.SECONDS;
	}

	public void stop() {
	}

	private void registerEventConsumer(String path, Consumer<Event> onEventReceived) {
		this.eventConsumers.putIfAbsent(path, new CopyOnWriteArrayList<>());
		this.eventConsumers.get(path).add(onEventReceived);
	}

	private void registerMessageConsumer(String path, MessageConsumer onMessageReceived) {
		this.messageConsumers.putIfAbsent(path, new CopyOnWriteArrayList<>());
		this.messageConsumers.get(path).add(onMessageReceived);
	}
}