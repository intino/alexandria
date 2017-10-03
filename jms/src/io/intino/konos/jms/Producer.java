package io.intino.konos.jms;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.logging.Level;

import static java.util.logging.Logger.getGlobal;
import static org.slf4j.Logger.ROOT_LOGGER_NAME;

public abstract class Producer {
	private static Logger logger = LoggerFactory.getLogger(ROOT_LOGGER_NAME);

	protected final Session session;
	private MessageProducer producer = null;


	public Producer(Session session, Destination destination) {
		this.session = session;
		try {
			this.producer = session.createProducer(destination);
			MessageProducer producer = this.producer;
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
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

	public void close() {
		if (producer != null) try {
			producer.close();
		} catch (JMSException e) {
			getGlobal().log(Level.SEVERE, e.getMessage(), e);
		}
	}
}
