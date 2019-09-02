package io.intino.alexandria.messagehub;

import io.intino.alexandria.jms.*;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.message.Message;
import io.intino.alexandria.message.MessageHub;
import io.intino.alexandria.message.MessageWriter;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static io.intino.alexandria.jms.MessageReader.textFrom;
import static javax.jms.Session.AUTO_ACKNOWLEDGE;
import static javax.jms.Session.SESSION_TRANSACTED;

public class JmsMessageHub implements MessageHub {
	private final Map<String, JmsProducer> producers;
	private final Map<String, JmsConsumer> consumers;
	private javax.jms.Connection connection;
	private Session session;

	public JmsMessageHub(String brokerUrl, String user, String password, String clientId) {
		this(brokerUrl, user, password, clientId, false);
	}

	public JmsMessageHub(String brokerUrl, String user, String password, String clientId, boolean transactedSession) {
		producers = new HashMap<>();
		consumers = new HashMap<>();
		if (brokerUrl != null && !brokerUrl.isEmpty()) {
			try {
				connection = BusConnector.createConnection(brokerUrl, user, password);
				if (connection != null) {
					if (clientId != null && !clientId.isEmpty()) connection.setClientID(clientId);
					connection.start();
					session = connection.createSession(transactedSession, transactedSession ? SESSION_TRANSACTED : AUTO_ACKNOWLEDGE);
				} else Logger.error("Connection is null");
			} catch (JMSException e) {
				Logger.error(e);
			}
		} else Logger.warn("Broker url is null");

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

	@Override
	public void sendMessage(String channel, Message message) {
		if (session == null) return;
		try {
			producers.putIfAbsent(channel, new TopicProducer(session, channel));
			JmsProducer producer = producers.get(channel);
			if (producer == null) return;
			producer.produce(MessageSerializer.serialize(message));
		} catch (JMSException | IOException e) {
			Logger.error(e);
		}
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

	private void acceptMessage(Consumer<String> onResponse, MessageConsumer consumer, TextMessage m) {
		try {
			onResponse.accept(m.getText());
			consumer.close();
		} catch (JMSException e) {
			Logger.error(e);
		}
	}

	@Override
	public void attachListener(String channel, Consumer<Message> onMessageReceived) {
		if (session == null) return;
		this.consumers.putIfAbsent(channel, topicConsumer(channel));
		JmsConsumer consumer = this.consumers.get(channel);
		if (consumer == null) return;
		consumer.listen(message -> onMessageReceived.accept(MessageDeserializer.deserialize(message)));
	}

	@Override
	public void attachListener(String channel, String subscriberId, Consumer<Message> onMessageReceived) {
		if (session == null) return;
		this.consumers.putIfAbsent(channel, topicConsumer(channel));
		TopicConsumer consumer = (TopicConsumer) this.consumers.get(channel);
		if (consumer == null) return;
		consumer.listen(message -> onMessageReceived.accept(MessageDeserializer.deserialize(message)), subscriberId);
	}

	@Override
	public void detachListeners(String channel) {
		if (this.consumers.containsKey(channel)) {
			this.consumers.get(channel).close();
			this.consumers.remove(channel);
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


	private static String createRandomString() {
		java.util.Random random = new java.util.Random(System.currentTimeMillis());
		long randomLong = random.nextLong();
		return Long.toHexString(randomLong);
	}


	private static class MessageSerializer {
		static javax.jms.Message serialize(Message message) throws IOException, JMSException {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			MessageWriter messageWriter = new MessageWriter(os);
			messageWriter.write(message);
			messageWriter.close();
			TextMessage textMessage = new ActiveMQTextMessage();
			textMessage.setText(os.toString());
			return textMessage;
		}
	}

	private static class MessageDeserializer {
		static Message deserialize(javax.jms.Message message) {
			return new io.intino.alexandria.message.MessageReader(textFrom(message)).next();
		}
	}
}
