package io.intino.konos.datalake;

import io.intino.konos.jms.Consumer;
import io.intino.konos.jms.TopicConsumer;
import io.intino.konos.jms.TopicProducer;
import io.intino.ness.konos.Main;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static java.util.logging.Logger.getGlobal;

public class Ness {

	private final Thread thread;
	private Session session;
	private Connection connection;


	public Ness(File workingDirectory, String nessieToken) {
		this(workingDirectory, nessieToken, 61616);
	}

	public Ness(File workingDirectory, String nessieToken, int busPort) {
		thread = new Thread(() -> Main.main(new String[]{
				"graph.store=" + new File(workingDirectory, "store"),
				"nessie.token=" + nessieToken,
				"ness.rootPath=" + new File(workingDirectory, "ness"),
				"broker.port=" + busPort,
				"broker.store=" + new File(workingDirectory, "broker").getAbsolutePath()
		}));
	}

	public void start() {
		try {
			thread.join();
			connection = new ActiveMQConnectionFactory("vm://ness").createConnection("ness", "ness");
			connection.start();
			this.session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
		} catch (JMSException e) {
			getGlobal().log(Level.SEVERE, e.getMessage(), e);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		thread.interrupt();
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
			getGlobal().log(Level.SEVERE, e.getMessage(), e);
		}
	}
}
