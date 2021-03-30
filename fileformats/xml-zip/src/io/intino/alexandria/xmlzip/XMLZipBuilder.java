package io.intino.alexandria.xmlzip;

import java.io.*;
import java.util.stream.Stream;
import java.util.zip.GZIPOutputStream;

public class XMLZipBuilder {
	private final File file;
	private final String[] lines;

	public XMLZipBuilder(File file, int size) {
		this.lines = lines(size);
		this.file = file;
	}

	public XMLZipBuilder(XMLZip zipFile) throws IOException {
		this.lines = zipFile.lines();
		this.file = zipFile.file();
	}

	public boolean isEmpty() {
		return Stream.of(lines).filter(l -> !isEmpty(l)).count() <= 0;
	}

	public XMLZipBuilder add(File file, int index) throws IOException {
		return add(toLine(file).toString(), index);
	}

	public XMLZipBuilder add(String line, int index) {
		lines[index] = line + "\n";
		return this;
	}

	public void build() throws IOException {
		try (OutputStream os = new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(file)))) {
			for (String line : lines) os.write(line.getBytes());
		}
	}

	private static String[] lines(int size) {
		String[] result = new String[size];
		for (int i = 0; i < size; i++) result[i] = "\n";
		return result;
	}

	private StringBuilder toLine(File file) throws IOException {
		StringBuilder sb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) sb.append(line.trim());
		}
		sb.append("\n");
		return sb;
	}

	private boolean isEmpty(String line) {
		return line.equals("\n");
	}

}
