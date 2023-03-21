package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.model.timeline.MeasurementDefinition;
import io.intino.alexandria.ui.model.timeline.TimelineDatasource;
import io.intino.alexandria.ui.model.timeline.TimelineDatasource.TimelineScale;

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
		timeline1.source(source());
		timeline1.refresh();
		timeline2.source(source());
		timeline2.refresh();
	}

	private TimelineDatasource source() {
		return new TimelineDatasource() {
			@Override
			public String name() {
				return "tds1";
			}

			@Override
			public List<MeasurementDefinition> measurements() {
				return List.of(measurementOf("m1", "%", "Medida 1"), measurementOf("m2", "€", "Medida 2"));
			}

			@Override
			public Measurement measurement(MeasurementDefinition definition) {
				if (definition.name().equalsIgnoreCase("m1")) return m1(definition);
				return m2(definition);
			}

			@Override
			public List<TimelineScale> scales() {
				return List.of(TimelineScale.Hour, TimelineScale.Day, TimelineScale.Week, TimelineScale.Month, TimelineScale.Year);
			}
		};
	}

	private TimelineDatasource.Measurement m1(MeasurementDefinition definition) {
		return new TimelineDatasource.Measurement() {
			@Override
			public Trend trend() {
				if (speed() == 0) return Trend.None;
				return speed() > 0 ? Trend.Increased : Trend.Decreased;
			}

			@Override
			public double distribution() {
				return 5;
			}

			@Override
			public DistributionTrend distributionTrend() {
				return DistributionTrend.Lower;
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
			public TimelineDatasource.Summary summary(Instant date, TimelineScale scale) {
				return new TimelineDatasource.Summary() {
					@Override
					public String label() {
						return scale.name();
					}

					@Override
					public double average() {
						return 10;
					}

					@Override
					public Instant averageDate() {
						return Instant.now();
					}

					@Override
					public double max() {
						return 100;
					}

					@Override
					public Instant maxDate() {
						return Instant.now();
					}

					@Override
					public double min() {
						return 2;
					}

					@Override
					public Instant minDate() {
						return Instant.now();
					}
				};
			}

			@Override
			public MeasurementDefinition definition() {
				return definition;
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
			public TimelineDatasource.Serie serie() {
				return new TimelineDatasource.Serie() {
					@Override
					public String name() {
						return "Evolución";
					}

					@Override
					public Map<Instant, Double> values() {
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
				};
			}

			@Override
			public TimelineDatasource.Serie serie(Instant start, Instant end) {
				return serie();
			}

		};
	}

	private TimelineDatasource.Measurement m2(MeasurementDefinition definition) {
		return new TimelineDatasource.Measurement() {
			@Override
			public Trend trend() {
				if (acceleration() == 0) return Trend.None;
				return acceleration() > 0 ? Trend.Increased : Trend.Decreased;
			}

			@Override
			public double distribution() {
				return 10;
			}

			@Override
			public DistributionTrend distributionTrend() {
				return DistributionTrend.Upper;
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
			public TimelineDatasource.Summary summary(Instant date, TimelineScale scale) {
				return new TimelineDatasource.Summary() {
					@Override
					public String label() {
						return scale.name();
					}

					@Override
					public double average() {
						return 35;
					}

					@Override
					public Instant averageDate() {
						return Instant.now();
					}

					@Override
					public double max() {
						return 92;
					}

					@Override
					public Instant maxDate() {
						return Instant.now();
					}

					@Override
					public double min() {
						return 10;
					}

					@Override
					public Instant minDate() {
						return Instant.now();
					}
				};
			}

			@Override
			public MeasurementDefinition definition() {
				return definition;
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
			public TimelineDatasource.Serie serie() {
				return new TimelineDatasource.Serie() {
					@Override
					public String name() {
						return "Evolución";
					}

					@Override
					public Map<Instant, Double> values() {
						return Collections.emptyMap();
					}
				};
			}

			@Override
			public TimelineDatasource.Serie serie(Instant start, Instant end) {
				return new TimelineDatasource.Serie() {
					@Override
					public String name() {
						return "Evolución";
					}

					@Override
					public Map<Instant, Double> values() {
						return Collections.emptyMap();
					}
				};
			}

		};
	}

	private MeasurementDefinition measurementOf(String name, String unit, String label) {
		return new MeasurementDefinition().name(name).unit(unit).add("es", label).decimalCount(0);
	}
}