package io.intino.alexandria.ui.model.timeline;

import io.intino.alexandria.Timetag;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public interface TimelineDatasource {
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
		Map<Instant, Double> stats();
		Map<Instant, Double> stats(Instant start, Instant end);
		Map<Instant, Double> speedStats();
		Map<Instant, Double> speedStats(Instant start, Instant end);
		Map<Instant, Double> accelerationStats();
		Map<Instant, Double> accelerationStats(Instant start, Instant end);
	}
}
