package io.intino.alexandria.office;

import java.io.File;
import java.io.IOException;

public class DocxTemplate_ {

	public static void main(String[] args) throws IOException {
		DocxTemplate template = DocxTemplate.of(new File("temp/ARML.docx"));
		System.out.println(template);
		if(template.fields().isEmpty()) throw new RuntimeException();
		if(template.images().isEmpty()) throw new RuntimeException();
	}
}
