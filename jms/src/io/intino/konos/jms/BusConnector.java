package io.intino.konos.jms;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.JMSException;

import static org.slf4j.Logger.ROOT_LOGGER_NAME;

public class BusConnector {

	public static Connection createConnection(String brokerURL, String user, String password) {
		try {
			return new ActiveMQConnectionFactory(user, password, brokerURL).createConnection();
		} catch (JMSException e) {
			LoggerFactory.getLogger(ROOT_LOGGER_NAME).error(e.getMessage(), e);
			return null;
		}
	}
}
