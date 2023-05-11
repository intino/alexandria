package io.intino.alexandria;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;

public enum Scale {
	Year(ChronoUnit.YEARS, 4),
	Month(ChronoUnit.MONTHS, 6),
	Week(ChronoUnit.WEEKS, 6),
	Day(ChronoUnit.DAYS, 8),
	Hour(ChronoUnit.HOURS, 10),
	Minute(ChronoUnit.MINUTES, 12);

	private final TemporalUnit unit;
	private final int digits;

	Scale(TemporalUnit unit, int digits) {
		this.unit = unit;
		this.digits = digits;
	}

	public static Scale of(int length) {
		return Arrays.stream(Scale.values()).filter(s -> s.digits == length).findFirst().orElse(null);
	}

	public TemporalUnit temporalUnit() {
		return unit;
	}

	public int digits() {
		return digits;
	}

}
