package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.Scale;
import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.model.timeline.MagnitudeDefinition;
import io.intino.alexandria.ui.model.timeline.TimelineDatasource;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.time.ZoneOffset.UTC;

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
			public List<MagnitudeDefinition> magnitudes() {
				return List.of(measurementOf("m1", "%", "Medida 1"), measurementOf("m2", "€", "Medida 2"));
			}

			@Override
			public Magnitude magnitude(MagnitudeDefinition definition) {
				if (definition.name().equalsIgnoreCase("m1")) return m1(definition);
				return m2(definition);
			}

			@Override
			public List<Scale> scales() {
				return List.of(Scale.Hour, Scale.Day, Scale.Week, Scale.Month, Scale.Year);
			}

			@Override
			public Instant from(Scale scale) {
				return LocalDateTime.ofInstant(Instant.now(), UTC).minus(30, scale.temporalUnit()).toInstant(UTC);
			}

			@Override
			public Instant previous(Scale scale, Instant date) {
				return LocalDateTime.ofInstant(date, UTC).minus(1, scale.temporalUnit()).toInstant(UTC);
			}

			@Override
			public Instant next(Scale scale, Instant date) {
				return LocalDateTime.ofInstant(date, UTC).plus(1, scale.temporalUnit()).toInstant(UTC);
			}

			@Override
			public Instant to(Scale scale) {
				return Instant.now();
			}

		};
	}

	private TimelineDatasource.Magnitude m1(MagnitudeDefinition definition) {
		return new TimelineDatasource.Magnitude() {

			@Override
			public TimelineDatasource.Summary summary(Instant date, Scale scale) {
				return new TimelineDatasource.Summary() {
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
			public MagnitudeDefinition definition() {
				return definition;
			}

			@Override
			public Status status() {
				return Status.Warning;
			}

			@Override
			public double value() {
				return 11;
			}

			@Override
			public Double min() {
				return null;
			}

			@Override
			public Double max() {
				return null;
			}

			@Override
			public Double percentage() {
				return null;
			}

			@Override
			public TimelineDatasource.Serie serie(Scale scale, Instant instant) {
				LocalDateTime date = LocalDateTime.ofInstant(instant, UTC);
				return new TimelineDatasource.Serie() {
					@Override
					public String name() {
						return "Evolución";
					}

					@Override
					public Map<Instant, Double> values() {
						return new LinkedHashMap<>() {{
							put(date.minus(8, scale.temporalUnit()).toInstant(UTC), 120.0);
							put(date.minus(7, scale.temporalUnit()).toInstant(UTC), 100.0);
							put(date.minus(6, scale.temporalUnit()).toInstant(UTC), 10.0);
							put(date.minus(5, scale.temporalUnit()).toInstant(UTC), 20.0);
							put(date.minus(4, scale.temporalUnit()).toInstant(UTC), 1220.0);
							put(date.minus(3, scale.temporalUnit()).toInstant(UTC), 192.0);
							put(date.minus(2, scale.temporalUnit()).toInstant(UTC), 1232.0);
							put(date.minus(1, scale.temporalUnit()).toInstant(UTC), 12.0);
							put(date.toInstant(UTC), 12.0);
						}};
					}

					@Override
					public Map<Instant, TimelineDatasource.Annotation> annotations() {
						return new LinkedHashMap<>() {{
							put(date.minus(5, scale.temporalUnit()).toInstant(UTC), new TimelineDatasource.Annotation("Warning value"));
							put(date.minus(4, scale.temporalUnit()).toInstant(UTC), new TimelineDatasource.Annotation("Out of range", "red"));
							put(date.minus(1, scale.temporalUnit()).toInstant(UTC), new TimelineDatasource.Annotation("Value is not valid", "green"));
						}};
					}
				};
			}

			@Override
			public TimelineDatasource.Serie serie(Scale scale, Instant start, Instant end) {
				return serie(scale, end);
			}

			@Override
			public String customHtmlView(Scale scale) {
				return "<div style=\"width:100%\">soy un custom view</div>";
			}

		};
	}

	private TimelineDatasource.Magnitude m2(MagnitudeDefinition definition) {
		return new TimelineDatasource.Magnitude() {

			@Override
			public TimelineDatasource.Summary summary(Instant date, Scale scale) {
				return new TimelineDatasource.Summary() {
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
			public MagnitudeDefinition definition() {
				return definition;
			}

			@Override
			public Status status() {
				return Status.Normal;
			}

			@Override
			public double value() {
				return 3300;
			}

			@Override
			public Double min() {
				return -10000.0;
			}

			@Override
			public Double max() {
				return 10000.0;
			}

			@Override
			public Double percentage() {
				return 16.5;
			}

			@Override
			public TimelineDatasource.Serie serie(Scale scale, Instant date) {
				return new TimelineDatasource.Serie() {
					@Override
					public String name() {
						return "Evolución";
					}

					@Override
					public Map<Instant, Double> values() {
						return Collections.emptyMap();
					}

					@Override
					public Map<Instant, TimelineDatasource.Annotation> annotations() {
						return Collections.emptyMap();
					}
				};
			}

			@Override
			public TimelineDatasource.Serie serie(Scale scale, Instant start, Instant end) {
				return new TimelineDatasource.Serie() {
					@Override
					public String name() {
						return "Evolución";
					}

					@Override
					public Map<Instant, Double> values() {
						return Collections.emptyMap();
					}

					@Override
					public Map<Instant, TimelineDatasource.Annotation> annotations() {
						return Collections.emptyMap();
					}
				};
			}

			@Override
			public String customHtmlView(Scale scale) {
				return null;
			}

		};
	}

	private MagnitudeDefinition measurementOf(String name, String unit, String label) {
		return new MagnitudeDefinition().name(name).unit(unit).add("es", label);
	}

}