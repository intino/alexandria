package io.intino.alexandria.office;

import io.intino.alexandria.logger.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
			this.fields.put(field, value);
		}

		void put(String field, byte[] value) {
			this.images.put(field, value);
		}

		void put(String[] data) {
			this.fields.put(data[0], data[1]);
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
		private final ZipFile zipFile;
		private final ArrayList<? extends ZipEntry> entries;
		private final Map<String, String> imagesRelationship;

		Zip(File template) throws IOException {
			this.zipFile = new ZipFile(template);
			this.entries = Collections.list(zipFile.entries());
			imagesRelationship = findImageReferencesInDocument();
//			imagesRelationship = new HashMap<>();
		}

		void to(File result) throws IOException {
			try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(result))) {
				for (ZipEntry entry : entries) create(zos, new ZipEntry(entry.getName()));
			}
			close();
		}

		private Map<String, String> findImageReferencesInDocument() {
			if (content.images.isEmpty()) return Collections.emptyMap();
			Map<String, String> rels = loadDocumentRels();
			Map<String, String> imageEntries = new HashMap<>();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(zipFile.getInputStream(entries.stream().filter(this::isDocument).findFirst().orElse(null)));
				final NodeList graphicNodes = doc.getElementsByTagName("pic:cNvPr");
				int bound = graphicNodes.getLength();
				for (int i = 0; i < bound; i++) {
					NamedNodeMap attributes = graphicNodes.item(i).getAttributes();
					if (attributes.getNamedItem("descr") != null) {
						String rel = attributes.getNamedItem("descr").getNodeValue();
						String relId = findRelId(graphicNodes.item(i).getParentNode().getParentNode());
						if (rels.containsKey(relId)) {
							imageEntries.put(findImage(rels.get(relId)).getName(), rel);
						}
					}
				}
			} catch (ParserConfigurationException | IOException | SAXException e) {
				Logger.error(e);
			}
			return imageEntries;
		}

		void close() throws IOException {
			zipFile.close();
		}

		private void create(ZipOutputStream zos, ZipEntry entry) throws IOException {
			zos.putNextEntry(entry);
			copy(entry, zos);
			zos.closeEntry();
		}

		private void copy(ZipEntry entry, ZipOutputStream zos) throws IOException {
			if (isDocument(entry)) replaceDocument(zipFile.getInputStream(entry), zos);
			else if (isImageToReplace(entry)) zos.write(imageOf(entry));
			else copyFile(zipFile.getInputStream(entry), zos);
		}

		private byte[] imageOf(ZipEntry entry) {
			return content.image(imagesRelationship.get(entry.getName()));
		}

		private boolean isImageToReplace(ZipEntry entry) {
			final String imageId = imagesRelationship.get(entry.getName());
			return imageId != null && content.hasImage(imageId);
		}

		private boolean isDocument(ZipEntry entry) {
			return entry.getName().equalsIgnoreCase("word/document.xml");
		}

		private ZipEntry findImage(String imageReference) {
			return entries.stream().filter(e -> e.getName().equals("word/" + imageReference)).findFirst().orElse(null);
		}

		private Map<String, String> loadDocumentRels() {
			final ZipEntry docRelsEntry = entries.stream().filter(this::isDocumentRels).findFirst().orElse(null);
			Map<String, String> rels = new HashMap<>();
			try {
				final InputStream is = zipFile.getInputStream(docRelsEntry);
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(is);
				final NodeList relNodes = doc.getElementsByTagName("Relationship");
				for (int i = 0; i < relNodes.getLength(); i++) {
					final NamedNodeMap attributes = relNodes.item(i).getAttributes();
					rels.put(attributes.getNamedItem("Id").getNodeValue(), attributes.getNamedItem("Target").getNodeValue());
				}
			} catch (ParserConfigurationException | IOException | SAXException e) {
				Logger.error(e);
			}
			return rels;
		}


		private String findRelId(Node node) {
			final NodeList childNodes = node.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				if (childNodes.item(i).getNodeName().equals("pic:blipFill")) {
					final Node firstChild = childNodes.item(i).getFirstChild();
					if (firstChild.getNodeName().equals("a:blip")) {
						return firstChild.getAttributes().getNamedItem("r:embed").getNodeValue();
					}
				}
			}
			return null;
		}

		private boolean isDocumentRels(ZipEntry entry) {
			return entry.getName().equalsIgnoreCase("word/_rels/document.xml.rels");
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
		private final byte[] buffer = new byte[32768];
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
