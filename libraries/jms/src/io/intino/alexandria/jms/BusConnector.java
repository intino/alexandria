package io.intino.alexandria.jms;

import io.intino.alexandria.logger.Logger;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.JMSException;

public class BusConnector {

	public static Connection createConnection(String brokerURL, String user, String password) {
		try {
			return new ActiveMQConnectionFactory(user, password, brokerURL).createConnection();
		} catch (JMSException e) {
			Logger.error(e.getMessage(), e);
			return null;
		}
	}
}
