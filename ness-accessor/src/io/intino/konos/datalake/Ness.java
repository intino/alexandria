package io.intino.konos.datalake;

import io.intino.konos.jms.Consumer;
import io.intino.konos.jms.TopicConsumer;
import io.intino.konos.jms.TopicProducer;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.time.Instant;
import java.util.Arrays;

import static io.intino.konos.jms.MessageFactory.createMessageFor;
import static java.lang.Thread.sleep;
import static java.util.Arrays.asList;

public class Ness {
	private static Logger logger = LoggerFactory.getLogger(Ness.class);
	private static final String REFLOW_PATH = "service.ness.reflow";
	private static final String REFLOW_READY = "service.ness.reflow.ready";

	public static final String REGISTER_ONLY = "registerOnly";
	private final String url;
	private final String user;
	private final String password;
	private String clientID;
	private Session session;
	private Connection connection;
	private Instant lastMessage;
	private int receivedMessages = 0;
	private TopicConsumer topicConsumer;
	private MessageConsumer ackConsumer;

	public Ness(String url, String user, String password, String clientID) {
		this.url = url;
		this.user = user;
		this.password = password;
		this.clientID = clientID;
	}

	public Session start() {
		try {
			connection = new ActiveMQConnectionFactory(url).createConnection(user, password);
			if (clientID != null && !clientID.isEmpty()) connection.setClientID(this.clientID);
			connection.start();
			this.session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
			return this.session;
		} catch (JMSException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	public Tank tank(String tank) {
		return new Tank(tank);
	}

	public ReflowSession reflow(int blockSize, Tank... tanks) {
		return reflow(blockSize, Arrays.stream(tanks).map(t -> t.name).toArray(String[]::new));
	}

	public Ness.ReflowSession reflow(int blockSize, String... tanks) {
		try {
			TopicProducer producer = new TopicProducer(session, REFLOW_PATH);
			producer.produce(createMessageFor(new Reflow().blockSize(blockSize).tanks(asList(tanks))));
			waitUntilReflowSession();
			return new ReflowSession() {
				public void next() throws JMSException {
					new TopicProducer(session, REFLOW_PATH).produce(createMessageFor("next"));
				}

				public void finish() throws JMSException {
					new TopicProducer(session, REFLOW_PATH).produce(createMessageFor("finish"));
				}
			};
		} catch (JMSException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	private void waitUntilReflowSession() {
		try {
			boolean ack = false;
			while (!ack) {
				stop();
				sleep(30 * 1000);
				start();
				ack = ack();
			}
		} catch (InterruptedException ignored) {
			try {
				ackConsumer.close();
			} catch (JMSException e) {
			}
		}
	}

	private boolean ack() {
		try {
			Thread thread = Thread.currentThread();
			final Queue responseQueue = session.createQueue(REFLOW_READY);
			ackConsumer = session.createConsumer(responseQueue);
			ackConsumer.setMessageListener(message -> {
				logger.info("Application ready to reflow");
				thread.interrupt();
			});

			final MessageProducer producer = session.createProducer(session.createTopic(REFLOW_PATH));
			final Message messageFor = createMessageFor("ready?");
			messageFor.setJMSReplyTo(responseQueue);
			producer.send(messageFor);
			producer.close();
			sleep(5 * 1000);
			ackConsumer.close();
		} catch (JMSException e) {
			logger.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			return true;
		}
		return false;
	}

	public void stop() {
		try {
			ackConsumer = null;
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
			return new TopicProducer(session(), tank);
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
				if (producer != null) {
					producer.produce(jmsMessage);
					producer.close();
				}
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
			topicConsumer = new TopicConsumer(session(), flowChannel());
			topicConsumer.listen(flow, flowID);
			return topicConsumer;
		}

		public void unregister() {
			if (topicConsumer != null) topicConsumer.stop();
			topicConsumer = null;
		}
	}

	public interface TankFlow extends Consumer {

	}

	public interface ReflowSession {

		void next() throws JMSException;

		void finish() throws JMSException;
	}

}
