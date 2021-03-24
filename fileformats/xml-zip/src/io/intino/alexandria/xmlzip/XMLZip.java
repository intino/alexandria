package io.intino.alexandria.xmlzip;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

public class XMLZip {
	private final File file;

	public XMLZip(File file) {
		this.file = file;
	}

	public String get(int index) throws IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(file))))) {
			for (int i = 0; i < index; i++) reader.readLine();
			return reader.readLine();
		}
	}

	File file() {
		return file;
	}

	String[] lines() throws IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(file))))) {
			return reader.lines().toArray(String[]::new);
		}
	}

}
