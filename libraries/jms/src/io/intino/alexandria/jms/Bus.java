package io.intino.alexandria.jms;

import javax.jms.JMSException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Bus {

	protected javax.jms.Connection connection;
	protected javax.jms.Session session;

	public javax.jms.Session session() {
		return session;
	}

	public javax.jms.Connection connection() {
		return connection;
	}


	public TopicProducer newTopicProducer(String path) {
		try {
			return new TopicProducer(session(), path);
		} catch (JMSException e) {
			Logger.getGlobal().severe(e.getMessage());
			return null;
		}
	}

	public void closeSession() {
		try {
			session.close();
		} catch (JMSException e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
		}
	}
}
