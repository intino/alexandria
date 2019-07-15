package io.intino.alexandria.datahub;

import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.jms.Consumer;
import io.intino.alexandria.jms.MessageReader;
import io.intino.alexandria.logger.Logger;
import org.apache.activemq.command.ActiveMQDestination;

import javax.jms.JMSException;
import javax.jms.Message;

import static io.intino.alexandria.jms.MessageFactory.createMessageFor;
import static java.util.stream.Collectors.joining;

public class AdminService implements Consumer {
	private final DataHub box;

	public AdminService(DataHub box) {
		this.box = box;
	}

	@Override
	public void accept(Message message) {
		String text = MessageReader.textFrom(message);
		if (text.startsWith("tanks"))
			replyTo(message, box.datalake().eventStore().tanks().map(Datalake.EventStore.Tank::name).collect(joining(";")));
	}

	private void replyTo(Message request, String reply) {
		try {
			box.brokerManager().getQueueProducer(((ActiveMQDestination) request.getJMSReplyTo()).getPhysicalName()).produce(createMessageFor(reply));
		} catch (JMSException e) {
			Logger.error(e.getMessage(), e);
		}
	}
}
