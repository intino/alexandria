package io.intino.alexandria.jms;

import javax.jms.JMSException;
import javax.jms.Session;

public class TopicProducer extends Producer {

	public TopicProducer(Session session, String channel) throws JMSException {
		super(session, session.createTopic(channel));
	}

	public TopicProducer(Session session, String channel, int messageExpirationSeconds) throws JMSException {
		super(session, session.createTopic(channel), messageExpirationSeconds);
	}
}
