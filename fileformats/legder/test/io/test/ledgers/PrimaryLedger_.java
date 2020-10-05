package io.test.ledgers;

import io.intino.alexandria.PrimaryLedger;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class PrimaryLedger_ {

	@Test
	public void should_keep_ids_sorted() {
		PrimaryLedger<TestItem> ledger = PrimaryLedger.load(TestItem.unsortedList().stream());
		long previous = Long.MIN_VALUE;
		for (TestItem item : ledger) {
			assertThat(item.id() > previous).isTrue();
			previous = item.id();
		}
	}

	@Test
	public void should_be_stored_and_loaded() throws IOException {
		File file = File.createTempFile("ledger","");
		PrimaryLedger<TestItem> original = PrimaryLedger.load(TestItem.unsortedList().stream());
		original.store(new FileOutputStream(file));
		PrimaryLedger<TestItem> stored = PrimaryLedger.load(TestItem.class, new FileInputStream(file));
		assertThat(original.size()).isEqualTo(stored.size());
		assertThat(original.iterator().next().id()).isEqualTo(stored.iterator().next().id());
		assertThat(original.iterator().next().i()).isEqualTo(stored.iterator().next().i());
		assertThat(original.iterator().next().d()).isEqualTo(stored.iterator().next().d());
	}

	@Test
	public void should_aggregate() {
		PrimaryLedger<TestItem> ledger = PrimaryLedger.load(TestItem.unsortedList().stream());
		PrimaryLedger<TestItem>.Aggregator<TestAggregation> aggregator = ledger.aggregate(TestAggregation.class)
				.set(new TestAggregation("TOTAL", i -> true))
				.set(new TestAggregation(">0", i -> i.i() > 0))
				.set(new TestAggregation("=0", i -> i.i() == 0))
				.set(new TestAggregation("<0", i -> i.i() < 0))
				.execute();
		assertThat(aggregator.aggregations().size()).isEqualTo(4);
		assertThat(aggregator.aggregation(0).label()).isEqualTo("TOTAL");
		assertThat(aggregator.aggregation(0).count()).isEqualTo(199);
		assertThat(aggregator.aggregation(0).i()).isEqualTo(995);
		assertThat(aggregator.aggregation(0).d()).isEqualTo(502475.0);
		assertThat(aggregator.aggregation(1).label()).isEqualTo(">0");
		assertThat(aggregator.aggregation(1).count()).isEqualTo(100);
		assertThat(aggregator.aggregation(1).i()).isEqualTo(25250);
		assertThat(aggregator.aggregation(2).label()).isEqualTo("=0");
		assertThat(aggregator.aggregation(2).count()).isEqualTo(1);
		assertThat(aggregator.aggregation(2).i()).isEqualTo(0);
		assertThat(aggregator.aggregation(3).label()).isEqualTo("<0");
		assertThat(aggregator.aggregation(3).count()).isEqualTo(98);
		assertThat(aggregator.aggregation(3).i()).isEqualTo(-24255);


	}

}
