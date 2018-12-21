package io.intino.alexandria.nessaccessor.tcp;

import io.intino.alexandria.jms.MessageFactory;
import io.intino.alexandria.jms.TopicProducer;
import io.intino.alexandria.logger.Logger;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import static io.intino.alexandria.jms.Consumer.textFrom;

public class AdminService {
	private final String ADMIN_PATH = "service.ness.admin";

	private final Session session;

	public AdminService(Session session) {
		this.session = session;
	}

	String request(String query) {
		try {
			TopicProducer producer = newProducer();
			if (producer == null) return "";
			Message message = MessageFactory.createMessageFor(query);
			message.setJMSReplyTo(this.session.createTemporaryQueue());
			producer.produce(message);
			Message response = session.createConsumer(message.getJMSReplyTo()).receive(1000);
			return response == null ? "" : textFrom(response);
		} catch (JMSException e) {
			Logger.error(e);
			return "";
		}
	}

	void send(String message) {
		TopicProducer producer = newProducer();
		if (producer == null) return;
		producer.produce(MessageFactory.createMessageFor(message));
		producer.close();
	}


	private TopicProducer newProducer() {
		try {
			return new TopicProducer(session, ADMIN_PATH);
		} catch (JMSException e) {
			Logger.error(e);
			return null;
		}
	}
}
