package io.intino.alexandria.nessaccesor.jms;

import io.intino.alexandria.jms.TopicConsumer;
import io.intino.alexandria.jms.TopicProducer;
import io.intino.alexandria.nessaccesor.NessAccessor;
import io.intino.alexandria.nessaccesor.ReflowConfiguration;
import io.intino.alexandria.nessaccesor.ReflowDispatcher;
import io.intino.alexandria.nessaccesor.fs.FSEventStore;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static io.intino.alexandria.jms.Consumer.textFrom;
import static io.intino.alexandria.jms.MessageFactory.createMessageFor;
import static io.intino.ness.inl.Message.load;
import static java.lang.Thread.sleep;
import static java.util.Objects.requireNonNull;

public class JMSEventStore implements NessAccessor.EventStore {
	private static Logger logger = LoggerFactory.getLogger(JMSEventStore.class);
	private final String ADMIN_PATH = "service.ness.admin";
	private final String url;
	private final String user;
	private final String password;
	private final String clientID;

	private javax.jms.Session session;
	private Connection connection;
	private Map<String, TopicProducer> topicProducers = new HashMap<>();
	private List<String> registeredTanks = new ArrayList<>();

	public JMSEventStore(String url, String user, String password, String clientID) {
		this.url = url;
		this.user = user;
		this.password = password;
		this.clientID = clientID;
	}

	public void connect(String... args) {
		try {
			if (session != null && !((ActiveMQSession) session).isClosed()) return;
			for (TopicProducer topicProducer : topicProducers.values()) topicProducer.close();
			createConnection();
			final boolean transacted = args.length > 0 && args[0].equalsIgnoreCase("transacted");
			this.session = connection.createSession(transacted, transacted ? javax.jms.Session.SESSION_TRANSACTED : javax.jms.Session.AUTO_ACKNOWLEDGE);
			if (registeredTanks == null || registeredTanks.isEmpty()) this.registeredTanks = registeredTanks();
		} catch (JMSException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public boolean isConnected() {
		return (session != null && !((ActiveMQSession) session).isClosed());
	}

	@SuppressWarnings("ConstantConditions")
	private List<String> registeredTanks() {
		TopicProducer producer = newProducer(ADMIN_PATH);
		final String tanks = requestResponseWithTimeout(producer, createMessageFor("tanks"), 1000);
		return tanks == null || tanks.isEmpty() ? Collections.emptyList() : Arrays.asList(tanks.split(";"));
	}

	public List<User> users() {
		TopicProducer producer = newProducer(ADMIN_PATH);
		final String users = requestResponseWithTimeout(producer, createMessageFor("users"), 1000);
		return users == null || users.isEmpty() ? Collections.emptyList() : Arrays.stream(users.split(";")).map(u -> new User(u, null)).collect(Collectors.toList());
	}

	@Override
	public void append(io.intino.ness.inl.Message message) {
		String tank = registeredTanks.stream().filter(t -> t.toLowerCase().endsWith(message.type().toLowerCase())).findFirst().orElse(null);
		if (tank == null) {
			logger.error("Tank not found " + message.type());
			return;
		}
		add(tank).put(message);
	}

	public void batch(String tank, int blockSize) {
		TopicProducer producer = newProducer(ADMIN_PATH);
		producer.produce(createMessageFor("batch:" + tank + ":" + blockSize));
	}

	public void endBatch(String tank) {
		TopicProducer producer = newProducer(ADMIN_PATH);
		producer.produce(createMessageFor("endBatch:" + tank));
	}

	public Session session() {
		if (session == null || ((ActiveMQSession) session).isClosed()) connect();
		return session;
	}


	public EventSession createEventSession() {
		return null;
	}

	public ReflowSession reflow(ReflowConfiguration reflow, ReflowDispatcher dispatcher) {
		TopicProducer producer = newProducer(REFLOW_PATH);
		String quickURL = tryWithQuickReflow(producer);
		if (quickURL != null && new File(quickURL.replace("file://", "")).exists())
			return requestResponse(producer, requireNonNull(createMessageFor("startQuickReflow"))).equalsIgnoreCase("ack") ?
					fsReflow(reflow, dispatcher, producer, quickURL) : null;
		else return reflow(reflow, dispatcher, producer);
	}

	private ReflowSession fsReflow(ReflowConfiguration reflow, ReflowDispatcher dispatcher, TopicProducer producer, String quickURL) {
		final FSEventStore fsDatalake = new FSEventStore(quickURL);
		dispatcher.tanks().forEach(t -> fsDatalake.add(t.name()));
		return fsDatalake.reflow(reflow, dispatcher, () -> producer.produce(createMessageFor("finish")));
	}

	private ReflowSession reflow(ReflowConfiguration reflow, ReflowDispatcher dispatcher, TopicProducer producer) {
		producer.produce(createMessageFor(reflow));
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

			public void play() {
				topicConsumer.listen((m) -> consume(dispatcher, m));
			}

			public void pause() {
				topicConsumer.stop();
			}
		};
	}

	private String tryWithQuickReflow(TopicProducer producer) {
		return requestResponse(producer, createMessageFor("quickReflow"));

	}

	public void commit() {
		try {
			session.commit();
		} catch (JMSException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private String requestResponse(TopicProducer producer, Message message) {
		try {
			message.setJMSReplyTo(this.session.createTemporaryQueue());
			producer.produce(message);
			Message response = session.createConsumer(message.getJMSReplyTo()).receive(1000);
			return response == null ? "" : textFrom(response);
		} catch (JMSException e) {
			logger.error(e.getMessage(), e);
			return "";
		}
	}

	private String requestResponseWithTimeout(TopicProducer producer, Message message, int timeout) {
		try {
			message.setJMSReplyTo(this.session.createTemporaryQueue());
			producer.produce(message);
			if (session.getTransacted()) session.commit();
			Message receive = session.createConsumer(message.getJMSReplyTo()).receive(timeout);
			return receive == null ? "" : textFrom(receive);
		} catch (JMSException e) {
			logger.error(e.getMessage(), e);
			return "";
		}
	}

	public Tank add(String tank) {
		if (!registeredTanks.contains(tank)) logger.warn("Tank " + tank + " is not registered in datalake");
		return new JMSTank(tank, this);
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
		if (this.session() == null) return null;
		try {
			if (!topicProducers.containsKey(tank) || topicProducers.get(tank).isClosed())
				topicProducers.put(tank, new TopicProducer(session, tank));
			return topicProducers.get(tank);
		} catch (JMSException e) {
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
