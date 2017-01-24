package io.intino.konos.jms;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.JMSException;

public class BusConnector {


	public static Connection createConnection(String brokerURL, String user, String password) {
		try {
			return new ActiveMQConnectionFactory(user, password, brokerURL).createConnection();
		} catch (JMSException e) {
			e.printStackTrace();
			return null;
		}
	}
}
