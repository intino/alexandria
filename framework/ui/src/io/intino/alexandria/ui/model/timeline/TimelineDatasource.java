package io.intino.alexandria.ui.model.timeline;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public interface TimelineDatasource {
	String name();
	List<MeasurementDefinition> measurements();
	Measurement measurement(MeasurementDefinition definition);
	enum TimelineScale { Minute, Hour, Day, Week, Month, Year }
	List<TimelineScale> scales();

	default MeasurementDefinition measurementDefinition(String name) {
		return measurements().stream().filter(d -> d.name().equals(name)).findFirst().orElse(null);
	}

	default Measurement measurement(String measurementName) {
		MeasurementDefinition measurement = measurementDefinition(measurementName);
		return measurement != null ? measurement(measurement) : null;
	}

	interface Measurement {
		MeasurementDefinition definition();
		double value();
		double speed();
		double acceleration();
		enum Trend { None, Increased, Decreased } Trend trend();
		double distribution();
		enum DistributionTrend { None, Lower, Upper } DistributionTrend distributionTrend();
		Instant from();
		Instant to();

		Summary summary(Instant date, TimelineScale scale);

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
