package io.intino.konos.datalake.fs;

import io.intino.konos.datalake.Datalake;
import io.intino.konos.datalake.ReflowConfiguration;
import io.intino.konos.datalake.ReflowDispatcher;
import io.intino.ness.datalake.ReflowMessageInputStream;
import io.intino.ness.datalake.Scale;
import io.intino.ness.datalake.graph.DatalakeGraph;
import io.intino.ness.inl.Message;
import io.intino.tara.magritte.Graph;
import io.intino.tara.magritte.stores.ResourcesStore;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class FSDatalake implements Datalake {

	private final DatalakeGraph datalake;

	public FSDatalake(String url) {
		datalake = new Graph(new ResourcesStore()).loadStashes("Datalake").as(DatalakeGraph.class);
		final File store = datalakeDirectory(url);
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

	public io.intino.ness.datalake.graph.Tank tank(String name) {
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

	private Map<io.intino.ness.datalake.graph.Tank, Map.Entry<Instant, Instant>> map(ReflowConfiguration reflow) {
		return reflow.tankList().stream().collect(toMap(t -> datalake.tank(t.name()), t -> new SimpleEntry<>(t.from() == null ? Instant.MIN : t.from(), t.to() == null ? Instant.MAX : t.to())));
	}

	public void commit() {
	}

	public void add(String tank) {
		datalake.add(clean(tank));
	}

	public void disconnect() {
		datalake.tankList().forEach(io.intino.ness.datalake.graph.Tank::terminate);
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

	private String clean(String url) {
		final int index = url.indexOf("?");
		if (index != -1) url = url.substring(0, index);
		return url.replace("file://", "");
	}

	private Scale scaleOf(String url) {
		return url.contains("?") ? Scale.valueOf(url.split("=")[1]) : Scale.Day;
	}

	private File datalakeDirectory(String url) {
		try {
			return new File(clean(url), "datalake").getCanonicalFile();
		} catch (IOException e) {
			return new File(clean(url), "datalake");
		}
	}
}
