package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.Scale;
import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.model.reel.ReelDatasource;
import io.intino.alexandria.ui.model.reel.SignalDefinition;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.time.ZoneOffset.UTC;

public class ReelExamplesMold extends AbstractReelExamplesMold<UiFrameworkBox> {

	public ReelExamplesMold(UiFrameworkBox box) {
		super(box);
	}

	@Override
	public void init() {
		super.init();
		reel1.source(source());
		reel1.refresh();
	}

	private ReelDatasource source() {
		return new ReelDatasource() {
			@Override
			public String name() {
				return "rds1";
			}

			@Override
			public List<SignalDefinition> signals() {
				return List.of(
						signalOf("s1", "status.usage.logical.reads", "red"),
						signalOf("s2", "status.usage.logical.writes", "red"),
						SignalDefinition.empty("se1"),
						signalOf("s3", "status.temperature.internal", "green"),
						signalOf("s4", "status.temperature.external", "green"),
						SignalDefinition.empty("se2"),
						signalOf("s5", "status.fan", "blue")
				);
			}

			@Override
			public Signal signal(SignalDefinition definition) {
				return new ReelDatasource.Signal() {
					@Override
					public SignalDefinition definition() {
						return definition;
					}

					@Override
					public String reel(Scale scale, Instant start, Instant end) {
						String result = "";
						for (int i=0; i<24; i++) {
							result += i%2 != 0 ? " " : "-";
						}
						return result + " ";
					}

					@Override
					public Map<Instant, List<Annotation>> annotations(Scale scale, Instant start, Instant end) {
						LocalDateTime date = LocalDateTime.ofInstant(end, UTC);
						return new LinkedHashMap<>() {{
							put(date.minus(18, scale.temporalUnit()).toInstant(UTC), List.of(annotationOf("Warning value")));
							put(date.minus(10, scale.temporalUnit()).toInstant(UTC), List.of(annotationOf("Out of range")));
							put(date.minus(1, scale.temporalUnit()).toInstant(UTC), List.of(annotationOf("Value is not valid", "green")));
						}

							private Annotation annotationOf(String label) {
								return new Annotation(label);
							}

							private Annotation annotationOf(String label, String color) {
								return new Annotation(label, color);
							}
						};
					}
				};
			}

			@Override
			public List<Scale> scales() {
				return List.of(Scale.Hour, Scale.Day, Scale.Week, Scale.Month, Scale.Year);
			}

			@Override
			public Instant from(Scale scale) {
				return LocalDateTime.ofInstant(Instant.now(), UTC).minus(500, scale.temporalUnit()).toInstant(UTC);
			}

			@Override
			public Instant to(Scale scale) {
				return Instant.now();
			}

			@Override
			public Instant previous(Scale scale, Instant date) {
				return LocalDateTime.ofInstant(date, UTC).minus(1, scale.temporalUnit()).toInstant(UTC);
			}

			@Override
			public Instant next(Scale scale, Instant date) {
				return LocalDateTime.ofInstant(date, UTC).plus(1, scale.temporalUnit()).toInstant(UTC);
			}

		};
	}

	private SignalDefinition signalOf(String name, String label, String color) {
		return new SignalDefinition().name(name).add("es", label).color(color);
	}

}