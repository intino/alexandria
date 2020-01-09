package io.intino.alexandria;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

import static java.lang.Integer.parseInt;

public class Timetag {
	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
	private final String tag;

	public Timetag(LocalDateTime dateTime, Scale scale) {
		this.tag = dateTimeFormatter.format(dateTime).substring(0, sizeOf(scale));
	}

	public Timetag(Instant instant, Scale scale) {
		this.tag = dateTimeFormatter.format(instant).substring(0, sizeOf(scale));
	}

	public Timetag(LocalDate date, Scale scale) {
		this.tag = dateFormatter.format(date).substring(0, sizeOf(scale));
	}

	public Timetag(String tag) {
		this.tag = tag;
	}

	public static Timetag of(String tag) {
		return new Timetag(tag);
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

	public LocalDateTime datetime() {
		return LocalDateTime.of(year(), month(), day(), hour(), minute());
	}

	public Scale scale() {
		return Scale.values()[precision()];
	}

	private int precision() {
		return (tag.length() - 4) / 2;
	}

	public String value() {
		return tag;
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
		return new Timetag(calculate(+count), scale());
	}

	public Timetag previous() {
		return previous(1);
	}

	public Timetag previous(int count) {
		return new Timetag(calculate(-count), scale());
	}

	public Iterable<Timetag> iterateTo(Timetag to) {
		return () -> new Iterator<Timetag>() {
			Timetag current = Timetag.this;

			@Override
			public boolean hasNext() {
				return !current.isAfter(to);
			}

			@Override
			public Timetag next() {
				Timetag result = current;
				current = current.next();
				return result;
			}
		};
	}

	public boolean isAfter(Timetag timetag) {
		return datetime().isAfter(timetag.datetime());
	}

	public boolean isBefore(Timetag timetag) {
		return datetime().isBefore(timetag.datetime());
	}

	public int compare(Timetag timetag) {
		return this.datetime().compareTo(timetag.datetime());
	}

	private LocalDateTime calculate(int amount) {
		return scale().temporalUnit().addTo(datetime(), amount);
	}


	private int sizeOf(Scale scale) {
		return scale.ordinal() * 2 + 4;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof String) return tag.equals(o);
		if (o instanceof Timetag) return tag.equals(((Timetag) o).tag);
		if (o instanceof Instant) return tag.equals(new Timetag((Instant) o, scale()).tag);
		return false;
	}

	public boolean isIn(List<Timetag> timetags) {
		return timetags.stream().anyMatch(t -> t.equals(this));
	}

	@Override
	public int hashCode() {
		return tag.hashCode();
	}

	@Override
	public String toString() {
		return tag;
	}

}
