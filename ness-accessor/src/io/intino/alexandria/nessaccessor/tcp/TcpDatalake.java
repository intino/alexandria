package io.intino.alexandria.nessaccessor.tcp;

import io.intino.alexandria.inl.Message;
import io.intino.alexandria.jms.MessageFactory;
import io.intino.alexandria.jms.TopicProducer;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.nessaccessor.MessageTranslator;
import io.intino.alexandria.zim.ZimReader;
import io.intino.alexandria.zim.ZimStream;
import io.intino.ness.core.Blob;
import io.intino.ness.core.Datalake;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;

import javax.jms.JMSException;
import javax.jms.Session;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static io.intino.ness.core.Blob.Type.event;
import static javax.jms.Session.AUTO_ACKNOWLEDGE;

public class TcpDatalake implements Datalake {
	private static final String SERVICE_NESS_ADMIN = "service.ness.admin";
	private final Connection connection;
	private final Session session;
	private final Map<String, TopicProducer> producers = new HashMap<>();

	public TcpDatalake(String uri, String username, String password, String clientId) {
		this.connection = new Connection(uri, username, password, clientId, onClose());
		this.session = this.connection.session;
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
	public void seal() {
		producer(SERVICE_NESS_ADMIN).produce(MessageFactory.createMessageFor("seal"));
	}

	@Override
	public void push(Stream<Blob> blobs) {
		blobs.filter(b -> b.type().equals(event)).forEach(b -> send(read(b), b.name()));
	}

	private void send(ZimStream stream, String blob) {
		while (stream.hasNext()) send(stream.next(), putTopicOf(blob));
	}

	private String putTopicOf(String name) {
		return "put." + tankName(name);
	}

	private String tankName(String name) {
		return name.substring(0, name.indexOf("-"));
	}

	private void send(Message message, String topic) {
		if (connection.session == null || ((ActiveMQSession) session).isClosed()) {
			Logger.error("Session closed");
			return;
		}
		producer(topic).produce(MessageTranslator.fromInlMessage(message));
	}

	private ZimStream read(Blob b) {
		return new ZimReader(b.inputStream());
	}

	private CallBack onClose() {
		return () -> producers.values().forEach(p -> {
			if (!p.isClosed()) p.close();
		});
	}

	private TopicProducer producer(String topic) {
		try {
			if (this.producers.containsKey(topic) && !this.producers.get(topic).isClosed()) return this.producers.get(topic);
			this.producers.put(topic, new TopicProducer(session, topic));
			return producers.get(topic);
		} catch (JMSException e) {
			Logger.error(e);
			return null;
		}
	}

	public static class Connection implements Datalake.Connection {
		private final String uri;
		private final String username;
		private final String password;
		private final String clientId;
		private Session session;
		private final TcpDatalake.CallBack onClose;

		Connection(String uri, String username, String password, String clientId, CallBack onClose) {
			this.uri = uri;
			this.username = username;
			this.password = password;
			this.clientId = clientId;
			this.onClose = onClose;
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

		public Session session() {
			return session;
		}

		@Override
		public void disconnect() {
			if (session != null && !((ActiveMQSession) session).isClosed()) {
				try {
					onClose.execute();
					session.close();
				} catch (JMSException e) {
					Logger.error(e);
				}
			}
		}
	}

	private interface CallBack {
		void execute();
	}
}