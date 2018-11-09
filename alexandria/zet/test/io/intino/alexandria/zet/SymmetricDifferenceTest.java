package io.intino.alexandria.zet;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

import static java.util.Arrays.asList;

public class SymmetricDifferenceTest {

	@Test
	public void should_make_a_difference_of_three_files_without_repeated_values() {
		ZetStream.Difference difference = new ZetStream.Difference(asList(
				new ZetReader(new File("test-res/testsets/rep1.zet")),
				new ZetReader(new File("test-res/testsets/rep2.zet")),
				new ZetReader(new File("test-res/testsets/rep3.zet"))));

		Assert.assertEquals(5, difference.next());
		Assert.assertFalse(difference.hasNext());
		Assert.assertEquals(-1, difference.next());
	}
}