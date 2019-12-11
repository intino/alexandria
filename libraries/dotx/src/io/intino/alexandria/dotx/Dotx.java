package io.intino.alexandria.dotx;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class Dotx {
	private static final String CRLF = "</w:t></w:r><w:r><w:rPr><w:noProof/><w:sz w:val=\"16\"/><w:szCs w:val=\"16\"/></w:rPr><w:br/></w:r><w:r><w:rPr><w:noProof/><w:sz w:val=\"16\"/><w:szCs w:val=\"16\"/></w:rPr><w:t>";
	private File template;
	private final Content content;

	public static Dotx from(File template) {
		return new Dotx(template);
	}

	public Dotx replace(String replacements) {
		for (String line : replacements.split("\n"))
			this.content.replace(line.split(":"));
		return this;
	}

	public Dotx replace(String field, String value) {
		this.content.put(field, value);
		return this;
	}

	public void to(File result) throws IOException {
		new Zip(template).to(result);
	}

	private Dotx(File template) {
		this.template = template;
		this.content = new Content(new HashMap<>());
	}

	private static final class Content {
		private final Map<String, String> data;

		Content(Map<String, String> data) {
			this.data = data;
		}

		String process(String line) {
			for (String key : data.keySet())
				line = line.replace(fieldOf(key), valueOf(key));
			return line;
		}

		private String fieldOf(String key) {
			return tagOf("«" + key + "»");
		}

		private String valueOf(String key) {
			return tagOf(data.get(key)).replace("\n", CRLF).replace("&", "&amp;");
		}

		private String tagOf(String name) {
			return "<w:t>" + name + "</w:t>";
		}

		void put(String field, String value) {
			this.data.put(field,value);
		}

		void replace(String[] data) {
			this.data.put(data[0],data[1]);
		}

	}

	private final class Zip {
		private final ZipFile zin;
		private final Enumeration<? extends ZipEntry> entries;

		Zip(File template) throws IOException {
			this.zin = new ZipFile(template);
			this.entries = zin.entries();
		}

		void to(File result) throws IOException {
			try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(result))) {
				while (entries.hasMoreElements())
					create(zos, new ZipEntry(entries.nextElement().getName()));
			}
			close();
		}

		void close() throws IOException {
			zin.close();
		}

		private void create(ZipOutputStream zos, ZipEntry entry) throws IOException {
			zos.putNextEntry(entry);
			copy(entry, zos);
			zos.closeEntry();
		}

		private void copy(ZipEntry entry, ZipOutputStream zos) throws IOException {
			if (isDocument(entry))
				copyDocument(zin.getInputStream(entry), zos);
			else
				copyFile(zin.getInputStream(entry), zos);
		}

		private boolean isDocument(ZipEntry entry) {
			return entry.getName().equalsIgnoreCase("word/document.xml");
		}

		private void copyFile(InputStream is, ZipOutputStream zos) throws IOException {
			Buffer buffer = new Buffer();
			while (buffer.read(is)) buffer.write(zos);
			is.close();
		}

		private void copyDocument(InputStream is, ZipOutputStream zos) throws IOException {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
				while (true) {
					String line = reader.readLine();
					if (line == null) break;
					zos.write(content.process(line).getBytes());
				}
			}
		}
	}

	private static class Buffer {
		private byte[] buffer = new byte[32768];
		private int length;

		boolean read(InputStream is) throws IOException {
			length = is.read(buffer);
			return length > 0;
		}

		void write(OutputStream os) throws IOException {
			os.write(buffer, 0, length);
		}
	}


}
