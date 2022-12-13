package io.intino.alexandria.terminal.remotedatalake;

import com.google.gson.JsonObject;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.event.EventReader;
import io.intino.alexandria.event.EventStream;
import io.intino.alexandria.logger.Logger;
import org.apache.activemq.BlobMessage;

import javax.jms.JMSException;
import javax.jms.Message;
import java.io.IOException;
import java.util.List;

import static io.intino.alexandria.terminal.remotedatalake.DatalakeAccessor.reflowSchema;

public class RemoteTub implements Datalake.EventStore.Tub {
	private final DatalakeAccessor accessor;
	private final String tank;
	private final String tub;

	public RemoteTub(DatalakeAccessor accessor, String tank, String tub) {
		this.accessor = accessor;
		this.tank = tank;
		this.tub = tub;
	}

	@Override
	public Timetag timetag() {
		return Timetag.of(tub);
	}

	@Override
	public EventStream events() {
		JsonObject jsonObject = reflowSchema(tank, List.of(tub));
		Message response = accessor.query(jsonObject.toString());
		return response instanceof BlobMessage ? openStream((BlobMessage) response) : null;
	}

	private static EventReader openStream(BlobMessage message) {
		try {
			return new EventReader(message.getInputStream());
		} catch (IOException | JMSException e) {
			Logger.error(e);
			return null;
		}
	}
}
