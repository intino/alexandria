package io.intino.alexandria;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.lang.Integer.parseInt;
import static java.util.Collections.singletonList;

public class Timetag implements Comparable<Timetag> {
	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd000000");

	private final String tag;

	public Timetag(LocalDateTime dateTime, Scale scale) {
		this(dateTimeFormatter.format(dateTime).substring(0, sizeOf(scale)));
	}

	public Timetag(Instant instant, Scale scale) {
		this(toString(instant).substring(0, sizeOf(scale)));
	}

	public Timetag(LocalDate date, Scale scale) {
		this(dateFormatter.format(date).substring(0, sizeOf(scale)));
	}

	public Timetag(String tag) {
		this.tag = tag;
	}

	public static Timetag today() {
		return now(Scale.Day);
	}

	public static Timetag now() {
		return now(Scale.Minute);
	}

	public static Timetag now(Scale scale) {
		return new Timetag(LocalDateTime.now(), scale);
	}

	public static Timetag of(String tag) {
		if(tag == null) throw new NullPointerException("Tag cannot be null");
		if(!isTimetag(tag)) throw new IllegalArgumentException(tag + " is not a valid timetag. It should follow the pattern yyyy[MMddhhmmss]");
		return new Timetag(tag);
	}

	public static Optional<Timetag> ofOptional(String tag) {
		return isTimetag(tag) ? Optional.of(new Timetag(tag)) : Optional.empty();
	}

	public static Timetag of(LocalDateTime datetime, Scale scale) {
		return new Timetag(datetime, scale);
	}

	public static Timetag of(LocalDate date, Scale scale) {
		return new Timetag(date, scale);
	}

	public static Timetag of(Instant instant, Scale scale) {
		return new Timetag(instant, scale);
	}

	public static boolean isTimetag(String str) {
		try {
			new Timetag(str).datetime();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static Stream<Timetag> range(String fromInclusive, String toInclusive) {
		Timetag from = Timetag.of(fromInclusive);
		Timetag to = Timetag.of(toInclusive);
		if(from.scale() != to.scale()) throw new IllegalArgumentException("Both from and to timetags must have the same scale");
		return range(from, to);
	}

	public static Stream<Timetag> range(Timetag fromInclusive, Timetag toInclusive) {
		return StreamSupport.stream(fromInclusive.iterateTo(toInclusive).spliterator(), false);
	}

	public String value() {
		return tag;
	}

	public int year() {
		return parseInt(tag.substring(0, 4));
	}

	public int month() {
		return hasMonth() ? parseInt((tag).substring(4, 6)) : 1;
	}

	public int day() {
		return hasDay() ? parseInt((tag).substring(6, 8)) : 1;
	}

	public int hour() {
		return hasHour() ? parseInt((tag).substring(8, 10)) : 0;
	}

	public int minute() {
		return hasMinute() ? parseInt((tag).substring(10, 12)) : 0;
	}

	public boolean hasMonth() {
		return precision() > 0;
	}

	public boolean hasDay() {
		return precision() > 1;
	}

	public boolean hasHour() {
		return precision() > 2;
	}

	public boolean hasMinute() {
		return precision() > 3;
	}

	public Scale scale() {
		return Scale.values()[precision()];
	}

	private int precision() {
		return (tag.length() - 4) >> 1;
	}

	public LocalDateTime datetime() {
		return LocalDateTime.of(year(), month(), day(), hour(), minute());
	}

	public Instant instant() {
		return datetime().toInstant(ZoneOffset.UTC);
	}

	public LocalDate date() {
		return datetime().toLocalDate();
	}

	public String label() {
		String result = tag;
		for (int i = result.length(); i > 4; i -= 2)
			result = result.substring(0, i - 2) + "-" + result.substring(i - 2);
		return result;
	}

	public Timetag next() {
		return next(1);
	}

	public Timetag next(int count) {
		return count == 0 ? this : new Timetag(shift(count), scale());
	}

	public Timetag previous() {
		return previous(1);
	}

	public Timetag previous(int count) {
		return count == 0 ? this : new Timetag(shift(-count), scale());
	}

	public Iterable<Timetag> iterateTo(String timetag) {
		return iterateTo(Timetag.of(timetag));
	}

	public Iterable<Timetag> iterateTo(Timetag toInclusive) {
		if(equals(toInclusive)) return singletonList(this);
		if(isBefore(toInclusive)) return () -> TimetagIterator.forward(this, toInclusive);
		return () -> TimetagIterator.backwards(this, toInclusive);
	}

	public boolean isAfter(Timetag timetag) {
		return compareTo(timetag) > 0;
	}

	public boolean isBefore(Timetag timetag) {
		return compareTo(timetag) < 0;
	}

	private LocalDateTime shift(int amount) {
		return scale().temporalUnit().addTo(datetime(), amount);
	}

	public boolean isIn(Collection<Timetag> timetags) {
		return timetags.contains(this);
	}

	public boolean isBetween(String from, String toInclusive) {
		return isBetween(Timetag.of(from), Timetag.of(toInclusive));
	}

	public boolean isBetween(Timetag from, Timetag toInclusive) {
		return !isBefore(from) && !isAfter(toInclusive);
	}

	private static int sizeOf(Scale scale) {
		return scale.ordinal() * 2 + 4;
	}

	private static String toString(Instant instant) {
		return instant.toString().replaceAll("[-TZ.:]","");
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Timetag) return tag.equals(((Timetag) o).tag);
		if (o instanceof String) return tag.equals(o);
		if (o instanceof Instant) return tag.equals(new Timetag((Instant) o, scale()).tag);
		if (o instanceof LocalDateTime) return tag.equals(new Timetag((LocalDateTime) o, scale()).tag);
		if (o instanceof LocalDate) return tag.equals(new Timetag((LocalDate) o, scale()).tag);
		return false;
	}

	@Override
	public int hashCode() {
		return tag.hashCode();
	}

	@Override
	public String toString() {
		return tag;
	}

	@Override
	public int compareTo(Timetag other) {
		return tag.compareTo(other.tag);
	}

	public int compare(Timetag timetag) {
		return compareTo(timetag);
	}

	private static class TimetagIterator implements Iterator<Timetag> {

		public static TimetagIterator forward(Timetag from, Timetag to) {
			return new TimetagIterator(from, to, 1);
		}

		public static TimetagIterator backwards(Timetag from, Timetag to) {
			return new TimetagIterator(from, to, -1);
		}

		private Timetag current;
		private final Timetag target;
		private final int direction;

		private TimetagIterator(Timetag current, Timetag target, int direction) {
			this.current = current;
			this.target = target;
			this.direction = direction;
		}

		@Override
		public boolean hasNext() {
			final int cmp = target.compareTo(current);
			return cmp == 0 || Math.signum(cmp) == direction;
		}

		@Override
		public Timetag next() {
			Timetag result = current;
			current = current.next(direction);
			return result;
		}
	}
}
