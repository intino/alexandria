package io.intino.konos.datalake.jms;

import io.intino.konos.datalake.Datalake;
import io.intino.konos.datalake.Ness;
import io.intino.konos.jms.TopicConsumer;
import io.intino.konos.jms.TopicProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import static io.intino.konos.datalake.Datalake.REGISTER_ONLY;
import static io.intino.konos.jms.MessageFactory.createMessageFor;

public class JMSTank implements Datalake.Tank {
	private static Logger logger = LoggerFactory.getLogger(JMSTank.class);

	private String name;
	private JMSDatalake datalake;
	private TopicConsumer flow;

	public JMSTank(String name, JMSDatalake datalake) {
		this.name = name;
		this.datalake = datalake;
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
		final TopicProducer producer = datalake.newProducer(feedChannel());
		if (producer != null) producer.produce(createMessageFor(message.toString()));
	}

	public void drop(io.intino.ness.inl.Message message) {
		try {
			final TextMessage jmsMessage = (TextMessage) createMessageFor(message.toString());
			if (jmsMessage == null) return;
			jmsMessage.setBooleanProperty(REGISTER_ONLY, true);
			final TopicProducer producer = datalake.newProducer(dropChannel());
			if (producer != null) producer.produce(jmsMessage);
		} catch (JMSException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public TopicConsumer flow(Ness.TankFlow flow) {
		if (datalake.session() == null) logger.error("Session is null");
		this.flow = new TopicConsumer(datalake.session(), flowChannel());
		this.flow.listen(flow);
		return this.flow;
	}

	public TopicConsumer flow(Ness.TankFlow flow, String flowID) {
		if (datalake.session() == null) logger.error("Session is null");
		this.flow = new TopicConsumer(datalake.session(), flowChannel());
		if (flowID != null) this.flow.listen(flow, flowID);
		else this.flow.listen(flow);
		return this.flow;
	}

	public void unregister() {
		if (flow != null) flow.stop();
		flow = null;
	}
}
