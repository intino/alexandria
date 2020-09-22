import io.intino.alexandria.jms.BusConnector;
import io.intino.alexandria.jms.ConnectionListener;
import io.intino.alexandria.jms.MessageReader;
import io.intino.alexandria.jms.TopicConsumer;
import org.junit.Ignore;
import org.junit.Test;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

public class ConnectionTest {
	@Test
	@Ignore
	public void shouldConnectAndSubscribe() throws JMSException {
		Connection connection = BusConnector.createConnection("failover:(tcp://localhost:63000)", "user", "password", connectionListener());
		if (connection == null) return;
		connection.setClientID("clientId"); //diferente del subscription_id
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		TopicConsumer topicConsumer = new TopicConsumer(session, "bla.bla.example");
		topicConsumer.listen(message -> System.out.println(MessageReader.textFrom(message)), "subscriber-id");
		topicConsumer.close();
		session.unsubscribe("subscriber-id");
		session.close();
		connection.close();

	}

	private ConnectionListener connectionListener() {
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
