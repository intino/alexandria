package io.intino.alexandria.terminal.remotedatalake.resource;

import com.google.gson.JsonObject;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.event.EventStream;
import io.intino.alexandria.event.resource.ResourceEvent;
import io.intino.alexandria.terminal.remotedatalake.DatalakeAccessor;

import javax.jms.BytesMessage;
import javax.jms.Message;
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
		return response instanceof BytesMessage ? openStream((BytesMessage) response) : null;
	}

	private static EventStream<ResourceEvent> openStream(BytesMessage message) {
		throw new UnsupportedOperationException("");
	}
}
