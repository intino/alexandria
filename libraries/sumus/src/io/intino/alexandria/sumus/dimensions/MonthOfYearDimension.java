package io.intino.alexandria.sumus.dimensions;

import io.intino.alexandria.sumus.Lookup;
import io.intino.alexandria.sumus.SumusException;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static io.intino.alexandria.sumus.Attribute.Type.date;
import static java.util.stream.Collectors.toList;

public class MonthOfYearDimension extends AbstractDimension {
	public MonthOfYearDimension(Lookup lookup) {
		super(lookup);
		this.slices.addAll(buildSlices());
		if (lookup.hasNA()) this.slices.add(new DimensionSlice());
	}

	public String name() {
		return lookup.name() + "-month-of-year";
	}


	private List<DimensionSlice> buildSlices() {
		return Arrays.stream(Month.values()).map(this::pointOf).collect(toList());
	}

	@Override
	protected void check() {
		if (lookup.type() == date) return;
		throw new SumusException("MonthOfYear dimension must use a date column");
	}

	private DimensionSlice pointOf(Month month) {
		return new DimensionSlice(month.name().toLowerCase(), v -> match(month, v));
	}

	private boolean match(Month month, Object value) {
		return value instanceof Long && LocalDate.ofEpochDay((long) value).getMonth() == month;
	}
}
