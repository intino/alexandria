package io.intino.alexandria.jms;

import io.intino.alexandria.logger.Logger;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSslConnectionFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import java.io.File;
import java.util.Arrays;

public class BrokerConnector {

	public static Connection createConnection(String url, ConnectionConfig config, ConnectionListener connectionListener) {
		if (config.hasSSlCredentials()) return createSSLConnection(url, config, connectionListener);
		return createPlainConnection(url, config, connectionListener);
	}

	private static Connection createPlainConnection(String brokerURL, ConnectionConfig config, ConnectionListener listener) {
		try {
			ActiveMQConnection connection = (ActiveMQConnection) new ActiveMQConnectionFactory(config.user(), config.password(), brokerURL).createConnection();
			connection.addTransportListener(listener);
			return connection;
		} catch (JMSException e) {
			Logger.error(e);
			return null;
		}
	}

	private static Connection createSSLConnection(String brokerURL, ConnectionConfig config, ConnectionListener listener) {
		try {
			ActiveMQSslConnectionFactory factory = new ActiveMQSslConnectionFactory(brokerURL);
			factory.setKeyStore(new File("../temp/datahub/client.jks").getAbsolutePath());
			factory.setTrustStore(new File("../temp/datahub/client.jts").getAbsolutePath());
			factory.setKeyStorePassword(Arrays.toString(config.keyStorePassword()));
			factory.setTrustStorePassword(Arrays.toString(config.trustStorePassword()));
			factory.setUserName(config.user());
			factory.setPassword(config.password());
			ActiveMQConnection connection = (ActiveMQConnection) factory.createConnection();
			connection.addTransportListener(listener);
			return connection;
		} catch (Exception e) {
			Logger.error(e);
			return null;
		}
	}
}
