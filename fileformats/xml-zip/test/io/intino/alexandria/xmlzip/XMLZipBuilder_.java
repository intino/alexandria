package io.intino.alexandria.xmlzip;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class XMLZipBuilder_ {
	@Test
	public void name() throws IOException {
		File root = new File("000");
		File[] files = root.listFiles(f -> f.getName().endsWith("xml"));
		XMLZipBuilder xmlZipBuilder = new XMLZipBuilder(new File("000.xz"), 100);
		for (File file : files) {
			xmlZipBuilder.add(file, pos(file)).build();
		}
	}

	private int pos(File file) {
		return Integer.parseInt(file.getName().substring(12,15));
	}
}
