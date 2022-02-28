package io.test;


import io.intino.alexandria.sumus.Cube;
import io.intino.alexandria.sumus.Dimension;
import io.intino.alexandria.sumus.Ledger;
import io.intino.alexandria.sumus.Slice;
import io.intino.alexandria.sumus.filters.SliceFilter;
import io.intino.alexandria.sumus.ledgers.columnar.ColumnarLedger;
import io.intino.alexandria.sumus.parser.LedgerDefinition;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Cube_ {
	@Test
	public void should_be_build_from_ledger() throws IOException {
		LedgerDefinition definition = LedgerDefinition.read(new File("ledgers/distritos.ledger"));
		Ledger ledger = new ColumnarLedger(definition).read(new File("ledgers/distritos/202201.tsv"),"\t");

		Dimension lugar = ledger.dimension("lugar");
		Dimension superficie_1 = ledger.dimension("superficie-1");
		Dimension superficie_2 = ledger.dimension("superficie-2");
		Dimension balance = ledger.dimension("balance-1");
		Slice granCanaria = lugar.slice("GRAN CANARIA");
		assertThat(lugar.name()).isEqualTo("lugar");
		assertThat(superficie_1.name()).isEqualTo("superficie-1");
		assertThat(superficie_2.name()).isEqualTo("superficie-2");
		assertThat(balance.name()).isEqualTo("balance-1");

		Cube cube = ledger.cube()
				.dimensions(lugar)
				.filter(new SliceFilter(granCanaria))
				.build();
		List<Cube.Cell> cells = cube.cells();
		assertThat(cells.size()).isEqualTo(23);
		assertThat(cells.get(0).slices().size()).isEqualTo(1);
		assertThat(cells.get(0).slices().get(0).toString()).isEqualTo("Gran Canaria");
		assertThat(cells.get(0).indicators().size()).isEqualTo(12);
	}

}
