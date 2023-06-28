package io.intino.alexandria.ui.model.reel;

import io.intino.alexandria.Scale;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public interface ReelDatasource {
	String name();
	List<SignalDefinition> signals();
	Signal signal(SignalDefinition definition);
	List<Scale> scales();

	Instant from(Scale scale);
	Instant to(Scale scale);
	Instant previous(Scale scale, Instant date);
	Instant next(Scale scale, Instant date);

	default SignalDefinition signalDefinition(String name) {
		return signals().stream().filter(d -> d.name().equals(name)).findFirst().orElse(null);
	}

	default Signal signal(String signalName) {
		SignalDefinition signal = signalDefinition(signalName);
		return signal != null ? signal(signal) : null;
	}

	interface Signal {
		SignalDefinition definition();
		String reel(Scale scale, Instant start, Instant end);
		Map<Instant, List<Annotation>> annotations(Scale scale, Instant start, Instant end);
	}

	class Annotation {
		private final String label;
		private final String color;

		public enum Symbol { Circle, Square, Diamond, Triangle }

		public Annotation(String label) {
			this(label, "#ed6c02", Annotation.Symbol.Circle); // green
		}

		public Annotation(String label, String color) {
			this(label, color, Annotation.Symbol.Circle);
		}

		private Annotation(String label, String color, Annotation.Symbol symbol) {
			this.label = label;
			this.color = color;
		}

		public String label() {
			return label;
		}

		public String color() {
			return color;
		}
	}

}
