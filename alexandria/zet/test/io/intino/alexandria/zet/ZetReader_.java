package io.intino.alexandria.zet;

import org.junit.Assert;
import org.junit.Test;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class ZetReader_ {

	public static void main(String[] args) {
		Random random = new Random();
		try (DataOutputStream stream = new DataOutputStream(new FileOutputStream("test-res/testsets/filereader.zet"))) {
			for (int i = 0; i < 1000; i++) stream.writeLong(random.nextLong());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void should_read_1000_longs() {
		ZetReader reader = new ZetReader(new File("test-res/testsets/filereader.zet"));
		int count = 0;
		while (reader.hasNext()) {
			reader.next();
			count++;
		}
		Assert.assertEquals(1000, count);
		assertEquals(-1032827242987649422L, reader.current());

		assertEquals(-1, reader.next());
	}
}