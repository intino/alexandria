package io.test;

import io.intino.alexandria.sumus.Cube;
import io.intino.alexandria.sumus.Fact;
import io.intino.alexandria.sumus.Slice;
import io.intino.alexandria.sumus.filters.SliceFilter;
import io.intino.alexandria.sumus.ledgers.columnar.ColumnarLedger;
import io.intino.alexandria.sumus.parser.LedgerDefinition;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class ColumnarLedger_ {
	@Test
	public void should_iterate_facts() {
		ColumnarLedger ledger = new ColumnarLedger(LedgerDefinition.of(DefinitionWithNoMeasures));
		ledger.read(content(LedgerWithNoMeasures));
		assertThat(str(ledger.dimensions)).isEqualTo(List.of("type", "class"));
		assertThat(str(ledger.dimensions.get(0).slices())).isEqualTo(List.of("A", "B", "C", "A.1", "B.2", "B.1", "C.1", "NA"));
		assertThat(str(ledger.dimensions.get(1).slices())).isEqualTo(List.of("X","Y","NA"));

		int idx = 0;
		for (Fact fact : ledger.facts())
			assertThat(fact.toString()).isEqualTo(LedgerWithNoMeasures[idx++]);
	}

	@Test
	public void should_query_cube_with_no_measure_without_filter_and_without_drill() {
		ColumnarLedger ledger = new ColumnarLedger(LedgerDefinition.of(DefinitionWithNoMeasures));
		ledger.read(content(LedgerWithNoMeasures));

		Cube cube = ledger.cube().build();
		assertThat(cube.dimensions().size()).isEqualTo(0);
		assertThat(cube.cells().size()).isEqualTo(1);

		Cube.Cell cell = cube.cells().get(0);
		assertThat(cell.slices().size()).isEqualTo(0);
		assertThat(strIndicator(cell.indicators())).isEqualTo(List.of("_count(id)=6", "_count(type)=6", "_count(class)=6"));

		int idx = 0;
		for (Fact fact : cube.facts())
			assertThat(fact.toString()).isEqualTo(LedgerWithNoMeasures[idx++]);
	}

	@Test
	public void should_query_cube_with_two_measures_without_filter_and_without_drill() {
		ColumnarLedger ledger = new ColumnarLedger(LedgerDefinition.of(DefinitionWithTwoMeasures));
		ledger.read(content(LedgerWithTwoMeasures));

		Cube cube = ledger.cube().build();
		assertThat(cube.dimensions().size()).isEqualTo(0);
		assertThat(cube.cells().size()).isEqualTo(1);

		Cube.Cell cell = cube.cells().get(0);
		assertThat(cell.slices().size()).isEqualTo(0);
		assertThat(strIndicator(cell.indicators())).isEqualTo(List.of("_count(id)=6", "_count(type)=6", "_count(class)=6", "_count(m1)=6", "_sum(m1)=13.8", "_avg(m1)=2.3000000000000003", "_min(m1)=0.8", "_max(m1)=4.4", "_count(m2)=4", "_sum(m2)=18", "_avg(m2)=4.5", "_min(m2)=1", "_max(m2)=8"));

		int idx = 0;
		for (Fact fact : cube.facts())
			assertThat(fact.toString()).isEqualTo(LedgerWithTwoMeasures[idx++]);
	}

	@Test
	public void should_query_ledger_without_filter_and_without_drill() {
		ColumnarLedger ledger = new ColumnarLedger(LedgerDefinition.of(DefinitionWithTwoMeasures));
		ledger.read(content(LedgerWithTwoMeasures));

		Cube cube = ledger.cube().build();
		assertThat(cube.dimensions().size()).isEqualTo(0);
		assertThat(cube.cells().size()).isEqualTo(1);

		Cube.Cell cell = cube.cells().get(0);
		assertThat(cell.slices().size()).isEqualTo(0);
		assertThat(strIndicator(cell.indicators())).isEqualTo(List.of("_count(id)=6", "_count(type)=6", "_count(class)=6", "_count(m1)=6", "_sum(m1)=13.8", "_avg(m1)=2.3000000000000003", "_min(m1)=0.8", "_max(m1)=4.4", "_count(m2)=4", "_sum(m2)=18", "_avg(m2)=4.5", "_min(m2)=1", "_max(m2)=8"));

		int idx = 0;
		for (Fact fact : cube.facts())
			assertThat(fact.toString()).isEqualTo(LedgerWithTwoMeasures[idx++]);
	}

	@Test
	public void should_query_ledger_with_filter_and_without_drill() {
		ColumnarLedger ledger = new ColumnarLedger(LedgerDefinition.of(DefinitionWithOneMeasure));
		ledger.read(content(LedgerWithOneMeasure));

		Cube cube = ledger.cube().filter(filterOf(ledger)).build();
		assertThat(cube.dimensions().size()).isEqualTo(0);
		assertThat(cube.cells().size()).isEqualTo(1);

		Cube.Cell cell = cube.cells().get(0);
		assertThat(cell.slices().size()).isEqualTo(0);
		assertThat(strIndicator(cell.indicators())).isEqualTo(List.of("_count(id)=2", "_count(type)=2", "_count(class)=2", "_count(value)=2","_sum(value)=3.0", "_avg(value)=1.5", "_min(value)=1.0", "_max(value)=2.0"));

		Iterator<Fact> iterator = cube.facts().iterator();
		for (int i : List.of(0,1)) {
			Fact fact = iterator.next();
			assertThat(fact.toString()).isEqualTo(LedgerWithOneMeasure[i]);
		}
	}

	@Test
	public void should_query_ledger_without_filter_and_with_drill() {
		ColumnarLedger ledger = new ColumnarLedger(LedgerDefinition.of(DefinitionWithOneMeasure));
		ledger.read(content(LedgerWithOneMeasure));

		Cube cube = ledger.cube().dimensions(ledger.dimensions.get(0)).build();
		assertThat(cube.dimensions().size()).isEqualTo(1);
		assertThat(str(cube.cells())).isEqualTo(List.of("A", "B", "C", "A.1", "B.2", "B.1", "C.1", "C.2", ""));

		Cube.Cell cell_0 = cube.cells().get(0);
		assertThat(str(cell_0.slices())).isEqualTo(List.of("A"));
		assertThat(strIndicator(cell_0.indicators())).isEqualTo(List.of("_count(id)=2", "_count(type)=2", "_count(class)=2", "_count(value)=2", "_sum(value)=5.4", "_avg(value)=2.7", "_min(value)=1.0", "_max(value)=4.4"));

		Cube.Cell cell_4 = cube.cells().get(1);
		assertThat(str(cell_4.slices())).isEqualTo(List.of("B"));
		assertThat(strIndicator(cell_4.indicators())).isEqualTo(List.of("_count(id)=2", "_count(type)=2", "_count(class)=2", "_count(value)=2", "_sum(value)=4.5", "_avg(value)=2.25", "_min(value)=2.0", "_max(value)=2.5"));

		Cube.Cell cell_1 = cube.cells().get(4);
		assertThat(str(cell_1.slices())).isEqualTo(List.of("B.2"));
		assertThat(strIndicator(cell_1.indicators())).isEqualTo(List.of("_count(id)=1", "_count(type)=1", "_count(class)=1", "_count(value)=1",  "_sum(value)=2.0", "_avg(value)=2.0", "_min(value)=2.0", "_max(value)=2.0"));

		Cube.Cell cell_2 = cube.cells().get(8);
		assertThat(str(cell_2.slices())).isEqualTo(List.of());
		assertThat(strIndicator(cell_2.indicators())).isEqualTo(List.of("_count(id)=6", "_count(type)=6", "_count(class)=6", "_count(value)=6", "_sum(value)=13.8", "_avg(value)=2.3000000000000003", "_min(value)=0.8", "_max(value)=4.4"));

		Iterator<Fact> iterator = cube.facts().iterator();
		for (int i : List.of(0,1)) {
			Fact fact = iterator.next();
			assertThat(fact.toString()).isEqualTo(LedgerWithOneMeasure[i]);
		}
	}

	@Test
	public void should_query_ledger_without_filter_and_with_dimension_drill() {
		ColumnarLedger ledger = new ColumnarLedger(LedgerDefinition.of(DefinitionWithOneMeasureDimension));
		ledger.read(content(LedgerWithOneMeasure));

		Cube cube = ledger.cube().dimensions(ledger.dimensions.get(2)).build();
		assertThat(cube.dimensions().size()).isEqualTo(1);
		assertThat(str(cube.cells())).isEqualTo(List.of("[0-1]", "[1-2]", "[2-]", ""));

		Cube.Cell cell_0 = cube.cells().get(0);
		assertThat(str(cell_0.slices())).isEqualTo(List.of("[0-1]"));
		assertThat(strIndicator(cell_0.indicators())).isEqualTo(List.of("_count(id)=1","_count(type)=1","_count(class)=1","_count(value)=1","_sum(value)=0.8","_avg(value)=0.8","_min(value)=0.8","_max(value)=0.8"));

		Cube.Cell cell_1 = cube.cells().get(1);
		assertThat(str(cell_1.slices())).isEqualTo(List.of("[1-2]"));
		assertThat(strIndicator(cell_1.indicators())).isEqualTo(List.of("_count(id)=1","_count(type)=1","_count(class)=1","_count(value)=1","_sum(value)=1.0","_avg(value)=1.0","_min(value)=1.0","_max(value)=1.0"));

		Cube.Cell cell_2 = cube.cells().get(2);
		assertThat(str(cell_2.slices())).isEqualTo(List.of("[2-]"));
		assertThat(strIndicator(cell_2.indicators())).isEqualTo(List.of("_count(id)=4","_count(type)=4","_count(class)=4","_count(value)=4","_sum(value)=12.0","_avg(value)=3.0","_min(value)=2.0","_max(value)=4.4"));

		Cube.Cell cell_3 = cube.cells().get(3);
		assertThat(str(cell_3.slices())).isEqualTo(List.of());
		assertThat(strIndicator(cell_3.indicators())).isEqualTo(List.of("_count(id)=6", "_count(type)=6", "_count(class)=6", "_count(value)=6", "_sum(value)=13.8", "_avg(value)=2.3000000000000003", "_min(value)=0.8", "_max(value)=4.4"));

		Iterator<Fact> iterator = cube.facts().iterator();
		for (int i : List.of(0,1)) {
			Fact fact = iterator.next();
			assertThat(fact.toString()).isEqualTo(LedgerWithOneMeasure[i]);
		}
	}

	@Test
	public void should_query_ledger_with_filter_and_with_drill() {
		ColumnarLedger ledger = new ColumnarLedger(LedgerDefinition.of(DefinitionWithTwoMeasures));
		ledger.read(content(LedgerWithTwoMeasures));

		SliceFilter filter = filterOf(ledger);
		Cube cube = ledger.cube().filter(filter).dimensions(ledger.dimensions.get(1)).build();
		assertThat(cube.dimensions().size()).isEqualTo(1);
		assertThat(str(cube.cells())).isEqualTo(List.of("X", ""));

		Cube.Cell cell_0 = cube.cells().get(0);
		assertThat(str(cell_0.slices())).isEqualTo(List.of("X"));
		assertThat(strIndicator(cell_0.indicators())).isEqualTo(List.of("_count(id)=2","_count(type)=2","_count(class)=2","_count(m1)=2","_sum(m1)=3.0","_avg(m1)=1.5","_min(m1)=1.0","_max(m1)=2.0","_count(m2)=2","_sum(m2)=9","_avg(m2)=4.5","_min(m2)=4","_max(m2)=5"));
		assertThat(cell_0.facts().iterator().next().toString()).isEqualTo("id:001,type:1:A.1,class:1:X,m1:1.0,m2:5");
		Iterator<Fact> cell_iterator = cell_0.facts().iterator();
		assertThat(cell_iterator.next().toString()).isEqualTo("id:001,type:1:A.1,class:1:X,m1:1.0,m2:5");
		assertThat(cell_iterator.next().toString()).isEqualTo("id:002,type:2:B.2,class:1:X,m1:2.0,m2:4");
		assertThat(cell_iterator.hasNext()).isEqualTo(false);

		Cube.Cell cell_2 = cube.cells().get(1);
		assertThat(str(cell_2.slices())).isEqualTo(List.of());
		assertThat(strIndicator(cell_2.indicators())).isEqualTo(List.of("_count(id)=2", "_count(type)=2", "_count(class)=2", "_count(m1)=2", "_sum(m1)=3.0", "_avg(m1)=1.5", "_min(m1)=1.0", "_max(m1)=2.0","_count(m2)=2", "_sum(m2)=9", "_avg(m2)=4.5", "_min(m2)=4", "_max(m2)=5"));

		Iterator<Fact> iterator = cube.facts().iterator();
		for (int i : List.of(0,1)) {
			Fact fact = iterator.next();
			assertThat(fact.toString()).isEqualTo(LedgerWithTwoMeasures[i]);
		}
	}

	@Test
	public void should_query_ledger_with_two_drills() {
		ColumnarLedger ledger = new ColumnarLedger(LedgerDefinition.of(DefinitionWithOneMeasure));
		ledger.read(content(LedgerWithOneMeasure));

		Cube cube = ledger.cube().dimensions(ledger.dimensions.get(0), ledger.dimensions.get(1)).build();
		assertThat(cube.dimensions().size()).isEqualTo(2);
		assertThat(str(cube.cells())).isEqualTo(List.of("A","A-X","A-Y","B","B-X","B-Y","C","C-X","C-Y","A.1","A.1-X","A.1-Y","B.2","B.2-X","B.2-Y","B.1","B.1-X","B.1-Y","C.1","C.1-X","C.1-Y","C.2","C.2-X","C.2-Y","X","Y",""));

		Cube.Cell cell_0 = cube.cells().get(0);
		assertThat(str(cell_0.slices())).isEqualTo(List.of("A"));
		assertThat(strIndicator(cell_0.indicators())).isEqualTo(List.of("_count(id)=2", "_count(type)=2", "_count(class)=2", "_count(value)=2","_sum(value)=5.4", "_avg(value)=2.7", "_min(value)=1.0", "_max(value)=4.4"));
		Iterator<Fact> cell_iterator = cell_0.facts().iterator();
		assertThat(cell_iterator.next().toString()).isEqualTo("id:001,type:1:A.1,class:1:X,value:1.0");
		assertThat(cell_iterator.next().toString()).isEqualTo("id:005,type:1:A.1,class:2:Y,value:4.4");
		assertThat(cell_iterator.hasNext()).isEqualTo(false);

		Cube.Cell cell_1 = cube.cells().get(1);
		assertThat(str(cell_1.slices())).isEqualTo(List.of("A","X"));
		assertThat(strIndicator(cell_1.indicators())).isEqualTo(List.of("_count(id)=1", "_count(type)=1", "_count(class)=1", "_count(value)=1","_sum(value)=1.0", "_avg(value)=1.0", "_min(value)=1.0", "_max(value)=1.0"));
		assertThat(cell_1.facts().iterator().hasNext()).isEqualTo(true);

		Cube.Cell cell_2 = cube.cells().get(5);
		assertThat(str(cell_2.slices())).isEqualTo(List.of("B", "Y"));
		assertThat(strIndicator(cell_2.indicators())).isEqualTo(List.of("_count(id)=0", "_count(type)=0", "_count(class)=0", "_count(value)=0"));

		Iterator<Fact> iterator = cube.facts().iterator();
		for (int i : List.of(0,1)) {
			Fact fact = iterator.next();
			assertThat(fact.toString()).isEqualTo(LedgerWithOneMeasure[i]);
		}
	}


	private SliceFilter filterOf(ColumnarLedger ledger) {
		Slice slice_0_0 = ledger.dimensions.get(0).slices().get(0);
		Slice slice_0_1 = ledger.dimensions.get(0).slices().get(4);
		Slice slice_1_0 = ledger.dimensions.get(1).slices().get(0);
		return new SliceFilter(slice_0_0, slice_0_1, slice_1_0);
	}

	static final String DefinitionWithNoMeasures =
			"Attributes\n" +
			"id: label\n" +
			"type: category\n" +
			"class: category";

	static final String DefinitionWithOneMeasure =
			"Attributes\n" +
			"id: label\n" +
			"type: category\n" +
			"class: category\n" +
			"value: number";

	static final String DefinitionWithOneMeasureDimension =
			"Attributes\n" +
			"id: label\n" +
			"type: category\n" +
			"class: category\n" +
			"value: number [0,1,2,-][-,2,-]";

	static final String DefinitionWithTwoMeasures =
			"Attributes\n" +
			"id: label\n" +
			"type: category\n" +
			"class: category\n" +
			"m1: number\n" +
			"m2: integer";


	static final String[] LedgerWithNoMeasures = new String[] {
			"id:001,type:1:A.1,class:",
			"id:,type:2:B.2,class:1:X",
			"id:003,type:3:B.1,class:1:X",
			"id:004,type:4:C.1,class:1:X",
			"id:,type:1:A.1,class:2:Y",
			"id:006,type:,class:2:Y"
	};

	static final String[] LedgerWithOneMeasure = new String[] {
			"id:001,type:1:A.1,class:1:X,value:1.0",
			"id:,type:2:B.2,class:1:X,value:2.0",
			"id:003,type:3:B.1,class:1:X,value:2.5",
			"id:004,type:4:C.1,class:1:X,value:3.1",
			"id:005,type:1:A.1,class:2:Y,value:4.4",
			"id:,type:5:C.2,class:2:Y,value:0.8"
	};

	static final String[] LedgerWithOneMeasure_2 = new String[] {
			"id:,type:1:A.3,class:1:Y,value:5.1",
			"id:007,type:2:A.2,class:1:X,value:1.4",
			"id:008,type:3:C.1,class:1:Z,value:3.1",
			"id:009,type:4:B.4,class:1:Y,value:2.7"
	};

	static final String[] LedgerWithOneMeasure_3 = new String[] {
			"id:010,type:1:A.3,class:1:X,value:3.4",
			"id:011,type:3:B.1,class:1:Y,value:4.8",
			"id:,type:4:B.3,class:1:Z,value:6.1"
	};

	static final String[] LedgerWithTwoMeasures = new String[] {
			"id:001,type:1:A.1,class:1:X,m1:1.0,m2:5",
			"id:002,type:2:B.2,class:1:X,m1:2.0,m2:4",
			"id:003,type:3:B.1,class:1:X,m1:2.5,m2:8",
			"id:004,type:4:C.1,class:1:X,m1:3.1,m2:1",
			"id:005,type:1:A.1,class:2:Y,m1:4.4,m2:",
			"id:006,type:5:C.2,class:2:Y,m1:0.8,m2:"
	};

	static List<String[]> content(String[] definition) {
		return Arrays.stream(definition)
				.map(s->s.replaceAll("\\w+:",""))
				.map(s->s.split(","))
				.collect(toList());
	}

	static List<String> str(List<?> list) {
		return list.stream().map(Object::toString).collect(toList());
	}

	private static List<String> strIndicator(List<Cube.Indicator> indicators) {
		return indicators.stream().map(i->i.name() + "=" + i.value()).collect(toList());
	}


}
