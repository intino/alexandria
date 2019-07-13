package io.intino.alexandria.jms;

import io.intino.alexandria.logger.Logger;

import javax.jms.InvalidDestinationException;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

public class TopicConsumer {

	private final Session session;
	private final String topic;
	private String subscriberID = null;
	private MessageConsumer consumer;

	public TopicConsumer(Session session, String topic) {
		this.session = session;
		this.topic = topic;
	}

	public void listen(Consumer consumer) {
		try {
			if (session == null) return;
			this.consumer = session.createConsumer(session.createTopic(topic));
			this.consumer.setMessageListener(consumer::accept);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	public void listen(Consumer consumer, String subscriberId) {
		try {
			if (session == null) return;
			this.consumer = session.createDurableSubscriber(session.createTopic(topic), subscriberId);
			this.consumer.setMessageListener(consumer::accept);
			this.subscriberID = subscriberId;
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	public void stop() {
		try {
			if (consumer == null) return;
			consumer.close();
			if (subscriberID != null) session.unsubscribe(subscriberID);
		} catch (InvalidDestinationException ignored) {
		} catch (JMSException e) {
			Logger.error(e.getMessage(), e);
		}
	}
}
