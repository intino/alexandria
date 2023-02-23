package io.intino.alexandria.zit.model;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.*;

public class Period {
	private static final Map<String, ChronoUnit> Units = units();
	public final int amount;
	public final ChronoUnit unit;


	public static Period of(int amount, ChronoUnit unit) {
		return new Period(amount, unit);
	}

	public static Period of(short amount, short chronoUnit) {
		return new Period(amount, ChronoUnit.values()[chronoUnit]);
	}

	public static Period of(String data) {
		return parse(data.toUpperCase().toCharArray());
	}

	public Period(int amount, ChronoUnit unit) {
		this.amount = amount;
		this.unit = unit;
	}

	private static Period parse(char[] chars) {
		int amount = amountIn(chars);
		ChronoUnit unit = Units.getOrDefault(unitIn(chars), DAYS);
		return new Period(amount, unit);
	}

	private static String unitIn(char[] chars) {
		StringBuilder unit = new StringBuilder();
		for (char c : chars) {
			if (c < 'A' || c > 'Z') continue;
			unit.append(c);
		}
		String s = unit.toString();
		return s.endsWith("S") ? singular(s) : s;
	}

	private static int amountIn(char[] chars) {
		int amount = 0;
		for (char c : chars) {
			if (c < '0' || c > '9') break;
			amount = amount * 10 + (c - '0');
		}
		return amount == 0 ? 1 : amount;
	}

	@Override
	public String toString() {
		return amount + " " + toString(unit.toString().toLowerCase());
	}

	private String toString(String unit) {
		return amount == 1 ? singular(unit) : unit;
	}

	private static String singular(String unit) {
		return unit.substring(0, unit.length() - 1);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		return this == o || equals((Period) o);
	}

	private boolean equals(Period period) {
		return amount == period.amount && unit == period.unit;
	}

	@Override
	public int hashCode() {
		return Objects.hash(amount, unit);
	}

	public Instant next(Instant instant) {
		return instant.plus(amount, unit);
	}

	public long duration() {
		return amount * unit.getDuration().toSeconds();
	}

	private static Map<String, ChronoUnit> units() {
		return Map.of("SECOND", SECONDS, "MINUTE", MINUTES, "HOUR", HOURS, "DAY", DAYS, "WEEK", WEEKS, "MONTH", MONTHS, "YEAR", YEARS);
	}

}