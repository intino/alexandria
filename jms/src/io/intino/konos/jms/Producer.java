package io.intino.konos.jms;


import org.apache.activemq.ActiveMQSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.logging.Level;

import static java.util.logging.Logger.getGlobal;
import static javax.jms.DeliveryMode.NON_PERSISTENT;
import static org.slf4j.Logger.ROOT_LOGGER_NAME;

public abstract class Producer {
	private static Logger logger = LoggerFactory.getLogger(ROOT_LOGGER_NAME);

	protected final Session session;
	private MessageProducer producer = null;


	public Producer(Session session, Destination destination) {
		this(session, destination, 0);
	}

	public Producer(Session session, Destination destination, int messageExpirationSeconds) {
		this.session = session;
		try {
			this.producer = session.createProducer(destination);
			this.producer.setTimeToLive(messageExpirationSeconds * 1000);
			this.producer.setDeliveryMode(NON_PERSISTENT);
		} catch (JMSException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void produce(Message message) {
		try {
			producer.send(message);
		} catch (Exception e) {
			getGlobal().log(Level.SEVERE, e.getMessage(), e);
		}
	}

	public void produce(Message message, int messageExpirationSeconds) {
		try {
			producer.send(message, NON_PERSISTENT, 4, messageExpirationSeconds * 1000);
		} catch (Exception e) {
			getGlobal().log(Level.SEVERE, e.getMessage(), e);
		}
	}

	public void close() {
		if (producer != null) try {
			producer.close();
			producer = null;
		} catch (JMSException e) {
			getGlobal().log(Level.SEVERE, e.getMessage(), e);
		}
	}


	public boolean isClosed() {
		return session == null || ((ActiveMQSession) session).isClosed() || producer == null;
	}
}
