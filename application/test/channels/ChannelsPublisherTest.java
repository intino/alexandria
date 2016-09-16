package channels;

import channels.schemas.Processes;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

public class ChannelsPublisherTest {


	public static void main(String[] args) throws JMSException {
		Connection connection = new ActiveMQConnectionFactory("happysense.sumus", "happysense.sumus", "tcp://bus.siani.es:61616").createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Channels.publishOutCatalogs(session, true);
		Channels.publishOutProcesses(session, new Processes().label("sample").name("namesample"));
		session.close();
		connection.close();
	}
}
