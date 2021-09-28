package io.intino.alexandria.jms;

import io.intino.alexandria.logger.Logger;

import javax.jms.JMSException;
import javax.jms.Session;

public class TopicConsumer extends JmsConsumer {

	public TopicConsumer(Session session, String topic) throws JMSException {
		super(session, session.createTopic(topic));
		try {
			this.consumer = session.createConsumer(destination, null, true);
		} catch (JMSException ex) {
			Logger.error(ex);
		}
	}
}