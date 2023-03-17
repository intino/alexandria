package io.intino.alexandria.terminal;

import io.intino.alexandria.event.Event;
import io.intino.alexandria.event.message.MessageEvent;
import io.intino.alexandria.jms.*;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.message.Message;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.command.ActiveMQDestination;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static io.intino.alexandria.jms.MessageReader.textFrom;
import static java.util.concurrent.TimeUnit.MINUTES;
import static javax.jms.Session.AUTO_ACKNOWLEDGE;
import static javax.jms.Session.SESSION_TRANSACTED;

public class JmsConnector implements Connector {
	private final Map<String, JmsProducer> producers;
	private final Map<String, JmsConsumer> consumers;
	private final Map<String, List<Consumer<Event>>> eventConsumers;
	private final Map<String, List<MessageConsumer>> messageConsumers;
	private final Map<Consumer<Event>, Integer> jmsEventConsumers;
	private final Map<MessageConsumer, Integer> jmsMessageConsumers;
	private final String brokerUrl;
	private final ConnectionConfig config;
	private final boolean transactedSession;
	private final AtomicBoolean connected = new AtomicBoolean(false);
	private final AtomicBoolean started = new AtomicBoolean(false);
	private EventOutBox eventOutBox;
	private MessageOutBox messageOutBox;
	private Connection connection;
	private Session session;
	private ScheduledExecutorService scheduler;
	private final ExecutorService eventDispatcher;

	public JmsConnector(String brokerUrl, ConnectionConfig config, File messageCacheDirectory) {
		this(brokerUrl, config, false, messageCacheDirectory);
	}

	public JmsConnector(String brokerUrl, ConnectionConfig config, boolean transactedSession, File outBoxDirectory) {
		this.brokerUrl = brokerUrl;
		this.config = config;
		this.transactedSession = transactedSession;
		producers = new HashMap<>();
		consumers = new HashMap<>();
		jmsEventConsumers = new HashMap<>();
		jmsMessageConsumers = new HashMap<>();
		eventConsumers = new HashMap<>();
		messageConsumers = new HashMap<>();
		if (outBoxDirectory != null) {
			this.eventOutBox = new EventOutBox(new File(outBoxDirectory, "events"));
			this.messageOutBox = new MessageOutBox(new File(outBoxDirectory, "requests"));
		}
		eventDispatcher = Executors.newSingleThreadExecutor(new NamedThreadFactory("jms-connector"));
	}

	@Override
	public String clientId() {
		return config.clientId();
	}

	public void start() {
		if (brokerUrl == null || brokerUrl.isEmpty()) {
			Logger.warn("Invalid broker URL (" + brokerUrl + "). Connection aborted");
			return;
		}
		try {
			connect();
		} catch (JMSException e) {
			Logger.error(e);
		}
		started.set(true);
		if (scheduler == null) {
			scheduler = Executors.newScheduledThreadPool(1);
			scheduler.scheduleAtFixedRate(this::checkConnection, 15, 10, MINUTES);
		}
	}

	private void connect() throws JMSException {
		if (!Broker.isRunning(brokerUrl)) {
			Logger.warn("Broker (" + brokerUrl + ") unreachable .Connection aborted");
			return;
		}
		initConnection();
		if (connection != null && ((ActiveMQConnection) connection).isStarted()) {
			clearProducers();
			session = createSession(transactedSession);
			if (session != null && ((ActiveMQSession) session).isRunning()) {
				connected.set(true);
				recoverEventsAndMessages();
			}
		}
	}

	@Override
	public synchronized void sendEvent(String path, Event event) {
		ArrayList<Consumer<Event>> consumers = new ArrayList<>(eventConsumers.getOrDefault(path, Collections.emptyList()));
		for (Consumer<Event> c : consumers) c.accept(event);
		eventDispatcher.execute(() -> {
			if (!doSendEvent(path, event) && eventOutBox != null) eventOutBox.push(path, event);
		});
	}


	public synchronized void sendEvents(String path, List<Event> events) {
		ArrayList<Consumer<Event>> consumers = new ArrayList<>(eventConsumers.getOrDefault(path, Collections.emptyList()));
		consumers.forEach(events::forEach);
		eventDispatcher.execute(() -> {
			if (!doSendEvents(path, events) && eventOutBox != null) events.forEach(e -> eventOutBox.push(path, e));
		});
	}

	public synchronized void sendEvents(String path, List<Event> events, int expirationInSeconds) {
		ArrayList<Consumer<Event>> consumers = new ArrayList<>(eventConsumers.getOrDefault(path, Collections.emptyList()));
		consumers.forEach(events::forEach);
		eventDispatcher.execute(() -> {
			if (!doSendEvents(path, events, expirationInSeconds) && eventOutBox != null)
				events.forEach(e -> eventOutBox.push(path, e));
		});
	}

	@Override
	public synchronized void sendEvent(String path, Event event, int expirationInSeconds) {
		ArrayList<Consumer<Event>> consumers = new ArrayList<>(eventConsumers.getOrDefault(path, Collections.emptyList()));
		for (Consumer<Event> eventConsumer : consumers) eventConsumer.accept(event);
		eventDispatcher.execute(() -> {
			if (!doSendEvent(path, event, expirationInSeconds) && eventOutBox != null) eventOutBox.push(path, event);
		});
	}

	@Override
	public void attachListener(String path, Consumer<Event> onEventReceived) {
		registerEventConsumer(path, onEventReceived);
		JmsConsumer consumer = this.consumers.get(path);
		if (consumer == null) return;
		Consumer<javax.jms.Message> eventConsumer = m -> MessageDeserializer.deserialize(m).forEachRemaining(ev -> onEventReceived.accept(newEvent(m, ev)));
		jmsEventConsumers.put(onEventReceived, eventConsumer.hashCode());
		consumer.listen(eventConsumer);
	}

	@Override
	public void sendQueueMessage(String path, String message) {
		recoverEventsAndMessages();
		if (!doSendMessageToQueue(path, message) && messageOutBox != null) messageOutBox.push(path, message);
	}

	@Override
	public void sendTopicMessage(String path, String message) {
		recoverEventsAndMessages();
		if (!doSendMessageToTopic(path, message) && messageOutBox != null) messageOutBox.push(path, message);
	}

	@Override
	public void attachListener(String path, String subscriberId, Consumer<Event> onEventReceived) {
		registerEventConsumer(path, subscriberId, onEventReceived);
		JmsConsumer consumer = this.consumers.get(path);
		if (consumer == null) return;
		Consumer<javax.jms.Message> eventConsumer = m -> MessageDeserializer.deserialize(m).forEachRemaining(ev -> onEventReceived.accept(newEvent(m, ev)));
		jmsEventConsumers.put(onEventReceived, eventConsumer.hashCode());
		consumer.listen(eventConsumer);
	}

	@Override
	public void attachListener(String path, String subscriberId, Consumer<Event> onEventReceived, Predicate<Instant> filter) {
		registerEventConsumer(path, subscriberId, onEventReceived);
		JmsConsumer consumer = this.consumers.get(path);
		if (consumer == null) return;
		Consumer<javax.jms.Message> eventConsumer = m -> MessageDeserializer.deserialize(m).forEachRemaining(ev -> {
			final Instant timestamp = timestamp(m);
			if (timestamp != null && filter.test(timestamp)) onEventReceived.accept(newEvent(m, ev));
		});
		jmsEventConsumers.put(onEventReceived, eventConsumer.hashCode());
		consumer.listen(eventConsumer);
	}

	@Override
	public void attachListener(String path, MessageConsumer onMessageReceived) {
		registerMessageConsumer(path, onMessageReceived);
		JmsConsumer consumer = this.consumers.get(path);
		if (consumer == null) return;
		Consumer<javax.jms.Message> messageConsumer = m -> onMessageReceived.accept(textFrom(m), callback(m));
		jmsMessageConsumers.put(onMessageReceived, messageConsumer.hashCode());
		consumer.listen(messageConsumer);
	}

	@Override
	public void attachListener(String path, String subscriberId, MessageConsumer onMessageReceived) {
		registerMessageConsumer(path, subscriberId, onMessageReceived);
		JmsConsumer consumer = this.consumers.get(path);
		if (consumer == null) return;
		Consumer<javax.jms.Message> messageConsumer = m -> onMessageReceived.accept(textFrom(m), callback(m));
		jmsMessageConsumers.put(onMessageReceived, messageConsumer.hashCode());
		consumer.listen(messageConsumer);
	}

	private Instant timestamp(javax.jms.Message m) {
		try {
			return Instant.ofEpochMilli(m.getJMSTimestamp());
		} catch (JMSException e) {
			Logger.error(e);
			return null;
		}
	}

	@Override
	public void detachListeners(Consumer<Event> consumer) {
		eventConsumers.values().forEach(list -> list.remove(consumer));
		Integer code = jmsEventConsumers.get(consumer);
		if (code == null) return;
		for (JmsConsumer jc : consumers.values()) {
			List<Consumer<javax.jms.Message>> toRemove = jc.listeners().stream().filter(l -> l.hashCode() == code).collect(Collectors.toList());
			toRemove.forEach(jc::removeListener);
		}
	}

	@Override
	public void detachListeners(MessageConsumer consumer) {
		Integer code = jmsMessageConsumers.get(consumer);
		if (code == null) return;
		for (JmsConsumer jc : consumers.values()) {
			List<Consumer<javax.jms.Message>> toRemove = jc.listeners().stream().filter(l -> l.hashCode() == code).collect(Collectors.toList());
			toRemove.forEach(jc::removeListener);
		}
		messageConsumers.values().forEach(list -> list.remove(consumer));
	}

	@Override
	public void createSubscription(String path, String subscriberId) {
		if (session != null && !this.consumers.containsKey(path))
			this.consumers.put(path, durableTopicConsumer(path, subscriberId));
	}

	public void destroySubscription(String subscriberId) {
		try {
			session.unsubscribe(subscriberId);
		} catch (JMSException e) {
			Logger.error(e);
		}
	}

	@Override
	public void detachListeners(String path) {
		if (this.consumers.containsKey(path)) {
			this.consumers.get(path).close();
			this.consumers.remove(path);
			this.eventConsumers.get(path).clear();
			this.messageConsumers.get(path).clear();
		}
	}

	@Override
	public void requestResponse(String path, javax.jms.Message message, Consumer<javax.jms.Message> onResponse) {
		if (session == null) {
			Logger.error("Connection lost. Invalid session");
			return;
		}
		try {
			QueueProducer producer = new QueueProducer(session, path);
			TemporaryQueue temporaryQueue = session.createTemporaryQueue();
			javax.jms.MessageConsumer consumer = session.createConsumer(temporaryQueue);
			consumer.setMessageListener(m -> acceptMessage(onResponse, consumer, m));
			message.setJMSReplyTo(temporaryQueue);
			message.setJMSCorrelationID(createRandomString());
			sendMessage(producer, message, 100);
			producer.close();
		} catch (JMSException e) {
			Logger.error(e);
		}
	}

	@Override
	public javax.jms.Message requestResponse(String path, javax.jms.Message message) {
		return requestResponse(path, message, -1, null);
	}

	@Override
	public javax.jms.Message requestResponse(String path, javax.jms.Message message, long timeout, TimeUnit timeUnit) {
		if (session == null) {
			Logger.error("Connection lost. Invalid session");
			return null;
		}
		try {
			CompletableFuture<javax.jms.Message> future = new CompletableFuture<>();
			requestResponse(path, message, future::complete);
			return waitFor(future, timeout, timeUnit);
		} catch (Exception e) {
			Logger.error(e);
			return null;
		}
	}

	private static javax.jms.Message waitFor(Future<javax.jms.Message> future, long timeout, TimeUnit timeUnit) throws Exception {
		return timeout <= 0 || timeUnit == null ? future.get() : future.get(timeout, timeUnit);
	}

	@Override
	public void requestResponse(String path, javax.jms.Message message, String responsePath) {
		try {
			message.setJMSReplyTo(this.session.createQueue(responsePath));
			message.setJMSCorrelationID(createRandomString());
			sendMessage(producers.get(path), message);
		} catch (JMSException e) {
			Logger.error(e);
		}
	}

	private MessageEvent newEvent(javax.jms.Message m, Message ev) {
		try {
			return new MessageEvent(ev);
		} catch (Throwable e) {
			Logger.error("Malformed message:\n" + textFrom(m));
			return new MessageEvent(ev);
		}
	}

	public Connection connection() {
		return connection;
	}

	public Session session() {
		return session;
	}

	public void stop() {
		try {
			consumers.values().forEach(JmsConsumer::close);
			consumers.clear();
			producers.values().forEach(JmsProducer::close);
			producers.clear();
			if (session != null) session.close();
			if (connection != null) connection.close();
			session = null;
			connection = null;
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	private Session createSession(boolean transactedSession) throws JMSException {
		return connection.createSession(transactedSession, transactedSession ? SESSION_TRANSACTED : AUTO_ACKNOWLEDGE);
	}

	private void registerEventConsumer(String path, Consumer<Event> onEventReceived) {
		this.eventConsumers.putIfAbsent(path, new CopyOnWriteArrayList<>());
		this.eventConsumers.get(path).add(onEventReceived);
		if (session != null && !this.consumers.containsKey(path)) this.consumers.put(path, topicConsumer(path));
	}

	private void registerEventConsumer(String path, String subscriberId, Consumer<Event> onEventReceived) {
		this.eventConsumers.putIfAbsent(path, new CopyOnWriteArrayList<>());
		this.eventConsumers.get(path).add(onEventReceived);
		if (session != null && !this.consumers.containsKey(path))
			this.consumers.put(path, durableTopicConsumer(path, subscriberId));
	}

	private void registerMessageConsumer(String path, MessageConsumer onMessageReceived) {
		this.messageConsumers.putIfAbsent(path, new CopyOnWriteArrayList<>());
		this.messageConsumers.get(path).add(onMessageReceived);
		if (session != null && !this.consumers.containsKey(path)) this.consumers.put(path, queueConsumer(path));
	}

	private void registerMessageConsumer(String path, String subscriberId, MessageConsumer onMessageReceived) {
		this.messageConsumers.putIfAbsent(path, new CopyOnWriteArrayList<>());
		this.messageConsumers.get(path).add(onMessageReceived);
		if (session != null && !this.consumers.containsKey(path))
			this.consumers.put(path, durableTopicConsumer(path, subscriberId));
	}

	private boolean doSendEvent(String path, Event event) {
		return doSendEvent(path, event, 0);
	}

	private boolean doSendEvents(String path, List<Event> event) {
		return doSendEvents(path, event, 0);
	}

	private boolean doSendEvent(String path, Event event, int expirationTimeInSeconds) {
		if (cannotSendMessage()) return false;
		try {
			topicProducer(path);
			JmsProducer producer = producers.get(path);
			return sendMessage(producer, serialize(event), expirationTimeInSeconds);
		} catch (JMSException | IOException e) {
			Logger.error(e);
			return false;
		}
	}

	private boolean doSendEvents(String path, List<Event> events, int expirationTimeInSeconds) {
		if (cannotSendMessage()) return false;
		try {
			topicProducer(path);
			JmsProducer producer = producers.get(path);
			return sendMessage(producer, serialize(events), expirationTimeInSeconds);
		} catch (JMSException | IOException e) {
			Logger.error(e);
			return false;
		}
	}

	private boolean doSendMessageToQueue(String path, String message) {
		try {
			if (cannotSendMessage()) return false;
			queueProducer(path);
			return sendMessage(producers.get(path), serialize(message));
		} catch (JMSException | IOException e) {
			Logger.error(e);
			return false;
		}
	}

	private boolean doSendMessageToTopic(String path, String message) {
		try {
			if (cannotSendMessage()) return false;
			topicProducer(path);
			return sendMessage(producers.get(path), serialize(message));
		} catch (JMSException | IOException e) {
			Logger.error(e);
			return false;
		}
	}

	private void topicProducer(String path) throws JMSException {
		if (!producers.containsKey(path)) producers.put(path, new TopicProducer(session, path));
	}

	private void queueProducer(String path) throws JMSException {
		if (!producers.containsKey(path)) producers.put(path, new QueueProducer(session, path));
	}

	private boolean cannotSendMessage() {
		return session == null || !connected.get();
	}

	private boolean sendMessage(JmsProducer producer, javax.jms.Message message) {
		return sendMessage(producer, message, 0);
	}

	private boolean sendMessage(JmsProducer producer, javax.jms.Message message, int expirationTimeInSeconds) {
		final boolean[] result = {false};
		try {
			Thread thread = new Thread(() -> result[0] = producer.produce(message, expirationTimeInSeconds));
			thread.start();
			thread.join(1000);
			thread.interrupt();
		} catch (InterruptedException ignored) {
		}
		return result[0];
	}

	private void acceptMessage(Consumer<javax.jms.Message> onResponse, javax.jms.MessageConsumer consumer, javax.jms.Message m) {
		try {
			onResponse.accept(m);
			consumer.close();
		} catch (JMSException e) {
			Logger.error(e);
		}
	}

	private ConnectionListener connectionListener() {
		return new ConnectionListener() {
			@Override
			public void transportInterupted() {
				Logger.warn("Connection with Data Hub (" + brokerUrl + ") interrupted!");
				connected.set(false);
			}

			@Override
			public void transportResumed() {
				Logger.info("Connection with Data Hub (" + brokerUrl + ") established!");
				connected.set(true);
				recoverConsumers();
			}
		};
	}

	private void clearProducers() {
		producers.values().forEach(JmsProducer::close);
		producers.clear();
	}

	private void clearConsumers() {
		consumers.values().forEach(JmsConsumer::close);
		consumers.clear();
	}

	private JmsConsumer topicConsumer(String path) {
		try {
			return new TopicConsumer(session, path);
		} catch (JMSException e) {
			Logger.error(e);
			return null;
		}
	}

	private JmsConsumer durableTopicConsumer(String path, String subscriberId) {
		try {
			return new DurableTopicConsumer(session, path, subscriberId);
		} catch (JMSException e) {
			Logger.error(e);
			return null;
		}
	}

	private QueueConsumer queueConsumer(String path) {
		try {
			return new QueueConsumer(session, path);
		} catch (JMSException e) {
			Logger.error(e);
			return null;
		}
	}

	private void recoverConsumers() {
		if (!started.get()) return;
		if (!eventConsumers.isEmpty() && consumers.isEmpty())
			for (String path : eventConsumers.keySet()) consumers.put(path, topicConsumer(path));
		if (!messageConsumers.isEmpty() && consumers.isEmpty())
			for (String path : messageConsumers.keySet()) {
				if (!consumers.containsKey(path) && session != null) consumers.put(path, queueConsumer(path));
				for (MessageConsumer mConsumer : messageConsumers.get(path)) {
					Consumer<javax.jms.Message> messageConsumer = m -> mConsumer.accept(textFrom(m), callback(m));
					jmsMessageConsumers.put(mConsumer, messageConsumer.hashCode());
					consumers.get(path).listen(messageConsumer);
				}
			}
	}

	private synchronized void recoverEventsAndMessages() {
		recoverEvents();
		recoverMessages();
	}

	private void recoverEvents() {
		if (eventOutBox == null) return;
		synchronized (eventOutBox) {
			if (eventOutBox.isEmpty()) return;
			Logger.info("Recovering events...");
			while (!eventOutBox.isEmpty())
				for (Map.Entry<String, Event> event : eventOutBox.get())
					if (doSendEvent(event.getKey(), event.getValue())) eventOutBox.pop();
					else return;
		}
		Logger.info("All events recovered!");
	}

	private void recoverMessages() {
		if (messageOutBox == null) return;
		synchronized (messageOutBox) {
			if (!messageOutBox.isEmpty())
				while (!messageOutBox.isEmpty()) {
					Map.Entry<String, String> message = messageOutBox.get();
					if (message == null) continue;
					if (doSendMessageToQueue(message.getKey(), message.getValue())) messageOutBox.pop();
					else break;
				}
		}
	}

	private void checkConnection() {
		if (session != null && brokerUrl.startsWith("failover") && !connected.get()) {
			Logger.debug("Data-hub currently disconnected. Waiting for reconnection...");
			return;
		}
		if (connection != null && ((ActiveMQConnection) connection).isStarted() && session != null && ((ActiveMQSession) session).isRunning()) {
			connected.set(true);
			return;
		}
		Logger.debug("Restarting data-hub connection...");
		stop();
		try {
			connect();
		} catch (JMSException e) {
		}
		connected.set(true);
	}

	private void initConnection() {
		try {
			connection = BrokerConnector.createConnection(removeAlexandriaParameters(brokerUrl), config, connectionListener());
			if (connection != null) {
				if (config.clientId() != null && !config.clientId().isEmpty())
					connection.setClientID(config.clientId());
				connection.start();
			}
		} catch (JMSException e) {
			Logger.error(e);
		}
	}

	private String removeAlexandriaParameters(String brokerUrl) {
		final String url = brokerUrl.replace("waitUntilConnect=true", "");
		return url.endsWith("?") ? url.replace("?", "") : url;
	}

	private String callback(javax.jms.Message m) {
		try {
			ActiveMQDestination replyTo = (ActiveMQDestination) m.getJMSReplyTo();
			return replyTo == null ? null : replyTo.getPhysicalName();
		} catch (JMSException e) {
			return null;
		}
	}

	private static javax.jms.Message serialize(String payload) throws IOException, JMSException {
		return io.intino.alexandria.jms.MessageWriter.write(payload);
	}

	public static String createRandomString() {
		Random random = new Random(System.currentTimeMillis());
		long randomLong = random.nextLong();
		return Long.toHexString(randomLong);
	}

	private static javax.jms.Message serialize(Event event) throws IOException, JMSException {
		return serialize(event.toString());
	}

	private static javax.jms.Message serialize(List<Event> event) throws IOException, JMSException {
		return serialize(event.stream().map(Event::toString).collect(Collectors.joining("\n\n")));
	}

	private static class MessageDeserializer {
		static Iterator<Message> deserialize(javax.jms.Message message) {
			return new io.intino.alexandria.message.MessageReader(textFrom(message)).iterator();
		}
	}

	public static class NamedThreadFactory implements ThreadFactory {
		private final AtomicInteger sequence = new AtomicInteger(1);
		private final String prefix;

		public NamedThreadFactory(String prefix) {
			this.prefix = prefix;
		}

		public Thread newThread(Runnable r) {
			Thread thread = new Thread(r);
			int seq = this.sequence.getAndIncrement();
			thread.setName(this.prefix + (seq > 1 ? "-" + seq : ""));
			if (!thread.isDaemon()) {
				thread.setDaemon(true);
			}
			return thread;
		}
	}
}
