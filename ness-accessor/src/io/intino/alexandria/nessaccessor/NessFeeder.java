package io.intino.alexandria.nessaccessor;

import io.intino.alexandria.inl.Message;
import io.intino.alexandria.jms.TopicProducer;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.nessaccessor.tcp.TcpDatalake;
import io.intino.ness.core.Datalake;
import org.apache.activemq.ActiveMQSession;

import javax.jms.JMSException;
import javax.jms.Session;

public class NessFeeder {

	private final Session session;
	private final String topic;

	public NessFeeder(Session session, String topic) {
		this.session = session;
		this.topic = topic;
	}

	public NessFeeder(Datalake.Connection connection, String topic) {
		if (connection instanceof TcpDatalake.Connection) this.session = ((TcpDatalake.Connection) connection).session();
		else session = null;
		this.topic = topic;
	}

	public void feed(Message... messages) {
		try {
			if (session == null || ((ActiveMQSession) session).isClosed()) {
				Logger.error("Session closed");
				return;
			}
			TopicProducer topicProducer = new TopicProducer(session, topic);
			for (Message message : messages) topicProducer.produce(MessageTranslator.fromInlMessage(message));
			topicProducer.close();
		} catch (JMSException e) {
			Logger.error(e);
		}
	}

}
