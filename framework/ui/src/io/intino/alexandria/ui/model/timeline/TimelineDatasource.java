package io.intino.alexandria.ui.model.timeline;

import io.intino.alexandria.Scale;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public interface TimelineDatasource {
	String name();
	List<MagnitudeDefinition> magnitudes();
	Magnitude magnitude(MagnitudeDefinition definition);
	List<Scale> scales();

	Instant from(Scale scale);
	Instant previous(Instant date, Scale scale);
	Instant next(Instant date, Scale scale);
	Instant to(Scale scale);

	default MagnitudeDefinition magnitudeDefinition(String name) {
		return magnitudes().stream().filter(d -> d.name().equals(name)).findFirst().orElse(null);
	}

	default Magnitude magnitude(String magnitudeName) {
		MagnitudeDefinition magnitude = magnitudeDefinition(magnitudeName);
		return magnitude != null ? magnitude(magnitude) : null;
	}

	interface Magnitude {
		MagnitudeDefinition definition();

		enum Status { Normal, Warning, Error }
		Status status();
		double value();
		Double min();
		Double max();
		Double percentage();

		Summary summary(Instant date, Scale scale);

		Serie serie(Scale scale, Instant end);
		Serie serie(Scale scale, Instant start, Instant end);

		String customHtmlView(Scale scale);
	}

	interface Summary {
		double average();
		Instant averageDate();
		double max();
		Instant maxDate();
		double min();
		Instant minDate();
	}

	interface Serie {
		String name();
		Map<Instant, Double> values();
		Map<Instant, Annotation> annotations();
	}

	class Annotation {
		private final String label;
		private final String color;
		private final Symbol symbol;

		public enum Symbol { Circle, Square, Diamond, Triangle }

		public Annotation(String label) {
			this(label, "#ed6c02", Symbol.Circle); // green
		}

		public Annotation(String label, String color) {
			this(label, color, Symbol.Circle);
		}

		private Annotation(String label, String color, Symbol symbol) {
			this.label = label;
			this.color = color;
			this.symbol = symbol;
		}

		public String label() {
			return label;
		}

		public String color() {
			return color;
		}

		public Symbol symbol() {
			return symbol;
		}
	}
}
