package io.intino.alexandria.jms;

import io.intino.alexandria.logger.Logger;
import org.apache.activemq.ActiveMQSession;

import javax.jms.*;
import java.util.function.Consumer;

public class TopicConsumer extends JmsConsumer {

	private String subscriberId = null;

	public TopicConsumer(Session session, String topic) throws JMSException {
		super(session, session.createTopic(topic));
	}

	public void listen(Consumer<Message> listener, String subscriberId) {
		try {
			this.listeners.add(listener);
			this.subscriberId = subscriberId;
			if (this.consumer == null) {
				this.consumer = session.createDurableSubscriber((Topic) destination, subscriberId, null, true);
				consumer.setMessageListener(m -> listeners.forEach(l -> l.accept(m)));
			}
		} catch (JMSException e) {
			Logger.error(e);
		}
	}

	public void close() {
		try {
			if (consumer == null) return;
			consumer.close();
			if (subscriberId != null && !((ActiveMQSession) session).isClosed()) session.unsubscribe(subscriberId);
		} catch (InvalidDestinationException ignored) {
		} catch (JMSException e) {
			Logger.error(e);
		}
	}
}
