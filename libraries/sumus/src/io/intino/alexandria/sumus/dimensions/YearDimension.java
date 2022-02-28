package io.intino.alexandria.sumus.dimensions;

import io.intino.alexandria.sumus.Lookup;
import io.intino.alexandria.sumus.Slice;
import io.intino.alexandria.sumus.SumusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

import static io.intino.alexandria.sumus.Attribute.Type.date;
import static java.util.stream.Collectors.toList;

public class YearDimension extends AbstractDimension {
	public YearDimension(Lookup lookup) {
		super(lookup);
		this.slices.addAll(buildSlices());
		if (lookup.hasNA()) this.slices.add(new DimensionSlice());
	}

	private List<Slice> buildSlices() {
		return years().boxed().map(this::sliceOf).collect(toList());
	}

	public String name() {
		return lookup.name() + "-year";
	}

	@Override
	protected void check() {
		if (lookup.type() == date) return;
		throw new SumusException("DayOfWeek dimension must use a date column");
	}

	private Slice sliceOf(int year) {
		return new DimensionSlice(String.valueOf(year), v -> match(year, v));
	}

	private boolean match(int year, Object value) {
		return value instanceof Long && LocalDate.ofEpochDay((long) value).getYear() == year;
	}

	private IntStream years() {
		try {
			int min = LocalDate.ofEpochDay((Long) lookup.min()).getYear();
			int max = LocalDate.ofEpochDay((Long) lookup.max()).getYear();
			return IntStream.range(min, max + 1);
		} catch (Exception e) {
			return IntStream.empty();
		}
	}
}
