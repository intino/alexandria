package io.intino.alexandria.terminal;

import io.intino.alexandria.event.Event;
import io.intino.alexandria.jms.*;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.message.Message;
import io.intino.alexandria.message.MessageWriter;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.command.ActiveMQDestination;

import javax.jms.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static io.intino.alexandria.jms.MessageReader.textFrom;
import static javax.jms.Session.AUTO_ACKNOWLEDGE;
import static javax.jms.Session.SESSION_TRANSACTED;

public class JmsConnector implements Connector {
	private final Map<String, JmsProducer> producers;
	private final Map<String, JmsConsumer> consumers;
	private final Map<String, List<Consumer<Event>>> eventConsumers;
	private final Map<String, List<MessageConsumer>> messageConsumers;
	private final Map<Consumer<Event>, Integer> jmsEventConsumers;
	private final Map<MessageConsumer, Integer> jmsMessageConsumers;
	private final EventOutBox eventOutBox;
	private final MessageOutBox messageOutBox;
	private final String brokerUrl;
	private final String user;
	private final String password;
	private final String clientId;
	private final boolean transactedSession;
	private final AtomicBoolean connected = new AtomicBoolean(false);
	private final AtomicBoolean started = new AtomicBoolean(false);
	private final AtomicBoolean recoveringEvents = new AtomicBoolean(false);
	private Connection connection;
	private Session session;

	public JmsConnector(String brokerUrl, String user, String password, String clientId, File messageCacheDirectory) {
		this(brokerUrl, user, password, clientId, false, messageCacheDirectory);
	}

	public JmsConnector(String brokerUrl, String user, String password, String clientId, boolean transactedSession, File outBoxDirectory) {
		this.brokerUrl = brokerUrl;
		this.user = user;
		this.password = password;
		this.clientId = clientId;
		this.transactedSession = transactedSession;
		this.eventOutBox = new EventOutBox(new File(outBoxDirectory, "events"));
		this.messageOutBox = new MessageOutBox(new File(outBoxDirectory, "requests"));
		producers = new HashMap<>();
		consumers = new HashMap<>();
		jmsEventConsumers = new HashMap<>();
		jmsMessageConsumers = new HashMap<>();
		eventConsumers = new HashMap<>();
		messageConsumers = new HashMap<>();
	}

	public void start() {
		if (brokerUrl == null || brokerUrl.isEmpty()) {
			Logger.warn("Broker url is null");
			return;
		}
		Thread thread = Thread.currentThread();
		new Thread(() -> {
			initConnection();
			thread.interrupt();
		}, "JmsEventHub start").start();
		try {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException ignored) {
			}
			if (connection != null && ((ActiveMQConnection) connection).isStarted()) {
				session = createSession(transactedSession);
				if (session != null && ((ActiveMQSession) session).isRunning()) connected.set(true);
			}
		} catch (JMSException e) {
			Logger.error(e);
		}
		started.set(true);
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(this::checkConnection, 15, 1, TimeUnit.MINUTES);
	}

	@Override
	public synchronized void sendEvent(String path, Event event) {
		new ArrayList<>(eventConsumers.getOrDefault(path, Collections.emptyList())).forEach(eventConsumer -> eventConsumer.accept(event));
		if (!doSendEvent(path, event)) eventOutBox.push(path, event);
	}

	@Override
	public void attachListener(String path, Consumer<Event> onEventReceived) {
		registerEventConsumer(path, onEventReceived);
		JmsConsumer consumer = this.consumers.get(path);
		if (consumer == null) return;
		Consumer<javax.jms.Message> eventConsumer = e -> onEventReceived.accept(new Event(MessageDeserializer.deserialize(e)));
		jmsEventConsumers.put(onEventReceived, eventConsumer.hashCode());
		consumer.listen(eventConsumer);
	}

	@Override
	public void attachListener(String path, String subscriberId, Consumer<Event> onEventReceived) {
		registerEventConsumer(path, onEventReceived);
		TopicConsumer consumer = (TopicConsumer) this.consumers.get(path);
		if (consumer == null) return;
		Consumer<javax.jms.Message> eventConsumer = m -> onEventReceived.accept(new Event(MessageDeserializer.deserialize(m)));
		jmsEventConsumers.put(onEventReceived, eventConsumer.hashCode());
		consumer.listen(eventConsumer, subscriberId);
	}

	@Override
	public void detachListeners(Consumer<Event> consumer) {
		Integer code = jmsEventConsumers.get(consumer);
		if (code == null) return;
		for (JmsConsumer jc : consumers.values()) {
			List<Consumer<javax.jms.Message>> toRemove = jc.listeners().stream().filter(l -> l.hashCode() == code).collect(Collectors.toList());
			toRemove.forEach(jc::removeListener);
		}
		eventConsumers.values().forEach(list -> list.remove(consumer));
	}

	@Override
	public void sendMessage(String path, String message) {
		if (!doSendMessage(path, message)) messageOutBox.push(path, message);
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
	public void detachListeners(String path) {
		if (this.consumers.containsKey(path)) {
			this.consumers.get(path).close();
			this.consumers.remove(path);
			this.eventConsumers.get(path).clear();
			this.messageConsumers.get(path).clear();
		}
	}

	@Override
	public void requestResponse(String path, String message, Consumer<String> onResponse) {
		if (session == null) {
			Logger.error("Session is null");
			return;
		}
		try {
			QueueProducer producer = new QueueProducer(session, path);
			Destination temporaryQueue = session.createTemporaryQueue();
			javax.jms.MessageConsumer consumer = session.createConsumer(temporaryQueue);
			consumer.setMessageListener(m -> acceptMessage(onResponse, consumer, (TextMessage) m));
			final TextMessage txtMessage = session.createTextMessage();
			txtMessage.setText(message);
			txtMessage.setJMSReplyTo(temporaryQueue);
			txtMessage.setJMSCorrelationID(createRandomString());
			sendMessage(producer, txtMessage);
			producer.close();
		} catch (JMSException e) {
			Logger.error(e);
		}
	}

	@Override
	public void requestResponse(String path, String message, String responsePath) {
		if (!doSendMessage(path, message, responsePath)) messageOutBox.push(path, message);
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
		if (!this.consumers.containsKey(path) && session != null) this.consumers.put(path, topicConsumer(path));
	}

	private void registerMessageConsumer(String path, MessageConsumer onEventReceived) {
		this.messageConsumers.putIfAbsent(path, new CopyOnWriteArrayList<>());
		this.messageConsumers.get(path).add(onEventReceived);
		if (session != null) this.consumers.putIfAbsent(path, queueConsumer(path));
	}

	private boolean doSendEvent(String path, Event event) {
		if (session == null || !connected.get() || recoveringEvents.get()) return false;
		try {
			producers.putIfAbsent(path, new TopicProducer(session, path));
			JmsProducer producer = producers.get(path);
			return sendMessage(producer, serialize(event));
		} catch (JMSException | IOException e) {
			Logger.error(e);
			return false;
		}
	}

	private boolean doSendMessage(String path, String message) {
		if (cannotSendMessage()) return false;
		try {
			producers.putIfAbsent(path, new QueueProducer(session, path));
			JmsProducer producer = producers.get(path);
			return sendMessage(producer, serialize(message));
		} catch (JMSException | IOException e) {
			Logger.error(e);
			return false;
		}
	}

	private boolean doSendMessage(String path, String message, String replyTo) {
		if (cannotSendMessage()) return false;
		try {
			producers.putIfAbsent(path, new QueueProducer(session, path));
			JmsProducer producer = producers.get(path);
			return sendMessage(producer, serialize(message, replyTo));
		} catch (JMSException | IOException e) {
			Logger.error(e);
			return false;
		}
	}

	private boolean cannotSendMessage() {
		return session == null || !connected.get() || recoveringEvents.get();
	}

	private boolean sendMessage(JmsProducer producer, javax.jms.Message payload) {
		final boolean[] result = {false};
		try {
			Thread thread = new Thread(() -> result[0] = producer.produce(payload));
			thread.start();
			thread.join(1000);
			thread.interrupt();
		} catch (InterruptedException ignored) {
		}
		return result[0];
	}

	private void acceptMessage(Consumer<String> onResponse, javax.jms.MessageConsumer consumer, TextMessage m) {
		try {
			onResponse.accept(m.getText());
			consumer.close();
		} catch (JMSException e) {
			Logger.error(e);
		}
	}

	private ConnectionListener connectionListener() {
		return new ConnectionListener() {
			@Override
			public void transportInterupted() {
				Logger.warn("Connection with Data Hub interrupted!");
				connected.set(false);
			}

			@Override
			public void transportResumed() {
				Logger.info("Connection with Data Hub established!");
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

	private TopicConsumer topicConsumer(String path) {
		try {
			return new TopicConsumer(session, path);
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
		this.recoveringEvents.set(false);
		recoverEvents();
		recoverMessages();
	}

	private void recoverMessages() {
		recoveringEvents.set(true);
		synchronized (messageOutBox) {
			if (!messageOutBox.isEmpty())
				while (!messageOutBox.isEmpty()) {
					Map.Entry<String, String> message = messageOutBox.get();
					if (message == null) continue;
					if (doSendMessage(message.getKey(), message.getValue())) messageOutBox.pop();
					else break;
				}
		}
		recoveringEvents.set(false);
	}

	private void recoverEvents() {
		if (recoveringEvents.get()) return;
		recoveringEvents.set(true);
		synchronized (eventOutBox) {
			if (!eventOutBox.isEmpty())
				while (!eventOutBox.isEmpty()) {
					Map.Entry<String, Event> event = eventOutBox.get();
					if (event == null) continue;
					if (doSendEvent(event.getKey(), event.getValue())) eventOutBox.pop();
					else break;
				}
		}
		recoveringEvents.set(false);
	}

	private void checkConnection() {
		Logger.debug("Checking DataHub connection...");
		if (brokerUrl.startsWith("failover") && !connected.get()) {
			Logger.debug("Currently disconnected. Waiting for reconnection...");
			return;
		}
		if (connection != null && ((ActiveMQConnection) connection).isStarted() && ((ActiveMQSession) session).isRunning()) {
			Logger.debug("Currently Connected");
			connected.set(true);
			return;
		}
		stop();
		start();
		connected.set(true);
	}

	private void initConnection() {
		try {
			connection = BusConnector.createConnection(brokerUrl, user, password, connectionListener());
			if (clientId != null && !clientId.isEmpty()) connection.setClientID(clientId);
			connection.start();
		} catch (JMSException e) {
			Logger.error(e);
		}
	}

	private String callback(javax.jms.Message m) {
		try {
			ActiveMQDestination replyTo = (ActiveMQDestination) m.getJMSReplyTo();
			return replyTo == null ? null : replyTo.getPhysicalName();
		} catch (JMSException e) {
			return null;
		}
	}

	private javax.jms.Message serialize(String payload, String replyTo) throws IOException, JMSException {
		javax.jms.Message message = io.intino.alexandria.jms.MessageWriter.write(payload);
		message.setJMSReplyTo(this.session.createQueue(replyTo));
		message.setJMSCorrelationID(createRandomString());
		return message;
	}

	private static javax.jms.Message serialize(String payload) throws IOException, JMSException {
		return io.intino.alexandria.jms.MessageWriter.write(payload);
	}

	private static String createRandomString() {
		Random random = new Random(System.currentTimeMillis());
		long randomLong = random.nextLong();
		return Long.toHexString(randomLong);
	}

	private static javax.jms.Message serialize(Event event) throws IOException, JMSException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		MessageWriter messageWriter = new MessageWriter(os);
		messageWriter.write(event.toMessage());
		messageWriter.close();
		return serialize(os.toString());
	}

	private static class MessageDeserializer {
		static Message deserialize(javax.jms.Message message) {
			return new io.intino.alexandria.message.MessageReader(textFrom(message)).next();
		}
	}
}
