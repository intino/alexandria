package io.intino.konos.datalake.sensors;

import io.intino.konos.datalake.Feeder;

import java.util.List;

public abstract class PollSensor extends Feeder {
	private final String defaultOption;
	private final List<Option> options;

	public PollSensor(String defaultOption, List<Option> options) {
		this.defaultOption = defaultOption;
		this.options = options;
	}

	public String defaultOption() {
		return defaultOption;
	}

	public List<Option> options() {
		return options;
	}

	public static class Option {

		private final String value;
		private final List<Option> options;

		public Option(String value, List<Option> options) {
			this.value = value;
			this.options = options;
		}

		public String value() {
			return value;
		}

		public List<Option> options() {
			return options;
		}
	}
}
