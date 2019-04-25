package io.intino.alexandria.zet;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class UnionTest {

	@Test
	public void should_read_three_sorted_streams_without_duplicates() {
		ZetStream.Union union = new ZetStream.Union(asList(
				new ZetReader(1, 3, 5),
				new ZetReader(2, 6, 8, 10),
				new ZetReader(4, 7, 9)));
		for (int i = 1; i <= 10; i++)
			assertEquals(i, union.next());
		assertFalse(union.hasNext());
		assertEquals(-1, union.next());
	}

	@Test
	public void should_make_a_union_of_streams_without_repeated_values() {
		ZetStream.Union union = new ZetStream.Union(asList(
				new ZetReader(1, 2, 3, 5),
				new ZetReader(2, 5, 6, 8, 10),
				new ZetReader(4, 6, 7, 9)));
		for (int i = 1; i <= 10; i++)
			assertEquals(i, union.next());
		assertFalse(union.hasNext());
		assertEquals(-1, union.next());
	}

	@Test
	public void should_read_streams_with_duplicates_imposing_freq_between_2_and_5() {
		ZetStream.Union union = new ZetStream.Union(asList(
				new ZetReader(1, 2, 3, 5),
				new ZetReader(2, 6, 8, 10),
				new ZetReader(2, 6, 8, 10),
				new ZetReader(2, 6, 8, 10),
				new ZetReader(2, 4, 7, 9),
				new ZetReader(2, 4, 7, 9),
				new ZetReader(2, 4, 7, 9)
		), 2, 5, false);

		List<Long> longs = new ArrayList<>();
		while (union.hasNext()) longs.add(union.next());

		Assert.assertEquals(6, longs.size());
		Assert.assertEquals((Long) 4L, longs.get(0));
		Assert.assertEquals((Long) 6L, longs.get(1));
		Assert.assertEquals((Long) 7L, longs.get(2));
		Assert.assertEquals((Long) 8L, longs.get(3));
		Assert.assertEquals((Long) 9L, longs.get(4));
		Assert.assertEquals((Long) 10L, longs.get(5));
	}


	@Test
	public void should_read_streams_with_duplicates_consecutive_between_2_and_5() {
		ZetStream.Union union = new ZetStream.Union(asList(
				new ZetReader(1, 2, 3, 5),
				new ZetReader(2, 6, 8, 10, 11),
				new ZetReader(6, 8, 10),
				new ZetReader(6, 8, 10, 11),
				new ZetReader(2, 4, 7, 9),
				new ZetReader(2, 4, 7, 9),
				new ZetReader(2, 4, 7, 9)
		), 2, 5, true);

		List<Long> longs = new ArrayList<>();
		while (union.hasNext()) longs.add(union.next());

		Assert.assertEquals(7, longs.size());
		Assert.assertEquals((Long) 2L, longs.get(0));
		Assert.assertEquals((Long) 4L, longs.get(1));
		Assert.assertEquals((Long) 6L, longs.get(2));
		Assert.assertEquals((Long) 7L, longs.get(3));
		Assert.assertEquals((Long) 8L, longs.get(4));
		Assert.assertEquals((Long) 9L, longs.get(5));
		Assert.assertEquals((Long) 10L, longs.get(6));
	}

	@Test
	public void should_read_three_sorted_files_without_duplicates() {
		ZetStream.Union union = new ZetStream.Union(asList(
				new ZetReader(new File("test-res/testsets/norep1.zet")),
				new ZetReader(new File("test-res/testsets/norep2.zet")),
				new ZetReader(new File("test-res/testsets/norep3.zet"))));

		int count = 0;
		assertEquals(0, union.next());
		count++;
		assertEquals(1, union.next());
		count++;
		assertEquals(2, union.next());
		count++;

		while (union.hasNext()) {
			union.next();
			count++;
		}

		Assert.assertEquals(1000, count);
		assertEquals(-1, union.next());
	}

	@Test
	public void should_make_a_join_of_three_files_without_repeated_values() {
		ZetStream.Union union = new ZetStream.Union(asList(
				new ZetReader(new File("test-res/testsets/rep1.zet")),
				new ZetReader(new File("test-res/testsets/rep2.zet")),
				new ZetReader(new File("test-res/testsets/rep3.zet"))));

		assertEquals(1, union.next());
		assertEquals(2, union.next());
		assertEquals(3, union.next());
		assertEquals(4, union.next());
		assertEquals(5, union.next());
		Assert.assertFalse(union.hasNext());
		assertEquals(-1, union.next());
	}

	@Test
	public void should_read_three_sorted_files_with_duplicates_imposing_freq_over_1() {
		ZetStream.Union union = new ZetStream.Union(asList(
				new ZetReader(new File("test-res/testsets/rep1.zet")),
				new ZetReader(new File("test-res/testsets/rep2.zet")),
				new ZetReader(new File("test-res/testsets/rep3.zet"))), 2, 5, false);

		List<Long> longs = new ArrayList<>();
		while (union.hasNext()) longs.add(union.next());

		Assert.assertEquals(4, longs.size());
		Assert.assertEquals((Long) 1L, longs.get(0));
		Assert.assertEquals((Long) 2L, longs.get(1));
		Assert.assertEquals((Long) 3L, longs.get(2));
		Assert.assertEquals((Long) 4L, longs.get(3));
	}

	@Test
	public void should_read_three_sorted_files_with_duplicates_imposing_freq_over_2() {
		ZetStream.Union union = new ZetStream.Union(asList(
				new ZetReader(new File("test-res/testsets/rep1.zet")),
				new ZetReader(new File("test-res/testsets/rep2.zet")),
				new ZetReader(new File("test-res/testsets/rep3.zet"))), 3, 5, false);

		List<Long> longs = new ArrayList<>();
		while (union.hasNext()) longs.add(union.next());

		Assert.assertEquals(1, longs.size());
		Assert.assertEquals((Long) 2L, longs.get(0));
	}
}