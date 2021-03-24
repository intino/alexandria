package io.intino.alexandria.xmlzip;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class XMLZip_ {

	@Test
	public void name() throws IOException {
		String s = new XMLZip(new File("000.xz")).get(95);
		System.out.println(s);
	}
}
