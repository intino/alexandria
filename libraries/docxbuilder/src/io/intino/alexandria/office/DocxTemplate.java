package io.intino.alexandria.office;

import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static io.intino.alexandria.office.DocxBuilder.Content.FIELD_END;
import static io.intino.alexandria.office.DocxBuilder.Content.FIELD_START;

public class DocxTemplate {

	public static DocxTemplate of(File docx) throws IOException {
		DocxBuilder.Zip zip = new DocxBuilder.Zip(docx, new DocxBuilder.Content());
		List<FieldDefinition> fields = findFieldsFrom(zip);
		List<ImageDefinition> images = zip.imageRelationships().values().stream().map(ImageDefinition::new).collect(Collectors.toList());
		return new DocxTemplate(fields, images);
	}

	private final List<FieldDefinition> fields;
	private final List<ImageDefinition> images;

	private DocxTemplate(List<FieldDefinition> fields, List<ImageDefinition> images) {
		this.fields = Collections.unmodifiableList(fields);
		this.images = Collections.unmodifiableList(images);
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public int size() {
		return fields.size() + images.size();
	}

	public List<FieldDefinition> fields() {
		return fields;
	}

	public List<ImageDefinition> images() {
		return images;
	}

	@Override
	public String toString() {
		return "DocxTemplate {" +
				"\n\tfields=" + fields +
				",\n\timages=" + images +
				"\n}";
	}

	public static class FieldDefinition {

		public final String name;
		public final String documentPart;

		public FieldDefinition(String name, String documentPart) {
			this.name = name;
			this.documentPart = documentPart;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	public static class ImageDefinition {

		public final String name;

		public ImageDefinition(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	private static final Pattern FieldPattern = Pattern.compile(FIELD_START + "[\\w-_.:+]+" + FIELD_END);
	private static List<FieldDefinition> findFieldsFrom(DocxBuilder.Zip zip) throws IOException {
		List<FieldDefinition> fieldDefinitions = new ArrayList<>();
		for (Map.Entry<String, Document> docEntry : zip.documents().entrySet()) {
			File tmp = DocumentHelper.createTmpDocument(zip.template().getParentFile(), docEntry.getValue());
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(tmp.toPath())))) {
				String line;
				while ((line = reader.readLine()) != null) {
					if(line.isBlank()) continue;
					Matcher matcher = FieldPattern.matcher(line);
					while(matcher.find()) {
						String field = matcher.group().replaceAll("[" + FIELD_START + FIELD_END + "]", "");
						fieldDefinitions.add(new FieldDefinition(field, docEntry.getKey().replace("word/", "").replace(".xml", "")));
					}
				}
			} finally {
				tmp.delete();
			}
		}
		return fieldDefinitions;
	}
}
