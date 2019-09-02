package io.intino.alexandria.jms;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

public class TopicProducer extends JmsProducer {

	public TopicProducer(Session session, String channel) throws JMSException {
		super(session, session.createTopic(channel));
	}

	public TopicProducer(Session session, String channel, int messageExpirationSeconds) throws JMSException {
		super(session, session.createTopic(channel), messageExpirationSeconds);
	}

	public TopicProducer(Session session, Destination destination) {
		super(session, destination);
	}

	public TopicProducer(Session session, Destination destination, int messageExpirationSeconds) {
		super(session, destination, messageExpirationSeconds);
	}
}
