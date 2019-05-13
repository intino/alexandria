package io.intino.alexandria;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public enum Scale {
	Year(ChronoUnit.YEARS),
	Month(ChronoUnit.MONTHS),
	Day(ChronoUnit.DAYS),
	Hour(ChronoUnit.HOURS),
	Minute(ChronoUnit.MINUTES);

	private TemporalUnit unit;

	Scale(TemporalUnit unit) {
		this.unit = unit;
	}

	public TemporalUnit temporalUnit() {
		return unit;
	}
}
