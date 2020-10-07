package io.test.led;

import io.intino.alexandria.led.Led;
import io.intino.alexandria.led.Led.Join;
import io.intino.alexandria.led.PrimaryLed;
import org.junit.Test;

import java.util.stream.Stream;

import static io.test.led.TestItem.anotherList;
import static io.test.led.TestItem.unsortedList;
import static java.util.Arrays.asList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class JoinLed_ {


	@Test
	public void should_join_ledger_with_empty_ledger() {
		Led<TestItem> a = PrimaryLed.load(unsortedList().stream());
		Led<TestItem> b = PrimaryLed.load(Stream.empty());
		Join<TestItem> joined = new Join<>(asList(a, b));
		assertThat((int) joined.stream().count()).isEqualTo(199);
		assertThat(joined.stream().mapToDouble(TestItem::d).sum()).isEqualTo(502475.0);
	}

	@Test
	public void should_join_ledger_with_another_ledger() {
		Led<TestItem> a = PrimaryLed.load(unsortedList().stream());
		Led<TestItem> b = PrimaryLed.load(anotherList().stream());
		Join<TestItem> joined = new Join<>(asList(a, b));
		assertThat((int) joined.stream().count()).isEqualTo(400);
		assertThat(joined.stream().mapToDouble(TestItem::d).sum()).isEqualTo(3014975.0);
	}
}
