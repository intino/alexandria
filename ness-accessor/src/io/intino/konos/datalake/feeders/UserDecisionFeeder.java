package io.intino.konos.datalake.feeders;

import io.intino.konos.datalake.Feeder;

import java.util.List;

public abstract class UserDecisionFeeder extends Feeder {
	private final String defaultOption;
	private final List<Option> options;

	public UserDecisionFeeder(String defaultOption, List<Option> options) {
		this.defaultOption = defaultOption;
		this.options = options;
	}

	public String defaultOption() {
		return defaultOption;
	}

	public List<Option> options() {
		return options;
	}

	interface Option {
		String value();
		List<Option> options();
	}
}
