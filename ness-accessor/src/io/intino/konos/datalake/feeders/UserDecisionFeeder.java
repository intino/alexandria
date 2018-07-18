package io.intino.konos.datalake.feeders;

import io.intino.konos.datalake.Feeder;

import java.util.List;

public abstract class UserDecisionFeeder extends Feeder {
	private final String defaultOption;
	private final List<String> options;

	public UserDecisionFeeder(String defaultOption, List<String> options) {
		this.defaultOption = defaultOption;
		this.options = options;
	}

	public String defaultOption() {
		return defaultOption;
	}

	public List<String> options() {
		return options;
	}
}
