package io.intino.alexandria.ui.model.eventline;

import io.intino.alexandria.Scale;
import io.intino.alexandria.ui.model.reel.SignalDefinition;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static java.time.ZoneOffset.UTC;

public interface EventlineDatasource {
	String name();
	Map<Instant, List<Event>> events(Instant start, Instant end);
	Scale scale();

	Instant from();
	Instant to();

	default Instant previous(Instant date) {
		return previous(date, 1);
	}

	default Instant previous(Instant date, long count) {
		return LocalDateTime.ofInstant(date, UTC).minus(count, scale().temporalUnit()).toInstant(UTC);
	}

	default Instant next(Instant date) {
		return next(date, 1);
	}

	default Instant next(Instant date, long count) {
		return LocalDateTime.ofInstant(date, UTC).plus(count, scale().temporalUnit()).toInstant(UTC);
	}

	class Event {
		private final String label;
		private final String description;
		private final String color;
		private final String icon;

		public enum Symbol { Circle, Square, Diamond, Triangle }

		public Event(String label) {
			this(label, null, "#ed6c02", null); // green
		}

		public Event(String label, String description) {
			this(label, description, "#ed6c02", null); // green
		}

		public Event(String label, String description, String color) {
			this(label, description, color, null);
		}

		private Event(String label, String description, String color, String icon) {
			this.label = label;
			this.description = description;
			this.color = color;
			this.icon = icon;
		}

		public String label() {
			return label;
		}

		public String description() {
			return description;
		}

		public String color() {
			return color;
		}

		public String icon() {
			return icon;
		}
	}

}
