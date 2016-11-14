package io.intino.pandora.jms;

import javax.jms.*;

public class TopicConsumer {

	private final Session session;
	private final String queue;

	public TopicConsumer(Session session, String topic) {
		this.session = session;
		this.queue = topic;
	}

	public void listen(Consumer reader) {
		try {
			session.createConsumer(session.createTopic(queue)).setMessageListener(reader::consume);
		} catch (Exception e) {
			System.out.println("Caught: " + e);
		}
	}

	public void listen(Consumer reader, String clientID) {
		try {
			session.createDurableSubscriber(session.createTopic(queue), clientID).setMessageListener(reader::consume);
		} catch (Exception e) {
			System.out.println("Caught: " + e);
		}
	}

	public void read(int timeout, Consumer messageConsumer) {
		try {
			Destination destination = session.createTopic(queue);
			MessageConsumer consumer = session.createConsumer(destination);
			Message message = consumer.receive(timeout);
			if (message != null) messageConsumer.consume(message);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
