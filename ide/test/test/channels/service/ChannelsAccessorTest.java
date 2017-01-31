package channels.service;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

public class ChannelsAccessorTest {


	public static void main(String[] args) throws JMSException {
		Connection connection = new ActiveMQConnectionFactory("happysense.sumus", "happysense.sumus", "tcp://bus.siani.es:61616").createConnection();
		connection.start();
		final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		runAccessor(session);
		session.close();
		connection.close();
	}


	private static void runAccessor(Session session) {
		//		MonetJMSAccessor producer = new MonetJMSAccessor(session);
//		producer.processes(value -> {
//			System.out.println(value.name());
//			System.out.println(value.label());
//			try {
//				session.close();
//				connection.close();
//			} catch (JMSException e) {
//				e.printStackTrace();
//			}
//		});
	}
}
