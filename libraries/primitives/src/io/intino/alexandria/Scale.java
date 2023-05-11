package io.intino.alexandria;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

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

	public TemporalUnit temporalUnit() {
		return unit;
	}

	public int digits() {
		return digits;
	}
}
