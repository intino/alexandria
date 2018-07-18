package io.intino.konos.datalake.feeders;

import io.intino.konos.datalake.Feeder;

public class UserDocumentEdition extends Feeder {
	private final Mode mode;

	public enum Mode { Offline, Online };

	public UserDocumentEdition(String mode) {
		this.mode = Mode.valueOf(mode);
	}

	public Mode mode() {
		return mode;
	}
}
