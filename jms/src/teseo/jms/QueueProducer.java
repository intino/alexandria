package teseo.jms;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;


public class QueueProducer extends Producer {

	public QueueProducer(Session session, String path) throws JMSException {
		super(session, session.createQueue(path));
	}

	public QueueProducer(Session session, Destination destination) throws JMSException {
		super(session, destination);
	}
}
