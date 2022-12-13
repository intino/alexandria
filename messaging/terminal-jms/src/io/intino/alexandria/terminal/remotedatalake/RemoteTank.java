package io.intino.alexandria.terminal.remotedatalake;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake.EventStore.Tank;
import io.intino.alexandria.datalake.Datalake.EventStore.Tub;
import io.intino.alexandria.event.Event;
import io.intino.alexandria.event.EventReader;
import io.intino.alexandria.event.EventStream;
import io.intino.alexandria.logger.Logger;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static io.intino.alexandria.terminal.remotedatalake.DatalakeAccessor.reflowSchema;

public class RemoteTank implements Tank {
	private final DatalakeAccessor accessor;
	private final JsonObject tank;
	private final JsonArray tubs;

	public RemoteTank(DatalakeAccessor accessor, JsonObject tank) {
		this.accessor = accessor;
		this.tank = tank;
		this.tubs = tank.get("tubs").getAsJsonArray();
	}

	@Override
	public String name() {
		return tank.get("name").getAsString();
	}

	@Override
	public Scale scale() {
		return Scale.valueOf(tank.get("scale").getAsString());
	}

	@Override
	public Stream<Tub> tubs() {
		return StreamSupport
				.stream(tubs.spliterator(), false)
				.map(t -> new RemoteTub(accessor, name(), t.getAsString()));
	}

	@Override
	public Tub first() {
		if (!tubs.isEmpty()) return new RemoteTub(accessor, name(), tubs.get(0).getAsString());
		return null;
	}

	@Override
	public Tub last() {
		return !tubs.isEmpty() ? new RemoteTub(accessor, name(), tubs.get(tubs.size() - 1).getAsString()) : null;
	}

	@Override
	public Tub on(Timetag timetag) {
		return tubs().filter(t -> t.timetag().equals(timetag)).findFirst().orElse(null);
	}

	@Override
	public EventStream content() {
		return eventStream(reflowSchema(name(), tubs));
	}

	@Override
	public EventStream content(Predicate<Timetag> filter) {
		List<String> tubs = tubs().filter(t -> filter.test(t.timetag())).map(t -> t.timetag().value()).collect(Collectors.toList());
		return eventStream(reflowSchema(name(), tubs));
	}

	public EventStream eventStream(JsonObject schema) {
		return new EventStream() {
			final MessageConsumer consumer = accessor.queryWithConsumer(schema.toString());
			boolean hasNextMessage;
			EventReader current;

			{
				Message received = receive(consumer);
				if (received != null) {
					hasNextMessage = calculateHasNext(received);
					current = read((BytesMessage) received);
				}
			}

			@Override
			public Event current() {
				return current != null ? current.current() : null;
			}

			@Override
			public Event next() {
				if (current.hasNext()) try {
					return current.next();
				} catch (NullPointerException ignored) {
				}
				if (hasNextMessage) {
					Message received = receive(consumer);
					if (received == null) return null;
					hasNextMessage = calculateHasNext(received);
					return (current = read((BytesMessage) received)).next();
				}
				return null;
			}

			@Override
			public boolean hasNext() {
				boolean hasNext = current.hasNext() || hasNextMessage;
				if (!hasNext) close(consumer);
				return hasNext;
			}
		};
	}

	private Message receive(MessageConsumer consumer) {
		try {
			return consumer.receive(1000);
		} catch (JMSException e) {
			Logger.error(e);
			return null;
		}
	}

	private static boolean calculateHasNext(Message message) {
		try {
			return message.getBooleanProperty("hasNext");
		} catch (JMSException e) {
			Logger.error(e);
			return false;
		}
	}

	private void close(MessageConsumer consumer) {
		try {
			consumer.close();
		} catch (JMSException e) {
			Logger.error(e);
		}
	}

	private static EventReader read(BytesMessage response) {
		try {
			byte[] buf = new byte[(int) response.getBodyLength()];
			response.readBytes(buf);
			return new EventReader(new ByteArrayInputStream(buf));
		} catch (JMSException e) {
			Logger.error(e);
			return null;
		}
	}
}