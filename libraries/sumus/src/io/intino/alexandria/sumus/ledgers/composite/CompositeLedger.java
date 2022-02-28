package io.intino.alexandria.sumus.ledgers.composite;

import io.intino.alexandria.sumus.builders.CubeBuilder;
import io.intino.alexandria.sumus.dimensions.DayDimension;
import io.intino.alexandria.sumus.dimensions.DayOfWeekDimension;
import io.intino.alexandria.sumus.dimensions.MonthOfYearDimension;
import io.intino.alexandria.sumus.dimensions.YearDimension;
import io.intino.alexandria.sumus.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import static java.util.Collections.emptyIterator;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class CompositeLedger implements Ledger {
	private final String name;
	private int size;
	private final List<Attribute> attributes;
	private final List<Dimension> dimensions;
	private final List<LocalDate> dates;
	private final List<Ledger> ledgers;

	public CompositeLedger(String name) {
		this.name = name;
		this.size = 0;
		this.attributes = new ArrayList<>();
		this.dimensions = new ArrayList<>();
		this.dates = new ArrayList<>();
		this.ledgers = new ArrayList<>();
	}

	private Attribute dateAttribute(String name) {
		return new Attribute(name, Attribute.Type.date);
	}

	public CompositeLedger add(Ledger ledger, LocalDate date) {
		if (ledgers.isEmpty() || hasSameAttributes(ledger, ledgers.get(0))) {
			dates.add(date);
			ledgers.add(ledger);
		}
		return this;
	}

	private boolean hasSameAttributes(Ledger a, Ledger b) {
		return a.attributes().equals(b.attributes());
	}

	@Override
	public Query cube() {
		return new Query() {
			private Filter filter = Filter.None;
			private List<Dimension> dimensions;

			@Override
			public Query filter(Filter filter) {
				this.filter = filter;
				return this;
			}

			@Override
			public Query dimensions(List<Dimension> dimensions) {
				this.dimensions = dimensions;
				return this;
			}

			@Override
			public Cube build() {
				return new CubeBuilder(CompositeLedger.this, filter, dimensions).get();
			}
		};
	}

	@Override
	public Iterable<Fact> facts(Filter filter) {
		return () -> new CompositeFactIterator(filter);
	}

	@Override
	public int size() {
		if (size == 0) size = ledgers.stream().mapToInt(Ledger::size).sum();
		return size;
	}

	@Override
	public List<Attribute> attributes() {
		if (attributes.isEmpty()) {
			attributes.add(dateAttribute(name));
			attributes.addAll(ledgers.get(0).attributes());
		}
		return attributes;
	}

	@Override
	public List<Dimension> dimensions() {
		if (dimensions.isEmpty()) {
			dimensions.addAll(dateDimensions());
			dimensions.addAll(compositeDimensions(offsets()));
		}
		return dimensions;
	}

	private int[] offsets() {
		int[] offsets = new int[ledgers.size()];
		for (int i = 1; i < offsets.length; i++)
			offsets[i] = offsets[i-1] + ledgers.get(i-1).size();
		return offsets;
	}

	private List<Dimension> compositeDimensions(int[] offsets) {
		return groupDimensionsOf(ledgers).stream()
				.map(d->new CompositeDimension(d, offsets))
				.collect(toList());
	}

	private static Collection<List<Dimension>> groupDimensionsOf(List<Ledger> ledgers) {
		return ledgers.stream()
				.flatMap(c -> c.dimensions().stream())
				.collect(groupingBy(Dimension::name))
				.values();
	}

	private class CompositeFactIterator implements Iterator<Fact> {
		private int idx = 0;
		private final Filter filter;
		private Fact fact;
		private LocalDate date;
		private final Iterator<Ledger> cubeIterator = ledgers.iterator();
		private final Iterator<LocalDate> dateIterator = dates.iterator();
		private Iterator<Fact> factIterator = emptyIterator();

		public CompositeFactIterator(Filter filter) {
			this.filter = filter;
			this.fact = nextFact();
		}

		@Override
		public boolean hasNext() {
			return fact != null;
		}

		@Override
		public Fact next() {
			Fact result = fact;
			fact = nextFact();
			return result;
		}

		private Fact nextFact() {
			while (idx < size()) {
				Fact cubeFact = nextCubeFact();
				if (filter.accepts(idx++)) return wrap(idx -1, cubeFact);
			}
			return null;
		}

		private Fact nextCubeFact() {
			while (true) {
				if (factIterator.hasNext()) return factIterator.next();
				if (!cubeIterator.hasNext()) return null;
				date = dateIterator.next();
				factIterator = cubeIterator.next().facts().iterator();
			}
		}

		private Fact wrap(int id, Fact fact) {
			return new Fact() {
				@Override
				public int id() {
					return id;
				}

				@Override
				public List<Attribute> attributes() {
					return attributes;
				}

				@Override
				public Object value(String attribute) {
					return attribute.equals(name) ? date : fact.value(attribute);
				}

				@Override
				public String toString() {
					return name+ ":" + date.toString() + "," + fact.toString();
				}
			};
		}
	}

	private List<Dimension> dateDimensions() {
		Lookup lookup = lookup();
		return dateDimensionsOf(lookup);
	}

	private Lookup lookup() {
		return new Lookup() {
			@Override
			public String name() {
				return name;
			}

			@Override
			public Attribute.Type type() {
				return Attribute.Type.date;
			}

			@Override
			public boolean hasNA() {
				return false;
			}

			@Override
			public List<Object> uniques() {
				return emptyList();
			}

			@Override
			public Object min() {
				return dates.stream().mapToLong(LocalDate::toEpochDay).min().orElse(Long.MAX_VALUE);
			}

			@Override
			public Object max() {
				return dates.stream().mapToLong(LocalDate::toEpochDay).max().orElse(Long.MIN_VALUE);
			}

			@Override
			public Index index(Predicate<Object> predicate) {
				return new Index() {
					@Override
					public boolean contains(int idx) {
						return predicate.test(date(idx));
					}

					private long date(int idx) {
						int sum = 0;
						for (int i = 0; i < ledgers.size(); i++) {
							sum += ledgers.get(i).size();
							if (idx < sum) return dates.get(i).toEpochDay();
						}
						return Long.MIN_VALUE;
					}

				};
			}

		};
	}

	private List<Dimension> dateDimensionsOf(Lookup lookup) {
		return List.of(
				new DayOfWeekDimension(lookup),
				new MonthOfYearDimension(lookup),
				new YearDimension(lookup),
				new DayDimension(lookup)
		);
	}

}
