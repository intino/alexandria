package io.intino.konos.datalake;

import io.intino.konos.jms.Consumer;
import io.intino.konos.jms.TopicConsumer;
import io.intino.konos.jms.TopicProducer;
import io.intino.ness.inl.Message;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static io.intino.konos.jms.Consumer.textFrom;
import static io.intino.konos.jms.MessageFactory.createMessageFor;
import static io.intino.ness.inl.Message.load;
import static java.lang.Thread.sleep;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;

public class Ness {
	private static Logger logger = LoggerFactory.getLogger(Ness.class);
	private static final String REFLOW_PATH = "service.ness.reflow";
	private static final String FLOW_PATH = "flow.ness.reflow";

	public static final String REGISTER_ONLY = "registerOnly";
	private final String url;
	private final String user;
	private final String password;
	private String clientID;
	private Session session;
	private Connection connection;
	private Instant lastMessage;
	private int receivedMessages = 0;
	Map<String, TopicProducer> tankProducers = new HashMap<>();

	public Ness(String url, String user, String password, String clientID) {
		this.url = url;
		this.user = user;
		this.password = password;
		this.clientID = clientID;
	}

	public Session start() {
		try {
			createConnection();
			this.session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
			return this.session;
		} catch (JMSException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	public Session startTransacted() {
		try {
			createConnection();
			this.session = connection.createSession(true, Session.SESSION_TRANSACTED);
			return this.session;
		} catch (JMSException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	private void createConnection() throws JMSException {
		connection = new ActiveMQConnectionFactory(url).createConnection(user, password);
		if (clientID != null && !clientID.isEmpty()) connection.setClientID(this.clientID);
		connection.start();
	}

	public Tank tank(String tank) {
		return new Tank(tank);
	}

	public ReflowSession reflow(int blockSize, MessageDispatcher dispatcher, Tank... tanks) {
		return reflow(blockSize, dispatcher, Instant.MIN, stream(tanks).map(t -> t.name).toArray(String[]::new));
	}

	public ReflowSession reflow(int blockSize, MessageDispatcher dispatcher, Instant from, Tank... tanks) {
		return reflow(blockSize, dispatcher, from, stream(tanks).map(t -> t.name).toArray(String[]::new));
	}

	public Ness.ReflowSession reflow(int blockSize, MessageDispatcher dispatcher, Instant from, String... tanks) {
		TopicProducer producer = newProducer(REFLOW_PATH);
		producer.produce(createMessageFor(new Reflow().blockSize(blockSize).from(from).tanks(asList(tanks))));
		waitUntilReflowSession();
		TopicConsumer topicConsumer = new TopicConsumer(session, FLOW_PATH);
		topicConsumer.listen((m) -> consume(dispatcher, m), "consumer-" + FLOW_PATH);
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


	private void consume(MessageDispatcher dispatcher, javax.jms.Message m) {
		dispatcher.dispatch(load(textFrom(m)));
	}

	private void waitUntilReflowSession() {
		try {
			stop();
			sleep(40000);
			start();
		} catch (InterruptedException e) {
		}
	}

	public void stop() {
		try {
			for (TopicProducer topicProducer : tankProducers.values()) topicProducer.close();
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

	public javax.jms.Session session() {
		return session == null || ((ActiveMQSession) session).isClosed() ? start() : session;
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

	private TopicProducer newProducer(String tank) {
		if (this.session() == null) {
			logger.error("Session is null");
			return null;
		}
		try {
			if (!tankProducers.containsKey(tank) || tankProducers.get(tank).isClosed())
				tankProducers.put(tank, new TopicProducer(session, tank));
			return tankProducers.get(tank);
		} catch (JMSException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	public Instant lastMessage() {
		return lastMessage;
	}

	public void lastMessage(Instant lastMessage) {
		this.lastMessage = lastMessage;
		if (lastMessage != null) this.receivedMessages++;
	}

	public int receivedMessages() {
		return receivedMessages;
	}

	public void reset() {
		this.receivedMessages = 0;
	}

	public class Tank {
		private String name;
		private TopicConsumer flow;

		Tank(String name) {
			this.name = name;
		}

		public String name() {
			return name;
		}

		public String flowChannel() {
			return "flow." + name;
		}

		public String dropChannel() {
			return "drop." + name;
		}

		public String feedChannel() {
			return "feed." + name;
		}

		public void feed(io.intino.ness.inl.Message message) {
			final TopicProducer producer = newProducer(feedChannel());
			if (producer != null) producer.produce(createMessageFor(message.toString()));
		}

		public void drop(io.intino.ness.inl.Message message) {
			try {
				final TextMessage jmsMessage = (TextMessage) createMessageFor(message.toString());
				if (jmsMessage == null) return;
				jmsMessage.setBooleanProperty(REGISTER_ONLY, true);
				final TopicProducer producer = newProducer(dropChannel());
				if (producer != null) producer.produce(jmsMessage);
			} catch (JMSException e) {
				logger.error(e.getMessage(), e);
			}
		}

		public TopicConsumer flow(TankFlow flow) {
			if (session() == null) logger.error("Session is null");
			this.flow = new TopicConsumer(session(), flowChannel());
			this.flow.listen(flow);
			return this.flow;
		}

		public TopicConsumer flow(TankFlow flow, String flowID) {
			if (session() == null) logger.error("Session is null");
			this.flow = new TopicConsumer(session(), flowChannel());
			if (flowID != null) this.flow.listen(flow, flowID);
			else this.flow.listen(flow);
			return this.flow;
		}

		public void unregister() {
			if (flow != null) flow.stop();
			flow = null;
		}
	}

	public interface TankFlow extends Consumer {
		void consume(Message message);
	}

	public interface ReflowSession {

		void next();

		void finish();

		void play();

		void pause();
	}

}
