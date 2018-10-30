package io.intino.alexandria.nessaccesor.fs;

import io.intino.alexandria.jms.TopicConsumer;
import io.intino.alexandria.ness.Scale;
import io.intino.alexandria.ness.eventstore.ReflowMessageInputStream;
import io.intino.alexandria.ness.eventstore.graph.DatalakeGraph;
import io.intino.alexandria.nessaccesor.MessageHandler;
import io.intino.alexandria.nessaccesor.NessAccessor;
import io.intino.alexandria.nessaccesor.ReflowConfiguration;
import io.intino.alexandria.nessaccesor.ReflowDispatcher;
import io.intino.ness.inl.Message;
import io.intino.tara.magritte.Graph;
import io.intino.tara.magritte.stores.ResourcesStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.Instant;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static io.intino.alexandria.nessaccesor.Helper.eventDatalakeDirectory;
import static io.intino.alexandria.nessaccesor.Helper.scaleOf;
import static java.util.stream.Collectors.toMap;

public class FSEventStore implements NessAccessor.EventStore {
	private static Logger logger = LoggerFactory.getLogger(FSEventStore.class);
	private final DatalakeGraph datalake;

	public FSEventStore(File directory, Scale scale) {
		datalake = new Graph(new ResourcesStore()).loadStashes("Datalake").as(DatalakeGraph.class);
		directory.mkdirs();
		datalake.directory(directory);
		datalake.scale(scale);
	}

	public FSEventStore(String url) {
		datalake = new Graph(new ResourcesStore()).loadStashes("Datalake").as(DatalakeGraph.class);
		final File store = eventDatalakeDirectory(url);
		store.mkdirs();
		datalake.directory(store);
		datalake.scale(scaleOf(url));
	}

	public boolean put(String name, Message message) {
		datalake.tank(name).put(message);
		return true;
	}

	public ReflowSession reflow(ReflowConfiguration reflow, ReflowDispatcher dispatcher) {
		return reflow(reflow, dispatcher, () -> {
		});
	}

	public io.intino.alexandria.ness.eventstore.graph.Tank tank(String name) {
		return datalake.tank(name);
	}

	public ReflowSession reflow(ReflowConfiguration reflow, ReflowDispatcher dispatcher, Runnable onFinish) {
		return new ReflowSession() {
			final ReflowMessageInputStream stream = new ReflowMessageInputStream(map(reflow));
			int messages = 0;

			public void next() {
				while (stream.hasNext()) {
					final Message next = stream.next();
					dispatcher.dispatch(next);
					if ((++messages % reflow.blockSize()) == 0) break;
				}
				dispatcher.dispatch(stream.hasNext() ? createEndBlockMessage(messages) : createEndReflowMessage(messages));
			}

			private Message createEndBlockMessage(int count) {
				return new Message("endBlock").set("count", count);
			}

			private Message createEndReflowMessage(int count) {
				return new Message("endReflow").set("count", count);
			}

			public void finish() {
				stream.close();
				onFinish.run();
			}

			public void play() {
			}

			public void pause() {
			}
		};
	}

	private Map<io.intino.alexandria.ness.eventstore.graph.Tank, Map.Entry<Instant, Instant>> map(ReflowConfiguration reflow) {
		return reflow.tankList().stream().collect(toMap(t -> datalake.tank(t.name()), t -> new SimpleEntry<>(t.from() == null ? Instant.MIN : t.from(), t.to() == null ? Instant.MAX : t.to())));
	}

	public void commit() {
	}

	public Tank add(String tank) {
		datalake.add(tank);
		return new FSTank(tank, this);
	}

	public void disconnect() {
		datalake.tankList().forEach(io.intino.alexandria.ness.eventstore.graph.Tank::terminate);
	}

	@Override
	public boolean isConnected() {
		return true;
	}


	public void connect(String... args) {
	}

	@Override
	public List<User> users() {
		return Collections.emptyList();
	}

	@Override
	public void append(Message message) {
		io.intino.alexandria.ness.eventstore.graph.Tank tank = datalake.tankList(t -> t.name$().toLowerCase().endsWith(message.type().toLowerCase())).findFirst().orElse(null);
		if (tank == null) {
			logger.error("Tank not found " + message.type());
			return;
		}
		tank.put(message);
	}

	public static class FSTank implements Tank {
		private final String name;
		private final FSEventStore datalake;
		private MessageHandler handler;

		public FSTank(String name, FSEventStore datalake) {
			this.name = name;
			this.datalake = datalake;
		}

		@Override
		public Tank handler(MessageHandler handler) {
			this.handler = handler;
			return this;
		}

		@Override
		public void handle(Message message) {
			handler.handle(message);
		}

		@Override
		public String name() {
			return name;
		}

		@Override
		public boolean feed(Message message) {
			return datalake.put(name, message);
		}

		@Override
		public boolean put(Message message) {
			return datalake.put(name, message);
		}

		@Override
		public Tank batchSession(int blockSize) {
			datalake.tank(name).batch(blockSize);
			return this;
		}

		@Override
		public Tank endBatch() {
			datalake.tank(name).endBatch();
			return this;
		}

		@Override
		public TopicConsumer flow(String flowID) {
			return null;
		}

		@Override
		public void unregister() {

		}
	}
}
