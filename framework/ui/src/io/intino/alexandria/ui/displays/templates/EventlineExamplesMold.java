package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.Scale;
import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.UserMessage;
import io.intino.alexandria.ui.model.eventline.EventlineDatasource;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

import static java.time.ZoneOffset.UTC;

public class EventlineExamplesMold extends AbstractEventlineExamplesMold<UiFrameworkBox> {

	public static final boolean WithJumps = true;

	public EventlineExamplesMold(UiFrameworkBox box) {
		super(box);
	}

	@Override
	public void init() {
		super.init();
		eventline1.label("Events");
		eventline1.source(source());
		eventline1.onSelectEvent(e -> {
			notifyUser(String.format("Event %s selected", e.event().label()), UserMessage.Type.Info);
			eventline1.refresh(e.event().color("blue").icon("CheckBox", "New label").operations(Collections.emptyList()));
		});
		eventline1.onSelectEvents(e -> notifyUser("Events selected", UserMessage.Type.Info));
		eventline1.onExecuteEventOperation(e -> notifyUser(String.format("Execute operation %s for event %s", e.operation(), e.event().label()), UserMessage.Type.Info));
		eventline1.refresh();
//		Instant instant = new ArrayList<>(source().events(source().from(), source().to()).keySet()).get(10);
//		System.out.println(instant);
//		eventline1.select(instant);
		eventline2.label("Events");
		eventline2.onSelectEvent(e -> notifyUser(String.format("Event %s selected", e.event().label()), UserMessage.Type.Info));
		eventline2.onSelectEvents(e -> notifyUser("Events selected", UserMessage.Type.Info));
		eventline2.onExecuteEventOperation(e -> notifyUser(String.format("Execute operation %s for event %s", e.operation(), e.event().label()), UserMessage.Type.Info));
		eventline2.source(source());
		eventline2.refresh();
//		eventline2.last();
	}

	private EventlineDatasource source() {
		return new EventlineDatasource() {
			@Override
			public String name() {
				return "eld";
			}

			@Override
			public Map<Instant, List<Event>> events(Instant start, Instant end) {
//				if (random.nextInt(2) == 0) return Collections.emptyMap();
				Random random = new Random();
				Instant current = start;
				Map<Instant, List<Event>> result = new HashMap<>();
				while (current.isBefore(end)) {
					result.put(current, randomEvents(current));
					current = current.plus(WithJumps ? random.nextInt(40) : 1, scale().temporalUnit());
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

	private static List<EventlineDatasource.Event> randomEvents(Instant date) {
		Random random = new Random();
		int value = random.nextInt(7);
		if (value == 0) return List.of(event("1", date, "infrequent-value.1 hour. Trigger value 112846.0"));
		if (value == 1) return List.of(event("2", date, "Event 2", "Anomalies"), event("3", date, "Event 3", "Data wrong"));
		if (value == 2) return List.of(event("4", date, "Event 4"));
		if (value == 3) return List.of(event("5", date, "Event 5"), event("6", date, "Event 6", "Data wrong"), event("7", date, "Event 7"));
		if (value == 4) return List.of(event("8", date, "Event 8"));
		if (value == 5) return List.of(event("9", date, "Event 9. This is an event description", "Anomalies"));
		if (value == 6) return List.of(event("10", date, "Event 10. This is an event description"));
		return List.of(event("11", date, "Event 11"));
	}

	private static EventlineDatasource.Event event(String id, Instant date, String label) {
		return new EventlineDatasource.Event(id, date, label).operations(List.of("Create order..."));
	}

	private static EventlineDatasource.Event event(String id, Instant date, String label, String category) {
		return new EventlineDatasource.Event(id, date, label, category).operations(List.of("Create order..."));
	}

}