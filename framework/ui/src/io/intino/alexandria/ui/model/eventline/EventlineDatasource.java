package io.intino.alexandria.ui.model.eventline;

import io.intino.alexandria.Scale;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
		private final String id;
		private final Instant date;
		private final String label;
		private final String category;
		private String color;
		private String icon = "CheckBoxOutlineBlank";
		private String iconTitle = "";
		private String comments;
		private List<String> operationList = new ArrayList<>();

		public enum Symbol { Circle, Square, Diamond, Triangle }

		public Event(String id, Instant date, String label) {
			this(id, date, label, null, "#ed6c02"); // green
		}

		public Event(String id, Instant date, String label, String category) {
			this(id, date, label, category, "#ed6c02"); // green
		}

		public Event(String id, Instant date, String label, String category, String color) {
			this.id = id;
			this.date = date;
			this.label = label;
			this.category = category;
			this.color = color;
		}

		public String id() {
			return id;
		}

		public Instant date() {
			return date;
		}

		public String label() {
			return label;
		}

		public String category() {
			return category;
		}

		public String color() {
			return color;
		}

		public Event color(String color) {
			this.color = color;
			return this;
		}

		public Event icon(String icon, String title) {
			this.icon = icon;
			this.iconTitle = title;
			return this;
		}

		public String icon() {
			return icon;
		}

		public String iconTitle() {
			return iconTitle;
		}

		public String comments() {
			return comments;
		}

		public Event comments(String comments) {
			this.comments = comments;
			return this;
		}

		public Event operations(List<String> operationList) {
			this.operationList = operationList;
			return this;
		}

		public List<String> operations() {
			return operationList;
		}
	}

}
