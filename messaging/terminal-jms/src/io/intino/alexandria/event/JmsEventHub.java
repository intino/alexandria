package io.intino.alexandria.event;

import io.intino.alexandria.jms.*;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.message.Message;
import io.intino.alexandria.message.MessageWriter;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQSession;

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

public class JmsEventHub implements EventHub {
	private final Map<String, JmsProducer> producers;
	private final Map<String, JmsConsumer> consumers;
	private final Map<String, List<Consumer<Event>>> eventConsumers;
	private final Map<Consumer<javax.jms.Message>, Integer> jmsConsumers;
	private final EventOutBox eventOutBox;
	private Connection connection;
	private Session session;
	private AtomicBoolean connected = new AtomicBoolean(false);
	private AtomicBoolean recoveringEvents = new AtomicBoolean(false);
	private ScheduledExecutorService scheduler;

	public JmsEventHub(String brokerUrl, String user, String password, String clientId, File messageCacheDirectory) {
		this(brokerUrl, user, password, clientId, false, messageCacheDirectory);
	}

	public JmsEventHub(String brokerUrl, String user, String password, String clientId, boolean transactedSession, File messageCacheDirectory) {
		producers = new HashMap<>();
		consumers = new HashMap<>();
		jmsConsumers = new HashMap<>();
		eventConsumers = new HashMap<>();
		this.eventOutBox = new EventOutBox(messageCacheDirectory);
		if (brokerUrl != null && !brokerUrl.isEmpty()) {
			Thread thread = Thread.currentThread();
			new Thread(() -> {
				initConnection(brokerUrl, user, password, clientId);
				thread.interrupt();
			}).start();
			try {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
				}
				if (connection != null && ((ActiveMQConnection) connection).isStarted()) {
					session = createSession(transactedSession);
					if (session != null && ((ActiveMQSession) session).isRunning()) {
						connected.set(true);
						Logger.info("Connection with Data Hub stablished!");
					}
				}
			} catch (JMSException e) {
				Logger.error(e);
			}
		} else Logger.warn("Broker url is null");
		scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(this::recoverEvents, 0, 1, TimeUnit.HOURS);
	}

	private void initConnection(String brokerUrl, String user, String password, String clientId) {
		try {
			connection = BusConnector.createConnection(brokerUrl, user, password, connectionListener());
			if (clientId != null && !clientId.isEmpty()) connection.setClientID(clientId);
			connection.start();
		} catch (JMSException e) {
			Logger.error(e);
		}
	}

	@Override
	public synchronized void sendEvent(String channel, Event event) {
		new ArrayList<>(eventConsumers.getOrDefault(channel, Collections.emptyList())).forEach(eventConsumer -> eventConsumer.accept(event));
		new Thread(() -> {
			if (connected.get() && !eventOutBox.isEmpty() && !recoveringEvents.get()) recoverEvents();
			if (!doSendEvent(channel, event)) eventOutBox.push(channel, event);
		}).start();
	}

	public void requestResponse(String channel, String event, Consumer<String> onResponse) {
		if (session == null) {
			Logger.error("Session is null");
			return;
		}
		try {
			QueueProducer producer = new QueueProducer(session, channel);
			Destination temporaryQueue = session.createTemporaryQueue();
			MessageConsumer consumer = session.createConsumer(temporaryQueue);
			consumer.setMessageListener(m -> acceptMessage(onResponse, consumer, (TextMessage) m));
			final TextMessage txtMessage = session.createTextMessage();
			txtMessage.setText(event);
			txtMessage.setJMSReplyTo(temporaryQueue);
			txtMessage.setJMSCorrelationID(createRandomString());
			producer.produce(txtMessage);
			producer.close();
		} catch (JMSException e) {
			Logger.error(e);
		}
	}

	@Override
	public void attachListener(String channel, Consumer<Event> onEventReceived) {
		registerConsumer(channel, onEventReceived);
		JmsConsumer consumer = this.consumers.get(channel);
		if (consumer == null) return;
		Consumer<javax.jms.Message> eventConsumer = e -> onEventReceived.accept(new Event(MessageDeserializer.deserialize(e)));
		jmsConsumers.put(eventConsumer, eventConsumer.hashCode());
		consumer.listen(eventConsumer);
	}

	@Override
	public void attachListener(String channel, String subscriberId, Consumer<Event> onEventReceived) {
		registerConsumer(channel, onEventReceived);
		TopicConsumer consumer = (TopicConsumer) this.consumers.get(channel);
		if (consumer == null) return;
		Consumer<javax.jms.Message> eventConsumer = m -> onEventReceived.accept(new Event(MessageDeserializer.deserialize(m)));
		jmsConsumers.put(eventConsumer, eventConsumer.hashCode());
		consumer.listen(eventConsumer, subscriberId);
	}

	@Override
	public void detachListeners(String channel) {
		if (this.consumers.containsKey(channel)) {
			this.consumers.get(channel).close();
			this.consumers.remove(channel);
			this.eventConsumers.get(channel).clear();
		}
	}

	@Override
	public void detachListeners(Consumer<Event> consumer) {
		Integer code = jmsConsumers.get(consumer);
		if (code == null) return;
		eventConsumers.values().forEach(list -> list.remove(consumer));
		for (JmsConsumer jc : consumers.values()) {
			List<Consumer<javax.jms.Message>> toRemove = jc.listeners().stream().filter(l -> l.hashCode() == code).collect(Collectors.toList());
			toRemove.forEach(jc::removeListener);
		}
	}

	@Override
	public void attachRequestListener(String channel, RequestConsumer onRequestReceived) {
		if (session == null) return;
		if (!this.consumers.containsKey(channel)) this.consumers.put(channel, queueConsumer(channel));
		JmsConsumer consumer = this.consumers.get(channel);
		if (consumer == null) return;
		if (!(consumer instanceof QueueConsumer)) {
			Logger.error("Already exists a topic and queue with this path " + channel);
			return;
		}
		consumer.listen(event -> new Thread(() -> {
			try {
				String result = onRequestReceived.accept(textFrom(event));
				if (result == null) return;
				TextMessage response = session.createTextMessage();
				response.setText(result);
				response.setJMSCorrelationID(event.getJMSCorrelationID());
				QueueProducer producer = new QueueProducer(session, event.getJMSReplyTo());
				producer.produce(response);
				producer.close();
			} catch (JMSException e) {
				Logger.error(e);
			}
		}).start());
	}

	public Connection connection() {
		return connection;
	}

	public Session session() {
		return session;
	}

	public void stop() {
		consumers.values().forEach(JmsConsumer::close);
		consumers.clear();
		producers.values().forEach(JmsProducer::close);
		try {
			session.close();
			connection.close();
		} catch (JMSException e) {
			Logger.error(e);
		}
	}

	private Session createSession(boolean transactedSession) throws JMSException {
		return connection.createSession(transactedSession, transactedSession ? SESSION_TRANSACTED : AUTO_ACKNOWLEDGE);
	}

	private void registerConsumer(String channel, Consumer<Event> onEventReceived) {
		this.eventConsumers.putIfAbsent(channel, new CopyOnWriteArrayList<>());
		this.eventConsumers.get(channel).add(onEventReceived);
		if (!this.consumers.containsKey(channel) && session != null)
			this.consumers.put(channel, topicConsumer(channel));
	}

	private boolean doSendEvent(String channel, Event event) {
		if (session == null || !connected.get()) return false;
		try {
			if (!this.producers.containsKey(channel)) producers.put(channel, new TopicProducer(session, channel));
			JmsProducer producer = producers.get(channel);
			if (producer == null) return false;
			return producer.produce(serialize(event));
		} catch (JMSException | IOException e) {
			Logger.error(e);
			return false;
		}
	}

	private void acceptMessage(Consumer<String> onResponse, MessageConsumer consumer, TextMessage m) {
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
				connected.set(false);
			}

			@Override
			public void transportResumed() {
				Logger.info("Connection with Data Hub resumed!");
				connected.set(true);
				if (!eventConsumers.isEmpty() && consumers.isEmpty()) try {
					session = createSession(false);
					for (String channel : eventConsumers.keySet()) consumers.put(channel, topicConsumer(channel));
				} catch (JMSException e) {
					Logger.error(e);
				}
			}
		};
	}

	private TopicConsumer topicConsumer(String channel) {
		try {
			return new TopicConsumer(session, channel);
		} catch (JMSException e) {
			Logger.error(e);
			return null;
		}
	}

	private QueueConsumer queueConsumer(String channel) {
		try {
			return new QueueConsumer(session, channel);
		} catch (JMSException e) {
			Logger.error(e);
			return null;
		}
	}

	private void recoverEvents() {
		recoveringEvents.set(true);
		if (!eventOutBox.isEmpty())
			while (!eventOutBox.isEmpty()) {
				Map.Entry<String, Event> event = eventOutBox.get();
				if (event == null) continue;
				if (doSendEvent(event.getKey(), event.getValue())) eventOutBox.pop();
				else break;
			}
		recoveringEvents.set(false);
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
		return io.intino.alexandria.jms.MessageWriter.write(os.toString());
	}


	private static class MessageDeserializer {
		static Message deserialize(javax.jms.Message message) {
			return new io.intino.alexandria.message.MessageReader(textFrom(message)).next();
		}
	}
}
