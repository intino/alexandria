package io.intino.konos.datalake.feeders;

import io.intino.konos.datalake.Feeder;

public abstract class UserEditionFeeder extends Feeder {
	private final String path;

	public UserEditionFeeder(String path) {
		this.path = path;
	}

	public String path() {
		return path;
	}
}
