package io.intino.konos.datalake;

import io.intino.konos.jms.Consumer;
import io.intino.konos.jms.MessageFactory;
import io.intino.konos.jms.TopicConsumer;
import io.intino.konos.jms.TopicProducer;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.ArrayList;
import java.util.List;

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

	public void start() {
		try {
			connection = new ActiveMQConnectionFactory(url).createConnection(user, password);
			if (clientID != null && !clientID.isEmpty()) connection.setClientID(this.clientID);
			connection.start();
			this.session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
		} catch (JMSException e) {
			getGlobal().log(SEVERE, e.getMessage(), e);
		}
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
		return session;
	}

	public javax.jms.Connection connection() {
		return connection;
	}

	public TopicProducer newProducer(String path) {
		if (this.session() == null) {
			getGlobal().log(SEVERE, "Session is null");
			return null;
		}
		try {
			return new TopicProducer(session(), path);
		} catch (JMSException e) {
			getGlobal().severe(e.getMessage());
			return null;
		}
	}

	public void send(String path, io.intino.ness.inl.Message message) {
		newProducer(path).produce(MessageFactory.createMessageFor(message.toString()));
	}

	public void register(String path, io.intino.ness.inl.Message message) {
		try {
			final TextMessage jmsMessage = (TextMessage) MessageFactory.createMessageFor(message.toString());
			if (jmsMessage == null) return;
			jmsMessage.setBooleanProperty(REGISTER_ONLY, true);
			newProducer(path).produce(jmsMessage);
		} catch (JMSException ignored) {
		}
	}

	public TopicConsumer registerConsumer(String path, Consumer consumer) {
		if (this.session() == null) getGlobal().log(SEVERE, "Session is null");
		TopicConsumer topicConsumer = new TopicConsumer(this.session(), path);
		topicConsumer.listen(consumer);
		return topicConsumer;
	}

	public TopicConsumer registerConsumer(String path, Consumer consumer, String subscriberID) {
		if (this.session() == null) getGlobal().log(SEVERE, "Session is null");
		TopicConsumer topicConsumer = new TopicConsumer(this.session(), path);
		topicConsumer.listen(consumer, subscriberID);
		return topicConsumer;
	}

	public List<String> topics() {
		List<String> topics = new ArrayList<>();
		try {
			for (ActiveMQTopic topic : ((ActiveMQConnection) connection).getDestinationSource().getTopics())
				topics.add(topic.getTopicName());
		} catch (JMSException e) {
		}
		return topics;
	}

	public void closeSession() {
		try {
			session.close();
		} catch (JMSException e) {
			getGlobal().log(SEVERE, e.getMessage(), e);
		}
	}
}
