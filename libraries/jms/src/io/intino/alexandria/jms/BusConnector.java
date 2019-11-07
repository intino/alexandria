package io.intino.alexandria.jms;

import io.intino.alexandria.logger.Logger;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.JMSException;

public class BusConnector {
	static {
		io.intino.alexandria.logger4j.Logger.init();
	}

	public static Connection createConnection(String brokerURL, String user, String password, ConnectionListener listener) {
		try {
			ActiveMQConnection connection = (ActiveMQConnection) new ActiveMQConnectionFactory(user, password, brokerURL).createConnection();
			connection.addTransportListener(listener);
			return connection;
		} catch (JMSException e) {
			Logger.error(e);
			return null;
		}
	}
}
