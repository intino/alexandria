package io.intino.alexandria.sumus.dimensions;

import io.intino.alexandria.sumus.Lookup;
import io.intino.alexandria.sumus.Slice;
import io.intino.alexandria.sumus.SumusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.LongStream;

import static io.intino.alexandria.sumus.Attribute.Type.date;
import static java.util.stream.Collectors.toList;

public class DayDimension extends AbstractDimension {
	public DayDimension(Lookup lookup) {
		super(lookup);
		this.slices.addAll(buildSlices());
		if (lookup.hasNA()) this.slices.add(new DimensionSlice());
	}

	private List<Slice> buildSlices() {
		return days().boxed().map(this::slice).collect(toList());
	}

	@Override
	public String name() {
		return lookup.name() + "-day";
	}

	@Override
	protected void check() {
		if (lookup.type() == date) return;
		throw new SumusException("DayOfWeek dimension must use a date column");
	}

	private Slice slice(long epoch) {
		LocalDate date = LocalDate.ofEpochDay(epoch);
		return new DimensionSlice(date.toString(), v -> match(epoch, v));
	}

	private boolean match(long epoch, Object value) {
		return value instanceof Long && epoch == (long) value;
	}

	private LongStream days() {
		try {
			long min = (long) lookup.min();
			long max = (long) lookup.max();
			return LongStream.range(min, max + 1);
		} catch (Exception e) {
			return LongStream.empty();
		}
	}

}
