package io.intino.konos.datalake;

import io.intino.konos.jms.Consumer;
import io.intino.konos.jms.TopicConsumer;
import io.intino.konos.jms.TopicProducer;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;
import java.util.ArrayList;
import java.util.List;

import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getGlobal;

public class Ness {

	private final String url;
	private final String user;
	private final String password;
	private Session session;
	private Connection connection;


	public Ness(String url, String user, String password) {
		this.url = url;
		this.user = user;
		this.password = password;
	}

	public void start() {
		try {
			connection = new ActiveMQConnectionFactory(url).createConnection(user, password);
			connection.start();
			this.session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
		} catch (JMSException e) {
			getGlobal().log(SEVERE, e.getMessage(), e);
		}
	}

	public void stop() {
		try {
			session.close();
			connection.close();
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

	public TopicProducer registerProducer(String path) {
		try {
			return new TopicProducer(session(), path);
		} catch (JMSException e) {
			getGlobal().severe(e.getMessage());
			return null;
		}
	}

	public TopicConsumer registerConsumer(String path, Consumer consumer) {
		TopicConsumer topicConsumer = new TopicConsumer(this.session(), path);
		topicConsumer.listen(consumer);
		return topicConsumer;
	}

	public TopicConsumer registerConsumer(String path, Consumer consumer, String subscriberID) {
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
