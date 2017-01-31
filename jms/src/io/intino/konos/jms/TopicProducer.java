package io.intino.konos.jms;

import javax.jms.JMSException;
import javax.jms.Session;

public class TopicProducer extends Producer {

	public TopicProducer(Session session, String path) throws JMSException {
		super(session, session.createTopic(path));
	}


}
