package channels.service;

import channels.MonetJMSService;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

public class ChannelsServerTest {
	public static void main(String[] args) throws JMSException {
		Connection connection = new ActiveMQConnectionFactory("happysense.sumus", "happysense.sumus", "tcp://bus.siani.es:61616").createConnection();
		connection.start();
		final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MonetJMSService.init(session, null);
		session.close();
		connection.close();
	}
}
