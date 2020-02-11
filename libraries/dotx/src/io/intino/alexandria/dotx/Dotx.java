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
	private final File template;
	private final Content content;

	public static Dotx from(File template) {
		return new Dotx(template);
	}

	public Dotx replace(String replacements) {
		for (String line : replacements.split("\n"))
			this.content.put(line.split(":"));
		return this;
	}

	public Dotx replace(String field, String value) {
		this.content.put(field, value);
		return this;
	}

	public Dotx replace(String field, byte[] value) {
		this.content.put(field, value);
		return this;
	}

	public void to(File result) throws IOException {
		new Zip(template).to(result);
	}

	private Dotx(File template) {
		this.template = template;
		this.content = new Content();
	}

	private static final class Content {
		private final Map<String, String> fields;
		private final Map<String, byte[]> images;

		Content() {
			this.fields = new HashMap<>();
			this.images = new HashMap<>();
		}

		private String fieldOf(String key) {
			return tagOf("«" + key + "»");
		}

		private String valueOf(String key) {
			return tagOf(fields.get(key)).replace("\n", CRLF).replace("&", "&amp;");
		}

		private String tagOf(String name) {
			return "<w:t>" + name + "</w:t>";
		}

		void put(String field, String value) {
			this.fields.put(field,value);
		}

		void put(String field, byte[] value) {
			this.images.put(field, value);
		}

		void put(String[] data) {
			this.fields.put(data[0],data[1]);
		}

		String replace(String line) {
			for (String key : fields.keySet())
				line = line.replace(fieldOf(key), valueOf(key));
			return line;
		}

		byte[] image(String name) {
			return images.get(name);
		}

		public boolean hasImage(String name) {
			return images.containsKey(name);
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
			if (isDocument(entry)) replaceDocument(zin.getInputStream(entry), zos);
			else if (isReplacedImage(entry)) zos.write(imageOf(entry));
			else copyFile(zin.getInputStream(entry), zos);
		}

		private byte[] imageOf(ZipEntry entry) {
			return content.image(entry.getName().substring(11));
		}

		private boolean isReplacedImage(ZipEntry entry) {
			if (!entry.getName().startsWith("word/media/")) return false;
			return content.hasImage(entry.getName().substring(11));
		}

		private boolean isDocument(ZipEntry entry) {
			return entry.getName().equalsIgnoreCase("word/document.xml");
		}

		private void copyFile(InputStream is, ZipOutputStream zos) throws IOException {
			Buffer buffer = new Buffer();
			while (buffer.read(is)) buffer.write(zos);
			is.close();
		}

		private void replaceDocument(InputStream is, ZipOutputStream zos) throws IOException {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
				while (true) {
					String line = reader.readLine();
					if (line == null) break;
					zos.write(content.replace(line).getBytes());
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
