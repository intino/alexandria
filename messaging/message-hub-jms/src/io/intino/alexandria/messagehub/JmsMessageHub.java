package io.intino.alexandria.messagehub;

import io.intino.alexandria.jms.*;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.message.Message;
import io.intino.alexandria.message.MessageHub;
import io.intino.alexandria.message.MessageWriter;

import javax.jms.JMSException;
import javax.jms.Session;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

import static io.intino.alexandria.jms.MessageReader.textFrom;
import static javax.jms.Session.AUTO_ACKNOWLEDGE;
import static javax.jms.Session.SESSION_TRANSACTED;

public class JmsMessageHub implements MessageHub {
	private final Map<String, Producer> producers;
	private final Map<String, List<TopicConsumer>> consumers;
	private javax.jms.Connection connection;
	private Session session;

	public JmsMessageHub(String brokerUrl, String user, String password) {
		this(brokerUrl, user, password, false);
	}

	public JmsMessageHub(String brokerUrl, String user, String password, boolean transactedSession) {
		if (brokerUrl != null && !brokerUrl.isEmpty()) {
			try {
				connection = BusConnector.createConnection(brokerUrl, user, password);
				if (connection != null)
					session = connection.createSession(transactedSession, transactedSession ? AUTO_ACKNOWLEDGE : SESSION_TRANSACTED);
				else Logger.error("Connection is null");
			} catch (JMSException e) {
				Logger.error(e);
			}
		} else Logger.warn("Broker url is null");
		producers = new HashMap<>();
		consumers = new HashMap<>();
	}

	public void stop() {
		consumers.values().forEach(c -> c.forEach(TopicConsumer::stop));
		consumers.clear();
		producers.values().forEach(Producer::close);
		try {
			session.close();
			connection.stop();
		} catch (JMSException e) {
			Logger.error(e);
		}
	}

	@Override
	public void sendMessage(String channel, Message message) {
		if (session == null) return;
		try {
			Producer producer = producers.putIfAbsent(channel, new TopicProducer(session, channel));
			if (producer == null) return;
			producer.produce(MessageSerializer.serialize(message));
		} catch (JMSException | IOException e) {
			Logger.error(e);
		}
	}

	@Override
	public void attachListener(String channel, Consumer<Message> onMessageReceived) {
		if (session == null) return;
		List<TopicConsumer> topicConsumers = this.consumers.putIfAbsent(channel, new ArrayList<>());
		TopicConsumer topicConsumer = new TopicConsumer(session, channel);
		topicConsumer.listen(message -> onMessageReceived.accept(MessageDeserializer.deserialize(message)));
		topicConsumers.add(topicConsumer);
	}

	@Override
	public void attachListener(String channel, String subscriberId, Consumer<Message> onMessageReceived) {
		if (session == null) return;
		List<TopicConsumer> topicConsumers = this.consumers.putIfAbsent(channel, new ArrayList<>());
		TopicConsumer topicConsumer = new TopicConsumer(session, channel);
		topicConsumer.listen(message -> onMessageReceived.accept(MessageDeserializer.deserialize(message)), subscriberId);
		topicConsumers.add(topicConsumer);
	}

	@Override
	public void detachListeners(String channel) {
		for (TopicConsumer topicConsumer : this.consumers.getOrDefault(channel, Collections.emptyList()))
			topicConsumer.stop();
	}

	private static class MessageSerializer {
		static javax.jms.Message serialize(Message message) throws IOException {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			MessageWriter messageWriter = new MessageWriter(os);
			messageWriter.write(message);
			messageWriter.close();
			return MessageFactory.createMessageFor(os.toString());
		}
	}

	private static class MessageDeserializer {
		static Message deserialize(javax.jms.Message message) {
			return new io.intino.alexandria.message.MessageReader(textFrom(message)).next();
		}
	}
}
