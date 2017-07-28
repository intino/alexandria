package io.intino.konos.jms;

import javax.jms.*;
import java.util.logging.Level;

import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getGlobal;

public class TopicConsumer {

	private final Session session;
	private final String topic;
	private String subscriberID = null;
	private MessageConsumer consumer;

	public TopicConsumer(Session session, String topic) {
		this.session = session;
		this.topic = topic;
	}

	public void listen(RequestConsumer listener) {
		try {
			Destination destination = session.createQueue(topic);
			MessageConsumer consumer = session.createConsumer(destination);
			consumer.setMessageListener(message -> listener.consume(session, message));
		} catch (Exception e) {
			getGlobal().log(Level.SEVERE, e.getMessage(), e);
		}
	}

	public void stop() {
		try {
			if (consumer == null) return;
			consumer.close();
			if (subscriberID != null) session.unsubscribe(subscriberID);
		} catch (JMSException e) {
			getGlobal().log(SEVERE, e.getMessage(), e);
		}
	}

	public void listen(Consumer reader) {
		try {
			if (session == null) return;
			consumer = session.createConsumer(session.createTopic(topic));
			consumer.setMessageListener(reader::consume);
		} catch (Exception e) {
			getGlobal().log(SEVERE, e.getMessage(), e);
		}
	}

	public void listen(Consumer reader, String subscriberID) {
		try {
			if (session == null) return;
			consumer = session.createDurableSubscriber(session.createTopic(topic), subscriberID);
			consumer.setMessageListener(reader::consume);
			this.subscriberID = subscriberID;
		} catch (Exception e) {
			getGlobal().log(SEVERE, e.getMessage(), e);
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
			getGlobal().log(Level.SEVERE, e.getMessage(), e);
		}
	}
}
