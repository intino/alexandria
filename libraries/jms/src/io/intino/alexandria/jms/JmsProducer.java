package io.intino.alexandria.jms;


import io.intino.alexandria.logger.Logger;

import javax.jms.*;

import static javax.jms.DeliveryMode.NON_PERSISTENT;

public abstract class JmsProducer {

	protected final Session session;
	private MessageProducer producer = null;

	public JmsProducer(Session session, Destination destination) {
		this(session, destination, 0);
	}

	public JmsProducer(Session session, Destination destination, int messageExpirationSeconds) {
		this.session = session;
		try {
			this.producer = session.createProducer(destination);
			this.producer.setTimeToLive(messageExpirationSeconds * 1000);
			this.producer.setDeliveryMode(NON_PERSISTENT);
		} catch (JMSException e) {
			Logger.error(e);
		}
	}

	public boolean produce(Message message) {
		try {
			producer.send(message);
			return true;
		} catch (Exception e) {
			Logger.error(e);
			return false;
		}
	}

	public boolean produce(Message message, int messageExpirationSeconds) {
		try {
			producer.send(message, NON_PERSISTENT, 4, messageExpirationSeconds * 1000);
			return true;
		} catch (Exception e) {
			Logger.error(e);
			return false;
		}
	}

	public void close() {
		if (producer != null) try {
			producer.close();
			producer = null;
		} catch (JMSException e) {
			Logger.error(e);
		}
	}
}
