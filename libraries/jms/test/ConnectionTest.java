import io.intino.alexandria.jms.BusConnector;
import io.intino.alexandria.jms.ConnectionListener;
import io.intino.alexandria.jms.DurableTopicConsumer;
import io.intino.alexandria.jms.MessageReader;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

public class ConnectionTest {
	public static void main(String[] args) throws JMSException, InterruptedException {
		Connection connection = BusConnector.createConnection("failover:(tcp://localhost:63000)", "digestor", "digestor", connectionListener());
		if (connection == null) return;
		connection.setClientID("clientId"); //diferente del subscription_id
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		DurableTopicConsumer topicConsumer = new DurableTopicConsumer(session, "bla.bla.example", "subscriber-id");
		Thread.sleep(10000);
		topicConsumer.close();
		session.unsubscribe("subscriber-id");
		session.close();
		connection.close();
	}

	private static ConnectionListener connectionListener() {
		return new ConnectionListener() {
			@Override
			public void transportInterupted() {
			}

			@Override
			public void transportResumed() {
			}
		};
	}
}
