package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.Scale;
import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.model.eventline.EventlineDatasource;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.time.ZoneOffset.UTC;

public class EventlineExamplesMold extends AbstractEventlineExamplesMold<UiFrameworkBox> {

	public EventlineExamplesMold(UiFrameworkBox box) {
		super(box);
	}

	@Override
	public void init() {
		super.init();
		eventline1.label("Events");
		eventline1.source(source());
		eventline1.refresh();
		eventline1.page(2);
		eventline2.label("Events");
		eventline2.source(source());
		eventline2.refresh();
		eventline2.page(2);
	}

	private EventlineDatasource source() {
		return new EventlineDatasource() {
			@Override
			public String name() {
				return "eld";
			}

			@Override
			public Map<Instant, List<Event>> events(Instant start, Instant end) {
				LocalDateTime date = LocalDateTime.ofInstant(end, UTC);
				Scale scale = scale();
				return new HashMap<>() {{
					put(date.minus(22, scale.temporalUnit()).toInstant(UTC), List.of(event("Event 1")));
					put(date.minus(21, scale.temporalUnit()).toInstant(UTC), List.of(event("Event 2"), event("Event 3")));
					put(date.minus(20, scale.temporalUnit()).toInstant(UTC), List.of(event("Event 4")));
					put(date.minus(19, scale.temporalUnit()).toInstant(UTC), List.of(event("Event 5"), event("Event 6"), event("Event 7")));
					put(date.minus(17, scale.temporalUnit()).toInstant(UTC), List.of(event("Event 8")));
					put(date.minus(2, scale.temporalUnit()).toInstant(UTC), List.of(event("Event 9", "This is an event description")));
					put(date.minus(1, scale.temporalUnit()).toInstant(UTC), List.of(event("Event 10", "This is an event description")));
					put(date.minus(0, scale.temporalUnit()).toInstant(UTC), List.of(event("Event 11")));
				}};
			}

			@Override
			public Scale scale() {
				return Scale.Hour;
			}

			@Override
			public Instant from() {
				return LocalDateTime.ofInstant(Instant.now(), UTC).minus(500, scale().temporalUnit()).toInstant(UTC);
			}

			@Override
			public Instant to() {
				return Instant.now();
			}

			private Event event(String label) {
				return new Event(label);
			}

			private Event event(String label, String color) {
				return new Event(label, color);
			}

		};
	}
}