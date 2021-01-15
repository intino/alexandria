package io.intino.alexandria.office;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class DocxBuilder {
	private static final String CRLF = "</w:t></w:r><w:r><w:rPr><w:noProof/><w:sz w:val=\"16\"/><w:szCs w:val=\"16\"/></w:rPr><w:br/></w:r><w:r><w:rPr><w:noProof/><w:sz w:val=\"16\"/><w:szCs w:val=\"16\"/></w:rPr><w:t>";
	private static final String TABLE = "<w:tbl>(.+?)</w:tbl>";
	private static final String TABLE_CAPTION = "<w:tblCaption w:val=\"([^\"]*)\"/>";
	private static final String TABLE_ROW = "(<w:tr.+?</w:tr>)";
	private final File template;
	private final Content content;

	public static DocxBuilder create(File template) {
		return new DocxBuilder(template);
	}

	public DocxBuilder replace(String replacements) {
		for (String line : replacements.split("\n"))
			this.content.put(line.split(":"));
		return this;
	}

	public DocxBuilder replace(String field, String value) {
		this.content.put(field, value);
		return this;
	}

	public interface TableEdition {
		TableEdition addRow(Map<String, String> columns);
		DocxBuilder end();
	}

	public TableEdition table(String table) { // In document, indicate table name on field Title in Table properties dialog
		return new TableEdition() {
			@Override
			public TableEdition addRow(Map<String, String> columns) {
				DocxBuilder.this.content.add(table, columns);
				return this;
			}

			@Override
			public DocxBuilder end() {
				return DocxBuilder.this;
			}
		};
	}

	public DocxBuilder replace(String field, byte[] value) {
		this.content.put(field, value);
		return this;
	}

	public void save(File file) throws IOException {
		new Zip(template).to(file);
	}

	private DocxBuilder(File template) {
		this.template = template;
		this.content = new Content();
	}

	private static final class Content {
		private final Map<String, String> fields;
		private final Map<String, List<Map<String, String>>> tables;
		private final Map<String, byte[]> images;
		private final Map<String, String> tablesTemplates;
		private final Map<String, String> rowsTemplates;

		Content() {
			this.fields = new HashMap<>();
			this.images = new HashMap<>();
			this.tables = new HashMap<>();
			this.tablesTemplates = new HashMap<>();
			this.rowsTemplates = new HashMap<>();
		}

		private String fieldOf(String key) {
			return tagOf("«" + key + "»");
		}

		private String valueOf(String key) {
			return tagOf(fields.get(key)).replace("\n", CRLF).replace("&", "&amp;");
		}

		private String columnOf(String value) {
			return tagOf(value).replace("\n", CRLF).replace("&", "&amp;");
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

		void add(String table, Map<String, String> values) {
			if (!this.tables.containsKey(table)) this.tables.put(table, new ArrayList<>());
			this.tables.get(table).add(values);
		}

		String replace(String line) {
			registerTablesTemplates(line);
			line = replaceTables(line);
			line = replaceFields(line);
			return line;
		}

		private String replaceTables(String line) {
			for (String table : tables.keySet()) {
				if (tablesTemplates.get(table) == null) continue;
				line = line.replace(tablesTemplates.get(table), tablesTemplates.get(table).replace(rowsTemplates.get(table), rowsOf(table)));
			}
			return line;
		}

		private String replaceFields(String line) {
			for (String key : fields.keySet())
				line = line.replace(fieldOf(key), valueOf(key));
			return line;
		}

		private String rowsOf(String table) {
			return tables.get(table).stream().map(row -> replaceRow(table, row)).filter(Objects::nonNull).collect(Collectors.joining(""));
		}

		private String replaceRow(String table, Map<String, String> row) {
			String result = rowsTemplates.get(table);
			if (result == null) return result;
			for (Map.Entry<String, String> column : row.entrySet()) {
				result = result.replace(fieldOf(column.getKey()), columnOf(column.getValue()));
			}
			return result;
		}

		private void registerTablesTemplates(String line) {
			Matcher tablesMatcher = Pattern.compile(TABLE, Pattern.DOTALL).matcher(line);
			while (tablesMatcher.find()) {
				registerTable(tablesMatcher.group(0));
			}
		}

		private void registerTable(String tableTemplate) {
			Matcher tableNameMatcher = Pattern.compile(TABLE_CAPTION).matcher(tableTemplate);
			if (!tableNameMatcher.find()) return;
			String tableName = tableNameMatcher.group(1);
			tablesTemplates.put(tableName, tableTemplate);
			rowsTemplates.put(tableName, rowTemplate(tableTemplate));
		}

		private String rowTemplate(String tableTemplate) {
			Matcher matcher = Pattern.compile(TABLE_ROW).matcher(tableTemplate);
			if (!matcher.find()) return "";
			return matcher.group(0);
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
