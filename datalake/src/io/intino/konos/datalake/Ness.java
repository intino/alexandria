package io.intino.konos.datalake;

import io.intino.konos.jms.Consumer;
import io.intino.konos.jms.TopicConsumer;
import io.intino.konos.jms.TopicProducer;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.command.ActiveMQTopic;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.ArrayList;
import java.util.List;

import static io.intino.konos.jms.MessageFactory.createMessageFor;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getGlobal;

public class Ness {

	public static final String REGISTER_ONLY = "registerOnly";
	private final String url;
	private final String user;
	private final String password;
	private String clientID;
	private Session session;
	private Connection connection;


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
			getGlobal().log(SEVERE, e.getMessage(), e);
			return null;
		}
	}

	public List<String> tanks() {
		List<String> topics = new ArrayList<>();
		try {
			for (ActiveMQTopic topic : ((ActiveMQConnection) connection).getDestinationSource().getTopics())
				topics.add(topic.getTopicName());
		} catch (JMSException e) {
		}
		return topics;
	}

	public Tank tank(String tank) {
		return new Tank(tank);
	}

	public void stop() {
		try {
			session.close();
			session = null;
			connection.close();
			connection = null;
		} catch (JMSException e) {
			getGlobal().log(SEVERE, e.getMessage(), e);
		}
	}

	public javax.jms.Session session() {
		return ((ActiveMQSession) session).isClosed() ? start() : session;
	}

	public javax.jms.Connection connection() {
		return connection;
	}

	public void closeSession() {
		try {
			if (session != null) session.close();
		} catch (JMSException e) {
			getGlobal().log(SEVERE, e.getMessage(), e);
		}
	}

	private TopicProducer newProducer(String tank) {
		if (this.session() == null) {
			getGlobal().log(SEVERE, "Session is null");
			return null;
		}
		try {
			return new TopicProducer(session(), tank);
		} catch (JMSException e) {
			getGlobal().severe(e.getMessage());
			return null;
		}
	}

	public class Tank {
		private String tank;

		Tank(String tank) {
			this.tank = tank;
		}

		public void feed(io.intino.ness.inl.Message message) {
			final TopicProducer producer = newProducer(tank);
			if (producer != null) producer.produce(createMessageFor(message.toString()));
		}

		public void drop(io.intino.ness.inl.Message message) {
			try {
				final TextMessage jmsMessage = (TextMessage) createMessageFor(message.toString());
				if (jmsMessage == null) return;
				jmsMessage.setBooleanProperty(REGISTER_ONLY, true);
				final TopicProducer producer = newProducer(tank);
				if (producer != null) producer.produce(jmsMessage);
			} catch (JMSException ignored) {
			}
		}

		public TopicConsumer sink(Sink sink) {
			if (session() == null) getGlobal().log(SEVERE, "Session is null");
			TopicConsumer topicConsumer = new TopicConsumer(session(), tank);
			topicConsumer.listen(sink);
			return topicConsumer;
		}

		public TopicConsumer sink(Sink sink, String sinkID) {
			if (session() == null) getGlobal().log(SEVERE, "Session is null");
			TopicConsumer topicConsumer = new TopicConsumer(session(), tank);
			topicConsumer.listen(sink, sinkID);
			return topicConsumer;
		}
	}

	public interface Sink extends Consumer {

	}
}
