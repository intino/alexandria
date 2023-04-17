package io.intino.alexandria.ui.model.timeline;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public interface TimelineDatasource {
	String name();
	List<MagnitudeDefinition> magnitudes();
	Magnitude magnitude(MagnitudeDefinition definition);
	enum TimelineScale { Minute, Hour, Day, Week, Month, Year }
	List<TimelineScale> scales();

	Instant from(TimelineScale scale);
	Instant previous(Instant date, TimelineScale scale);
	Instant next(Instant date, TimelineScale scale);
	Instant to(TimelineScale scale);

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

		Summary summary(Instant date, TimelineScale scale);

		Serie serie(TimelineScale scale, Instant end);
		Serie serie(TimelineScale scale, Instant start, Instant end);

		String customHtmlView(TimelineScale scale);
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
	}

}
