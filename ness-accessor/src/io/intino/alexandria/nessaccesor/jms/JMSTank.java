package io.intino.alexandria.nessaccesor.jms;

import io.intino.alexandria.nessaccesor.MessageHandler;
import io.intino.alexandria.jms.Consumer;
import io.intino.alexandria.jms.TopicConsumer;
import io.intino.alexandria.jms.TopicProducer;
import io.intino.alexandria.nessaccesor.NessAccessor;
import io.intino.ness.inl.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;

import static io.intino.alexandria.nessaccesor.NessAccessor.EventStore.REGISTER_ONLY;
import static io.intino.alexandria.nessaccesor.MessageTranslator.fromInlMessage;
import static io.intino.alexandria.nessaccesor.MessageTranslator.toInlMessage;

public class JMSTank implements NessAccessor.EventStore.Tank {
	private static Logger logger = LoggerFactory.getLogger(JMSTank.class);

	private String name;
	private JMSEventStore datalake;
	private TopicConsumer flow;
	private MessageHandler handler;

	public JMSTank(String name, JMSEventStore datalake) {
		this.name = name;
		this.datalake = datalake;
	}

	@Override
	public NessAccessor.EventStore.Tank handler(MessageHandler handler) {
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
		if (!datalake.isConnected()) datalake.connect();
		final TopicProducer producer = datalake.newProducer(feedChannel());
		if (producer == null) return false;
		return producer.produce(fromInlMessage(message));
	}

	public boolean put(io.intino.ness.inl.Message message) {
		if (!datalake.isConnected()) datalake.connect();
		final TopicProducer producer = datalake.newProducer(putChannel());
		if (producer == null) return false;
		return producer.produce(onlyRegister(fromInlMessage(message)));
	}

	@Override
	public NessAccessor.EventStore.Tank batchSession(int blockSize) {
		datalake.batch(name, blockSize);
		return this;
	}

	@Override
	public NessAccessor.EventStore.Tank endBatch() {
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
