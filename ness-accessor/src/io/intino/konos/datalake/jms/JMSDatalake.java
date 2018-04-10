package io.intino.konos.datalake.jms;

import io.intino.konos.datalake.Datalake;
import io.intino.konos.datalake.Reflow;
import io.intino.konos.datalake.ReflowDispatcher;
import io.intino.konos.jms.TopicConsumer;
import io.intino.konos.jms.TopicProducer;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static io.intino.konos.jms.Consumer.textFrom;
import static io.intino.konos.jms.MessageFactory.createMessageFor;
import static io.intino.ness.inl.Message.load;
import static java.lang.Thread.sleep;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;

public class JMSDatalake implements Datalake {
	private static Logger logger = LoggerFactory.getLogger(JMSDatalake.class);

	private final String url;
	private final String user;
	private final String password;
	private final String clientID;

	private Session session;
	private Connection connection;
	private Map<String, TopicProducer> topicProducers = new HashMap<>();


	public JMSDatalake(String url, String user, String password, String clientID) {
		this.url = url;
		this.user = user;
		this.password = password;
		this.clientID = clientID;
	}

	public void connect(String... args) {
		try {
			if (session != null && !((ActiveMQSession) session).isClosed()) return;
			createConnection();
			final boolean transacted = args.length > 0 && args[0].equalsIgnoreCase("transacted");
			this.session = connection.createSession(transacted, transacted ? Session.SESSION_TRANSACTED : Session.AUTO_ACKNOWLEDGE);
		} catch (JMSException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public javax.jms.Session session() {
		if (session == null || ((ActiveMQSession) session).isClosed()) connect();
		return session;
	}

	@Override
	public ReflowSession reflow(int blockSize, ReflowDispatcher dispatcher, Instant from, Tank... tanks) {
		return reflow(blockSize, dispatcher, from, stream(tanks).map(Tank::name).toArray(String[]::new));
	}

	@Override
	public void commit() {
		try {
			session.commit();
		} catch (JMSException e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void add(String tank) {

	}

	private ReflowSession reflow(int blockSize, ReflowDispatcher dispatcher, Instant from, String... tanks) {
		TopicProducer producer = newProducer(REFLOW_PATH);
		producer.produce(createMessageFor(new Reflow().blockSize(blockSize).from(from).tanks(asList(tanks))));
		waitUntilReflowSession();
		TopicConsumer topicConsumer = new TopicConsumer(session, FLOW_PATH);
		topicConsumer.listen(m -> consume(dispatcher, m), "consumer-" + FLOW_PATH);
		return new ReflowSession() {
			public void next() {
				final TopicProducer topicProducer = newProducer(REFLOW_PATH);
				topicProducer.produce(createMessageFor("next"));
				topicProducer.close();
			}

			public void finish() {
				final TopicProducer topicProducer = newProducer(REFLOW_PATH);
				topicProducer.produce(createMessageFor("finish"));
				topicProducer.close();
				topicConsumer.stop();
			}

			@Override
			public void play() {
				topicConsumer.listen((m) -> consume(dispatcher, m));
			}

			@Override
			public void pause() {
				topicConsumer.stop();
			}

		};
	}

	public void disconnect() {
		try {
			for (TopicProducer topicProducer : topicProducers.values()) topicProducer.close();
			if (session != null) {
				session.close();
				session = null;
			}
			if (connection != null) {
				connection.close();
				connection = null;
			}
		} catch (JMSException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public javax.jms.Connection connection() {
		return connection;
	}

	public void closeSession() {
		try {
			if (session != null) session.close();
		} catch (JMSException e) {
			logger.error(e.getMessage(), e);
		}
	}

	TopicProducer newProducer(String tank) {
		if (this.session() == null) {
			logger.error("Session is null");
			return null;
		}
		try {
			if (!topicProducers.containsKey(tank) || topicProducers.get(tank).isClosed())
				topicProducers.put(tank, new TopicProducer(session, tank));
			return topicProducers.get(tank);
		} catch (JMSException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	private void waitUntilReflowSession() {
		try {
			disconnect();
			sleep(40 * 1000);
			connect();
		} catch (InterruptedException ignored) {
		}
	}

	private void createConnection() throws JMSException {
		connection = new ActiveMQConnectionFactory(url).createConnection(user, password);
		if (clientID != null && !clientID.isEmpty()) connection.setClientID(this.clientID);
		connection.start();
	}

	private void consume(ReflowDispatcher dispatcher, javax.jms.Message m) {
		dispatcher.dispatch(load(textFrom(m)));
	}
}
