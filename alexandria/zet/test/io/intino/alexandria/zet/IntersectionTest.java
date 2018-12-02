package io.intino.alexandria.zet;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class IntersectionTest {
	@Test
	public void should_make_an_intersection_of_three_files_without_repeated_values() {
		ZetStream.Intersection intersection = new ZetStream.Intersection(asList(
				new ZetReader(1, 2, 3, 4, 5),
				new ZetReader(1, 2, 3),
				new ZetReader(2, 4)));
		assertEquals(2, intersection.next());
		Assert.assertFalse(intersection.hasNext());
		assertEquals(-1, intersection.next());
	}

	@Test
	public void should_make_an_empty_intersection() {
		ZetStream.Intersection intersection = new ZetStream.Intersection(asList(
				new ZetReader(1, 2, 3, 4, 5),
				new ZetReader(1, 2, 3),
				new ZetReader(4),
				new ZetReader(new File("test-res/testsets/rep1.zet"))));
		Assert.assertFalse(intersection.hasNext());
		assertEquals(-1, intersection.next());
	}
}
