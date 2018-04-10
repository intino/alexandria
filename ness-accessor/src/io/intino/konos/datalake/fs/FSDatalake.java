package io.intino.konos.datalake.fs;

import io.intino.konos.datalake.Datalake;
import io.intino.konos.datalake.MessageDispatcher;
import io.intino.ness.datalake.ReflowMessageInputStream;
import io.intino.ness.datalake.Scale;
import io.intino.ness.datalake.graph.DatalakeGraph;
import io.intino.ness.inl.Message;
import io.intino.tara.magritte.Graph;
import io.intino.tara.magritte.stores.ResourcesStore;

import java.io.File;
import java.time.Instant;

import static java.util.Arrays.stream;
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

	@Override
	public ReflowSession reflow(int blockSize, MessageDispatcher dispatcher, Instant from, Tank... tanks) {
		return new ReflowSession() {
			final ReflowMessageInputStream stream = new ReflowMessageInputStream(stream(tanks).map(t -> datalake.tank(t.name())).collect(toList()), from);

			@Override
			public void next() {
				int messages = 0;
				while (stream.hasNext()) {
					final Message next = stream.next();
					dispatcher.dispatch(next);
					if (++messages == blockSize) return;
				}
			}

			@Override
			public void finish() {
				stream.close();
			}

			@Override
			public void play() {
			}

			@Override
			public void pause() {
			}
		};
	}

	@Override
	public void commit() {
	}

	@Override
	public void add(String tank) {
		datalake.add(clean(tank));
	}

	@Override
	public void disconnect() {

	}

	@Override
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
