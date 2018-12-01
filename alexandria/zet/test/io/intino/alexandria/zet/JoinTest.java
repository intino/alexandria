package io.intino.alexandria.zet;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class JoinTest {

	@Test
	public void should_read_three_sorted_files_without_duplicates() {
		ZetStream.Join join = new ZetStream.Join(asList(
				new ZetReader(new File("test-res/testsets/norep1.zet")),
				new ZetReader(new File("test-res/testsets/norep2.zet")),
				new ZetReader(new File("test-res/testsets/norep3.zet"))));

		int count = 0;

		for (int i = 0; i < 1000; i++) {
			assertEquals(i, join.next());
			count++;
		}

		Assert.assertEquals(1000, count);
		assertEquals(-1, join.next());
	}
}