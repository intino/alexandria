package io.intino.konos.datalake.jms;

import io.intino.konos.datalake.Datalake;
import io.intino.konos.datalake.MessageHandler;
import io.intino.konos.jms.Consumer;
import io.intino.konos.jms.TopicConsumer;
import io.intino.konos.jms.TopicProducer;
import io.intino.ness.inl.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;

import static io.intino.konos.datalake.Datalake.REGISTER_ONLY;
import static io.intino.konos.datalake.MessageTranslator.fromInlMessage;
import static io.intino.konos.datalake.MessageTranslator.toInlMessage;

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
	public Datalake.Tank handler(MessageHandler handler) {
		this.handler = handler;
		return this;
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

	public String putChannel() {
		return "put." + name;
	}

	public String feedChannel() {
		return "feed." + name;
	}

	public boolean feed(io.intino.ness.inl.Message message) {
		final TopicProducer producer = datalake.newProducer(feedChannel());
		return producer.produce(fromInlMessage(message));
	}

	public boolean put(io.intino.ness.inl.Message message) {
		final TopicProducer producer = datalake.newProducer(putChannel());
		return producer.produce(onlyRegister(fromInlMessage(message)));
	}

	@Override
	public Datalake.Tank batchSession(int blockSize) {
		datalake.batch(name, blockSize);
		return this;
	}

	@Override
	public Datalake.Tank endBatch() {
		datalake.endBatch(name);
		return this;
	}

	public TopicConsumer flow(String flowID) {
		if (datalake.session() == null) logger.error("Session is null");
		this.flow = new TopicConsumer(datalake.session(), flowChannel());
		if (flowID != null) this.flow.listen(dispatcher(), flowID);
		else this.flow.listen(dispatcher());
		return this.flow;
	}

	private Consumer dispatcher() {
		return m -> handler.handle(toInlMessage(m));
	}

	public void unregister() {
		if (flow != null) flow.stop();
		flow = null;
	}

	private javax.jms.Message onlyRegister(javax.jms.Message message) {
		try {
			message.setBooleanProperty(REGISTER_ONLY, true);
		} catch (JMSException e) {
			logger.error(e.getMessage(), e);
		}
		return message;
	}
}
