package io.intino.alexandria.zet;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class IntersectionTest {

	@Test
	public void should_make_an_intersection_of_three_files_without_repeated_values() {
		ZetStream.Intersection intersection = new ZetStream.Intersection(asList(
				new ZetReader(new File("test-res/testsets/rep1.zet")),
				new ZetReader(new File("test-res/testsets/rep2.zet")),
				new ZetReader(new File("test-res/testsets/rep3.zet"))));
		assertEquals(2, intersection.next());
		Assert.assertFalse(intersection.hasNext());
		assertEquals(-1, intersection.next());
	}
}
