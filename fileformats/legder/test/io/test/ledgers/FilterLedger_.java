package io.test.ledgers;

import io.intino.alexandria.Ledger;
import io.intino.alexandria.PrimaryLedger;
import org.junit.Test;

import java.util.stream.Collectors;

import static io.test.ledgers.TestItem.unsortedList;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class FilterLedger_ {

	@Test
	public void should_filter_no_items() {
		PrimaryLedger<TestItem> primary = PrimaryLedger.load(unsortedList().stream());
		Ledger.Filter<TestItem> filtered = new Ledger.Filter<>(primary, t -> true);
		assertThat(filtered.stream().collect(Collectors.toList()).size()).isEqualTo(primary.size());
	}

	@Test
	public void should_filter_items() {
		Ledger<TestItem> primary = PrimaryLedger.load(unsortedList().stream());
		Ledger.Filter<TestItem> filtered = new Ledger.Filter<>(primary, t -> t.i() > 0);
		assertThat(filtered.stream().collect(Collectors.toList()).size()).isEqualTo(100);
		assertThat(filtered.stream().mapToDouble(TestItem::d).sum()).isEqualTo(376250.0);
	}
}
