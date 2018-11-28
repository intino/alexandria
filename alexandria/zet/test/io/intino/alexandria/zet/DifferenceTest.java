package io.intino.alexandria.zet;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class DifferenceTest {

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