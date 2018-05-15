package io.intino.konos.datalake.jms;

import io.intino.konos.datalake.Datalake;
import io.intino.konos.datalake.MessageHandler;
import io.intino.konos.jms.TopicConsumer;
import io.intino.konos.jms.TopicProducer;
import io.intino.ness.inl.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;

import static io.intino.konos.datalake.Datalake.REGISTER_ONLY;
import static io.intino.konos.datalake.MessageTranslator.fromInlMessage;

public class JMSTank implements Datalake.Tank {
	private static Logger logger = LoggerFactory.getLogger(JMSTank.class);

	private String name;
	private JMSDatalake datalake;
	private TopicConsumer flow;
	private MessageHandler handler;

	public JMSTank(String name, JMSDatalake datalake) {
		this.name = name;
		this.datalake = datalake;
	}

	@Override
	public void handler(MessageHandler handler) {
		this.handler = handler;
	}

	@Override
	public void handle(Message message) {
		handler.handle(message);
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
		if (producer != null) producer.produce(fromInlMessage(message));
	}

	public void drop(io.intino.ness.inl.Message message) {
		try {
			final javax.jms.Message jmsMessage = fromInlMessage(message);
			if (jmsMessage == null) return;
			jmsMessage.setBooleanProperty(REGISTER_ONLY, true);
			final TopicProducer producer = datalake.newProducer(dropChannel());
			if (producer != null) producer.produce(jmsMessage);
		} catch (JMSException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public TopicConsumer flow(String flowID) {
		if (datalake.session() == null) logger.error("Session is null");
		this.flow = new TopicConsumer(datalake.session(), flowChannel());
		if (flowID != null) this.flow.listen(handler, flowID);
		else this.flow.listen(handler);
		return this.flow;
	}

	public void unregister() {
		if (flow != null) flow.stop();
		flow = null;
	}
}
