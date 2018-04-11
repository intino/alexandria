package io.intino.konos.datalake.fs;

import io.intino.konos.datalake.Datalake;
import io.intino.konos.datalake.ReflowDispatcher;
import io.intino.ness.datalake.ReflowMessageInputStream;
import io.intino.ness.datalake.Scale;
import io.intino.ness.datalake.graph.DatalakeGraph;
import io.intino.ness.inl.Message;
import io.intino.tara.magritte.Graph;
import io.intino.tara.magritte.stores.ResourcesStore;

import java.io.File;
import java.time.Instant;

import static java.util.stream.Collectors.toList;

public class FSDatalake implements Datalake {

	private final DatalakeGraph datalake;

	public FSDatalake(String url) {
		datalake = new Graph(new ResourcesStore()).loadStashes("Datalake").as(DatalakeGraph.class);
		final File store = new File(clean(url), "datalake");
		store.mkdirs();
		datalake.directory(store);
		datalake.scale(scaleOf(url));
	}

	public void drop(String name, Message message) {
		datalake.tank(name).drop(message);
	}

	public ReflowSession reflow(int blockSize, ReflowDispatcher dispatcher, Instant from) {
		return reflow(blockSize, dispatcher, from, () -> {});
	}

	public ReflowSession reflow(int blockSize, ReflowDispatcher dispatcher, Instant from, Runnable onFinish) {
		return new ReflowSession() {
			final ReflowMessageInputStream stream = new ReflowMessageInputStream(dispatcher.tanks().stream().map(t -> datalake.tank(t.name())).collect(toList()), from);
			int messages = 0;

			public void next() {
				while (stream.hasNext()) {
					final Message next = stream.next();
					dispatcher.dispatch(next);
					if ((++messages % blockSize) == 0) break;
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

	public void commit() {
	}

	public void add(String tank) {
		datalake.add(clean(tank));
	}

	public void disconnect() {
	}

	public void connect(String... args) {
	}

	private String clean(String url) {
		final int index = url.indexOf("?");
		return url.substring(index != -1 ? index : 0).replace("file://", "");
	}

	private Scale scaleOf(String url) {
		return url.contains("?") ? Scale.valueOf(url.split("=")[1]) : Scale.Day;
	}
}
