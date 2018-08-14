package io.intino.konos.jms;

import javax.jms.*;

import static org.slf4j.Logger.ROOT_LOGGER_NAME;
import static org.slf4j.LoggerFactory.getLogger;

public class TopicConsumer {

	private final Session session;
	private final String topic;
	private String subscriberID = null;
	private MessageConsumer consumer;

	public TopicConsumer(Session session, String topic) {
		this.session = session;
		this.topic = topic;
	}

	public TopicConsumer listen(RequestConsumer listener) {
		try {
			Destination destination = session.createQueue(topic);
			MessageConsumer consumer = session.createConsumer(destination);
			consumer.setMessageListener(message -> listener.consume(session, message));
			return this;
		} catch (Exception e) {
			getLogger(ROOT_LOGGER_NAME).error(e.getMessage(), e);
			return null;
		}
	}

	public void stop() {
		try {
			if (consumer == null) return;
			consumer.close();
			if (subscriberID != null) session.unsubscribe(subscriberID);
		} catch (InvalidDestinationException ignored) {
		} catch (JMSException e) {
			getLogger(ROOT_LOGGER_NAME).error(e.getMessage(), e);
		}
	}

	public void listen(Consumer reader) {
		try {
			if (session == null) return;
			consumer = session.createConsumer(session.createTopic(topic));
			consumer.setMessageListener(reader::consume);
		} catch (Exception e) {
			getLogger(ROOT_LOGGER_NAME).error(e.getMessage(), e);
		}
	}

	public void listen(Consumer reader, String subscriberID) {
		try {
			if (session == null) return;
			consumer = session.createDurableSubscriber(session.createTopic(topic), subscriberID);
			consumer.setMessageListener(reader::consume);
			this.subscriberID = subscriberID;
		} catch (Exception e) {
			getLogger(ROOT_LOGGER_NAME).error(e.getMessage(), e);
		}
	}

	public void read(int timeout, Consumer messageConsumer) {
		try {
			Destination destination = session.createTopic(topic);
			MessageConsumer consumer = session.createConsumer(destination);
			Message message = consumer.receive(timeout);
			if (message != null) messageConsumer.consume(message);
			consumer.close();
		} catch (JMSException e) {
			getLogger(ROOT_LOGGER_NAME).error(e.getMessage(), e);
		}
	}
}
