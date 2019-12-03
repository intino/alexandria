package io.intino.alexandria.messagehub;

import io.intino.alexandria.event.Event;
import io.intino.alexandria.event.EventHub;
import io.intino.alexandria.jms.*;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.message.Message;
import io.intino.alexandria.message.MessageWriter;

import javax.jms.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
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
	private final EventOutBox messageOutBox;
	private Connection connection;
	private Session session;
	private AtomicBoolean connected = new AtomicBoolean(false);
	private ScheduledExecutorService scheduler;

	public JmsEventHub(String brokerUrl, String user, String password, String clientId, File messageCacheDirectory) {
		this(brokerUrl, user, password, clientId, false, messageCacheDirectory);
	}

	public JmsEventHub(String brokerUrl, String user, String password, String clientId, boolean transactedSession, File messageCacheDirectory) {
		producers = new HashMap<>();
		consumers = new HashMap<>();
		this.messageOutBox = new EventOutBox(messageCacheDirectory);
		if (brokerUrl != null && !brokerUrl.isEmpty()) {
			try {
				connection = BusConnector.createConnection(brokerUrl, user, password, connectionListener());
				if (connection != null) {
					if (clientId != null && !clientId.isEmpty()) connection.setClientID(clientId);
					connection.start();
					session = connection.createSession(transactedSession, transactedSession ? SESSION_TRANSACTED : AUTO_ACKNOWLEDGE);
				} else Logger.error("Connection is null");
			} catch (JMSException e) {
				Logger.error(e);
			}
		} else Logger.warn("Broker url is null");
		jmsConsumers = new HashMap<>();
		eventConsumers = new HashMap<>();
		scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(recoverMessages(), 0, 1, TimeUnit.HOURS);
	}


	@Override
	public synchronized void sendEvent(String channel, Event event) {
		eventConsumers.getOrDefault(channel, Collections.emptyList()).forEach(messageConsumer -> messageConsumer.accept(event));
		new Thread(() -> {
			if (connected.get() && !messageOutBox.isEmpty()) scheduler.execute(recoverMessages());
			if (!doSendMessage(channel, event)) messageOutBox.push(channel, event);
		}).start();

	}

	public void requestResponse(String channel, String message, Consumer<String> onResponse) {
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
			txtMessage.setText(message);
			txtMessage.setJMSReplyTo(temporaryQueue);
			txtMessage.setJMSCorrelationID(createRandomString());
			producer.produce(txtMessage);
			producer.close();
		} catch (JMSException e) {
			Logger.error(e);
		}
	}

	@Override
	public void attachListener(String channel, Consumer<Event> onMessageReceived) {
		if (session == null) return;
		registerConsumer(channel, onMessageReceived);
		JmsConsumer consumer = this.consumers.get(channel);
		if (consumer == null) return;
		Consumer<javax.jms.Message> messageConsumer = e -> onMessageReceived.accept(new Event(MessageDeserializer.deserialize(e)));
		jmsConsumers.put(messageConsumer, messageConsumer.hashCode());
		consumer.listen(messageConsumer);
	}

	@Override
	public void attachListener(String channel, String subscriberId, Consumer<Event> onMessageReceived) {
		if (session == null) return;
		registerConsumer(channel, onMessageReceived);
		TopicConsumer consumer = (TopicConsumer) this.consumers.get(channel);
		if (consumer == null) return;
		Consumer<javax.jms.Message> messageConsumer = m -> onMessageReceived.accept(new Event(MessageDeserializer.deserialize(m)));
		jmsConsumers.put(messageConsumer, messageConsumer.hashCode());
		consumer.listen(messageConsumer, subscriberId);
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
	public void attachRequestListener(String channel, RequestConsumer onMessageReceived) {
		if (session == null) return;
		this.consumers.putIfAbsent(channel, queueConsumer(channel));
		JmsConsumer consumer = this.consumers.get(channel);
		if (consumer == null) return;
		if (!(consumer instanceof QueueConsumer)) {
			Logger.error("Already exists a topic and queue with this path " + channel);
			return;
		}
		consumer.listen(message -> new Thread(() -> {
			try {
				String result = onMessageReceived.accept(textFrom(message));
				if (result == null) return;
				session.createTextMessage().setText(result);
				message.setJMSCorrelationID(message.getJMSCorrelationID());
				QueueProducer producer = new QueueProducer(session, message.getJMSReplyTo());
				producer.produce(message);
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

	private void registerConsumer(String channel, Consumer<Event> onMessageReceived) {
		this.consumers.putIfAbsent(channel, topicConsumer(channel));
		this.eventConsumers.putIfAbsent(channel, new ArrayList<>());
		this.eventConsumers.get(channel).add(onMessageReceived);
	}

	private boolean doSendMessage(String channel, Event event) {
		if (session == null || !connected.get()) return false;
		try {
			producers.putIfAbsent(channel, new TopicProducer(session, channel));
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
				connected.set(true);
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

	private Runnable recoverMessages() {
		return () -> {
			if (messageOutBox.isEmpty()) return;
			while (!messageOutBox.isEmpty()) {
				Map.Entry<String, Event> poll = messageOutBox.get();
				if (doSendMessage(poll.getKey(), poll.getValue())) messageOutBox.pop();
				else return;
			}
		};
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
