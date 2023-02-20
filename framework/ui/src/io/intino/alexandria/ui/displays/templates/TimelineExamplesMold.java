package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.components.Timeline;
import io.intino.alexandria.ui.model.timeline.Measurement;
import io.intino.alexandria.ui.model.timeline.TimelineDatasource;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimelineExamplesMold extends AbstractTimelineExamplesMold<UiFrameworkBox> {

	public TimelineExamplesMold(UiFrameworkBox box) {
		super(box);
	}

	@Override
	public void init() {
		super.init();
		timeline.source(new TimelineDatasource() {
			@Override
			public String name() {
				return "tds1";
			}

			@Override
			public List<Measurement> measurements() {
				return List.of(measurementOf("m1", "%", "Medida 1"), measurementOf("m2", "€", "Medida 2"));
			}

			@Override
			public Timeline timeline(Measurement measurement) {
				if (measurement.name().equalsIgnoreCase("m1")) return m1();
				return m2();
			}
		});
		timeline.refresh();
	}

	private TimelineDatasource.Timeline m1() {
		return new TimelineDatasource.Timeline() {
			@Override
			public Trend trend() {
				if (speed() == 0) return Trend.None;
				return speed() > 0 ? Trend.Increased : Trend.Decreased;
			}

			@Override
			public Instant from() {
				return Instant.now().minus(30, ChronoUnit.DAYS);
			}

			@Override
			public Instant to() {
				return Instant.now();
			}

			@Override
			public double value() {
				return 11;
			}

			@Override
			public double speed() {
				return 0;
			}

			@Override
			public double acceleration() {
				return 11;
			}

			@Override
			public Map<Instant, Double> stats() {
				return new HashMap<>() {{
					put(Instant.now().minus(8, ChronoUnit.DAYS), 120.0);
					put(Instant.now().minus(7, ChronoUnit.DAYS), 100.0);
					put(Instant.now().minus(6, ChronoUnit.DAYS), 10.0);
					put(Instant.now().minus(5, ChronoUnit.DAYS), 20.0);
					put(Instant.now().minus(4, ChronoUnit.DAYS), 1220.0);
					put(Instant.now().minus(3, ChronoUnit.DAYS), 192.0);
					put(Instant.now().minus(2, ChronoUnit.DAYS), 1232.0);
					put(Instant.now().minus(1, ChronoUnit.DAYS), 12.0);
					put(Instant.now(), 12.0);
				}};
			}

			@Override
			public Map<Instant, Double> stats(Instant start, Instant end) {
				return null;
			}

			@Override
			public Map<Instant, Double> speedStats() {
				return Collections.emptyMap();
			}

			@Override
			public Map<Instant, Double> speedStats(Instant start, Instant end) {
				return null;
			}

			@Override
			public Map<Instant, Double> accelerationStats() {
				return Collections.emptyMap();
			}

			@Override
			public Map<Instant, Double> accelerationStats(Instant start, Instant end) {
				return null;
			}
		};
	}

	private TimelineDatasource.Timeline m2() {
		return new TimelineDatasource.Timeline() {
			@Override
			public Trend trend() {
				if (acceleration() == 0) return Trend.None;
				return acceleration() > 0 ? Trend.Increased : Trend.Decreased;
			}

			@Override
			public Instant from() {
				return Instant.now().minus(30, ChronoUnit.DAYS);
			}

			@Override
			public Instant to() {
				return Instant.now();
			}

			@Override
			public double value() {
				return 100;
			}

			@Override
			public double speed() {
				return 0;
			}

			@Override
			public double acceleration() {
				return 10;
			}

			@Override
			public Map<Instant, Double> stats() {
				return Collections.emptyMap();
			}

			@Override
			public Map<Instant, Double> stats(Instant start, Instant end) {
				return null;
			}

			@Override
			public Map<Instant, Double> speedStats() {
				return Collections.emptyMap();
			}

			@Override
			public Map<Instant, Double> speedStats(Instant start, Instant end) {
				return null;
			}

			@Override
			public Map<Instant, Double> accelerationStats() {
				return Collections.emptyMap();
			}

			@Override
			public Map<Instant, Double> accelerationStats(Instant start, Instant end) {
				return null;
			}
		};
	}

	private Measurement measurementOf(String name, String unit, String label) {
		return new Measurement().name(name).unit(unit).add("es", label);
	}
}