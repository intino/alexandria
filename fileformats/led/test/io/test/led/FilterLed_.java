package io.test.led;

import io.intino.alexandria.led.Led;
import io.intino.alexandria.led.PrimaryLed;
import org.junit.Test;

import java.util.stream.Collectors;

import static io.test.led.TestItem.unsortedList;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class FilterLed_ {

	@Test
	public void should_filter_no_items() {
		PrimaryLed<TestItem> primary = PrimaryLed.load(unsortedList().stream());
		Led.Filter<TestItem> filtered = new Led.Filter<>(primary, t -> true);
		assertThat(filtered.stream().collect(Collectors.toList()).size()).isEqualTo(primary.size());
	}

	@Test
	public void should_filter_items() {
		Led<TestItem> primary = PrimaryLed.load(unsortedList().stream());
		Led.Filter<TestItem> filtered = new Led.Filter<>(primary, t -> t.i() > 0);
		assertThat(filtered.stream().collect(Collectors.toList()).size()).isEqualTo(100);
		assertThat(filtered.stream().mapToDouble(TestItem::d).sum()).isEqualTo(376250.0);
	}
}
