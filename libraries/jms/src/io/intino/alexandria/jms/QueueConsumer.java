package io.intino.alexandria.jms;

import javax.jms.JMSException;
import javax.jms.Session;

public class QueueConsumer extends JmsConsumer {

	public QueueConsumer(Session session, String path) throws JMSException {
		super(session, session.createQueue(path));
	}
}
