package io.test.ledgers;

import io.intino.alexandria.Ledger;
import io.intino.alexandria.Ledger.Join;
import io.intino.alexandria.PrimaryLedger;
import org.junit.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.test.ledgers.TestItem.anotherList;
import static io.test.ledgers.TestItem.unsortedList;
import static java.util.Arrays.asList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class JoinLedger_ {


	@Test
	public void should_join_ledger_with_empty_ledger() {
		Ledger<TestItem> a = PrimaryLedger.load(unsortedList().stream());
		Ledger<TestItem> b = PrimaryLedger.load(Stream.empty());
		Join<TestItem> merged = new Join<>(asList(a, b));
		assertThat(merged.stream().collect(Collectors.toList()).size()).isEqualTo(199);
		assertThat(merged.stream().mapToDouble(TestItem::d).sum()).isEqualTo(502475.0);
	}

	@Test
	public void should_join_ledger_with_another_ledger() {
		Ledger<TestItem> a = PrimaryLedger.load(unsortedList().stream());
		Ledger<TestItem> b = PrimaryLedger.load(anotherList().stream());
		Join<TestItem> merged = new Join<>(asList(a, b));
		assertThat(merged.stream().collect(Collectors.toList()).size()).isEqualTo(400);
		assertThat(merged.stream().mapToDouble(TestItem::d).sum()).isEqualTo(3014975.0);
	}
}
