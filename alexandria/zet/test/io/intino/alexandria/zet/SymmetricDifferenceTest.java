package io.intino.alexandria.zet;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class SymmetricDifferenceTest {

	@Test
	public void should_make_a_symmetric_difference_of_three_streams() {
		ZetStream difference = new ZetStream.SymmetricDifference(asList(
				new ZetReader(1, 2, 3, 5),
				new ZetReader(2, 5, 6, 8, 10),
				new ZetReader(4, 6, 7, 9)));

		List<Long> longs = new ArrayList<>();
		while (difference.hasNext()) longs.add(difference.next());

		Assert.assertEquals(7, longs.size());
		Assert.assertEquals((Long) 1L, longs.get(0));
		Assert.assertEquals((Long) 3L, longs.get(1));
		Assert.assertEquals((Long) 4L, longs.get(2));
		Assert.assertEquals((Long) 7L, longs.get(3));
		Assert.assertEquals((Long) 8L, longs.get(4));
		Assert.assertEquals((Long) 9L, longs.get(5));
		Assert.assertEquals((Long) 10L, longs.get(6));
	}


	@Test
	public void should_make_a_difference_of_three_files_without_repeated_values() {
		ZetStream.SymmetricDifference symmetricDifference = new ZetStream.SymmetricDifference(asList(
				new ZetReader(new File("test-res/testsets/rep1.zet")),
				new ZetReader(new File("test-res/testsets/rep2.zet")),
				new ZetReader(new File("test-res/testsets/rep3.zet"))));

		Assert.assertEquals(5, symmetricDifference.next());
		Assert.assertFalse(symmetricDifference.hasNext());
		Assert.assertEquals(-1, symmetricDifference.next());
	}
}