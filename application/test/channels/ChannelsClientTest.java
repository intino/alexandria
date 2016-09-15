package channels;

import channels_.accessor.AmidasJMSAccessor;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

public class ChannelsClientTest {


	//CLIENT
	public static void main(String[] args) throws JMSException {
		Connection connection = new ActiveMQConnectionFactory("happysense.sumus", "happysense.sumus", "tcp://bus.siani.es:61616").createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		AmidasJMSAccessor producer = new AmidasJMSAccessor(session);
		producer.processes(value -> {
			System.out.println(value.name());
			System.out.println(value.label());
			try {
				session.close();
				connection.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		});

	}
}
