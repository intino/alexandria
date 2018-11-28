package io.intino.alexandria.zet;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class UnionTest {

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

	public static void main(String[] args) throws IOException {
		ZOutputStream test = new ZOutputStream(new FileOutputStream("test"));
		for (int i = 1; i <= 20; i++) {
			test.writeLong((long)i);
		}
		test.close();
		Zet test1 = new Zet(new ZetReader(new File("test")));
		System.out.println("AAA");
	}

//	@Test // TODO TEST CONSECUTIVES
//	public void should_read_three_sorted_files_with_duplicates_imposing_freq_over_2_and_recency() {
//		ZetStream.Union union = new ZetStream.Union(asList(
//				new SourceZetStream(new File("test-res/testsets/rep1.zet")),
//				new SourceZetStream(new File("test-res/testsets/rep2.zet")),
//				new SourceZetStream(new File("test-res/testsets/rep3.zet"))), 2, 5, 2);
//
//		List<Long> longs = new ArrayList<>();
//		while (union.hasNext()) longs.add(union.next());
//
//		Assert.assertEquals(2, longs.size());
//		Assert.assertEquals((Long) 2L, longs.get(0));
//		Assert.assertEquals((Long) 4L, longs.get(1));
//	}
}