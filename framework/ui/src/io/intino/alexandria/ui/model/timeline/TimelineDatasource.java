package io.intino.alexandria.ui.model.timeline;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public interface TimelineDatasource {
	String name();
	List<Measurement> measurements();
	Timeline timeline(Measurement measurement);

	default Measurement measurement(String name) {
		return measurements().stream().filter(d -> d.name().equals(name)).findFirst().orElse(null);
	}

	default Timeline timeline(String measurementName) {
		Measurement measurement = measurement(measurementName);
		return measurement != null ? timeline(measurement) : null;
	}

	interface Timeline {
		double value();
		double speed();
		double acceleration();
		enum Trend { None, Increased, Decreased } Trend trend();
		Instant from();
		Instant to();

		enum Scale { Day, Week, Month, Year }
		Summary summary(Instant date, Scale scale);

		Serie serie();
		Serie serie(Instant start, Instant end);
	}

	interface Summary {
		String label();
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
