package io.intino.alexandria.nessaccessor.local;

import io.intino.ness.core.Blob;
import io.intino.ness.core.Datalake;
import io.intino.ness.core.fs.FSDatalake;

import java.io.File;
import java.util.stream.Stream;

public class LocalDatalake implements Datalake {

	private final FSDatalake datalake;

	public LocalDatalake(File root) {
		this.datalake = new FSDatalake(root);
	}


	@Override
	public Connection connection() {
		return new Connection() {
			@Override
			public void connect(String... args) {

			}

			@Override
			public void disconnect() {

			}
		};
	}

	@Override
	public EventStore eventStore() {
		return datalake.eventStore();
	}

	@Override
	public SetStore setStore() {
		return datalake.setStore();
	}

	@Override
	public void push(Stream<Blob> blobs) {
		datalake.push(blobs);
	}

	@Override
	public void seal() {
		datalake.seal();
	}
}
