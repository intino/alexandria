package io.intino.alexandria.nessaccessor;

import io.intino.ness.core.Datalake;

public class NessAccessor {
	private Datalake datalake;

	public NessAccessor(Datalake datalake) {
		this.datalake = datalake;
	}

	public void push(Stage stage) {
		datalake.push(stage.blobs());
		datalake.seal();
		stage.clear();
	}

	public Datalake.EventStore eventStore() {
		return datalake.eventStore();
	}

	public Datalake.SetStore setStore() {
		return datalake.setStore();
	}

	public Datalake.Connection connection() {
		return datalake.connection();
	}
}