package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.Scale;
import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.model.eventline.EventlineDatasource;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

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
				Random random = new Random();
//				if (random.nextInt(2) == 0) return Collections.emptyMap();
				Instant current = start;
				Map<Instant, List<Event>> result = new HashMap<>();
				while (current.isBefore(end)) {
					result.put(current, RandomEvents.get(random.nextInt(RandomEvents.size())));
					current = current.plus(1, scale().temporalUnit());
				}
				return result;
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

		};
	}

	private static final List<List<EventlineDatasource.Event>> RandomEvents = new ArrayList<>() {{
		add(List.of(event("Event 1")));
		add(List.of(event("Event 2"), event("Event 3")));
		add(List.of(event("Event 4")));
		add(List.of(event("Event 5"), event("Event 6"), event("Event 7")));
		add(List.of(event("Event 8")));
		add(List.of(event("Event 9", "This is an event description")));
		add(List.of(event("Event 10", "This is an event description")));
		add(List.of(event("Event 11")));
	}};

	private static EventlineDatasource.Event event(String label) {
		return new EventlineDatasource.Event(label);
	}

	private static EventlineDatasource.Event event(String label, String color) {
		return new EventlineDatasource.Event(label, color);
	}

}