package io.intino.alexandria.terminal.remotedatalake.message;

import com.google.gson.JsonObject;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.event.EventStream;
import io.intino.alexandria.event.message.MessageEvent;
import io.intino.alexandria.event.message.MessageEventReader;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.terminal.remotedatalake.DatalakeAccessor;
import org.apache.activemq.BlobMessage;

import javax.jms.JMSException;
import javax.jms.Message;
import java.io.IOException;
import java.util.List;

import static io.intino.alexandria.terminal.remotedatalake.DatalakeAccessor.reflowSchema;

public class RemoteMessageTub implements Datalake.Store.Tub<MessageEvent> {
	private final DatalakeAccessor accessor;
	private final String tank;
	private final String source;
	private final String tub;

	public RemoteMessageTub(DatalakeAccessor accessor, String tank, String source, String tub) {
		this.accessor = accessor;
		this.tank = tank;
		this.source = source;
		this.tub = tub;
	}

	@Override
	public Timetag timetag() {
		return Timetag.of(tub);
	}

	@Override
	public EventStream<MessageEvent> events() {
		JsonObject jsonObject = reflowSchema(tank, source, List.of(tub));
		Message response = accessor.query(jsonObject.toString());
		return response instanceof BlobMessage ? openStream((BlobMessage) response) : null;
	}

	private static EventStream<MessageEvent> openStream(BlobMessage message) {
		try {
			return new EventStream<>(new MessageEventReader(message.getInputStream()));
		} catch (IOException | JMSException e) {
			Logger.error(e);
			return null;
		}
	}
}
