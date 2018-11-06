package io.intino.alexandria.nessaccessor.tcp;

import io.intino.alexandria.logger.Logger;
import io.intino.ness.core.Datalake;
import io.intino.ness.core.Stage;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;

import javax.jms.JMSException;
import javax.jms.Session;

import static javax.jms.Session.AUTO_ACKNOWLEDGE;

public class TcpDatalake implements Datalake {
	private final Connection connection;

	public TcpDatalake(String uri, String username, String password, String clientId) {
		this.connection = new Connection(uri, username, password, clientId);
	}

	@Override
	public Datalake.Connection connection() {
		return connection;
	}

	@Override
	public EventStore eventStore() {
		return new TCPEventStore(connection.session);
	}

	@Override
	public SetStore setStore() {
		return null;
	}

	@Override
	public void push(Stage stage) {

	}

	@Override
	public void seal() {

	}

	public static class Connection implements Datalake.Connection {
		private final String uri;
		private final String username;
		private final String password;
		private final String clientId;
		private Session session;

		public Connection(String uri, String username, String password, String clientId) {
			this.uri = uri;
			this.username = username;
			this.password = password;
			this.clientId = clientId;
		}

		@Override
		public void connect(String... args) {
			session = createSession(args[0]);
		}

		private Session createSession(String arg) {
			try {
				ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(uri);
				javax.jms.Connection connection = connectionFactory.createConnection(username, password);
				connection.start();

				return connection.createSession(arg != null && arg.equals("Transacted"), AUTO_ACKNOWLEDGE);
			} catch (JMSException e) {
				Logger.error(e);
				return null;
			}
		}

		@Override
		public void disconnect() {
			if (session != null && !((ActiveMQSession) session).isClosed()) {
				try {
					session.close();
				} catch (JMSException e) {
					Logger.error(e);
				}
			}
		}

		public Session session() {
			return session;
		}
	}
}
