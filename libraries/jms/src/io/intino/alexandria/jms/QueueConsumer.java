package io.intino.alexandria.jms;

import io.intino.alexandria.logger.Logger;

import javax.jms.JMSException;
import javax.jms.Session;

public class QueueConsumer extends JmsConsumer {
	public QueueConsumer(Session session, String path) throws JMSException {
		super(session, session.createQueue(path));
		try {
			this.consumer = session.createConsumer(destination, null, true);
		} catch (JMSException ex) {
			Logger.error(ex);
		}
	}
}