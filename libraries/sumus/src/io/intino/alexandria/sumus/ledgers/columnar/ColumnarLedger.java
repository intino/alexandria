package io.intino.alexandria.sumus.ledgers.columnar;

import io.intino.alexandria.sumus.*;
import io.intino.alexandria.sumus.builders.CubeBuilder;
import io.intino.alexandria.sumus.parser.DimensionDefinition;
import io.intino.alexandria.sumus.parser.LedgerDefinition;
import io.intino.alexandria.sumus.dimensions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import static io.intino.alexandria.sumus.Attribute.Type.category;
import static io.intino.alexandria.sumus.Attribute.Type.date;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.*;

public class ColumnarLedger implements Ledger {
	public final LedgerDefinition definition;
	public final List<Column> columns;
	public final List<Dimension> dimensions;
	private int size;

	public ColumnarLedger(LedgerDefinition definition) {
		this.definition = definition;
		this.columns = new ArrayList<>();
		this.dimensions = new ArrayList<>();
	}

	public ColumnarLedger read(File file, String separator) throws IOException {
		return read(Files.readAllLines(file.toPath()).stream().map(s -> s.split(separator)).collect(toList()));
	}

	public ColumnarLedger read(List<String[]> rows) {
		List<String[]> data = transpose(rows);
		check(data);
		for (int i = 0; i < data.size(); i++) {
			Attribute attribute = new Attribute(this.definition.attributes.get(i));
			add(new Column(attribute, data.get(i)));
		}
		return this;
	}

	private void check(List<String[]> columns) {
		if (columns.size() == definition.attributes.size()) return;
		throw new SumusException(String.format("Columns found %d are less than attributes defined %d", columns.size(), definition.attributes.size()));
	}

	private static List<String[]> transpose(List<String[]> rows) {
		List<String[]> columns = new ArrayList<>();
		for (int i = 0; i < rows.size(); i++) {
			String[] row = rows.get(i);
			for (int j = columns.size(); j < row.length; j++) columns.add(new String[rows.size()]);
			for (int j = 0; j < row.length; j++) columns.get(j)[i] = row[j];
		}
		return columns;
	}

	public ColumnarLedger add(Column column) {
		if (columns.isEmpty()) size = column.size();
		this.columns.add(column);
		this.dimensions.addAll(dimensionsOf(column));
		return this;
	}

	private List<Dimension> dimensionsOf(Column column) {
		if (column.type() == category) return categoricalDimensionsOf(column);
		if (column.type() == date) return dateDimensionsOf(column);
		if (column.type().isNumeric()) return numericDimensionsOf(column);
		return emptyList();
	}

	private List<Dimension> categoricalDimensionsOf(Column column) {
		return List.of(new CategoricalDimension(column));
	}

	private List<Dimension> dateDimensionsOf(Column column) {
		return List.of(
				new DayOfWeekDimension(column),
				new MonthOfYearDimension(column),
				new YearDimension(column),
				new DayDimension(column)
		);
	}

	private List<Dimension> numericDimensionsOf(Column column) {
		return Arrays.stream(column.attribute.dimensions)
				.map(d -> dimension(column, d))
				.collect(toList());
	}

	private Dimension dimension(Column column, DimensionDefinition definition) {
		return new NumericalDimension(column, definition.name(), definition.classifier());
	}

	@Override
	public Query cube() {
		return new Query() {
			private List<Dimension> dimensions = emptyList();
			private Filter filter = Filter.None;

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
				return new CubeBuilder(ColumnarLedger.this, filter, dimensions).get();
			}
		};
	}

	@Override
	public Iterable<Fact> facts(Filter filter) {
		return () -> new FactIterator(filter);
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public List<Attribute> attributes() {
		return columns.stream().map(c -> c.attribute).collect(toList());
	}

	@Override
	public List<Dimension> dimensions() {
		return dimensions;
	}

	private final Map<String, Column> columnMap = new HashMap<>();

	Column column(String name) {
		if (columnMap.isEmpty()) loadColumnMap();
		return columnMap.getOrDefault(name, Column.Null);
	}

	private void loadColumnMap() {
		columnMap.clear();
		columnMap.putAll(columns.stream().collect(toMap(c -> c.attribute.name, c -> c)));
	}

	private class FactIterator implements Iterator<Fact> {
		private final Filter filter;
		private int idx = 0;
		private Fact fact;

		public FactIterator(Filter filter) {
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
			while (idx < size)
				if (filter.accepts(idx++)) return fact(idx - 1);
			return null;
		}

		private Fact fact(int id) {
			return new Fact() {
				@Override
				public int id() {
					return id;
				}

				@Override
				public List<Attribute> attributes() {
					return ColumnarLedger.this.attributes();
				}

				@Override
				public Object value(String attribute) {
					return column(attribute).value(id);
				}

				private Object format(Object value) {
					return value == null ? "" : value.toString();
				}

				@Override
				public String toString() {
					return attributes().stream()
							.map(a -> a.name + ":" + format(value(a.name)))
							.collect(joining(","));
				}
			};

		}
	}
}
