package io.intino.alexandria.terminal.remotedatalake.resource;

import com.google.gson.JsonObject;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.event.EventStream;
import io.intino.alexandria.event.resource.ResourceEvent;
import io.intino.alexandria.event.resource.ResourceEventReader;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.terminal.remotedatalake.DatalakeAccessor;
import org.apache.activemq.BlobMessage;

import javax.jms.JMSException;
import javax.jms.Message;
import java.io.IOException;
import java.util.List;

import static io.intino.alexandria.terminal.remotedatalake.DatalakeAccessor.reflowSchema;

public class RemoteResourceTub implements Datalake.Store.Tub<ResourceEvent> {
	private final DatalakeAccessor accessor;
	private final String tank;
	private final String source;
	private final String tub;

	public RemoteResourceTub(DatalakeAccessor accessor, String tank, String source, String tub) {
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
	public EventStream<ResourceEvent> events() {
		JsonObject jsonObject = reflowSchema(tank, source, List.of(tub));
		Message response = accessor.query(jsonObject.toString());
		return response instanceof BlobMessage ? openStream((BlobMessage) response) : null;
	}

	private static EventStream<ResourceEvent> openStream(BlobMessage message) {
		try {
			return new EventStream<>(new ResourceEventReader(message.getInputStream()));
		} catch (IOException | JMSException e) {
			Logger.error(e);
			return null;
		}
	}
}
