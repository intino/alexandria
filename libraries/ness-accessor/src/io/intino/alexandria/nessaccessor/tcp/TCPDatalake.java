package io.intino.alexandria.nessaccessor.tcp;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.zim.ZimReader;
import io.intino.alexandria.zim.ZimStream;
import io.intino.ness.core.Blob;
import io.intino.ness.core.Datalake;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;

import javax.jms.JMSException;
import javax.jms.Session;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;

import static io.intino.ness.core.Blob.Type.event;
import static javax.jms.Session.AUTO_ACKNOWLEDGE;

public class TCPDatalake implements Datalake {
	private final Connection connection;
	private TCPEventStore tcpEventStore;

	public TCPDatalake(String uri, String username, String password, String clientId) {
		this.connection = new Connection(uri, username, password, clientId, onOpen(), onClose());
		this.tcpEventStore = new TCPEventStore(connection);
	}

	@Override
	public Datalake.Connection connection() {
		return connection;
	}

	@Override
	public EventStore eventStore() {
		return tcpEventStore;
	}

	@Override
	public SetStore setStore() {
		return null;
	}

	@Override
	public void seal() {
		this.tcpEventStore.seal();
	}

	@Override
	public void push(Stream<Blob> blobs) {
		blobs.filter(b -> b.type().equals(event)).forEach(b -> tcpEventStore.put(read(b), b.name()));
	}

	private CallBack onOpen() {
		return () -> tcpEventStore.open();
	}

	private CallBack onClose() {
		return () -> tcpEventStore.producers().forEach(p -> {
			if (!p.isClosed()) p.close();
		});
	}

	private ZimStream read(Blob b) {
		try {
			return new ZimReader(new GZIPInputStream(b.inputStream()));
		} catch (IOException e) {
			Logger.error(e);
			return new ZimReader(new ByteArrayInputStream(new byte[0]));
		}
	}

	private interface CallBack {
		void execute();
	}

	public static class Connection implements Datalake.Connection {
		private final String uri;
		private final String username;
		private final String password;
		private final String clientId;
		private final CallBack onOpen;
		private final TCPDatalake.CallBack onClose;
		private final ActiveMQConnectionFactory factory;
		private Session session;
		private String[] args;
		private javax.jms.Connection connection;

		Connection(String uri, String username, String password, String clientId, CallBack onOpen, CallBack onClose) {
			this.uri = uri;
			this.username = username;
			this.password = password;
			this.clientId = clientId;
			this.onOpen = onOpen;
			this.onClose = onClose;
			factory = new ActiveMQConnectionFactory(uri);
		}

		@Override
		public void connect(String... args) {
			this.args = args;
			try {
				if (connection != null) connection.close();
				connection = factory.createConnection(username, password);
				if (clientId != null) connection.setClientID(clientId);
				this.session = createSession(args.length > 0 ? args[0] : "");
				this.onOpen.execute();
				Logger.warn("Established new connection");
			} catch (Throwable e) {
				Logger.error(e);
			}
		}

		private Session createSession(String arg) throws JMSException {
			connection.start();
			return connection.createSession(arg != null && arg.equals("Transacted"), AUTO_ACKNOWLEDGE);
		}

		public Session session() {
			if (!((ActiveMQConnection) connection).isStarted() || session == null || !((ActiveMQSession) session).isRunning() || ((ActiveMQSession) session).isClosed()) {
				Logger.warn("Connection lost");
				connect(args);
			}
			return session;
		}

		@Override
		public void disconnect() {
			if (session != null && !((ActiveMQSession) session).isClosed()) {
				try {
					onClose.execute();
					session.close();
					connection.close();
				} catch (Throwable e) {
					Logger.error(e);
				}
			}
		}
	}
}