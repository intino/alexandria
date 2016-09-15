package teseo.jms;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ConnectionInfo;

import javax.jms.*;

public class TopicConsumer {

	private final String location;
	private final String user;
	private final String password;

	public TopicConsumer(String location, String user, String password) {
		this.location = location;
		this.user = user;
		this.password = password;
	}


	public void subscribe(String topic) {

	}

	public void subscribe(String topic, String clientID) {
		try {
			Connection connection = new ActiveMQConnectionFactory(user, password, location).createConnection();
			if (!clientID.isEmpty()) connection.setClientID(clientID);
			connection.start();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createTopic(topic);
			MessageConsumer consumer = session.createConsumer(destination);
			consumer.setMessageListener(message -> System.out.println(((ConnectionInfo) ((ActiveMQMessage) message).getDataStructure()).getClientId()));
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
