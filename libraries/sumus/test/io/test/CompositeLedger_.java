package io.test;

import io.intino.alexandria.sumus.Cube;
import io.intino.alexandria.sumus.Fact;
import io.intino.alexandria.sumus.Slice;
import io.intino.alexandria.sumus.filters.SliceFilter;
import io.intino.alexandria.sumus.ledgers.columnar.ColumnarLedger;
import io.intino.alexandria.sumus.ledgers.composite.CompositeLedger;
import io.intino.alexandria.sumus.parser.LedgerDefinition;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static io.test.ColumnarLedger_.*;
import static org.assertj.core.api.Assertions.assertThat;

public class CompositeLedger_ {

	private CompositeLedger ledger;

	@Before
	public void setUp() {
		LedgerDefinition definition = LedgerDefinition.of(DefinitionWithOneMeasure);
		ColumnarLedger ledger1 = new ColumnarLedger(definition).read(ColumnarLedger_.content(LedgerWithOneMeasure));
		ColumnarLedger ledger2 = new ColumnarLedger(definition).read(ColumnarLedger_.content(LedgerWithOneMeasure_2));
		ColumnarLedger ledger3 = new ColumnarLedger(definition).read(ColumnarLedger_.content(LedgerWithOneMeasure_3));
		this.ledger = new CompositeLedger("date")
				.add(ledger1, LocalDate.of(2021,1,1))
				.add(ledger2, LocalDate.of(2021,1,2))
				.add(ledger3, LocalDate.of(2021,1,3));

	}

	@Test
	public void should_get_dimensions() {
		assertThat(str(ledger.attributes())).isEqualTo(List.of("date", "id", "type", "class", "value"));
		assertThat(ledger.size()).isEqualTo(13);
		assertThat(ledger.dimensions().get(0).name()).isEqualTo("date-day-of-week");
		assertThat(str(ledger.dimensions().get(0).slices())).isEqualTo(List.of("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"));
		assertThat(ledger.dimensions().get(1).name()).isEqualTo("date-month-of-year");
		assertThat(str(ledger.dimensions().get(1).slices())).isEqualTo(List.of("january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"));
		assertThat(ledger.dimensions().get(2).name()).isEqualTo("date-year");
		assertThat(str(ledger.dimensions().get(2).slices())).isEqualTo(List.of("2021"));
		assertThat(ledger.dimensions().get(3).name()).isEqualTo("date-day");
		assertThat(str(ledger.dimensions().get(3).slices())).isEqualTo(List.of("2021-01-01", "2021-01-02", "2021-01-03"));
		assertThat(ledger.dimensions().get(4).name()).isEqualTo("type");
		assertThat(str(ledger.dimensions().get(4).slices())).isEqualTo(List.of("A", "B", "C", "A.1", "B.2", "B.1", "C.1", "C.2", "A.3", "A.2", "B.4", "B.3"));
		assertThat(ledger.dimensions().get(5).name()).isEqualTo("class");
		assertThat(str(ledger.dimensions().get(5).slices())).isEqualTo(List.of("X","Y","Z"));
	}

	@Test
	public void should_get_slices() {
		Slice slice_0 = ledger.dimensions().get(4).slices().get(0);
		assertThat(slice_0.name()).isEqualTo("A");
		assertThat(Dimension_.toString(slice_0.index())).isEqualTo("0,4,6,7,10");

		Slice slice_1 = ledger.dimensions().get(5).slices().get(2);
		assertThat(slice_1.name()).isEqualTo("Z");
		assertThat(Dimension_.toString(slice_1.index())).isEqualTo("8,12");

		Slice slice_2 = ledger.dimensions().get(4).slices().get(11);
		assertThat(slice_2.name()).isEqualTo("B.3");
		assertThat(Dimension_.toString(slice_2.index())).isEqualTo("12");

		Slice slice_3 = ledger.dimensions().get(4).slices().get(6);
		assertThat(slice_3.name()).isEqualTo("C.1");
		assertThat(Dimension_.toString(slice_3.index())).isEqualTo("3,8");

		assertThat(count(ledger.facts(new SliceFilter(slice_0, slice_1, slice_2, slice_3)))).isEqualTo(2);
		assertThat(count(ledger.facts(new SliceFilter(slice_0)))).isEqualTo(5);
		assertThat(count(ledger.facts(new SliceFilter(slice_1)))).isEqualTo(2);
		assertThat(count(ledger.facts(new SliceFilter(slice_2)))).isEqualTo(1);
		assertThat(count(ledger.facts(new SliceFilter(slice_3)))).isEqualTo(2);
	}

	@Test
	public void should_get_cell_facts() {
		Cube cube = this.ledger.cube().dimensions(this.ledger.dimensions().get(0)).build();
		assertThat(count(cube.facts())).isEqualTo(13);
		assertThat(cube.cells().get(0).toString()).isEqualTo("monday");
		assertThat(count(cube.cells().get(0).facts())).isEqualTo(0);
		assertThat(cube.cells().get(4).toString()).isEqualTo("friday");
		assertThat(count(cube.cells().get(4).facts())).isEqualTo(6);
		assertThat(cube.cells().get(5).toString()).isEqualTo("saturday");
		assertThat(count(cube.cells().get(5).facts())).isEqualTo(4);
		assertThat(cube.cells().get(6).toString()).isEqualTo("sunday");
		assertThat(count(cube.cells().get(6).facts())).isEqualTo(3);
		assertThat(cube.cells().get(7).toString()).isEqualTo("");
		assertThat(count(cube.cells().get(7).facts())).isEqualTo(13);
	}

	private int count(Iterable<Fact> facts) {
		int i = 0;
		for (Fact fact : facts) i++;
		return i;
	}
}
