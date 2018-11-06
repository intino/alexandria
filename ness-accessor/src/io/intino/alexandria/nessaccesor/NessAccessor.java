package io.intino.alexandria.nessaccesor;

import io.intino.ness.core.Datalake;
import io.intino.ness.core.Stage;

public class NessAccessor {
	private Datalake datalake;

	public NessAccessor(Datalake datalake) {
		this.datalake = datalake;
	}

	public void push(Stage stage) {
		datalake.push(stage);
		datalake.seal();
		stage.clear();
	}

	public Datalake.EventStore eventStore() {
		return datalake.eventStore();
	}

	public Datalake.SetStore setStore() {
		return datalake.setStore();
	}


}