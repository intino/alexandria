package io.test;

import io.intino.alexandria.sumus.*;
import io.intino.alexandria.sumus.dimensions.*;
import io.intino.alexandria.sumus.ledgers.columnar.Column;
import io.intino.alexandria.sumus.parser.DimensionDefinition;
import org.junit.Test;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import static io.intino.alexandria.sumus.Attribute.Type.*;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class Dimension_ {

	@Test
	public void should_create_empty_dimension() {
		assertThat(new CategoricalDimension(lookup("type", category)).slices()).isEqualTo(List.of());
		assertThat(str(new CategoricalDimension(lookup("type", category,"A","B")).slices())).isEqualTo(List.of("A","B"));
		assertThat(new YearDimension(lookup("year", date)).slices()).isEqualTo(List.of());
	}

	@Test
	public void should_create_categorical_dimension_with_NA() {
		Column column = new Column(attribute("type", category), "A ","B"," A","C ","B","",null);
		Dimension dimension = new CategoricalDimension(column);
		assertThat(dimension.isOrdinal()).isFalse();
		assertThat(dimension.hasNA()).isTrue();
		assertThat(dimension.slice("A").name()).isEqualTo("A");
		assertThat(toString(dimension.slice("A").index())).isEqualTo("0,2");
		assertThat(dimension.slice("B").name()).isEqualTo("B");
		assertThat(toString(dimension.slice("B").index())).isEqualTo("1,4");
		assertThat(dimension.slice("C").name()).isEqualTo("C");
		assertThat(toString(dimension.slice("C").index())).isEqualTo("3");
		assertThat(dimension.slice("NA").isNA()).isTrue();
		assertThat(toString(dimension.slice("NA").index())).isEqualTo("5,6");
	}

	@Test
	public void should_create_categorical_dimension() {
		Column column = new Column(attribute("type", category), "A ","B"," A","C ","B");
		Dimension dimension = new CategoricalDimension(column);
		assertThat(dimension.isOrdinal()).isFalse();
		assertThat(dimension.levels()).isEqualTo(1);
		assertThat(dimension.hasNA()).isFalse();
		assertThat(dimension.slice("A").name()).isEqualTo("A");
		assertThat(toString(dimension.slice("A").index())).isEqualTo("0,2");
		assertThat(dimension.slice("B").name()).isEqualTo("B");
		assertThat(toString(dimension.slice("B").index())).isEqualTo("1,4");
		assertThat(dimension.slice("C").name()).isEqualTo("C");
		assertThat(toString(dimension.slice("C").index())).isEqualTo("3");
	}

	@Test
	public void should_create_categorical_hierarchical_dimension() {
		Column column = new Column(attribute("type", category), "A.1","B.2"," A.2","B.1 ","A.1","A.1.X","A.1.Y");
		Dimension dimension = new CategoricalDimension(column);
		assertThat(dimension.isOrdinal()).isFalse();
		assertThat(dimension.levels()).isEqualTo(3);
		assertThat(str(dimension.slices())).isEqualTo(List.of("A", "B", "A.1", "B.2", "A.2", "B.1", "A.1.X", "A.1.Y"));
		assertThat(str(dimension.slices(1))).isEqualTo(List.of("A","B"));
		assertThat(str(dimension.slices(2))).isEqualTo(List.of("A.1", "B.2", "A.2", "B.1"));
		assertThat(str(dimension.slices(3))).isEqualTo(List.of("B.2", "A.2", "B.1", "A.1.X", "A.1.Y"));
		assertThat(dimension.hasNA()).isFalse();
		assertThat(dimension.slice("A").name()).isEqualTo("A");
		assertThat(toString(dimension.slice("A").index())).isEqualTo("0,2,4,5,6");
		assertThat(dimension.slice("B").name()).isEqualTo("B");
		assertThat(toString(dimension.slice("B").index())).isEqualTo("1,3");
		assertThat(dimension.slice("A.1").name()).isEqualTo("A.1");
		assertThat(toString(dimension.slice("A.1").index())).isEqualTo("0,4,5,6");
	}

	private List<String> str(List<Slice> slices) {
		return slices.stream().map(Slice::name).collect(toList());
	}

	@Test
	public void should_create_categorical_hierarchical_dimension_with_NA() {
		Column column = new Column(attribute("type", category), "A.1","B.2"," A.2","B.1 ",null,"A.1");
		Dimension dimension = new CategoricalDimension(column);
		assertThat(dimension.isOrdinal()).isFalse();
		assertThat(dimension.hasNA()).isTrue();
		assertThat(dimension.slice("A").name()).isEqualTo("A");
		assertThat(toString(dimension.slice("A").index())).isEqualTo("0,2,5");
		assertThat(dimension.slice("B").name()).isEqualTo("B");
		assertThat(toString(dimension.slice("B").index())).isEqualTo("1,3");
		assertThat(dimension.slice("A.1").name()).isEqualTo("A.1");
		assertThat(toString(dimension.slice("A.1").index())).isEqualTo("0,5");
		assertThat(dimension.slice("NA").isNA()).isTrue();
		assertThat(toString(dimension.slice("NA").index())).isEqualTo("4");
	}

	@Test
	public void should_create_year_dimension() {
		Column column = new Column(attribute("date", date), "2020-01-01","2020/01/02","2020-05-03","2022-04-04","2020-03-01");
		Dimension dimension = new YearDimension(column);
		assertThat(dimension.name()).isEqualTo("date-year");
		assertThat(dimension.isOrdinal()).isTrue();
		assertThat(dimension.hasNA()).isFalse();
		assertThat(dimension.slice("2020").name()).isEqualTo("2020");
		assertThat(toString(dimension.slice("2020").index())).isEqualTo("0,1,2,4");
		assertThat(dimension.slice("2021").name()).isEqualTo("2021");
		assertThat(toString(dimension.slice("2021").index())).isEqualTo("");
		assertThat(dimension.slice("2022").name()).isEqualTo("2022");
		assertThat(toString(dimension.slice("2022").index())).isEqualTo("3");
	}

	@Test
	public void should_create_year_dimension_with_NA() {
		Column column = new Column(attribute("date", date), "2020-01-01","2020/01/02","2020-05-03","2022-04-04","2020-03-01","");
		Dimension dimension = new YearDimension(column);
		assertThat(dimension.name()).isEqualTo("date-year");
		assertThat(dimension.isOrdinal()).isTrue();
		assertThat(dimension.hasNA()).isTrue();
		assertThat(dimension.slice("2020").name()).isEqualTo("2020");
		assertThat(toString(dimension.slice("2020").index())).isEqualTo("0,1,2,4");
		assertThat(dimension.slice("2021").name()).isEqualTo("2021");
		assertThat(toString(dimension.slice("2021").index())).isEqualTo("");
		assertThat(dimension.slice("2022").name()).isEqualTo("2022");
		assertThat(toString(dimension.slice("2022").index())).isEqualTo("3");
		assertThat(dimension.slice("NA").isNA()).isTrue();
		assertThat(toString(dimension.slice("NA").index())).isEqualTo("5");
	}

	@Test
	public void should_create_day_dimension_with_NA() {
		Column column = new Column(attribute("date", date), "2020-01-01","2020/01/02","2020-05-03","2020-04-04","2020-03-01","");
		Dimension dimension = new DayDimension(column);
		assertThat(dimension.name()).isEqualTo("date-day");
		assertThat(dimension.isOrdinal()).isTrue();
		assertThat(dimension.hasNA()).isTrue();
		assertThat(dimension.slice("2020-01-01").name()).isEqualTo("2020-01-01");
		assertThat(toString(dimension.slice("2020-01-01").index())).isEqualTo("0");
		assertThat(dimension.slice("2020-01-02").name()).isEqualTo("2020-01-02");
		assertThat(toString(dimension.slice("2020-01-02").index())).isEqualTo("1");
		assertThat(dimension.slice("NA").isNA()).isTrue();
		assertThat(toString(dimension.slice("NA").index())).isEqualTo("5");
	}

	@Test
	public void should_create_day_dimension() {
		Column column = new Column(attribute("d", date), "2020-01-01","2020/01/02","2020-05-03","2020-04-04","2020-03-01");
		Dimension dimension = new DayDimension(column);
		assertThat(dimension.name()).isEqualTo("d-day");
		assertThat(dimension.isOrdinal()).isTrue();
		assertThat(dimension.hasNA()).isFalse();
		assertThat(dimension.slice("2020-01-01").name()).isEqualTo("2020-01-01");
		assertThat(toString(dimension.slice("2020-01-01").index())).isEqualTo("0");
		assertThat(dimension.slice("2020-01-02").name()).isEqualTo("2020-01-02");
		assertThat(toString(dimension.slice("2020-01-02").index())).isEqualTo("1");
	}

	public static String toString(Index index) {
		StringBuilder sb = new StringBuilder();
		try {
			for (int i = 0; i < 100; i++) {
				if (index.contains(i)) sb.append(',').append(i);
			}
		}
		catch (Exception ignored) {

		}
		return sb.length() > 0 ? sb.substring(1) : "";
	}

	@Test
	public void should_create_month_of_year_dimension_with_NA() {
		Column column = new Column(attribute("date", date), "2020-01-01","2020/01/02","2020-05-03","2022-04-04","2020-03-01","");
		Dimension dimension = new MonthOfYearDimension(column);
		assertThat(dimension.name()).isEqualTo("date-month-of-year");
		assertThat(dimension.isOrdinal()).isTrue();
		assertThat(dimension.hasNA()).isTrue();
		assertThat(dimension.slice("january").name()).isEqualTo("january");
		assertThat(toString(dimension.slice("january").index())).isEqualTo("0,1");
		assertThat(dimension.slice("december").name()).isEqualTo("december");
		assertThat(toString(dimension.slice("december").index())).isEqualTo("");
		assertThat(dimension.slice("NA").isNA()).isTrue();
		assertThat(toString(dimension.slice("NA").index())).isEqualTo("5");
	}

	@Test
	public void should_create_month_of_year_dimension() {
		Column column = new Column(attribute("date", date), "2020-01-01","2020/01/02","2020-05-03","2022-04-04","2020-03-01");
		Dimension dimension = new MonthOfYearDimension(column);
		assertThat(dimension.isOrdinal()).isTrue();
		assertThat(dimension.hasNA()).isFalse();
		assertThat(dimension.slice("january").name()).isEqualTo("january");
		assertThat(toString(dimension.slice("january").index())).isEqualTo("0,1");
		assertThat(dimension.slice("december").name()).isEqualTo("december");
		assertThat(toString(dimension.slice("december").index())).isEqualTo("");
	}

	@Test
	public void should_create_day_of_week_dimension_with_NA() {
		Column column = new Column(attribute("date", date), "2020-01-05","2020/01/07","2020-05-03","2022-04-04","2020-03-01", null);
		Dimension dimension = new DayOfWeekDimension(column);
		assertThat(dimension.name()).isEqualTo("date-day-of-week");
		assertThat(dimension.isOrdinal()).isTrue();
		assertThat(dimension.hasNA()).isTrue();
		assertThat(dimension.slice("sunday").name()).isEqualTo("sunday");
		assertThat(toString(dimension.slice("sunday").index())).isEqualTo("0,2,4");
		assertThat(dimension.slice("NA").isNA()).isTrue();
		assertThat(toString(dimension.slice("NA").index())).isEqualTo("5");
	}

	@Test
	public void should_create_day_of_week_dimension() {
		Column column = new Column(attribute("date", date), "2020-01-05","2020/01/07","2020-05-03","2022-04-04","2020-03-01");
		Dimension dimension = new DayOfWeekDimension(column);
		assertThat(dimension.name()).isEqualTo("date-day-of-week");
		assertThat(dimension.isOrdinal()).isTrue();
		assertThat(dimension.hasNA()).isFalse();
		assertThat(dimension.slice("sunday").name()).isEqualTo("sunday");
		assertThat(toString(dimension.slice("sunday").index())).isEqualTo("0,2,4");
	}

	@Test
	public void should_create_numeric_dimension() {
		Column column = new Column(attribute("value", integer), "50","30","2","80","40","60","10");
		DimensionDefinition definition = new DimensionDefinition("A", integer, "-,10,20,30,40,50,60,-");
		Dimension dimension = new NumericalDimension(column, definition.name(), definition.classifier());
		assertThat(dimension.name()).isEqualTo("value-A");
		assertThat(dimension.isOrdinal()).isTrue();
		assertThat(dimension.slice("[-10]").name()).isEqualTo("[-10]");
		assertThat(toString(dimension.slice("[-10]").index())).isEqualTo("2");
		assertThat(dimension.slice("[10-20]").name()).isEqualTo("[10-20]");
		assertThat(toString(dimension.slice("[10-20]").index())).isEqualTo("6");
		assertThat(dimension.slice("[60-]").name()).isEqualTo("[60-]");
		assertThat(toString(dimension.slice("[60-]").index())).isEqualTo("3,5");
	}

	@Test
	public void should_create_numeric_dimension_with_NA() {
		Column column = new Column(attribute("value", integer), "50","30","2","80","40","60","10",null);
		DimensionDefinition definition = new DimensionDefinition("B", integer, "-,10,20,30,40,50,60,-");
		Dimension dimension = new NumericalDimension(column, definition.name(), definition.classifier());
		assertThat(dimension.name()).isEqualTo("value-B");
		assertThat(dimension.isOrdinal()).isTrue();
		assertThat(dimension.hasNA()).isTrue();
		assertThat(dimension.slice("[-10]").name()).isEqualTo("[-10]");
		assertThat(toString(dimension.slice("[-10]").index())).isEqualTo("2");
		assertThat(dimension.slice("[10-20]").name()).isEqualTo("[10-20]");
		assertThat(toString(dimension.slice("[10-20]").index())).isEqualTo("6");
		assertThat(dimension.slice("[60-]").name()).isEqualTo("[60-]");
		assertThat(toString(dimension.slice("[60-]").index())).isEqualTo("3,5");
		assertThat(dimension.slice("NA").isNA()).isTrue();
		assertThat(toString(dimension.slice("NA").index())).isEqualTo("7");
	}

	private Attribute attribute(String name, Attribute.Type type) {
		return new Attribute(name,type);
	}

	private Lookup lookup(String name, Attribute.Type type, String... categories) {
		return new Lookup() {
			@Override
			public String name() {
				return name;
			}

			@Override
			public Attribute.Type type() {
				return type;
			}

			@Override
			public boolean hasNA() {
				return false;
			}

			@Override
			public List<Object> uniques() {
				return IntStream.range(0, categories.length)
						.mapToObj(i -> new Category(i, categories[i]))
						.collect(toList());
			}

			@Override
			public Object min() {
				return null;
			}

			@Override
			public Object max() {
				return null;
			}

			@Override
			public Index index(Predicate<Object> predicate) {
				return null;
			}

		};
	}
}
