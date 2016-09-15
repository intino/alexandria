package teseo.jms;

import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;

public class QueueConsumer {

	private final Session session;

	public QueueConsumer(Session session) {
		this.session = session;
	}

	public void listen(String queue, RequestConsumer listener) {
		try {
			Destination destination = session.createQueue(queue);
			MessageConsumer consumer = session.createConsumer(destination);
			consumer.setMessageListener(message -> listener.consume(session, message));
		} catch (Exception e) {
			System.out.println("Caught: " + e);
		}
	}
}
