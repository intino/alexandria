package io.intino.alexandria.nessaccessor;

import io.intino.alexandria.inl.Message;
import io.intino.ness.core.Datalake;
import io.intino.ness.core.MemoryStage;
import io.intino.ness.core.Scale;
import io.intino.ness.core.Timetag;
import io.intino.ness.core.sessions.EventSession;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class NessSender {

	private final Datalake datalake;

	public NessSender(Datalake datalake) {
		this.datalake = datalake;
	}

	public void feed(Datalake.EventStore.Tank tank, Message... messages) {
		send(tank, new FeedMemoryStage(), messages);
	}

	public void send(Datalake.EventStore.Tank tank, Message... messages) {
		send(tank, new MemoryStage(), messages);
	}

	private void send(Datalake.EventStore.Tank tank, MemoryStage stage, Message[] messages) {
		EventSession session = stage.createEventSession();
		for (Message message : messages) session.put(tank.name(), timeTagOf(message), message);
		datalake.push(stage);
		session.close();
	}

	private Timetag timeTagOf(Message message) {
		return Timetag.of(LocalDateTime.ofInstant(Instant.parse(message.get("ts")), ZoneOffset.UTC), Scale.Month);//TODO
	}

	public static class FeedMemoryStage extends MemoryStage {

	}
}


