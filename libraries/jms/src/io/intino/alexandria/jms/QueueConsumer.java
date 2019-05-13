package io.intino.alexandria.jms;

import javax.jms.*;

import static org.slf4j.Logger.ROOT_LOGGER_NAME;
import static org.slf4j.LoggerFactory.getLogger;

public class QueueConsumer {

	private final Session session;
	private final String queue;

	public QueueConsumer(Session session, String queue) {
		this.session = session;
		this.queue = queue;
	}

	public void listen(RequestConsumer listener) {
		try {
			Destination destination = session.createQueue(queue);
			MessageConsumer consumer = session.createConsumer(destination);
			consumer.setMessageListener(message -> listener.consume(session, message));
		} catch (Exception e) {
			getLogger(ROOT_LOGGER_NAME).error(e.getMessage(), e);
		}
	}

	public void listen(Consumer listener) {
		try {
			Destination destination = session.createQueue(queue);
			MessageConsumer consumer = session.createConsumer(destination);
			consumer.setMessageListener(listener::consume);
		} catch (Exception e) {
			getLogger(ROOT_LOGGER_NAME).error(e.getMessage(), e);
		}
	}

	public void read(int timeout, Consumer messageConsumer) {
		try {
			Destination destination = session.createQueue(queue);
			MessageConsumer consumer = session.createConsumer(destination);
			Message message = consumer.receive(timeout);
			if (message != null) messageConsumer.consume(message);
		} catch (JMSException e) {
			getLogger(ROOT_LOGGER_NAME).error(e.getMessage(), e);
		}
	}
}
