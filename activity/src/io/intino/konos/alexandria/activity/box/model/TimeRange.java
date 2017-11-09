package io.intino.konos.alexandria.activity.box.model;

import java.time.Instant;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class TimeRange {

	private final Instant from;
	private final Instant to;
	private final TimeScale scale;

	public TimeRange(Instant from, Instant to, TimeScale scale) {
		this.from = from;
		this.to = to;
		this.scale = scale;
	}

	public Instant from() {
		return from;
	}

	public Instant to() {
		return to;
	}

	public TimeScale scale() {
		return scale;
	}

	public boolean inside(Instant instant) {
		return from().equals(instant) || to().equals(instant) || from().isBefore(instant) && to().isAfter(instant);
	}

	public void iterateRange(Consumer<Instant> action) {
		allInstants().forEach(action);
	}

	public Stream<Instant> allInstants() {
		return Stream.iterate(scale.normalise(from), scale::nextTime).limit(scale.instantsBetween(from, to));
	}

	public boolean isEmpty() {
		return from.equals(to) || to.isBefore(from);
	}
}
