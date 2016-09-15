package teseo.jms;

import javax.jms.*;

public class QueueProducer {

	private final Session session;
	private Destination queue;

	public QueueProducer(Session session, String queue) {
		this.session = session;
		try {
			this.queue = session.createQueue(queue);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public QueueProducer(Session session, Destination queue) {
		this.session = session;
		this.queue = queue;
	}

	public void produce(Message message) {
		try {
			MessageProducer producer = session.createProducer(queue);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			producer.send(message);
		} catch (Exception e) {
			System.out.println("Caught: " + e);
			e.printStackTrace();
		}
	}
}
