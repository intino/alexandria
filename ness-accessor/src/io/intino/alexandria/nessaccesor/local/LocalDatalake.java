package io.intino.alexandria.nessaccesor.local;

import io.intino.ness.core.Datalake;
import io.intino.ness.core.Stage;
import io.intino.ness.core.fs.FSDatalake;

public class LocalDatalake implements Datalake {

	private final FSDatalake datalake;

	public LocalDatalake(FSDatalake datalake) {
		this.datalake = datalake;
	}

	@Override
	public Connection connection() {
		return new Connection() {
			@Override
			public void connect() {

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
	public void push(Stage stage) {
		datalake.push(stage);
	}

	@Override
	public void seal() {
		datalake.seal();
	}
}
