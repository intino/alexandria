package io.intino.alexandria.nessaccesor.local;


import io.intino.ness.core.Blob;
import io.intino.ness.core.Datalake;
import io.intino.ness.core.fs.FSDatalake;

import java.util.stream.Stream;

public class LocalDatalake implements Datalake {

	private final FSDatalake datalake;

	public LocalDatalake(FSDatalake datalake) {
		this.datalake = datalake;
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
