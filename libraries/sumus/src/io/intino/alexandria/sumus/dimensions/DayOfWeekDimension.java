package io.intino.alexandria.sumus.dimensions;

import io.intino.alexandria.sumus.Lookup;
import io.intino.alexandria.sumus.Slice;
import io.intino.alexandria.sumus.SumusException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static io.intino.alexandria.sumus.Attribute.Type.date;
import static java.util.stream.Collectors.toList;

public class DayOfWeekDimension extends AbstractDimension {
	public DayOfWeekDimension(Lookup lookup) {
		super(lookup);
		this.slices.addAll(buildSlices());
		if (lookup.hasNA()) this.slices.add(new DimensionSlice());
	}

	public String name() {
		return lookup.name() + "-day-of-week";
	}

	private List<Slice> buildSlices() {
		return Arrays.stream(java.time.DayOfWeek.values())
				.map(this::sliceOf)
				.collect(toList());
	}

	@Override
	protected void check() {
		if (lookup.type() == date) return;
		throw new SumusException("DayOfWeek dimension must use a date column");
	}

	private Slice sliceOf(java.time.DayOfWeek dayOfWeek) {
		return new DimensionSlice(dayOfWeek.name().toLowerCase(), v -> match(dayOfWeek, v));
	}

	private boolean match(java.time.DayOfWeek dayOfWeek, Object value) {
		return value instanceof Long && LocalDate.ofEpochDay((long) value).getDayOfWeek() == dayOfWeek;
	}

}
