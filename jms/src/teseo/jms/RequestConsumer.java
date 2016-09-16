package teseo.jms;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

public interface RequestConsumer {

	void consume(Session session, Message message);


	default Destination replyTo(Message request) {
		try {
			return request.getJMSReplyTo();
		} catch (JMSException e) {
			e.printStackTrace();
			return null;
		}
	}

	default String idOf(Message message) {
		try {
			return message.getJMSCorrelationID();
		} catch (JMSException e) {
			e.printStackTrace();
			return "";
		}
	}

	default void response(Session session, Destination destination, Message message) {
		if (session == null || destination == null || message == null) return;
		try {
			new QueueProducer(session, destination).produce(message);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
