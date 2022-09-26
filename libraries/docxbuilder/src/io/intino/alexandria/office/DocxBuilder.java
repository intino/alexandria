package io.intino.alexandria.office;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.office.components.*;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import static io.intino.alexandria.office.components.ImageView.WrapOption.ClampToPage;
import static io.intino.alexandria.office.components.ImageView.WrapOption.ClampToTemplate;

public class DocxBuilder {

	private static final String CRLF = "</w:t></w:r><w:r><w:rPr><w:noProof/><w:sz w:val=\"16\"/><w:szCs w:val=\"16\"/></w:rPr><w:br/></w:r><w:r><w:rPr><w:noProof/><w:sz w:val=\"16\"/><w:szCs w:val=\"16\"/></w:rPr><w:t>";
	private static final String TABLE = "<w:tbl>(.+?)</w:tbl>";
	private static final String TABLE_CAPTION = "<w:tblCaption w:val=\"([^\"]*)\"/>";
	private static final String TABLE_ROW = "(<w:tr.+?</w:tr>)";
	private final File template;
	private final Content content;

	private static final long EMU_PER_INCH = 914400L;
	public static final long EMUS_PER_CM = 360000;
	private static final long MAX_EMU_WIDTH_PER_PAGE = EMUS_PER_CM * EMU_PER_INCH;

	private static final long MAX_IMAGE_WIDTH_EMU = 6289040;
	private static final long MAX_IMAGE_HEIGHT_EMU = 9281160;

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

	public DocxBuilder replace(String field, Paragraph paragraph) {
		this.content.put(field, paragraph);
		return this;
	}

	public DocxBuilder replace(String field, Paragraph... paragraphs) {
		this.content.put(field, paragraphs);
		return this;
	}

	public DocxBuilder replace(String field, List<Paragraph> paragraphs) {
		this.content.put(field, paragraphs);
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

	public DocxBuilder replace(String field, ImageView imageView) {
		this.content.put(field, imageView);
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

		public static final String FIELD_START = "«";
		public static final String FIELD_END = "»";
		private final Map<String, String> fields;
		private final Map<String, List<Paragraph>> paragraphs;
		private final Map<String, List<Map<String, String>>> tables;
		private final Map<String, ImageView> images;
		private final Map<String, String> tablesTemplates;
		private final Map<String, String> rowsTemplates;

		Content() {
			this.fields = new HashMap<>();
			this.paragraphs = new HashMap<>();
			this.images = new HashMap<>();
			this.tables = new HashMap<>();
			this.tablesTemplates = new HashMap<>();
			this.rowsTemplates = new HashMap<>();
		}

		private String fieldOf(String key) {
			return tagOf(FIELD_START + key + FIELD_END);
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
			if(value.contains("\n")) put(field, new Paragraph().text(value));
			else this.fields.put(field, value);
		}

		void put(String field, Paragraph paragraphs) {
			this.put(field, List.of(paragraphs));
		}

		void put(String field, Paragraph... paragraphs) {
			this.put(field, Arrays.asList(paragraphs));
		}

		void put(String field, List<Paragraph> paragraphs) {
			this.paragraphs.put(field, paragraphs);
		}

		void put(String field, ImageView imageView) {
			this.images.put(field, imageView);
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

		ImageView image(String name) {
			return images.get(name);
		}

		public boolean hasImage(String name) {
			return images.containsKey(name);
		}
	}

	private final class Zip {

		private final ZipFile zipFile;
		private final ArrayList<? extends ZipEntry> entries;
		private final Map<String, String> imageRelationships;
		private final Document document; // word/document.xml

		Zip(File template) throws IOException {
			this.zipFile = new ZipFile(template);
			this.entries = Collections.list(zipFile.entries());
			this.document = loadDocument();
			this.imageRelationships = findImageReferencesInDocument();
			setupImageExtents();
		}

		private void setupImageExtents() {
			NodeList drawingNodes = document.getElementsByTagName("w:drawing");
			int length = drawingNodes.getLength();
			for(int i = 0; i < length; i++) {
				Node drawingNode = drawingNodes.item(i);
				String desc = getDescrOfDrawingNode(drawingNode);
				if(desc == null) continue;
				ImageView view = content.image(desc);
				if(view == null) continue;
				if(doesNotNeedToModifyImageExtent(view)) continue;
				setNewImageExtent(drawingNode, view);
			}
		}

		private void setNewImageExtent(Node drawingNode, ImageView view) {
			Node extent = getExtentNodeOf(drawingNode);
			setImageExtents(view, extent.getAttributes().getNamedItem("cx"), extent.getAttributes().getNamedItem("cy"));
			Node xfrmExt = getXfrmExtNodeOf(drawingNode);
			setImageExtents(view, xfrmExt.getAttributes().getNamedItem("cx"), xfrmExt.getAttributes().getNamedItem("cy"));
		}

		private void setImageExtents(ImageView view, Node cx, Node cy) {
			Image image = view.image();
			long width = wrap(cx, image.getWidth(), image.getPhysicalWidthDpi(), view.widthWrapping());
			long height = wrap(cy, image.getHeight(), image.getPhysicalHeightDpi(), view.heightWrapping());

			if(view.keepAspectRatio()) {
				if (view.heightWrapping() == ClampToPage)
					height = (long) Math.ceil(width * (1 / image.aspect()));
				else if (view.widthWrapping() == ClampToPage)
					width = (long) Math.ceil(height * image.aspect());
			}

			width = getSizeInEMU(width, image.getPhysicalWidthDpi());
			height = getSizeInEMU(height, image.getPhysicalHeightDpi());

			width = Math.min(width, MAX_IMAGE_WIDTH_EMU);
			height = Math.min(height, MAX_IMAGE_HEIGHT_EMU);

			cx.setNodeValue(String.valueOf(width));
			cy.setNodeValue(String.valueOf(height));
		}

		private int wrap(Node node, int value, int dpi, ImageView.WrapOption wrapping) {
			return wrapping == ClampToTemplate ? imageSizeOf(node, dpi) : value;
		}

		private int imageSizeOf(Node node, int dpi) {
			long emuSize = Long.parseLong(node.getNodeValue());
			return (int) ((emuSize / (float)EMU_PER_INCH) * dpi);
		}

		private boolean doesNotNeedToModifyImageExtent(ImageView view) {
			return view.widthWrapping() == ClampToTemplate && view.heightWrapping() == ClampToTemplate;
		}

		// English metric units
		private long getSizeInEMU(long value, int dpi) {
			return (long)((value / (float)dpi) * EMU_PER_INCH);
		}

		private Node getXfrmExtNodeOf(Node drawingNode) {
			Node wpInline = drawingNode.getFirstChild();
			Node aGraphic = findChild(wpInline, "a:graphic");
			Node aGraphicData = findChild(aGraphic, "a:graphicData");
			Node pic = findChild(aGraphicData, "pic:pic");
			Node spPr = findChild(pic, "pic:spPr");
			Node xfrm = findChild(spPr, "a:xfrm");
			return xfrm.getLastChild();
		}

		private Node getExtentNodeOf(Node drawingNode) {
			Node container = drawingNode.getFirstChild();
			return findChild(container, "wp:extent");
		}

		private String getDescrOfDrawingNode(Node drawingNode) {
			try {
				// wp:inline || wp:anchor
				Node container = drawingNode.getFirstChild();
				Node wpDocPr = findChild(container, "wp:docPr");
				if(wpDocPr == null) return null;
				return wpDocPr.getAttributes().getNamedItem("descr").getNodeValue();
			} catch (Exception e) {
				return null;
			}
		}

		private Node findChild(Node parent, String name) {
			NodeList children = parent.getChildNodes();
			for(int i = 0;i < children.getLength();i++) {
				Node child = children.item(i);
				if(child.getNodeName().equals(name)) return child;
			}
			return null;
		}

		private Document loadDocument() throws IOException {
			try {
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				ZipEntry docEntry = entries.stream().filter(this::isDocument).findFirst().orElse(null);
				return db.parse(zipFile.getInputStream(docEntry));
			} catch (Exception e) {
				throw new IOException(e);
			}
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
			try {
				final NodeList graphicNodes = document.getElementsByTagName("pic:cNvPr");
				int bound = graphicNodes.getLength();
				for (int i = 0; i < bound; i++) {
					Node cNvPr = graphicNodes.item(i);
					NamedNodeMap attributes = cNvPr.getAttributes();
					if (attributes.getNamedItem("descr") != null) {
						String rel = attributes.getNamedItem("descr").getNodeValue();
						String relId = findRelId(cNvPr.getParentNode().getParentNode());
						if (rels.containsKey(relId)) {
							ZipEntry image = findImage(rels.get(relId));
							imageEntries.put(image.getName(), rel);
						}
					}
				}
			} catch (Exception e) {
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
			if (isDocument(entry)) replaceDocument(entry, zos);
			else if (isImageToReplace(entry)) zos.write(imageOf(entry));
			else copyFile(zipFile.getInputStream(entry), zos);
		}

		private byte[] imageOf(ZipEntry entry) {
			ImageView imageView = content.image(imageRelationships.get(entry.getName()));
			return imageView.image().data();
		}

		private boolean isImageToReplace(ZipEntry entry) {
			final String imageId = imageRelationships.get(entry.getName());
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

		private void replaceDocument(ZipEntry entry, ZipOutputStream zos) throws IOException {
			File tmp = writeTmpDoc();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(tmp.toPath())))) {
				String line;
				while ((line = reader.readLine()) != null) {
					if(isParagraphBegin(line))
						replaceParagraph(reader, zos, line);
					else
						zos.write(content.replace(line).getBytes());
				}
			} finally {
				tmp.delete();
			}
		}

		private void replaceParagraph(BufferedReader reader, ZipOutputStream zos, String line) throws IOException {
			List<String> paragraph = new ArrayList<>();
			paragraph.add(line);

			String mergeField = null;
			String alignment = null;
			StyleGroup styles = new StyleGroup();
			boolean pPr = false;

			while ((line = reader.readLine()) != null) {
				paragraph.add(line);
				if(isParagraphEnd(line)) break;
				if (isMergeFieldDeclaration(line)) {
					mergeField = line.substring(line.indexOf(Content.FIELD_START) + 1, line.indexOf(Content.FIELD_END));
				} else if(isAlignmentDeclaration(line)) {
					alignment = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
				} else if(isParagraphPropertiesBegin(line)) {
					pPr = true;
				} else if(isParagraphPropertiesEnd(line)) {
					pPr = false;
				} else if(pPr && isRunPropertiesBegin(line)) {
					getDeclaredStyles(paragraph, styles, reader);
				}
			}

			if (content.paragraphs.containsKey(mergeField)) {
				replaceWithCustomParagraphs(content.paragraphs.get(mergeField), alignment, styles, zos);
			} else {
				for(String l : paragraph) {
					zos.write(content.replace(l).getBytes());
				}
			}
		}

		private void getDeclaredStyles(List<String> paragraph, StyleGroup styles, BufferedReader reader) throws IOException {
			String line;
			while((line = reader.readLine()) != null) {
				paragraph.add(line);
				line = line.trim();

				if(isRunPropertiesEnd(line)) break;
				if(line.contains("noProof")) continue;

				if(line.startsWith("<w:sz")) styles.add(new Style.FontSize(Integer.parseInt(getVal(line)) / 2));
				else if(line.startsWith("<w:color")) styles.add(new Style.Color(getVal(line), get("themeColor", line), get("themeShade", line)));
				else if(line.startsWith("<w:b")) styles.add(new Style.Bold());
				else if(line.startsWith("<w:i")) styles.add(new Style.Italic());
				else if(line.startsWith("<w:u")) styles.add(new Style.Underlined());
			}
		}

		private String get(String property, String line) {
			if(!line.contains("w:" + property)) return null;
			String propertyTag = "w:" + property + "=\"";
			int start = line.indexOf(propertyTag) + propertyTag.length();
			return line.substring(start, line.indexOf('"', start));
		}

		private String getVal(String line) {
			return get("val", line);
		}

		private boolean isParagraphPropertiesEnd(String line) {
			return line.trim().equals("</w:pPr>");
		}

		private boolean isParagraphPropertiesBegin(String line) {
			return line.trim().equals("<w:pPr>");
		}

		private boolean isRunPropertiesEnd(String line) {
			return line.trim().equals("</w:rPr>");
		}

		private boolean isRunPropertiesBegin(String line) {
			return line.trim().equals("<w:rPr>");
		}

		private boolean isAlignmentDeclaration(String line) {
			return line.trim().startsWith("<w:jc") && line.contains("\"");
		}

		private boolean isMergeFieldDeclaration(String line) {
			return line.contains(Content.FIELD_START) && line.contains(Content.FIELD_END);
		}

		private boolean isParagraphEnd(String line) {
			return line.trim().startsWith("</w:p>");
		}

		private boolean isParagraphBegin(String line) {
			return line.trim().startsWith("<w:p ") || line.trim().startsWith("<w:p>");
		}

		private void replaceWithCustomParagraphs(List<Paragraph> paragraphs, String alignment, StyleGroup styles, ZipOutputStream zos) throws IOException {
			Alignment originalAlignment = Alignment.byName(alignment);
			for(Paragraph paragraph : paragraphs) {
				if(paragraph.alignment() == null && originalAlignment != null)
					paragraph.alignment(originalAlignment);

				for(Run run : paragraph.runs()) {
					if(!(run instanceof Br) && run.styles() == null)
						run.styles(styles);
				}

				zos.write(paragraph.xml().getBytes());
			}
		}

		private File writeTmpDoc() throws IOException {

			File tmp = new File(template.getParentFile(), "document_" + System.nanoTime() + ".xml");
			tmp.deleteOnExit();

			try(OutputStream output = Files.newOutputStream(tmp.toPath())) {

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				// The default add many empty new line, not sure why?
				// https://mkyong.com/java/pretty-print-xml-with-java-dom-and-xslt/
				Transformer transformer = transformerFactory.newTransformer();

				// add a xslt to remove the extra newlines
				//Transformer transformer = transformerFactory.newTransformer(new StreamSource(new File(FORMAT_XSLT)));

				// pretty print
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty(OutputKeys.STANDALONE, "no");

				DOMSource source = new DOMSource(document);
				StreamResult result = new StreamResult(output);

				transformer.transform(source, result);

			} catch (TransformerException e) {
				throw new RuntimeException(e);
			}

			return tmp;
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
